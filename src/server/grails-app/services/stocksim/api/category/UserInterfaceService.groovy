package stocksim.api.category

import stocksim.api.*
import stocksim.*

class UserInterfaceService {
    def userService
    
    // TODO: eventually this is a security hole that should be fixed
    // (it is possible to see if an email is registered here since requests take longer
    //  when we have to compute the password hash, which is only when an account is registered)
    // 
    // ...but in theory this is not that big of a deal, especially since you can tell
    // if an email is registered simply by trying to register as that email
    public def login = { response, action, params ->
        def email = params.email
        def password = params.password
        
        response.apiCode = AppInterface.codes.BAD_LOGIN_INFO
        
        if (email != null && password != null) {
            def user = User.findByEmail(email)
            
            if (user != null) {
                if (user.passwordMatches(password)) {
                    response.apiCode = AppInterface.codes.OK
                    userService.becomeAPI(response, user)
                }
            }
        }
    }
}
