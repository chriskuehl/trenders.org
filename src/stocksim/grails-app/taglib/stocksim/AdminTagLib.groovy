package stocksim

class AdminTagLib {
    static namespace = "admin"
    static returnObjectForTags = ["getUsers", "getUserSessions"]
    
    def getUsers = {
        User.list()
    }
    
    def getUserSessions = {
        UserSession.list()
    }
}
