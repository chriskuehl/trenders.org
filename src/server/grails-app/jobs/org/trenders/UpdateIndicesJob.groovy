package org.trenders


class UpdateIndicesJob {
	static triggers = { // start 15 seconds after bootstrap, repeat every 30 minutes
		simple name: "updateStocks", startDelay: 15000, repeatInterval: 60 * 30 * 1000
	}

    def googleFinanceService
    def cacheService

    def execute() {
        def assets = [
            [title: "Dow", id: ".DJI"], 
            [title: "S&P 500", id: ".INX"]
        ]
        
        println "Updating index caches"
        
        assets.each { asset -> 
            def results = googleFinanceService.getInfoForAsset(asset.id)
            
            def index = new MarketIndex()
            index.title = asset.title
            index.percentChange = results.c
            index.currentValue = results.l_cur
            
            cacheService.storeInCache("indices", asset.id, index)
            println "Updated asset: ${asset.id}"
        }
    }
}
