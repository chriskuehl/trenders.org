package stocksim

import stocksim.exception.*;

class FinanceService {
    static def getStocks(def tickers) {
        def stocks = [:]
        def tickerList = ""
        
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
            stocks[stock.symbol.toLowerCase()] = new Stock(
                ticker: stock.Symbol,
                name: stock.Name,
                lastClose: stock.PreviousClose,
                dayChange: stock.Change,
                dayChangePercent: stock.PercentChange,
                open: stock.Open,
                yearTarget: stock.OneyrTargetPrice,
                dayRange: stock.DaysRange,
                yearRange: stock.YearRange,
                marketCap: stock.MarketCapitalization,
                peRatio: stock.PERatio,
                value: stock.Ask
            )
        }
        
        stocks
    }
}
