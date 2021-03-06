package org.trenders.data

import org.trenders.*
import au.com.bytecode.opencsv.CSVReader
import groovy.sql.Sql
import org.apache.commons.lang.StringEscapeUtils
import org.h2.jdbc.JdbcSQLException


class StockDataListService {
    def hasSuccessfullyLoaded = false
    
    def stockDataHelperService
    def grailsApplication
    def dataSource_temp
    
    // constants used for queries
    enum Query {
        INSERT,
        UPDATE
    }
    
    final def exchanges = ["nasdaq", "nyse"] // TODO: read from config file
    
    // methods called by other parts of the app
    def hasLoaded() {
        hasSuccessfullyLoaded
    }
    
    def update() {
        // download CSV files from nasdaq.com for the two important exchanges
        def fullStart = new Date().getTime()

        def start = new Date().getTime()
        
        // fetch the download for all the exchanges
        def actions = determineCacheActionForExchanges(exchanges)
        println "Found stock list for exchanges, time ${new Date().getTime() - start}"
        
        // update the caches with the data
        updateStockCachesWithData(actions)
        
        // finished
        println "Finished, total time ${new Date().getTime() - fullStart}"
    }
    
    // private methods
    def updateStockCachesWithData(def actions) {
        // prepare SQL handler
        def sql = new Sql(dataSource_temp)
        
        // get the list of SQL actions
        println "Creating list of SQL actions..."
        def sqlActions = getSQLActions(sql, actions)
        
        // perform all the SQL actions in batches
        println "Performing SQL actions..."
        performSQLActions(sql, sqlActions)
        
        // close SQL handler
        sql.close()
    }
    
    def performSQLActions(def sql, def sqlActions) {
        // prepare column mappings
        def columnMappings = stockDataHelperService.getStockColumnMappings()
        
        println "Performing SQL inserts... (${sqlActions.queryParams.insert.size()})"
        performSQLInserts(sql, columnMappings, sqlActions.queryParams.insert, sqlActions.queryModels.insert)

        println "Performing SQL updates... (${sqlActions.queryParams.update.size()})"
        performSQLUpdates(sql, columnMappings, sqlActions.queryParams.update, sqlActions.queryModels.update)
    }
    
    def performSQLInserts(def sql, def columnMappings, def queryParamSet, def model) {
        if (queryParamSet.size() <= 0) {
            println "No inserts to perform"
            return
        }
        
        // build the query to use for inserting based on the model
        def query = "insert into stock (version, "
        def blanks = "?, "
        
        // add property blanks
        model.keySet().each { propertyName ->
            query += columnMappings[propertyName] + ", "
            blanks += "?, "
        }

        // add special properties that can't be null
        def specialProperties = ["lastSale", "open", "yearTarget", "peRatio"]

        specialProperties.each { propertyName ->
            query += columnMappings[propertyName] + ", "
            blanks += "?, "
        }
        
        // trim query and blanks to remove excess comma and space
        query = query.substring(0, query.length() - 2)
        blanks = blanks.substring(0, blanks.length() - 2)
        
        // finish query
        query += ") values (" + blanks + ")"
        
        // now perform the inserts with the query we just built
        sql.withBatch(20, query) { preparedStatement ->
            queryParamSet.each { queryParams ->
                preparedStatement.addBatch(queryParams)
            }
        }
    }
    
    def performSQLUpdates(def sql, def columnMappings, def queryParamSet, def model) {
        if (queryParamSet.size() <= 0) {
            println "No updates to perform"
            return
        }
        
        // build the query to use for updating based on the model
        def query = "update stock set "
        
        // add all properties to the query
        model.keySet().each { propertyName ->
            query += columnMappings[propertyName] + " = ?, "
        }
        
        // trim query and blanks to remove excess comma and space
        query = query.substring(0, query.length() - 2)
        
        // finish query
        query += " where ticker = ?"
        
        // now perform the updates with the query we just built
        sql.withBatch(20, query) { preparedStatement ->
            queryParamSet.each { queryParams ->
                preparedStatement.addBatch(queryParams)
            }
        }
    }
    
