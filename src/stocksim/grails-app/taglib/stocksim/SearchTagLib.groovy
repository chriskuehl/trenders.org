package stocksim

class SearchTagLib {
    static namespace = "search"
    static returnObjectForTags = ["resultReturn"]
    
    def searchService
    
    def results = { attrs, body ->
        def query = attrs.query
        def offset = attrs.offset.toInteger()
        
        def results = searchService.getResults(query, 100)
        
        if (offset > 1) {
            for (i in (1..offset)) {
                results.remove(0)
            }
        }
        
        request.searchResults = results
        
        out << body()
    }
    
    def eachResult = { attrs, body ->
        def max = attrs.max.toInteger()
        max = Math.min(max, request.searchResults.size())
        
        for (i in (1..max)) {
            out << body()
            request.searchResults.remove(0)
        }
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