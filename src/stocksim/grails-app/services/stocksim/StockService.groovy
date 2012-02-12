package stocksim

import stocksim.exception.*;

class StockService {
    static def getStocks(String[] tickers) {
        def stocks = [:]
        def tickerList = ""
        
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
            stocks[stock.symbol.toLowerCase()] = new Stock(stock.symbol, stock.StockExchange, stock.Ask.toDouble())
        }
        
        stocks
    }
}
