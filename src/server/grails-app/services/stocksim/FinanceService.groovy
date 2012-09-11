package stocksim

import stocksim.temp.*
import stocksim.exception.*
import groovy.sql.Sql

class FinanceService {
    def cacheService
    def dataSource_temp
    
    def getStocks(def tickers) {
        def stocks = [:]
        
        tickers.each { ticker ->
            stocks[ticker.toUpperCase()] = getStock(ticker)
        }
        
        stocks
    }
    
    def getStock(def ticker) {
        return Stock.findByTicker(ticker.toUpperCase())
    }
    
    def getSectors() {
        def sectors = []
        
        def sql = new Sql(dataSource_temp)
        sql.eachRow("select distinct sector from stock order by sector") { row ->
            if (row.sector.trim().length() > 0) {
                sectors.add(row.sector)
            }
        }
        
        sectors
    }
    
    // TODO: have this return Stock objects
    def getRelatedStocks(def ticker) {
        def stock = Stock.findByTicker(ticker)
        def sector = stock.getSector()
        
        def allRelatedStocks = Stock.findAll("from Stock as s where s.sector = ? AND s.ticker != ? order by marketCap desc", [sector, ticker], [max: 15])
        def tickers = []
        allRelatedStocks.each { relatedStock -> 
            tickers.add(relatedStock.getTicker())
        }
        
        tickers
    }
    
    def getSimpleName(def name) {
        def removeAtEnd = ["(The)", "Corporation", "Inc.", "Inc", "Incorporated", "Company", "&"]
        
        removeAtEnd.each { rm ->
            if (name.endsWith(" ${rm}")) {
                name = name.substring(0, name.indexOf(" ${rm}"))
                name = name.trim()
            }
        }
        
        name = name.trim()
        
        if (name.endsWith(",")) {
            name = name.substring(0, name.length() - 1)
        }
        
        name
    }
}
