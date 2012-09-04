package stocksim

import stocksim.data.*

/*
    The task for this job is to update the list of all stocks from the NASDAQ site
    and to fetch recent data from the Yahoo! CSV dumps
*/
class UpdateStockDataJob {
    def startDelay = 15000 // 15 seconds
    def timeout = 60 * 15 * 1000 // every 15 minutes
    
    def stockDataService

    def execute() {
        def start = new Date().getTime()
        stockDataService.updateStockData()
        println "Updated stock data in ${(new Date().getTime() - start) / 1000} seconds."
    }
}