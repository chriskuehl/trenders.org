package org.trenders.data

import org.trenders.*
import groovy.sql.Sql
import au.com.bytecode.opencsv.CSVReader

class StockFullDataService {
    final def batchSize = 200 // Yahoo limits to 200 per query
    
    def stockDataHelperService
    def dataSource_temp
    def financeService
    
    // for each existing ticker, we need to get the info
    def update() {
        def start = new Date()
        
        def sql = new Sql(dataSource_temp)
        def tickerList = stockDataHelperService.getTickerList(sql)
        
        // split into batches of 200
        def batches = splitListIntoSubListsOfSize(tickerList, batchSize)
        
        // iterate through each batch and fetch the new data
        def stocksToUpdate = []
        println "Fetching all stock data... ${stocksToUpdate.size()}/${tickerList.size()}"
        
        batches.each { batch ->
            updateBatch(batch, stocksToUpdate)
            
            println "Fetching all stock data... ${stocksToUpdate.size()}/${tickerList.size()}"
        }
        
        println "Fetched stock data; stocks to update: ${stocksToUpdate.size()} (time: ${new Date().getTime() - start.getTime()})"
        updateStocks(sql, stocksToUpdate)
        
        println "Stocks fully updated (time: ${new Date().getTime() - start.getTime()})"
        sql.close()
    }
    
    def updateStocks(def sql, def stocksToUpdate) {
        if (stocksToUpdate.size() <= 0) {
            println "No updates to perform"
            return
        }
        
        def columns = ["lastSale", "dayChange", "dayChangePercent", "open", "dayRange"]
        def query = buildQuery(columns)
        
        // now perform the updates with the query we just built
        sql.withBatch(20, query) { preparedStatement ->
            stocksToUpdate.eachWithIndex { stockToUpdate, i ->
                // ticker, lastSale, lastUpdateDate, lastUpdateTime, dayChange, open, dayRangeUpper, dayRangeLower, volume
                // example row: "MSFT",30.82,"8/31/2012","4:00pm",+0.50,30.62,30.96,30.38,36595416
                def ticker = stockToUpdate[0]
                def lastSale = stockToUpdate[1].toDouble()
                def lastUpdateDate = stockToUpdate[2]
                def lastUpdateTime = stockToUpdate[3]
                def dayChange = stockToUpdate[4]
                
                if (dayChange.startsWith("+")) {
                    dayChange = dayChange.substring(1)
                }
                
                dayChange = doubleIfExists(dayChange)
                
                def open = doubleIfExists(stockToUpdate[5])
                def dayRangeUpper = doubleIfExists(stockToUpdate[6])
                def dayRangeLower = doubleIfExists(stockToUpdate[7])
                def volume = integerIfExists(stockToUpdate[8])
                
                // calculate some things based on this data
                def dayChangePercent = null
                
                if (dayChange != 0.0 && open != 0.0) {
                    dayChangePercent = Math.floor((dayChange / open) * 100)
                    
                    if (dayChangePercent > 0) {
                        dayChangePercent = "+${dayChangePercent}"
                    }
                    
                    dayChangePercent = dayChangePercent.toString() + "%"
                }
                
                def dayRange = "${dayRangeLower} - ${dayRangeUpper}".toString()
                
                // order these into an array for the prepared statement
                def params = [lastSale, dayChange, dayChangePercent, open, dayRange, ticker.toUpperCase()]
                
                preparedStatement.addBatch(params)
            }
        }
        
        def stockMap = [:]
        
        Stock.findAll().each { stock ->
            stockMap[stock.ticker.toUpperCase()] = stock
        }
        
        financeService.updateStockMap(stockMap)
    }
    
    def doubleIfExists(def str) {
        if (str == "N/A") {
            return 0.0
        }
        
        return str.toDouble()
    }
    
    def integerIfExists(def str) {
        if (str == "N/A") {
            return 0
        }
        
        return str.toInteger()
    }
    
    def buildQuery(def columns) {
        def query = "update stock set "
        def columnMappings = stockDataHelperService.getStockColumnMappings()
        
        columns.each { column ->
            query += "${columnMappings[column]} = ?, "
        }
        
        query = query.substring(0, query.length() - 2) // trim comma off end
        query += " where ticker = ?"
        
        query
    }
    
    def updateBatch(def tickers, def stocksToUpdate) {
        // example URL: http://download.finance.yahoo.com/d/quotes.csv?s=RHT,MSFT,NOV&f=sl1d1t1c1ohgv&e=.csv
        def url = buildURLForTickers(tickers)
        def connection = url.openConnection()
        
        def rows = readRowsFromConnection(connection, stocksToUpdate)
    }
    
    def readRowsFromConnection(def connection, def stocksToUpdate) {
        def reader = new CSVReader(new InputStreamReader(connection.getInputStream()))
        readRowsFromCSVReader(reader, stocksToUpdate)
    }
    
    def readRowsFromCSVReader(def reader, def stocksToUpdate) {
        def stocks = []
        def nextLine
        
        // read each stock from the CSV file and add it to our array after making sure it's ok
        while ((nextLine = reader.readNext()) != null) {
            // example line: "WWW",47.03,"8/31/2012","4:04pm",+0.11,47.17,47.529,46.805,353265
            if (nextLine.size() == 9) {
                stocksToUpdate.add(nextLine)
            } else {
                println "Error, got line with wrong size (${nextLine.size()}, should be 9): ${nextLine}"
            }
        }
    }
    
    def buildURLForTickers(def tickers) {
        def url = "http://download.finance.yahoo.com/d/quotes.csv?s="

        // add the ticker batch to the URL
        tickers.each { ticker ->
            url += ticker.toUpperCase() + ","
        }

        // trim the last comma off the URL
        url = url.substring(0, url.length() - 2)

        // finish the URL
        url += "&f=sl1d1t1c1ohgv&e=.csv"
        
        url.toURL()
    }
    
    def splitListIntoSubListsOfSize(def list, def listSize) {
        def lists = [[]]
        
        // now add each item to a sublist
        list.each { item ->
            // add to the last sublist
            lists[lists.size() - 1].add(item)
            
            // is the last sublist now full?
            if (lists[lists.size() - 1].size() >= listSize) {
                // it is, so add a new sublist
                lists.add([])
            }
        }
        
        // is the last sublist empty? then remove it
        def lastListIndex = lists.size() - 1
        
        if (lists[lastListIndex].size() <= 0) {
            lists.remove(lastListIndex)
        }
        
        lists
    }
}
