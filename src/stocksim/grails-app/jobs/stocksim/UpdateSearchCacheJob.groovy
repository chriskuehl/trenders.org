package stocksim


class UpdateSearchCacheJob {
    def startDelay = 60 * 1000 // wait a minute after starting
    def timeout = 60 * 60 * 1000 // hourly
    def SearchService

    def execute() {
        // execute task
        println "Executing search cache update"
        SearchService.updateCache()
    }
}