    def getSQLActions(def sql, def actions) {
        def successfulExchanges = []
        def sqlActions = [queryParams: [:], queryModels: [:]]
        
        // setup map for actions
        sqlActions.queryParams.insert = []
        sqlActions.queryParams.update = []
        
        // get ticker list
        def tickerList = stockDataHelperService.getTickerList(sql)
        
        // handle each exchange
        exchanges.each { exchange ->
            def action = actions[exchange]
            def success = getSQLActionsForExchange(sqlActions, action, exchange, tickerList)
            
            if (success) {
                successfulExchanges.add(exchange)
            } else {
                println "Exchange failed: ${exchange}"
            }
        }

        if (! hasSuccessfullyLoaded && successfulExchanges.size() >= 2) { // TODO: move this to a more logical place
            hasSuccessfullyLoaded = true
        }
        
        sqlActions
    }
    
    def getSQLActionsForExchange(def sqlActions, def action, def exchange, def tickerList) {
        if (action.command == "replace") {
            // now add them back
            action.data.each { model ->
                def query = handleStockForExchange(model, exchange, tickerList)
                
                if (query != null) { // if null, nothing needed to be updated
                    if (query.type == Query.INSERT) {
                        sqlActions.queryParams.insert.add(query.query)
                        
                        // has a model not been set yet? if so, use this one
                        if (sqlActions.queryModels.insert == null) {
                            sqlActions.queryModels.insert = model
                        }
                    } else if (query.type == Query.UPDATE) {
                        sqlActions.queryParams.update.add(query.query)
                        
                        // has a model not been set yet? if so, use this one
                        if (sqlActions.queryModels.update == null) {
                            sqlActions.queryModels.update = model
                        }
                    } // there shouldn't be any other types (yet)
                }
            }
            
            return true
        } else if (action.command == "ok") { // current version is fine
            // do nothing, all is ok
            return true
        } else if (action.command == "giveup") {
            println "Unable to find any stocks for exchange ${exchange}, try again soon."
            return false
        }
        
        return false
    }
    
    // returns the query for the stock
    def handleStockForExchange(def stockData, def exchange, def tickerList) {
        stockData.exchange = exchange

        // have we already created a Stock object for this stock?
        def alreadyExisted = tickerList.contains(stockData.ticker.toLowerCase()) // TODO: adapt this to figure out which stocks have been removed

        // either update the existing stock, or add a non-existing one
        def query = null
        
        if (! alreadyExisted) { // stock hasn't been added yet, so create a new one
            query = [type: Query.INSERT, query: getQueryParamsForStockInsert(stockData)]
        } else { // stock already exists, so update the existing one with our new data
            query = [type: Query.UPDATE, query: getQueryParamsForStockUpdate(stockData)]
        }
        
        query
    }
    
    def getQueryParamsForStockInsert(def stockData) { // first column: version (TODO: remove this debug comment)
        def valuesList = [0]

        // add actual properties
        stockData.keySet().each { propertyName ->
            valuesList.add(stockData[propertyName])
        }

        // add special properties that can't be null
        def specialProperties = ["lastSale", "open", "yearTarget", "peRatio"]

        specialProperties.each { propertyName ->
            valuesList.add(0)
        }

        valuesList
    }
    
    def getQueryParamsForStockUpdate(def stockData) {
        // stock has already been added, so update the existing values

        if (stockData.keySet().size() > 0) { // are there any properties to update?
            def valuesList = []

            stockData.keySet().each { propertyName ->
                valuesList.add(stockData[propertyName])
            }

            valuesList.add(stockData.ticker)
            return valuesList
        }
        
        return null
    }
    
    def determineCacheActionForExchanges(def exchanges) {
        def actions = [:]
        
        exchanges.each { exchange ->
            def action = determineActionForExchange(exchange)
            actions[exchange] = action
        }
        
        actions
    }
    
