package stocksim

import stocksim.temp.*

class FinanceTagLib {
    static returnObjectForTags = ["relatedStocks"]
    static namespace = "finance"
    
    def googleFinanceService
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
    
    def eachSector = { attrs, body ->
        request.sectors = financeService.getSectors()
        def max = request.sectors.size()
        
        for (i in (1..max)) {
            request.sectorIndex = i - 1
            out << body()
        }
    }
    
    def sector = { attrs, body ->
        if (attrs.url == "true") {
            out << URLEncoder.encode(request.sectors[request.sectorIndex])
        } else {
            out << request.sectors[request.sectorIndex]
        }
    }
    
    def gainers = { attrs, body ->
        request.movers = googleFinanceService.getGainersLosers().gainers
        def max = Math.min(attrs.num.toInteger(), request.movers.size())
        
        for (i in (1..max)) {
            request.currentMoverIndex = i - 1
            out << body()
        }
    }
    
    def losers = { attrs, body ->
        request.movers = googleFinanceService.getGainersLosers().losers
        def max = Math.min(attrs.num.toInteger(), request.movers.size())
        
        for (i in (1..max)) {
            request.currentMoverIndex = i - 1
            out << body()
        }
    }
    
    def mover = { attrs, body ->
        out << request.movers[request.currentMoverIndex][attrs.req]
    }
}
