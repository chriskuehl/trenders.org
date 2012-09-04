package stocksim

import stocksim.data.*

/*
    This is called by UpdateStockDataJob once everything has been loaded
*/
class UpdateStockFullDataJob {
    def stockDataService

    def execute() {
        return 
        println "Updating full data now..."
        stockDataService.updateStockFullData()
    }
}