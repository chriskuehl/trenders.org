package stocksim

import stocksim.temp.*
import stocksim.exception.*

class FinanceService {
    def cacheService
    
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
        tickers.add("MSFT")
        tickers.add("YHOO") // TODO: fix this
        
        // build a ticker list & ensure all tickers are alphanumeric
        tickers.each { ticker ->
            if (! YahooQueryService.isAlphaNumeric(ticker)) {
                throw new BadTickerSymbolException("Bad ticket symbol: " + ticker);
            }
            
            tickerList += "\"${ticker.toUpperCase()}\","
        }
        
        tickerList = tickerList[0..tickerList.size() - 2]
        
        // generate a YQL query
        // example: select * from yahoo.finance.quotes where symbol in ("YHOO","AAPL","GOOG","MSFT")
        def query = "SELECT * FROM yahoo.finance.quotes where symbol in (${tickerList})"
        def json = YahooQueryService.getResultsFromQuery(query)
        
        json.query.results.quote.each { stock ->
            def realName = SearchableStock.findByTicker(stock.symbol.toUpperCase()).name
            def stockO = new Stock(
                ticker: stock.Symbol,
                name: realName,
                prevClose: stock.PreviousClose,
                dayChange: stock.Change,
                dayChangePercent: stock.PercentChange,
                open: stock.Open,
                yearTarget: stock.OneyrTargetPrice,
                dayRange: stock.DaysRange,
                yearRange: stock.YearRange,
                marketCap: stock.MarketCapitalization,
                peRatio: stock.PERatio,
                value: stock.LastTradePriceOnly,
                exchange: stock.StockExchange.startsWith("Nasdaq") ? "nasdaq" : "nyse"
            )
            
            stocks[stock.symbol.toLowerCase()] = stockO
            cacheService.storeInCache("stocks", stock.symbol.toLowerCase(), stockO)
        }
        
        stocks
    }
}