    def determineActionForExchange(def exchange) {
        def action = [:]
        action.command = "skip" // do nothing by default
        
        // try to download the CSV files from the API
        def url = buildURLForExchangeStocksDownload(exchange)
        
        // rather than using the nice, clean url.getText() method, we're going to
        // use some plain old Java methods since we need to change read and connection
        // timeouts to something more reasonable in case the server is down
        def connection = url.openConnection()

        connection.setReadTimeout(5000) // TODO: move these to app config
        connection.setConnectTimeout(5000)
        
        // try to load from the URLConnection now
        try {
            def stocks = getStocksFromSource(connection)
            action.command = "replace" // replace the stocks currently in the database, they're outdated
            action.data = stocks
        } catch (IOException ex) {
            // there was a problem downloading from the server (likely it's down,
            // so fall back to the old caches or to existing data)
            ex.printStackTrace()
            println "Unable to download stocks from API, so falling back on backup caches"
            
            // check if there are any currently in the database
            if (Stock.countByExchange(exchange) > 0) {
                // there are, so the stocks in database are newer than our old backup caches
                // therefore do nothing
                println "Not touching stocks for exchange ${exchange}; can't download from server, database has newer than backups"
                action.command = "ok"
            } else {
                println "Database had no entries for exchange ${exchange}; using backup cache file"

                // get stored cache file as a last backup
                def cacheDir = grailsApplication.parentContext.getResource("stockcsv-cache").file.getAbsolutePath()
                def dir = new File(cacheDir)
                
                FilenameFilter filter = new StockCacheFilenameFilter(exchange + "-")
                File[] files = dir.listFiles(filter)
                
                if (files.length > 0) {
                    def file = files[0]
                    def stocks = getStocksFromSource(file)
                    
                    action.command = "replace" // replace the stocks currently in the database, they're outdated
                    action.data = stocks
                } else {
                    action.command = "giveup" // give up, nothing more we can do, but try again soon since
                                              // the index is empty at the moment
                                              // TODO: try again soon
                    println "Couldn't find a backup cache file"
                }
            }
        }
        
        action
    }
    
    def buildURLForExchangeStocksDownload(exchange) {
        "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=${exchange}&render=download".toURL()
    }
    
    def getStocksFromSource(URLConnection connection) {
        def reader = new CSVReader(new InputStreamReader(connection.getInputStream()))
        getStocksFromCSVReader(reader)
    }
    
    def getStocksFromSource(File file) {
        def reader = new CSVReader(new FileReader(file))
        getStocksFromCSVReader(reader)
    }
    
    def getStocksFromCSVReader(CSVReader reader) {
        if (! reader.readNext()) { // skip the headers on the CSV file
            throw new IOException("Bad CSV file (no headers)")
        }
        
        def stocks = []
        def nextLine
        
        // read each stock from the CSV file and add it to our array
        while ((nextLine = reader.readNext()) != null) {
            // headers:
            // Symbol, Name, LastSale, ExchangeCap, ADR TSO, IPOyear, Sector, industry, Summary Quote
            def stock = [:] // we'll return a map of some stock properties; these will
                            // either update the existing ones, or be added as new
                            // (if the stock isn't already in the stock list)
            
            (0..7).each { i ->
                nextLine[i] = StringEscapeUtils.unescapeHtml(nextLine[i])
            }
            
            if (nextLine[0].contains("^") || nextLine[0].contains("/")) {
                continue
            }
            
            // set some basic data on the stocks (others will be set later by services
            // which provide more data than the NASDAQ download)
            stock.ticker = nextLine[0].trim()
            stock.name = nextLine[1] == "n/a" ? nextLine[0] : nextLine[1]
            stock.marketCap = nextLine[3] == "n/a" ? 0L : (nextLine[3].indexOf(".") > (- 1) ? nextLine[3].substring(0, nextLine[3].indexOf(".")) : nextLine[3]).toLong()
            stock.ipoYear = nextLine[5] == "n/a" ? (- 1) : nextLine[5].toInteger()
            stock.sector = nextLine[6] == "n/a" ? "" : nextLine[6]
            stock.industry = nextLine[7] == "n/a" ? "" : nextLine[7]
            
            stocks.add(stock)
        }
        
        stocks
    }
}
