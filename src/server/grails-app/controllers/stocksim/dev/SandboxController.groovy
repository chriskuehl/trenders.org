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
    def hashingService
    def emailService
    
    def gainersLosers() {
        def map = GoogleFinanceService.getGainersLosers()
        render map
    }
    
    def sendEmails() {
        User.findAll([fetch: "eager"]).each { user ->
            def body =
                "Hello there, ${user.displayName}.\n\n" +
                "Thanks for beta-testing trenders.org. The dozens of willing testers " +
                "helped us to perfect trenders.org last year. Since then, we've done even " +
                "more work to improve the speed and reliability of the site, including " +
                "a complete rewrite of the core methods for fetching data.\n\n" +
                "As such, we'd like to ask for your help in testing once again. " +
                "This coming Monday, trenders.org will be put to the test in an actual " +
                "economics classroom at WCHS. Before the end of this week, we hope to " +
                "have another round of testing completed to ensure everything goes smoothly " +
                "come Monday.\n\n" +
                "If you've got a few minutes, we'd really appreciate it if you'd visit " +
                "trenders.org and browse around. Maybe update your portfolio or see how " +
                "the summer treated you.\n\n" +
                "Once you're done, or if you discover any issues, please click \"Submit Testing Report\" " +
                "at the top of any page and tell us a little bit about your experience.\n\n" +
                "Thanks again for your help. I hope to receive a testing report from you soon!\n\n" +
                "Chris Kuehl\n\n"

            def passHash = user.passwordHash

            body += "P.S.: "

            if (passHash == null) {
                body += "You haven't created a password yet. To create one, visit: " + user.getResetPasswordURL()
                user.save()
            } else {
                body += "You've already created a password. To login, visit: http://trenders.org/login"
            }

            body += "\n\n(if you've got any questions, you can reply to this email)"

            println "${user.displayName}: ${user.email}"

            emailService.sendMail(user.email, "Last-Minute Beta Testing: trenders.org", body)
            
        }
        
        render "ok"
    }
    
    def hashTest() {
        def start = new Date().getTime()
        def hash = hashingService.hash("testing", params.it)
        def end = new Date().getTime()
        
        render "time: ${end - start}, ${hash}"
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