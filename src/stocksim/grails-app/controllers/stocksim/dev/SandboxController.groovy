package stocksim.dev

import stocksim.*
import stocksim.exception.*
import grails.converters.XML

class SandboxController {
    static defaultAction = "viewstocks"
    def userService
    def cacheService
    
    def cacheTest() {
        def obj = cacheService.fetchFromCache("test", "date", 0.1)
        
        if (obj == null) {
            render "null<br>adding"
            cacheService.storeInCache("test", "date", new Date())
        } else {
            render "not null<br>${obj}"
        }
    }
    
    def alertTest() {
        new UserAlert(type: "error", title: "All done!", message: "The cache has been flushed.").add(flash)
        new UserAlert(type: "info", title: "All done!", message: "The cache has been flushed.").add(flash)
        new UserAlert(type: "success", title: "All done!", message: "The cache has been flushed.").add(flash)
        
        render(view: "/index")
    }
    
    def getIndex() {
        def index = params.idx
        def results = GoogleFinanceService.getInfoForAsset(index)
        render results as XML
    }
    
    def newUser() {
        def token = userService.generateTokenHash()
        render "Got: ${token}"
    }
    
    def viewstocks() {
        if (! params.stocks) {
            // there's a better way to do this...
            render "<form method=\"get\"><input type=\"text\" name=\"stocks\" /><input type=\"submit\" /></form>";
        } else {
            def stocks = params.stocks.split(",")
            render "Stocks to check:<br />"
            
            try {
                Stock[] stockObjects = FinanceService.getStocks(stocks)
                
                render "<ul>"
                
                stockObjects.each { stock ->
                    render "<li>${stock.getTickerSymbol()}: ${stock.getPrice()}</li>"
                }
                
                render "</ul>"
            } catch (BadTickerSymbolException ex) {
                render ex.getMessage()
            }
        }
    }
}