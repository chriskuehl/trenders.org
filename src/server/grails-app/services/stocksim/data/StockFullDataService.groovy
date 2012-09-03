package stocksim.data

class StockFullDataService {
    final def batchSize = 20
    def stockDataHelperService
    
    // for each existing ticker, we need to get the info
    def update() {
        def tickerList = stockDataHelperService.getTickerList()
        
        // split into batches of 20
        def batches = splitListIntoSubListsOfSize(tickerList, batchSize)
        
        // iterate through each batch and fetch the new data
        batches.each { batch ->
            // example URL: http://download.finance.yahoo.com/d/quotes.csv?s=RHT,MSFT,NOV&f=sl1d1t1c1ohgv&e=.csv
            println batch
        }
    }
    
    def splitListIntoSubListsOfSize(def list, def listSize) {
        def lists = [[]]
        
        // now add each item to a sublist
        list.each { item ->
            // add to the last sublist
            lists[lists.size() - 1].add(list[0])
            
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
