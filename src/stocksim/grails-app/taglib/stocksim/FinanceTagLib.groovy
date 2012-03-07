package stocksim

import stocksim.temp.*

class FinanceTagLib {
    static returnObjectForTags = ["relatedStocks"]
    static namespace = "finance"
    
    def financeService
    
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
        def index = servletContext["index_${fullAssetName}"]
        
        if (index) {
            out << render(template: "/marketIndex/marketIndex", model: [index: index])
        }
    }
    
    def relatedStocks = { attrs ->
        financeService.getRelatedStocks(attrs.ticker.toUpperCase(), attrs.max)
    }
    
    def simpleName = { attrs ->
        def name = attrs.name
        def removeAtEnd = ["Corporation", "Inc.", "Inc", "Incorporated", "Company"]
        
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
        
        out << name
    }
}
