package stocksim

import java.util.Random
import javax.servlet.http.Cookie

class UserService {
    def random = new Random()
    
    def generateTokenHash() {
        def randomCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        def tokenLength = 100 // TODO: move this to a config file
        def hash = ""
        
        (0..tokenLength).each {
            def randomChar = randomCharacters[random.nextInt(randomCharacters.length())]
            hash += randomChar
        }
        
        hash
    }
    
    def makeNewSession(def user) {
        def userSession = new UserSession(user: user, sessionTokenHash: generateTokenHash()).save()
        userSession
    }
    
    def become(def response, def user) {
        def userSession = makeNewSession(user)
        
        def userIdCookie = new Cookie("user", user.getId().toString())
        userIdCookie.maxAge = 60 * 60 * 24 * 365 * 10 // 10 years
        userIdCookie.setHttpOnly(true)
        userIdCookie.setPath("/")
        
        response.addCookie(userIdCookie)
        
        def userHashCookie = new Cookie("token", userSession.getSessionTokenHash())
        userHashCookie.maxAge = 60 * 60 * 24 * 365 * 10 // 10 years
        userHashCookie.setHttpOnly(true)
        userHashCookie.setPath("/")
        
        response.addCookie(userHashCookie)
    }
    
    def addUser(def email) {
        def user = new User(email: email).save()
        // TODO: send them an email
        user
    }
}
