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
        def fullAssetName = ".${attrs.index}"
        def index = servletContext["index_${fullAssetName}"]
        
        if (index) {
            out << render(template: "/marketIndex/marketIndex", model: [index: index])
        } else {
            out << "Asset $fullAssetName isn't available."
        }
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
