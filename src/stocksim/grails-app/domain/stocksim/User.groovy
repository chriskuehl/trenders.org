package stocksim

class User {
    // TODO: add some of these other features like passwords, etc.
    
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
    
    // some custom getters
    def getDisplayName() {
        displayName
    }
    
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
    
    def getTotalAssets() {
        getBalance()
    }
    
    def getPrettyTotalAssets() {
        def utilService
        utilService.makePretty(getTotalAssets())
    }
    
    def getPrettyBalance() {
        def utilService
        utilService.makePretty(getBalance())
    }
}
