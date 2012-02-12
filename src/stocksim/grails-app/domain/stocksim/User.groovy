package stocksim

class User {
    static contraints = {
        email(email: true, unique: true)
    }
    
    String email
    boolean emailConfirmed = false
    String displayName = "Guest"
    String passwordHash = null
    Classroom classroom = null
    
    // status
    boolean disabled = false
    
    // information from account creation
    String registerIP
    Date registerTime
    String registerUserAgent
    
    // information from last visit
    String lastSeenIP
    Date lastSeenTime
    String lastSeenUserAgent
    String lastSeenURL
}
