package stocksim

import java.util.Random

class UserService {
    def random = new Random()
    
    public def generateTokenHash() {
        def randomCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        def tokenLength = 50 // TODO: move this to a config file
        def hash = ""
        
        (0..tokenLength).each {
            def randomChar = randomCharacters[random.nextInt(randomCharacters.length())]
            hash += randomChar
        }
        
        hash
    }
}
