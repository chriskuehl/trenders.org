package stocksim


class UpdateSearchCacheJob {
    def startDelay = 15000
    def timeout = 60 * 15 * 1000 // every 30 minutes
    
    def searchService

    def execute() {
        def start = new Date().getTime()
        searchService.updateCache()
        println "Refreshed cache in ${(new Date().getTime() - start) / 1000} seconds."
    }
}
