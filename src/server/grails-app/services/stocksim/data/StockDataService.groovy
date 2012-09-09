package stocksim.data

import stocksim.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.DateTimeConstants

class StockDataService {
    def stockDataListService
    def stockFullDataService
    
    def listReady = false
    def dataReady = false
    
    def lastCheckMarketsClosed = false
    
    def isReady() {
        listReady && dataReady
    }
    
    def updateStockData() {
        def marketOpen = marketIsOpen()
        
        if (! marketOpen && lastCheckMarketsClosed) {
            println "Market still closed, not updating."
            return
        } else if (! marketOpen) {
            println "Market closed, but was open last check, going ahead with update..."
        }
        
        lastCheckMarketsClosed = ! marketOpen
        
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
    
    def marketIsOpen() {
        def currentTime = new DateTime(DateTimeZone.forID("America/New_York"))
        def currentHour = currentTime.getHourOfDay()
        def currentDayOfWeek = currentTime.getDayOfWeek()
        
        if (! (DateTimeConstants.MONDAY..DateTimeConstants.FRIDAY).contains(currentDayOfWeek)) {
            println "Markets closed, bad day of the week"
            return false
        }
        
        if (! (9..16).contains(currentHour)) {
            println "Markets closed, bad hour"
            return false
        }
        
        println "Markets probably open"
        return true
    }
    
    def updateStockList() {
        stockDataListService.update()
    }
    
    def updateStockFullData() {
        stockFullDataService.update()
    }
}