package stocksim

import stocksim.temp.*
import grails.converters.JSON

class SearchController {
    static defaultAction = "browse"
    
    def searchService
    def utilService
    
    /*
    def refresh() {
        def start = new Date().getTime()
        searchService.updateCache()
        render "Refreshed cache in ${(new Date().getTime() - start) / 1000} seconds."
    }*/
    
    def sector() {
        render(view: "/browse")
    }
    
    def browse() {
        render(view: "/search")
    }
    
    def json() {
        def results = searchService.getResults(params["query"], 5, false)
        def resultsPlain = []
        
        results.each { result ->
            def map = [:]
            
            map.name = result.getName()
            map.sector = utilService.trim(result.getSector(), 15, true)
            map.ticker = result.getTicker()
            map.lastSale = result.getLastSale()
            
            resultsPlain.add(map)
        }
        
        render resultsPlain as JSON
    }
}
