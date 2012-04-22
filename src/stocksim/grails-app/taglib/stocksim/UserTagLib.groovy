package stocksim

import org.springframework.web.context.request.RequestContextHolder as RCH 
import java.text.DecimalFormat

class UserTagLib {
    static namespace = "user"
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
    
    def totalAssets = {
        
        def amount = request.user.balance
        def formatter = new DecimalFormat("#,###")

        out << formatter.format(amount)
    }
}
