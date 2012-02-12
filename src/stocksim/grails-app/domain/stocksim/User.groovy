package stocksim

class User {
    String email
    boolean emailConfirmed
    String displayName
    String passwordHash
    Classroom classroom
    
    // status
    boolean disabled
    
    // information from account creation
    String registerIP
    Date registerTime
    String registerUserAgent
    String registerEmail
    
    // information from last visit
    String lastSeenIP
    Date lastSeenTime
    String lastSeenUserAgent
    String lastSeenURL
}
