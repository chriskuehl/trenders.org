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
        
        // TODO: some real scheduling
        def index
        
        if (! servletContext["index_${attrs.index}"]) {
            def results = GoogleFinanceService.getInfoForAsset(fullAssetName)

            index = new MarketIndex()
            index.title = attrs.title
            index.percentChange = results.c
            index.currentValue = results.l_cur
            
            servletContext["index_${attrs.index}"] = index
        } else {
            index = servletContext["index_${attrs.index}"]
        }
        
        out << render(template: "/marketIndex/marketIndex", model: [index: index])
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
