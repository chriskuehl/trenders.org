package stocksim

class FinanceTagLib {
    static namespace = "finance"
    
    def stocks = { attrs, body ->
        String[] tickers = attrs.tickers
        request.stocks = FinanceService.getStocks(tickers)
        
        out << body()
    }
    
    def stock = { attrs ->
        out << request.stocks.get(attrs.ticker)[attrs.req]
    }
    
    def index = { attrs ->
        
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
