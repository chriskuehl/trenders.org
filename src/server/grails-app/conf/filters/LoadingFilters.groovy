package filters

class LoadingFilters {
    def request
    def searchService
    
    def filters = {
        searchLoadedFilter(uri: "/**") {
            before = { // have searches been loaded?
                if (! searchService.cacheHasLoaded()) {
                    println "Unable to load!"
                    render(view: "/loading", status: 503)
                    return false
                }
            }
        }
    }
}

