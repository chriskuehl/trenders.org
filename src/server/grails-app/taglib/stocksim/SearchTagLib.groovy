package stocksim

class SearchTagLib {
    static namespace = "search"
    static returnObjectForTags = ["resultReturn", "getNumResults", "getResultTickers"]
    
    def searchService
    def financeService
    
    def results = { attrs, body ->
        def query
        
        if (attrs.query) {
            query = attrs.query
        } else {
            query = attrs.sector
        }
        
        def offset = attrs.offset.toInteger()
        
        def results = searchService.getResultsRaw(query, 1000, attrs.sector ? true : false)
        
        request.numResults = results.size()
        
        if (offset > 1) {
            for (i in (1..offset)) {
                results.remove(0)
            }
        }
        
        def newResults = []
        
        results.each { result ->
            newResults.add(financeService.getStock(result.ticker))
        }
        
        request.searchResults = newResults
        
        out << body()
    }
    
    def getNumResults = {
        request.numResults
    }
    
    def eachResult = { attrs, body ->
        def max = attrs.max.toInteger()
        max = Math.min(max, request.searchResults.size())
        
        for (i in (1..max)) {
            if (request.searchResults.size() > 0) {
                out << body()
                request.searchResults.remove(0)
            }
        }
    }
    
    def getResultTickers = { attrs ->
        def max = Math.min(request.searchResults.size(), attrs.max)
        def tickers = []
        
        for (i in (1..max)) {
            tickers << request.searchResults[i - 1]["ticker"]
        }
        
        tickers
    }
    
    def result = { attrs ->
        def req = attrs.req
        out << request.searchResults[0][req]
    }
    
    def resultReturn = { attrs ->
        def req = attrs.req
        request.searchResults[0][req]
    }
}