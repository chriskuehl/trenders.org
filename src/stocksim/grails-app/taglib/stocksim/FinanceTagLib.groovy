package stocksim

class FinanceTagLib {
    static namespace = "finance"
    
    def financeService
    
    def stocks = { attrs, body ->
        def tickers = attrs.tickers
        request.stocks = financeService.getStocks(tickers)
        
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
        }
    }
}
