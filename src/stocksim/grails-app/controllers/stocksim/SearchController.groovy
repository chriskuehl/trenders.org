package stocksim

import stocksim.temp.*

class SearchController {
    SearchService searchService
    
    def index() {
        render "<form method=\"GET\">"
        render "    <p>Search: <input type=\"text\" value=\"${params["query"] ? params["query"] : ""}\" name=\"query\" /> <input type=\"submit\" value=\"Search\" />"
        render "    <p>Try searching for a company name (Apple) or a stock (AAPL). Partial matches are OK.</p>"
        render "</form>"
        
        if (params["query"]) {
            def results = searchService.searchForStocks(params["query"])
            
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
}
