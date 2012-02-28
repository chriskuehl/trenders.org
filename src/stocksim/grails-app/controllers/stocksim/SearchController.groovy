package stocksim

import stocksim.temp.*
import grails.converters.JSON

class SearchController {
    SearchService searchService
    
    def refresh() {
        def start = new Date().getTime()
        searchService.updateCache()
        render "Refreshed cache in ${(new Date().getTime() - start) / 1000} seconds."
    }
    
    def index() {
        render "<form method=\"GET\">"
        render "    <p>Search: <input type=\"text\" value=\"${params["query"] ? params["query"] : ""}\" name=\"query\" /> <input type=\"submit\" value=\"Search\" />"
        render "    <p>Try searching for a company name (Apple) or a stock (AAPL). Partial matches are OK.</p>"
        render "</form>"
        
        if (params["query"]) {
            def results = searchService.searchForStocks(params["query"], 100)
            
            render "<p>Results for your query (${results.size()}):</p>"
            render "<table border=\"1\">"
            render "    <tr>"
            render "        <th>Ticker</th>"
            render "        <th>Name</th>"
            render "        <th>Last Sale</th>"
            render "        <th>Industry</th>"
            render "        <th>Sector</th>"
            render "        <th>Market Cap</th>"
            render "        <th>IPO Year</th>"
            render "        <th>Market</th>"
            render "    </tr>"
            
            results.each { result ->
                render "<tr>"
                render "    <td>${result.getTicker()}</td>"
                render "    <td>${result.getName()}</td>"
                render "    <td>${result.getLastSale()}</td>"
                render "    <td>${result.getIndustry()}</td>"
                render "    <td>${result.getSector()}</td>"
                render "    <td>\$${result.getMarketCap()}</td>"
                render "    <td>${result.getIpoYear()}</td>"
                render "    <td>${result.getMarket()}</td>"
                render "</tr>"
            }
            
            render "</table>"
        }
    }
    
    def json() {
        def results = searchService.searchForStocks(params["query"], 5)
        render results as JSON
    }
}
