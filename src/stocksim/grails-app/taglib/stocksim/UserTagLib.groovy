package stocksim

class UserTagLib {
    static namespace = "user"
    
    def ifLoggedIn = { attrs, body ->
        if (false) { // TODO: add some logged-in logic here
            out << body()
        }
    }
    
    def displayAlert = { attrs, body ->
        out << render(template: "/userAlert/userAlert", model: [alert: new UserAlert(type: attrs.type, title: attrs.title, message: attrs.message)])
    }
}
