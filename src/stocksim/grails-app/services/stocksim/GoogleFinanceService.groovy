package stocksim

import groovy.json.JsonSlurper
import stocksim.temp.*

class GoogleFinanceService {
    def wikipediaService
    def utilService
    
    // in reality we are using this for the indicies more than stocks
    // since we use YQL to get stocks
    // 
    // .DJI = Dow Jones Industrial Average
    // .INX = S&P 500
    static def getInfoForAsset(def stock) {
        def body = "http://finance.google.com/finance/info?client=ig&q=INDEXDJX%3A${URLEncoder.encode(stock)}".toURL().getText()
        
        // this isn't returning proper JSON; for some reason, the first line
        // starts with " // ", but we can fix it and then parse it as JSON
        body = body.substring(3) // remove " // [" at the beginning
        
        // slurp it
        def slurper = new JsonSlurper()
        def result = slurper.parseText(body)[0]
        
        // minor number fixes
        result.l = result.l.replace(",", "")
        result.l_cur = result.l_cur.replace(",", "")
        
        
        result
    }
    
    def getGainersLosers() {
        def body = "http://www.google.com/finance".toURL().getText()
        def content = WikipediaService.betweenMore(body, "<td class=\"title chg\">Gainers<td class=change>Change", "</table>", 2)
        
        def gainers = "<tr>" + WikipediaService.between(content, "<tr>", "<tr><td style=\"height:.7em\">")
        def losers = WikipediaService.between(content, "<td class=\"title chr\">Losers<td class=change>Change", "<tr><td style=\"height:.7em\">")
        losers = losers.substring(32)
        
        def stats = [:]
        
        stats.gainers = parseStockList(gainers)
        stats.losers = parseStockList(losers)
        
        stats
    }
    
    def parseStockList(html) {
        def stockList = []
        def stocks = html.split("<tr>")
        
        stocks.each { stock ->
            def ticker = WikipediaService.between(stock, "<a", "<td")
            ticker = WikipediaService.between(ticker, ">", "</a>")
            
            def change = WikipediaService.between(stock, "\"change ch", "\n")
            change = WikipediaService.between(change, ">", "%") + "%"
            
            if (ticker != null) {
                def tempStock = SearchableStock.findByTicker(ticker)
                
                if (tempStock != null && tempStock.getMarketCap() > 0) {
                    def marketCap = utilService.formatBigNumber(tempStock.getMarketCap())
                    println "got back: $marketCap"
                    
                    stockList.add([
                        stock: tempStock,
                        marketCap: marketCap,
                        ticker: ticker,
                        name: tempStock.getName(),
                        change: change
                    ])
                }
            }
        }
        
        stockList
    }
}
