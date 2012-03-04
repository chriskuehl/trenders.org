package stocksim


class UpdateIndicesJob {
    def startDelay = 15000
    def timeout = 60 * 30 * 1000 // every 30 minutes
    
    def googleFinanceService
    def servletContext

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

            servletContext["index_${asset.id}"] = index
            println "Updated asset: ${asset.id}"
        }
    }
}
