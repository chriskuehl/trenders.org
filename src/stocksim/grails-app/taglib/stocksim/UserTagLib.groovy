package stocksim

import org.springframework.web.context.request.RequestContextHolder as RCH 

class UserTagLib {
    static namespace = "user"
    def webRequest
    
    def ifLoggedIn = { attrs, body ->
        if (false) { // TODO: add some logged-in logic here
            out << body()
        }
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
}
