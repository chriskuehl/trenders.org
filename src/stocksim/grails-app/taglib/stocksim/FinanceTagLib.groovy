package stocksim

import stocksim.temp.*

class FinanceTagLib {
    static returnObjectForTags = ["relatedStocks"]
    static namespace = "finance"
    
    def financeService
    def cacheService
    
    def stocks = { attrs, body ->
        def tickers = attrs.tickers
        request.stocks = financeService.getStocks(tickers)
        
        out << body()
    }
    
    def stock = { attrs ->
        out << request.stocks.get(attrs.ticker.toLowerCase())[attrs.req]
    }
    
    def index = { attrs ->
        def fullAssetName = ".${attrs.index}"
        def index = cacheService.fetchFromCache("indices", fullAssetName, (- 1))
        
        if (index) {
            out << render(template: "/marketIndex/marketIndex", model: [index: index])
        }
    }
    
    def relatedStocks = { attrs ->
        financeService.getRelatedStocks(attrs.ticker.toUpperCase())
    }
    
    def simpleName = { attrs ->
        def name = attrs.name
        name = financeService.getSimpleName(name)
        
        out << name
    }
}
