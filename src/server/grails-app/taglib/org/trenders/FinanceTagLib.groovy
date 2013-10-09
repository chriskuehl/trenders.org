package org.trenders

import org.trenders.temp.*

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
        def stock
        
        if (request.stocks) {
            stock = request.stocks[attrs.ticker.toUpperCase()]
        } else {
            stock = financeService.getStock(attrs.ticker.toUpperCase())
        }
    
        if (stock != null) {
            out << stock[attrs.req]
        } else {
            if (attrs.req == "name") {
                out << "${attrs.ticker.toUpperCase()} (no longer exists)"
            } else {
                out << "?"
            }
        }
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
        //try {
            request.sectors = financeService.getSectors()
            def max = request.sectors.size()

            for (i in (1..max)) {
                request.sectorIndex = i - 1
                out << body()
            }
        //} catch (Exception ex) {
            //ex.printStackTrace(out)
        //}
    }
    
    def sector = { attrs, body ->
        if (attrs.url == "true") {
            out << URLEncoder.encode(request.sectors[request.sectorIndex])
        } else {
            out << request.sectors[request.sectorIndex]
        }
    }
	
	def ifGainersLosers = { attrs, body ->
		def gainersLosers = googleFinanceService.getGainersLosers()
		
		if (gainersLosers.gainers.size() > 0) {
			out << body()
		}
	}
    
    def gainers = { attrs, body ->
		def gainersLosers = googleFinanceService.getGainersLosers()
		
		request.movers = gainersLosers.gainers
		def max = Math.min(attrs.num.toInteger(), request.movers.size())

		for (i in (1..max)) {
			request.currentMoverIndex = i - 1
			out << body()
		}
    }
    
    def losers = { attrs, body ->
		def gainersLosers = googleFinanceService.getGainersLosers()
		
		if (gainersLosers) {
			request.movers = gainersLosers.losers
			def max = Math.min(attrs.num.toInteger(), request.movers.size())

			for (i in (1..max)) {
				request.currentMoverIndex = i - 1
				out << body()
			}
		}
    }
    
    def mover = { attrs, body ->
		if (request.currentMoverIndex > 0 && request.movers[request.currentMoverIndex].hasProperty(attrs.req)) {
			out << request.movers[request.currentMoverIndex][attrs.req]
		}
    }
}
