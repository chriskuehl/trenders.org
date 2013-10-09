package org.trenders

import org.trenders.temp.*
import org.trenders.exception.*
import groovy.sql.Sql

class FinanceService {
    def cacheService
    def dataSource_temp
    def stockMap
    
    def updateStockMap(newStockMap) {
        stockMap = newStockMap
    }
    
    def getStocks(def tickers) {
        def stocks = [:]
        //def n = (new Date().getTime())
        
        tickers.each { ticker ->
            def s = (new Date()).getTime()
            stocks[ticker] = getStock(ticker)
            // println stocks
            // println " (" + ((new Date()).getTime() - s) + ")"
        }
        
        //println "nnn=" + ((new Date().getTime()) - n)
        
        stocks
    }
    
    def getStock(def ticker) {
        return stockMap[ticker.toUpperCase()] // Stock.findByTicker(ticker.toUpperCase())
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
        def removeAtEnd = ["(The)", "Corporation", "Inc.", "Inc", "Incorporated", "Company", "&", ".com"]
        
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
