package stocksim.admin

import stocksim.*

class AdminUserController {
    def userService
    
    def index() {
        render(view: "/admin/user/index")
    }
    
    def addUser() {
        def user = userService.addUser(params.email)
        println "added a user: $user"
        redirect(action: "become", params: [user: user.getId()])
    }
    
    def become() {
        // create a new session
        def user = User.get(params.user)
        def userSession = userService.makeNewSession(user)
    }
}