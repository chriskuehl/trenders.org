package stocksim

import groovy.sql.Sql

class SearchService {
    def financeService
    def dataSource_temp
    
    def getResults(def query, int limit, def sector) {
        def results = []
        def sql = new Sql(dataSource_temp)
        def term = YahooQueryService.makeAlphaNumeric(query).toLowerCase() + "%"
        def mquery
        def p
        
        if (sector) {
            mquery = "SELECT * FROM stock WHERE sector = ? ORDER BY market_cap DESC LIMIT ?"
            p = [query, limit]
        } else {
            mquery = "SELECT * FROM stock WHERE LOWER(name) LIKE ? OR LOWER(ticker) LIKE ? ORDER BY market_cap DESC LIMIT ?"
            p = [term, term, limit]
        }
        
        sql.eachRow(mquery, p) { row ->
            def stock = financeService.getStock(row.ticker)
            results.add(stock)
        }
        
        sql.close()
        
        results
    }
}