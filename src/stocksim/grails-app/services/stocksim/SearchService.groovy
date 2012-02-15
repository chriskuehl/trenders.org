package stocksim

import stocksim.temp.*
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import grails.util.BuildSettingsHolder as BSH
import au.com.bytecode.opencsv.CSVReader

class SearchService {
    def servletContext = SCH.servletContext
    def domainClass
    def sessionFactory
    
    SearchableStock[] searchForStocks(String query) {
        // should we fetch a new CSV file?
        // TODO: read time from config file?
        if (servletContext.lastSearchCacheUpdate == null || (new Date().getTime()) - servletContext.lastSearchCacheUpdate > 60) {
            println "Going to update cache"
            updateCache()
        } else {
            println "No need to update cache"
        }
    }
    
    // TODO: break this up
    private void updateCache() {
        // download CSV files from nasdaq.com for the two important markets
        def markets = ["nasdaq", "nyse"] // TODO: read from config file?
        
        // TODO: find a way to speed this up
        def loadTime = 0
        def fullStart = new Date().getTime()
        
        SearchableStock.withTransaction {
            markets.each { market ->
                println "Loading market ${market}"
                
                File file = null
                
                def start = new Date().getTime()
                
                def url = "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=${market}&render=download".toURL()
                def connection = url.openConnection()
                
                // TODO: move to app config?
                connection.setReadTimeout(2500)
                connection.setConnectTimeout(2500)
                
                // nasdaq.com goes down a lot, so fall back on an old cache if necessary
                try {
                    def text = connection.getInputStream().getText()
                    file = File.createTempFile("tempstocks", "csv")
                    file.deleteOnExit()
                    file.write(text)
                } catch (IOException ex) {
                    ex.printStackTrace()
                    // use an old version
                    println "Unable to download latest CSV file for market ${market}"
                    
                    // are there already stocks in the table? if so, just leave it as-is
                    if (SearchableStock.countByMarket(market) > 0) {
                        // no need to update anything, it's already fine
                        println "Not touching stocks for market ${market}"
                    } else {
                        println "Loading stocks for market ${market} from local cache file"
                        
                        // get stored cache file as a last backup
                        File dir = new File("${BSH.settings.baseDir}/grails-app/conf/stockcsv-cache/")
                        FilenameFilter filter = new StockCacheFilenameFilter(market + "-")
                        File[] files = dir.listFiles(filter)
                        
                        if (files.length > 0) {
                            file = files[0]
                        }
                    }
                }
                
                if (file) {
                    // clear the table
                    //SearchableStock.list()*.delete(flush: true)
                    // the above is the proper way to do this, but below is the better way
                    // because otherwise it can take 5 minutes to delete just a few thousand
                    // entries, even from a nice and fast in-memory DB like H2
                    SearchableStock.executeUpdate("DELETE FROM SearchableStock WHERE market = '${market}'") // TODO: escape market?
        
                    def urlLoadTime = new Date().getTime() - start

                    println "Loaded file: ${urlLoadTime}"
                    loadTime += urlLoadTime

                    // dropping into a little Java here to benefit from this nice CSV lib
                    CSVReader reader = new CSVReader(new FileReader(file))

                    def nextLine = reader.readNext() // skip the first line (headers)

                    while ((nextLine = reader.readNext()) != null) {
                        // headers:
                        // Symbol, Name, LastSale, MarketCap, ADR TSO, IPOyear, Sector, industry, Summary Quote
                        SearchableStock stock = new SearchableStock()

                        stock.setTicker(nextLine[0])
                        stock.setName(nextLine[1])
                        stock.setLastSale(nextLine[2] == "n/a" ? (- 1) : nextLine[2].toDouble())
                        stock.setMarketCap(nextLine[3].toDouble())
                        stock.setIpoYear(nextLine[5] == "n/a" ? (- 1) : nextLine[5].toInteger())
                        stock.setSector(nextLine[6])
                        stock.setIndustry(nextLine[7])
                        stock.setMarket(market)

                        stock.save()
                    }
                } else {
                    println "Not getting files for market ${market}"
                }

                println "Loaded market ${market}"
            }
        }
        
        println "Finished, total time ${new Date().getTime() - fullStart - loadTime}"
    }
}
