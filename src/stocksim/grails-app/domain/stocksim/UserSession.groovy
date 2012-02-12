package stocksim

class UserSession {
    User user
    String sessionTokenHash
    
    // last seen info
    String lastSeenIP
    Date lastSeenTime
    String lastSeenUserAgent
    String lastSeenURL
}
