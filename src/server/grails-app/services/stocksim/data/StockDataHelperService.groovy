package stocksim.data

import stocksim.*

class StockDataHelperService {
    def getTickerList() {
        Stock.findAll().collect { it.ticker.toLowerCase() }
    }
    
    def getStockColumnMappings() {
        def metadata = sessionFactory_temp.getClassMetadata(Stock)
        def columnMappings = [:]
        
        def propertyNames = metadata.getPropertyNames()
        
        propertyNames.eachWithIndex { propertyName, i ->
            def columnName = metadata.getPropertyColumnNames(i)[0]
            columnMappings[propertyName] = columnName
        }
        
        columnMappings
    }
}
