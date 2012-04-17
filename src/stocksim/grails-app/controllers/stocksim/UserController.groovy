package stocksim
import javax.servlet.http.Cookie

class UserController {
    def logout() {
        if (! params.s) { // silent?
            new UserAlert(type: "success", title: "You've logged out!", message: "Visit the home page to log in again.").add(flash)
        }
        
        def cookiesToRemove = ["user", "token"]
        
        cookiesToRemove.each { name ->
            def cookie = request.cookies.find { it.getName() == name }
            cookie.setMaxAge(0)
            
            response.addCookie(cookie)
        }
        
        if (params.r) {
            redirect(params.r)
        } else {
            redirect(mapping: "home")
        }
        
        render "You've been logged out. Redirecting..."
    }
}
