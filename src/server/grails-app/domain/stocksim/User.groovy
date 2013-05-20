package stocksim

import java.util.Date
import java.text.DecimalFormat

class User {
    // TODO: add some of these other features like passwords, etc.
    def utilService
    def financeService
    def mailService
    def userService
    def hashingService
    def emailService
    
    static constraints = {
        email email: true, unique: true, nullable: false, blank: false
        displayName nullable: false, size: (2..30), blank: false
        passwordHash nullable: true
        classroom nullable: true
        registerIP nullable: true
        registerTime nullable: true
        registerUserAgent nullable: true
        lastSeenIP nullable: true
        lastSeenTime nullable: true
        lastSeenUserAgent nullable: true
        lastSeenURL nullable: true
        passwordResetToken nullable: true
        lastPasswordChange nullable: true
    }
    
    static transients = [
        "orderedHistoryEvents", "resetPasswordURL", "classmates", "ownedTickers",
        "portfolioValue", "moneySpentOnPortfolio", "prettyMoneySpentOnPortfolio", "prettyPortfolioValue",
        "totalAssets", "prettyTotalAssets", "prettyBalance", "maxPurchasableStocks", "numberOwned",
        "classmatesByTotalAssets", "allHistoryCompanies", "lastTotalAssets", "largestInvestment", "prettyLargestInvestment"
    ]
    def lastTotalAssets
    
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
    
    String passwordResetToken = null
    
    // status
    boolean disabled = false
    boolean isGuest = true
    boolean isAdmin = false
    
    // information from account creation
    String registerIP
    Date registerTime
    String registerUserAgent
    Date lastPasswordChange
    
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
    
    def passwordMatches(def password) {
        if (passwordHash == null) {
            return false
        }
        
        return hashingService.matches(passwordHash, password)
    }
    
    def setPassword(def password) {
        passwordHash = hashingService.hash(password)
    }
    
    def createNewSession() {
        def session = new UserSession()
        
        session.user = this
        session.sessionTokenHash = UserService.generateTokenHash()
        
        session.lastSeenIP = request.getRemoteAddr()
        session.lastSeenTime = new Date()
        session.lastSeenUserAgent = request.getHeader("User-Agent")
        session.lastSeenURL = request.getRequestURL()
        
        session
    }
    
    def sendEmail(def subject, def body) {
        emailService.sendMail(email, subject, body)
    }
    
    def getResetPasswordURL() {
        passwordResetToken = userService.generateResetHash()
        return "http://trenders.org/user/reset-password/" + id + "/" + passwordResetToken
    }
    
    def sendResetPasswordEmail() {
        def subject = "Password Reset Request for trenders.org"
        def body =
            "Howdy there, ${displayName}!\n" +
            "\n" +
            "Somebody (probably you) asked us to reset your password on trenders.org. If you want to go ahead and change your password now, just visit this link:\n" +
            getResetPasswordURL() + "\n" +
            "\n" +
            "If you've got any questions, feel free to reply to this message, or visit our site: http://trenders.org/"
        
        if (passwordHash == null) {
            // send a different email if they haven't set a password yet
            subject = "Password Creation Request for trenders.org"
            
            body =
                "Howdy there, ${displayName}!\n" +
                "\n" +
                "Somebody (probably you) asked us to create a password for your account on trenders.org. If you want to go ahead and create a password now, just visit this link:\n" +
                getResetPasswordURL() + "\n" +
                "\n" +
                "If you've got any questions, feel free to reply to this message, or visit our site: http://trenders.org/"
        }
        
        sendEmail(subject, body)
    }
    
    def getClassmates() {
        User.findAllByClassroom(getClassroom()).sort { it.getDisplayName() }
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
        //def r = new Date().getTime()
        def tickers = []
        def portfolioValue = 0
        
        def s = ownedStocks
        
        s.each { stock ->
            tickers.add(stock.getTicker())
        }
        
        //println "g=" + ((new Date().getTime()) - r)
        
        // def stocks = financeService.getStocks(tickers)
        
        
        //println "h=" + ((new Date().getTime()) - r)
        
        def stockMap = financeService.stockMap
        
        s.each { stock ->
            def stockData = stockMap[stock.getTicker().toUpperCase()] // stocks[stock.getTicker().toUpperCase()]
            
            if (stockData != null) {
                portfolioValue += stockData.lastSale * stock.getQuantity()
            } else {
                println "Stock no longer exists: " + stock.getTicker().toUpperCase()
            }
        }
        
        
        //println "i=" + ((new Date().getTime()) - r)
        
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
        makePretty(getTotalAssets(true) - getBalance())
    }
    
    def calculateTotalAssets() {
        lastTotalAssets = getBalance() + getPortfolioValue()
    }
    
    def getTotalAssets(lazy) {
        if (lazy && lastTotalAssets) {
            return lastTotalAssets
        }
        
        calculateTotalAssets()
        getTotalAssets(true)
    }
    
    def getLargestInvestment() {
        def s = ownedStocks
        def mostSpent = null
        
        s.each { stock ->
            if (mostSpent == null || mostSpent[1] < stock.totalSpent) {
                mostSpent = [stock, stock.totalSpent]
            }
        }
        
        mostSpent
    }
    
    def getPrettyLargestInvestment() {
        def largestInvestment = getLargestInvestment()
        if (largestInvestment == null) {
            return null
        }
        
        largestInvestment[1] = "\$" + makePretty(largestInvestment[1])
        largestInvestment
    }
    
    def getPrettyTotalAssets() {
        makePretty(getTotalAssets(true))
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
        if (num > getMaxPurchasableStocks(stock.lastSale)) {
            return false
        }
        
        def totalPrice = 8.95 + (stock.lastSale * num)
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
        
        def totalPrice = (stock.lastSale * num)
        
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
        //println "get23"
        def classmates = getClassmates()
       // println "got classmates"
        
        def totalAssetsMap = [:]
        totalAssetsMap[this] = getTotalAssets(false)
       // println "got my assets"
        
        def f = new Date().getTime()
        
        classmates.each { classmate ->
            totalAssetsMap[classmate] = classmate.getTotalAssets(false)
        }
        
        def g = new Date().getTime()
        //println "time=" + ((g - f) / classmates.size()) + "each"
        
        // sort the classmates based on total assets
        classmates.sort { a, b ->
            if (totalAssetsMap[a] == totalAssetsMap[b]) {
                return 0
            } else if (totalAssetsMap[a] < totalAssetsMap[b]) {
                return 1
            } else {
                return (- 1)
            }
        }
        //println "end2"
        
        classmates
    }
    
    def getAllHistoryCompanies() {
        def tickers = []
        
        getHistoryEvents().each { event ->
            if (! tickers.contains(event.getTicker().toUpperCase())) {
                tickers.add(event.getTicker().toUpperCase())
            }
        }
        
        tickers
    }
    
    def makePretty(def number) {
        def formatter = new DecimalFormat("#,###")
        formatter.format(number)
    }
}
