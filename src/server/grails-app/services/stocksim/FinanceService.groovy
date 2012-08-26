package stocksim

import stocksim.temp.*
import stocksim.exception.*
import groovy.sql.Sql

class FinanceService {
    def cacheService
    def dataSource_temp
    
    def getStocks(def tickers) {
        def stocks = [:]
        def tickerList = ""
        
        // see if we can just use the cache
        def foundOld = false
        
        tickers.each { ticker ->
            def stockC = cacheService.fetchFromCache("stocks", ticker.toLowerCase(), 15)
            
            if (stockC == null) {
                foundOld = true
            } else {
                stocks[ticker.toLowerCase()] = stockC
            }
        }
        
        // we can just use the cache
        if (! foundOld) {
            return stocks
        }
        
        
        // can't just use the cache, so fetch the data via YQL
        if (tickers.size() < 2) {
            tickers.add("MSFT")
            tickers.add("YHOO") // TODO: fix this
        }
        
        // build a ticker list & ensure all tickers are alphanumeric
        tickers.each { ticker ->
            if (! YahooQueryService.isAlphaNumericOrSlash(ticker)) {
                throw new BadTickerSymbolException("Bad ticker symbol: " + ticker);
            }
            
            tickerList += "\"${ticker.trim().toUpperCase()}\","
        }
        
        tickerList = tickerList[0..tickerList.size() - 2]
        
        // generate a YQL query
        // example: select * from yahoo.finance.quotes where symbol in ("YHOO","AAPL","GOOG","MSFT")
        def query = "SELECT * FROM yahoo.finance.quotes where symbol in (${tickerList})"
        def json = YahooQueryService.getResultsFromQuery(query)
        
        json.query.results.quote.each { stock ->
            def cachedStock = SearchableStock.findByTicker(stock.symbol.toUpperCase())
            def realName = "(unknown)"
            
            if (cachedStock != null) {
                realName = cachedStock.name
            } else {
                // TODO: handle these kind of errors (where a stock stops existing) better...
                println "Error: Unable to find a stock with the ticker ${stock.Symbol} in the SearchableStock DB, but apparently it exists...?"
            }
            
            def stockO = new Stock(
                ticker: stock.Symbol,
                name: realName,
                prevClose: stock.PreviousClose,
                dayChange: stock.Change.startsWith("+") ? stock.Change.substring(1) : stock.Change,
                dayChangePercent: stock.PercentChange,
                open: stock.Open,
                yearTarget: stock.OneyrTargetPrice,
                dayRange: stock.DaysRange,
                yearRange: stock.YearRange,
                marketCap: stock.MarketCapitalization,
                peRatio: stock.PERatio,
                value: (stock.LastTradePriceOnly.toDouble() * 100).toInteger() / 100,
                exchange: stock.StockExchange.startsWith("Nasdaq") ? "nasdaq" : "nyse"
            )
            
            stocks[stock.symbol.toLowerCase()] = stockO
            cacheService.storeInCache("stocks", stock.symbol.toLowerCase(), stockO)
        }
        
        stocks
    }
    
    def getSectors() {
        def sectors = []
        
        def sql = new Sql(dataSource_temp)
        sql.eachRow("select distinct sector from searchable_stock order by sector") { row ->
            if (row.sector.trim().length() > 0) {
                sectors.add(row.sector)
            }
        }
        
        sectors
    }
    
    def getRelatedStocks(def ticker) {
        def stock = SearchableStock.findByTicker(ticker)
        def sector = stock.getSector()
        
        def allRelatedStocks = SearchableStock.findAll("from SearchableStock as s where s.sector = ? AND s.ticker != ? order by marketCap desc", [sector, ticker], [max: 15])
        def tickers = []
        allRelatedStocks.each { relatedStock -> 
            tickers.add(relatedStock.getTicker())
        }
        
        tickers
    }
    
    def getSimpleName(def name) {
        def removeAtEnd = ["(The)", "Corporation", "Inc.", "Inc", "Incorporated", "Company", "&"]
        
        removeAtEnd.each { rm ->
            if (name.endsWith(" ${rm}")) {
                name = name.substring(0, name.indexOf(" ${rm}"))
                name = name.trim()
            }
        }
        
        name = name.trim()
        
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1)
        }
        
        name
    }
}
