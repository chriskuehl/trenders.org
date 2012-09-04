package stocksim.data

import stocksim.*

class StockDataService {
    def stockDataListService
    def stockFullDataService
    
    def updateStockData() {
        println "Updating stock data..."
        
        // update all the data
        println "Updating stock list..."
        updateStockList()
        
        // only if previous succeeded
        if (stockDataListService.hasSuccessfullyLoaded) {
            println "Updating full data now..."
            updateStockFullData()
        } else {
            println "Unable to fetch full stock data, failed to load stock list; SITE IS DOWN!"
            // TODO: speed up retrieval next time since site is down!
        }
    }
    
    def updateStockList() {
        stockDataListService.update()
    }
    
    def updateStockFullData() {
        stockFullDataService.update()
    }
}