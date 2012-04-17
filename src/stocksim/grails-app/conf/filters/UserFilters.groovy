package filters
import javax.servlet.http.Cookie

class UserFilters {
    def userService
    
    def filters = {
        checkUserFilters(uri: "/**") {
            before = {
                def userID
                def sessionToken

                // try to find them in cookies
                if (request.cookies) {
                    try {
                        userID = request.cookies.find { it.getName() == "user" }.getValue() // "user"
                    } catch (Exception ex) {}
                    
                    try {
                        sessionToken = request.cookies.find { it.getName() == "token" }.getValue() // "token"
                    } catch (Exception ex) {}
                }

                // check flash for any user cookies that have just been set
                /*if (flash.userCookies) {
                    flash.userCookies.each { name, value ->
                        def cookie = new Cookie(name, value)
                        cookie.maxAge = 60 * 60 * 24 * 365 * 10 // 10 years
                        cookie.setHttpOnly(true)
                        cookie.setPath("/")
                        
                        if (name == "user") {
                            userID = value
                        } else if (name == "token") {
                            sessionToken = value
                        }
                        
                        response.addCookie(cookie) // send it back to the user
                    }
                }*/
                
                // find the user based on the session token
                // TODO: use the user too, and probably hash it or so with lots
                // of extra entropy so it's less revealing
                // TODO: hash the session token since it's a p-word equivalent
                
                def user = null
                
                if (sessionToken) {
                    user = userService.getUserForSessionToken(sessionToken)
                }
                
                if (user != null) {
                    println "here's the user: ${user}"
                    println "here's the user i got: ${user.getEmail()}"
                }
            }
        }
    }
}