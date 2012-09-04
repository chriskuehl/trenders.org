package stocksim.dev

import stocksim.*
import stocksim.temp.*
import stocksim.exception.*
import grails.converters.XML

class SandboxController {
    static defaultAction = "viewstocks"
    def userService
    def cacheService
    def financeService
    def mailService
    def googleFinanceService
    
    def gainersLosers() {
        def map = GoogleFinanceService.getGainersLosers()
        render map
    }
    
    def allTickers() {
        def allTickers = Stock.findAll().collect { it.ticker.toLowerCase() }
        render allTickers
    }
    
    def getAllStocks() {
        def stocks = OwnedStock.executeQuery("select distinct a.ticker from OwnedStock a")
        def f = ""
        
        stocks.each { stock ->
            f += stock + ","
        }
        
        //render f
        render SearchableStock.findAll().size()
    }
    
    def email() {
        mailService.sendMail {
            multipart true
            
            to "chris@techxonline.net"
            from "chris@trenders.org"
            subject "Hello!"
            html 'this is <b>some</b> text'
            body 'this is some plain text'
        }
        
        render "it worked!"
    }
    
    def putAllInCache() {
        def allStocks = SearchableStock.findAll()
        def allTickers = []
        
        allStocks.each{ stock ->
            allTickers.add(stock.getTicker())
        }
        
        def m = 50
        
        for (int i = 0; i < allTickers.size(); i += m) {
            def tickers = []
                
            for (int j = 0; j < m; j ++) {
                if (allTickers[i + j]) {
                    tickers.add(allTickers[i + j])
                }
            }
            
            def bad = true
            
            while (bad) {
                println "Trying: ${tickers}"
                try {
                    financeService.getStocks(tickers)
                    bad = false
                    println "GOOD"
                    break;
                } catch (Exception ex) {
                    println "BAD"
                    
                    try {
                        Thread.sleep(1000)
                    } catch (Exception exx) {
                        
                    }
                }
            }
        }
    }
    
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