package stocksim

import java.util.Date
import java.text.DecimalFormat

class User {
    // TODO: add some of these other features like passwords, etc.
    def utilService
    def financeService
    
    static constraints = {
        email(email: true, unique: true)
        displayName(nullable: true)
        passwordHash(nullable: true)
        classroom(nullable: true)
        registerIP(nullable: true)
        registerTime(nullable: true)
        registerUserAgent(nullable: true)
        lastSeenIP(nullable: true)
        lastSeenTime(nullable: true)
        lastSeenUserAgent(nullable: true)
        lastSeenURL(nullable: true)
    }
    
    static transients = ["orderedHistoryEvents"]
    
    static mapping = {
        ownedStocks lazy: false
        historyEvents lazy: false
    }
    
    static hasMany = [ownedStocks: OwnedStock, historyEvents: HistoryEvent]
    
    String email
    boolean emailConfirmed = false
    String displayName = null
    String passwordHash = null
    Classroom classroom = null
    double balance = 100000
    
    // status
    boolean disabled = false
    boolean isGuest = true
    
    // information from account creation
    String registerIP
    Date registerTime
    String registerUserAgent
    
    // information from last visit
    String lastSeenIP
    Date lastSeenTime
    String lastSeenUserAgent
    String lastSeenURL
    
    // some helper methods
    def updateLastSeen(def request) {
        lastSeenIP = request.getRemoteAddr()
        lastSeenTime = new Date()
        lastSeenUserAgent = request.getHeader("User-Agent")
        lastSeenURL = request.getRequestURL()
        
        save()
    }
    
    def createNewSession() {
        def session = new UserSession()
        
        session.user = this
        session.sessionTokenHash = UserService.generateTokenHash()
        
        session.lastSeenIP = request.getRemoteAddr()
        session.lastSeenTime = new Date()
        session.lastSeenUserAgent = request.getHeader("User-Agent")
        session.lastSeenURL = request.getRequestURL()
    }
    
    def getClassmates() {
        User.findAllByClassroom(getClassroom())
    }
    
    def getOwnedTickers() {
        def tickers = []
        def s = ownedStocks
        
        s.each { stock ->
            tickers.add(stock.getTicker())
        }
        
        tickers
    }
    
    def getPortfolioValue() {
        def tickers = []
        def portfolioValue = 0
        
        def s = ownedStocks
        
        s.each { stock ->
            tickers.add(stock.getTicker())
        }
        
        def stocks = financeService.getStocks(tickers)
        
        s.each { stock ->
            portfolioValue += stocks[stock.getTicker().toLowerCase()].getValue() * stock.getQuantity()
        }
        
        portfolioValue
    }
    
    def getMoneySpentOnPortfolio() {
        def money = 0
        def s = ownedStocks
        
        s.each { stock ->
            money += stock.getTotalSpent()
        }
        
        money
    }
    
    def getPrettyMoneySpentOnPortfolio() {
        makePretty(getMoneySpentOnPortfolio())
    }
    
    def getPrettyPortfolioValue() {
        makePretty(getPortfolioValue())
    }
    
    def getTotalAssets() {
        getBalance() + getPortfolioValue()
    }
    
    def getPrettyTotalAssets() {
        makePretty(getTotalAssets())
    }
    
    def getPrettyBalance() {
        makePretty(getBalance())
    }
    
    def getMaxPurchasableStocks(price) {
        Math.floor((getBalance() - 8.95) / price)
    }
    
    def getNumberOwned(def ticker) {
        def ownedStock = ownedStocks.find { it.getTicker().toLowerCase() == ticker.toLowerCase() }
        
        if (ownedStock) {
            return ownedStock.getQuantity()
        }
        
        0
    }
    
    def purchaseStocks(def stock, def num) {
        if (num > getMaxPurchasableStocks(stock.getValue())) {
            return false
        }
        
        def totalPrice = 8.95 + (stock.getValue() * num)
        def ownedStock = ownedStocks.find { it.getTicker().toLowerCase() == stock.getTicker().toLowerCase() }
        def existed = ownedStock != null
        
        if (! existed) {
            ownedStock = new OwnedStock(ticker: stock.getTicker())
            addToOwnedStocks(ownedStock)
        } else {
            ownedStock = OwnedStock.get(ownedStock.getId())
        }
        
        ownedStock.setQuantity((ownedStock.getQuantity() + num).toInteger())
        ownedStock.setTotalSpent(ownedStock.getTotalSpent() + totalPrice)
        
        ownedStock.save(flush: true)
        
        // add the history event
        def event = new HistoryEvent(ticker: stock.getTicker().toLowerCase(), date: new Date())
        addToHistoryEvents(event)
        
        event.setTicker(stock.getTicker())
        event.setDate(new Date())
        event.setWasPurchase(true)
        event.setQuantity(num.toInteger())
        event.setMoney(totalPrice)
        
        event.save()
        
        // save
        
        setBalance(balance - totalPrice)
        save(flush: true)
        
        true
    }
    
    def sellStocks(def stock, def num) {
        def ownedStock = ownedStocks.find { it.getTicker().toLowerCase() == stock.getTicker().toLowerCase() }
        
        if (ownedStock == null || ownedStock.getQuantity() < num) {
            return false
        }
        
        def totalPrice = (stock.getValue() * num)
        
        setBalance(Math.max(getBalance() - 8.95, 0))
        setBalance(getBalance() + totalPrice)
        
        ownedStock.quantity -= num
        ownedStock.totalSpent += 8.95
        ownedStock.totalSpent -= totalPrice
        
        if (ownedStock.quantity > 0) {
            ownedStock.save(flush: true)
        } else {
            removeFromOwnedStocks(ownedStock)
            ownedStock.delete(flush: true)
        }
        
        // add the history event
        def event = new HistoryEvent(ticker: stock.getTicker().toLowerCase(), date: new Date())
        addToHistoryEvents(event)
        
        event.setDate(new Date())
        event.setTicker(stock.getTicker().toLowerCase())
        event.setWasPurchase(false)
        event.setQuantity(num)
        event.setMoney(totalPrice)
        
        event.save(flush: true)
        
        // save
        save(flush: true)
        
        true
    }
    
    def getOrderedHistoryEvents() {
        //def events = []
        
        // make a copy
        /*historyEvents.each { event ->
            events.add(event)
        }*/
        //Collections.copy(events, historyEvents)
        
        
        // now sort events
        historyEvents.sort { a, b ->
            def ad = a.getDate()
            def bd = b.getDate()
            
            if (ad == bd) {
                return 0
            } else if (ad.getTime() < bd.getTime()) {
                return -1
            } else {
                return 1
            }
        }
    }
    
    def getClassmatesByTotalAssets() {
        def classmates = getClassmates()
        classmates.sort { a, b ->
            if (a.getTotalAssets() == b.getTotalAssets()) {
                return 0
            } else if (a.getTotalAssets() < b.getTotalAssets()) {
                return 1
            } else {
                return (- 1)
            }
        }
    }
    
    def getAllHistoryCompanies() {
        def tickers = []
        
        historyEvents.each { event ->
            if (! tickers.contains(event.getTicker().toLowerCase())) {
                tickers.add(event.getTicker().toLowerCase())
            }
        }
        
        tickers
    }
    
    def makePretty(def number) {
        def formatter = new DecimalFormat("#,###")

        formatter.format(number)
    }
}
