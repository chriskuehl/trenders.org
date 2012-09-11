package filters

class LoadingFilters {
    def request
    def stockDataService
    
    def filters = {
        stocksLoadedFilter(uri: "/**") {
            before = { // have stocks been loaded?
                if (! stockDataService.isReady()) {
                    println "Unable to load!"
                    render(view: "/loading", status: 503)
                    return false
                }
            }
        }
    }
}