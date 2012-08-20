package stocksim

import org.mindrot.jbcrypt.BCrypt

class HashingService {
    def hash(def str) {
        // 6 rounds takes about a second on my development machine; additional
        // benchmarking and testing is needed before we go live
        BCrypt.hashpw(str, BCrypt.gensalt(6))
    }
    
    def matches(def hash, def str) {
        BCrypt.checkpw(str, hash)
    }
}