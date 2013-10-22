package org.trenders

import org.trenders.data.*

/*
    The task for this job is to update the list of all stocks from the NASDAQ site
    and to fetch recent data from the Yahoo! CSV dumps
*/
class UpdateStockDataJob {
	static triggers = { // start 15 seconds after bootstrap, repeat every 15 minutes
		simple name: "updateStocks", startDelay: 15000, repeatInterval: 60 * 15 * 1000
	}
    
    def stockDataService

    def execute() {
        def start = new Date().getTime()
        stockDataService.updateStockData()
        println "Updated stock data in ${(new Date().getTime() - start) / 1000} seconds."
    }
}
