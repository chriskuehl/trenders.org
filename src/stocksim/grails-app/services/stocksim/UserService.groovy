package stocksim

import java.util.Random

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
    
    def addUser(def email) {
        def user = new User(email: email).save()
        // TODO: send them an email
        user
    }
}
