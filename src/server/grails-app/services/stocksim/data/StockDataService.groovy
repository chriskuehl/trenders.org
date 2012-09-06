package stocksim.data

import stocksim.*

class StockDataService {
    def stockDataListService
    def stockFullDataService
    
    def listReady = false
    def dataReady = false
    
    def isReady() {
        listReady && dataReady
    }
    
    def updateStockData() {
        println "Updating stock data..."
        
        // update all the data
        println "Updating stock list..."
        updateStockList()
        
        // only if previous succeeded
        if (stockDataListService.hasSuccessfullyLoaded) {
            listReady = true
            
            println "Updating full data now..."
            updateStockFullData()
            
            dataReady = true
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