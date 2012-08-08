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
    
    def generateClassCode() {
        def randomCharacters = "0123456789"
        def tokenLength = 5 // TODO: move this to a config file
        def code = ""
        
        (0..tokenLength).each {
            def randomChar = randomCharacters[random.nextInt(randomCharacters.length())]
            code += randomChar
        }
        
        code
    }
    
    def makeNewSession(def user) {
        def userSession = new UserSession(user: user, sessionTokenHash: generateTokenHash()).save()
        userSession
    }
    
    def become(def response, def user) {
        def userSession = makeNewSession(user)
        
        /* IGNORE BELOW, SEEMS FIXED (TODO: testing?)
           Safari has problems accepting cookies on 302 redirects, so set it in
           the next flash instead:
           http://stackoverflow.com/questions/1144894/safari-doesnt-set-cookie-but-ie-ff-does
           
           Chrome/Chromium occasionaly has this issue too based on some random
           Google reports, but it seems to mostly be a Safari issue
        */
        
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
        
        /* see above
        flash.userCookies = [:]
        flash.userCookies.user = user.getId().toString()
        flash.userCookies.token = userSession.getSessionTokenHash() */
    }
    
    def addUser(def email) {
        def user = new User(email: email).save()
        // TODO: send them an email
        user
    }
    
    def createClassroom(def teacher) {
        new Classroom(teacher: teacher, classCode: generateClassCode()).save()
    }
    
    def getUserForSessionToken(def sessionToken) {
        // TODO: hash this session token in the database
        def session = UserSession.findBySessionTokenHash(sessionToken)
        
        if (session != null) {
            return session.getUser()
        }
        
        null
    }
}
