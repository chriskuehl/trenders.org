package stocksim

import org.springframework.web.context.request.RequestContextHolder as RCH

class UserTagLib {
    static namespace = "user"
    static returnObjectForTags = ["getClassmates"]
    def webRequest
    
    def ifLoggedIn = { attrs, body ->
        if (request.user) {
            out << body()
        }
    }
    
    def ifNotLoggedIn = { attrs, body ->
        if (! request.user) {
            out << body()
        }
    }
    
    def att = { attrs ->
        out << request.user[attrs.req]
    }
    
    def displayAlert = { attrs, body ->
        out << render(template: "/userAlert/userAlert", model: [alert: new UserAlert(type: attrs.type, title: attrs.title, message: attrs.message)])
    }
    
    def userAgentChoose = { attrs ->
        def userAgent = RCH.currentRequestAttributes().currentRequest.getHeader("User-Agent")
        def iOS = userAgent.contains("iPad") || userAgent.contains("iPhone") || userAgent.contains("iPod") || userAgent.contains("Android")
        
        if (iOS) {
            out << attrs.iOS
        } else {
            out << attrs.other
        }
    }
    
    def displayName = {
        out << request.user.getDisplayName()
    }
    
    def totalAssets = {
        out << request.user.getPrettyTotalAssets()
    }
    
    def getClassmates = {
        return User.findByClassroom(request.user.getClassroom())
    }
}
