package stocksim

class UserTagLib {
    static namespace = "user"
    
    def ifLoggedIn = { attrs, body ->
        if (false) { // TODO: add some logged-in logic here
            out << body()
        }
    }
}
