package stocksim

import stocksim.temp.*

class SearchController {
    SearchService searchService
    
    def index() {
        SearchableStock[] results = searchService.searchForStocks(params["query"])
        
        render "<ul>"
        results.each { result ->
            render "<li>${result.getTicker()}</li>"
        }
        render "</ul>"
    }
}
