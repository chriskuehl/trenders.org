package stocksim.data

import stocksim.*
import au.com.bytecode.opencsv.CSVReader
import groovy.sql.Sql
import org.apache.commons.lang.StringEscapeUtils

class StockDataListService {
    def hasSuccessfullyLoaded = false
    
    def grailsApplication
    def dataSource_temp
    
    def hasLoaded() {
        hasSuccessfullyLoaded
    }
    
    def update() {
        // download CSV files from nasdaq.com for the two important exchanges
        def exchanges = ["nasdaq", "nyse"] // TODO: read from config file
        def fullStart = new Date().getTime()
        
        // in order to keep Stock locked for updates for as little time as
        // possible, we're going to download and process all CSV files and then
        // start writing so we don't have to wait for a slow network while the
        // stocks database is locked up
        // 
        // this will determine if we should delete rows or not (e.g. we won't delete
        // rows if the API necessary to grab the stock files is down if newer ones than
        // the backup caches are already loaded)
        def start = new Date().getTime()
        def actions = determineCacheActionForExchanges(exchanges)
        println "Found stock list for exchanges, time ${new Date().getTime() - start}"
        
        // now that we know what to do, execute the actions
        Stock.withTransaction {
            def successfulExchanges = []
            
            exchanges.each { exchange ->
                def action = actions[exchange]
                
                if (action.command == "replace") {
                    // clear the table of existing stocks for this exchange
                    successfulExchanges.add(exchange)
                    Stock.executeUpdate("DELETE FROM Stock WHERE exchange = ?", [exchange]) // GORM is just too slow for this
                    
                    // now add them back
                    action.data.each { stock ->
                        //def sql = new Sql(dataSource_temp) // TODO: is it necessary to do it manually for speed?
                        stock.exchange = exchange
                        stock.save()
                        
                        //sql.execute("insert into stock (version, ticker, name, industry, sector, exchange, ipo_year, last_sale, exchange_cap) values (0.1, ?, ?, ?, ?, ?, ?, ?, ?)",
                        //    [stock.getTicker(), stock.getName(), stock.getIndustry(), stock.getSector(), stock.getExchange(), stock.getIpoYear(), stock.getLastSale(), stock.getMarketCap()])
                    }
                } else if (action.command == "giveup") {
                    println "Unable to find any stocks for exchange ${exchange}, try again soon."
                }
            }
            
            if (! hasSuccessfullyLoaded && successfulExchanges.size() >= 2) {
                hasSuccessfullyLoaded = true
            }
        }
        
        println "Finished, total time ${new Date().getTime() - fullStart}"
    }
    
    private def determineCacheActionForExchanges(exchanges) {
        def actions = [:]
        
        exchanges.each { exchange ->
            def action = determineActionForExchange(exchange)
            actions[exchange] = action
        }
        
        actions
    }
    
    private def determineActionForExchange(exchange) {
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
            } else {
                println "Database had no entries for exchange ${exchange}; using backup cache file"

                // get stored cache file as a last backup
                def resourcePath = grailsApplication.parentContext.getResource(File.separator).file.getAbsolutePath()
                def appPath = resourcePath.substring(0, resourcePath.lastIndexOf(File.separator)) + File.separator + "grails-app" + File.separator
                def cacheDir = appPath + "conf" + File.separator + "stockcsv-cache" + File.separator
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
                    println "Couldn't find a backup cache file"
                }
            }
        }
        
        action
    }
    
    private def buildURLForExchangeStocksDownload(exchange) {
        "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=${exchange}&render=download".toURL()
    }
    
    private def getStocksFromSource(URLConnection connection) {
        def reader = new CSVReader(new InputStreamReader(connection.getInputStream()))
        getStocksFromCSVReader(reader)
    }
    
    private def getStocksFromSource(File file) {
        def reader = new CSVReader(new FileReader(file))
        getStocksFromCSVReader(reader)
    }
    
    private def getStocksFromCSVReader(CSVReader reader) {
        if (! reader.readNext()) { // skip the headers on the CSV file
            throw new IOException("Bad CSV file (no headers)")
        }
        
        def stocks = []
        def nextLine
        
        // read each stock from the CSV file and add it to our array
        while ((nextLine = reader.readNext()) != null) {
            // headers:
            // Symbol, Name, LastSale, ExchangeCap, ADR TSO, IPOyear, Sector, industry, Summary Quote
            def stock = new Stock()
            
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
            stock.ipoYear = nextLine[5] == "n/a" ? (- 1) : nextLine[5].toInteger()
            stock.sector = nextLine[6] == "n/a" ? "" : nextLine[6]
            stock.industry = nextLine[7] == "n/a" ? "" : nextLine[7]
            
            stocks.add(stock)
        }
        
        stocks
    }
}
