package stocksim

import groovy.sql.Sql

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
}