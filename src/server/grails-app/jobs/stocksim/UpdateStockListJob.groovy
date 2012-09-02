package stocksim

import stocksim.data.*

/*
    The task for this job is to update the list of all stocks from the NASDAQ site.
    Once this is done, then we can fetch more data from the Yahoo! CSV dumps
*/
class UpdateStockListJob {
    def startDelay = 1000 // 15 seconds
    def timeout = 60 * 15 * 1000 // every 15 minutes
    
    def stockDataService

    def execute() {
        def start = new Date().getTime()
        stockDataService.updateStockList()
        println "Updated stock list in ${(new Date().getTime() - start) / 1000} seconds."
    }
}