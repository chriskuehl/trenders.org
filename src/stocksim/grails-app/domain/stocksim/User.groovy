package stocksim

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
    
    static mapping = {
        ownedStocks lazy: false
    }
    
    static hasMany = [ownedStocks: OwnedStock]
    
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
        utilService.makePretty(getMoneySpentOnPortfolio())
    }
    
    def getPrettyPortfolioValue() {
        utilService.makePretty(getPortfolioValue())
    }
    
    def getTotalAssets() {
        getBalance() + getPortfolioValue()
    }
    
    def getPrettyTotalAssets() {
        utilService.makePretty(getTotalAssets())
    }
    
    def getPrettyBalance() {
        utilService.makePretty(getBalance())
    }
    
    def getMaxPurchasableStocks(price) {
        Math.floor((getBalance() - 8.95) / price)
    }
    
    def purchaseStocks(def stock, def num) {
        if (num > getMaxPurchasableStocks(stock.getValue())) {
            return false
        }
        
        balance -= 8.95
        balance -= (stock.getValue() * num)
        
        def ownedStock = ownedStocks.find { it.getTicker().toLowerCase() == stock.getTicker().toLowerCase() }
        def existed = ownedStock != null
        
        if (! existed) {
            ownedStock = new OwnedStock(ticker: stock.getTicker())
            
            addToOwnedStocks(ownedStock)
        }
        
        ownedStock.quantity += num
        ownedStock.totalSpent += 8.95 + (stock.getValue() * num)
        
        ownedStock.save()
        save()
        
        true
    }
}
