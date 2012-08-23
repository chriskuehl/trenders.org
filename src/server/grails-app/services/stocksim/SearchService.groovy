package stocksim

import stocksim.temp.*
import au.com.bytecode.opencsv.CSVReader
import groovy.sql.Sql
import org.apache.commons.lang.StringEscapeUtils

class SearchService {
    def grailsApplication
    
    def hasSuccessfullyLoaded = false
    
    def sessionFactory
    def dataSource_temp
    def financeService
    
    def getResults(def query, int limit, def sector) {
        def results = []
        def sql = new Sql(dataSource_temp)
        def term = YahooQueryService.makeAlphaNumeric(query).toLowerCase() + "%"
        def mquery
        def p
        
        if (sector) {
            mquery = "SELECT * FROM searchable_stock WHERE sector = ? ORDER BY market_cap DESC LIMIT ?"
            p = [query, limit]
        } else {
            mquery = "SELECT * FROM searchable_stock WHERE LOWER(name) LIKE ? OR LOWER(ticker) LIKE ? ORDER BY market_cap DESC LIMIT ?"
            p = [term, term, limit]
        }
        
        sql.eachRow(mquery, p) { row ->
            def stock = new SearchableStock()
            
            stock.setIndustry(row.industry)
            stock.setIpoYear(row.ipo_year)
            stock.setLastSale(row.last_sale)
            stock.setMarket(row.market)
            stock.setMarketCap(row.market_cap)
            stock.setName(financeService.getSimpleName(row.name))
            stock.setSector(row.sector)
            stock.setTicker(row.ticker)
            
            results.add(stock)
        }
        
        results
    }
    
    void updateCache() {
        // download CSV files from nasdaq.com for the two important markets
        def markets = ["nasdaq", "nyse"] // TODO: read from config file?
        def fullStart = new Date().getTime()
        
        // in order to keep SearchableStock locked for updates for as little time as
        // possible, we're going to download and process all CSV files and then
        // start writing so we don't have to wait for a slow network while the
        // stocks database is locked up
        // 
        // this will determine if we should delete rows or not (e.g. we won't delete
        // rows if the API necessary to grab the stock files is down if newer ones than
        // the backup caches are already loaded)
        def start = new Date().getTime()
        def actions = determineCacheActionForMarkets(markets)
        println "Found data for markets, time ${new Date().getTime() - start}"
        
        // now that we know what to do, execute the actions
        SearchableStock.withTransaction {
            def successfulMarkets = []
            
            markets.each { market ->
                def action = actions[market]
                
                if (action.command == "replace") {
                    // clear the table of existing stocks for this market
                    successfulMarkets.add(market)
                    SearchableStock.executeUpdate("DELETE FROM SearchableStock WHERE market = ?", [market]) // GORM is just too slow for this
                    
                    // now add them back
                    action.data.each { stock ->
                        def sql = new Sql(dataSource_temp)
                        stock.setMarket(market)
                        
                        sql.execute("insert into searchable_stock (version, ticker, name, industry, sector, market, ipo_year, last_sale, market_cap) values (0.1, ?, ?, ?, ?, ?, ?, ?, ?)",
                            [stock.getTicker(), stock.getName(), stock.getIndustry(), stock.getSector(), stock.getMarket(), stock.getIpoYear(), stock.getLastSale(), stock.getMarketCap()])
                    }
                } else if (action.command == "giveup") {
                    println "Unable to find any stocks for market ${market}, try again soon."
                }
            }
            
            if (! hasSuccessfullyLoaded && successfulMarkets.size() >= 2) {
                hasSuccessfullyLoaded = true
            }
        }
        
        println "Finished, total time ${new Date().getTime() - fullStart}"
    }
    
    private def determineCacheActionForMarkets(markets) {
        def actions = [:]
        
        markets.each { market ->
            def action = determineActionForMarket(market)
            actions[market] = action
        }
        
        actions
    }
    
    private def determineActionForMarket(market) {
        def action = [:]
        action.command = "skip" // do nothing by default
        
        // try to download the CSV files from the API
        def url = buildURLForMarketStocksDownload(market)
        
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
            if (SearchableStock.countByMarket(market) > 0) {
                // there are, so the stocks in database are newer than our old backup caches
                // therefore do nothing
                println "Not touching stocks for market ${market}; can't download from server, database has newer than backups"
            } else {
                println "Database had no entries for market ${market}; using backup cache file"

                // get stored cache file as a last backup
                def resourcePath = grailsApplication.parentContext.getResource(File.separator).file.getAbsolutePath()
                def appPath = resourcePath.substring(0, resourcePath.lastIndexOf(File.separator)) + File.separator + "grails-app" + File.separator
                def cacheDir = appPath + "conf" + File.separator + "stockcsv-cache" + File.separator
                def dir = new File(cacheDir)
                
                FilenameFilter filter = new StockCacheFilenameFilter(market + "-")
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
    
    def cacheHasLoaded() {
        hasSuccessfullyLoaded
    }
    
    private URL buildURLForMarketStocksDownload(market) {
        "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=${market}&render=download".toURL()
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
            // Symbol, Name, LastSale, MarketCap, ADR TSO, IPOyear, Sector, industry, Summary Quote
            SearchableStock stock = new SearchableStock()
            
            (0..7).each { i ->
                nextLine[i] = StringEscapeUtils.unescapeHtml(nextLine[i])
            }
            
            if (nextLine[0].contains("^") || nextLine[0].contains("/")) {
                continue
            }
            
            stock.setTicker(nextLine[0].trim()) // this *should* always be set
            stock.setName(nextLine[1] == "n/a" ? nextLine[0] : nextLine[1])
            stock.setLastSale(nextLine[2] == "n/a" ? (- 1) : nextLine[2].toDouble())
            stock.setMarketCap(nextLine[3].toDouble())
            stock.setIpoYear(nextLine[5] == "n/a" ? (- 1) : nextLine[5].toInteger())
            stock.setSector(nextLine[6] == "n/a" ? "" : nextLine[6])
            stock.setIndustry(nextLine[7] == "n/a" ? "" : nextLine[7])

            stocks.add(stock)
        }
        
        stocks
    }
}
