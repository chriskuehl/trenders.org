package org.trenders

class AdminTagLib {
    static namespace = "admin"
    static returnObjectForTags = ["getUsers", "getUserSessions", "getClassrooms"]
    
    def getUsers = {
        User.list()
    }
    
    def getUserSessions = {
        UserSession.list()
    }
    
    def getClassrooms = {
        Classroom.list()
    }
}
