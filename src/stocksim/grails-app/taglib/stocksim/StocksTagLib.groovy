package stocksim

class StocksTagLib {
    def stocks = { attrs, body ->
        String[] tickers = attrs.tickers
        request.stocks = StockService.getStocks(tickers)
        
        out << body()
    }
    
    def stock = { attrs ->
        out << request.stocks.get(attrs.ticker)[attrs.req]
    }
    
    /*
    def books = { attrs, body ->
        request.books = BookService.getBooks(attrs.books)
        out << body()
    }
    
    def book = { attrs ->
        request.books[attrs.title][attrs.req]
    }*/
}
