package stocksim.data

import stocksim.*
import au.com.bytecode.opencsv.CSVReader

class StockFullDataService {
    final def batchSize = 200 // Yahoo limits to 200 per query
    def stockDataHelperService
    
    // for each existing ticker, we need to get the info
    def update() {
        def tickerList = stockDataHelperService.getTickerList()
        
        // split into batches of 200
        def batches = splitListIntoSubListsOfSize(tickerList, batchSize)
        
        // iterate through each batch and fetch the new data
        batches.each { batch ->
            updateBatch(batch)
        }
    }
    
    def updateBatch(def tickers) {
        // example URL: http://download.finance.yahoo.com/d/quotes.csv?s=RHT,MSFT,NOV&f=sl1d1t1c1ohgv&e=.csv
        def url = buildURLForTickers(tickers)
        def connection = url.openConnection()
        
        def rows = readRowsFromConnection(connection)
        
    }
    
    def readRowsFromConnection(def connection) {
        def reader = new CSVReader(new InputStreamReader(connection.getInputStream()))
        readRowsFromCSVReader(reader)
    }
    
    def readRowsFromCSVReader(def reader) {
        def stocks = []
        def nextLine
        
        // read each stock from the CSV file and add it to our array
        println "========================="
        while ((nextLine = reader.readNext()) != null) {
            println nextLine
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
