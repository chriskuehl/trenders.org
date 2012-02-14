package stocksim

import stocksim.temp.*
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
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
    
    private void updateCache() {
        //SearchableStock.list()*.delete(flush: true)
        // the above is the proper way to do this, but below is the better way
        // because otherwise it can take 5 minutes to delete just a few thousand
        // entries, even from an in-memory DB like H2
        SearchableStock.executeUpdate('delete from SearchableStock')
        
        // download CSV files from nasdaq.com for the two important markets
        def markets = ["nasdaq", "nyse"] // TODO: read from config file?
        
        // TODO: find a way to speed this up
        SearchableStock.withTransaction {
            markets.each { market ->
                println "Loading market ${market}"

                def url = "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=${market}&render=download".toURL()
                def text = url.getText()

                // dropping into a little Java here to benefit from this nice CSV lib
                CSVReader reader = new CSVReader(new InputStreamReader(url.openStream()))

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

                println "Loaded market ${market}"
            }
        }
    }
}
