package stocksim.admin

import stocksim.*
import javax.servlet.http.Cookie

class AdminUserController {
    def userService
    
    def index() {
        render(view: "/admin/user/index")
    }
    
    def addUser() {
        def user = userService.addUser(params.email)
        redirect(action: "become", params: [user: user.getId()])
    }
    
    def become() {
        // create a new session
        def user = User.get(params.user)
        def userSession = userService.makeNewSession(user)
        
        def userIdCookie = new Cookie("user", user.getId().toString())
        userIdCookie.maxAge = 60 * 60 * 24 * 365 * 10 // 10 years
        userIdCookie.setHttpOnly(true)
        
        response.addCookie(userIdCookie)
        
        def userHashCookie = new Cookie("token", userSession.getSessionTokenHash())
        userHashCookie.maxAge = 60 * 60 * 24 * 365 * 10 // 10 years
        userHashCookie.setHttpOnly(true)
        
        response.addCookie(userHashCookie)
        
        redirect(action: "index")
    }
}