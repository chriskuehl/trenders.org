package org.trenders.data

import org.trenders.*

class StockDataHelperService {
    def sessionFactory_temp
    
    def getTickerList(sql) {
        def tickerList = []
        
        sql.eachRow("select ticker from stock") { stockRow ->
            tickerList.add(stockRow.ticker.toLowerCase())
        }
        
        tickerList
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
