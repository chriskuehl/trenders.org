package stocksim
import javax.servlet.http.Cookie

class UserController {
    def signup() {
        render(view: "/signup")
    }
    
    // TODO: email user on account creation
    // TODO: login (less important since permanent login upon account creation)
    // TODO: recover password via email
    
    def logout() {
        if (! params.s) { // silent?
            new UserAlert(type: "success", title: "You've logged out!", message: "Visit the home page to log in again.").add(flash)
        }
        
        def cookiesToRemove = ["user", "token"]
        
        cookiesToRemove.each { name ->
            def cookie = request.cookies.find { it.getName() == name }
            cookie.setMaxAge(0)
            cookie.setHttpOnly(true)
            cookie.setPath("/")
            cookie.setValue("")
            
            response.addCookie(cookie)
        }
        
        if (params.r) {
            redirect(params.r)
        } else {
            redirect(mapping: "home")
        }
        
        /*
        if (params.r) { // TODO: fix XSS vulnerability
            render("<script>window.location = \"" + params.r + "\";</script>")
        } else {
            render("<script>window.location = \"" + createLink(mapping: "home") + "\";</script>")
        }
        */
        
        render "You've been logged out. Redirecting..."
    }
}
