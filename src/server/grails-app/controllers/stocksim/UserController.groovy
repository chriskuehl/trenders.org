package stocksim
import javax.servlet.http.Cookie

class UserController {
    def userService
    def financeService
    
    def login() {
        if (params.login) {
            def email = params.email
            def password = params.password
            
            println email
            println password
            
            if (email == null || email.length() <= 0) {
                new UserAlert(type: "error", title: "Please fill in your email.", message: "Without it, we can't tell who you're trying to log in as!").add(flash)
                redirect(mapping: "login")
                
                return
            } else {
                def user = User.findByEmail(email)
                
                if (user == null) {
                    new UserAlert(type: "error", title: "I've never heard of that email.", message: "Are you sure that's the one you used to sign up?").add(flash)
                    redirect(mapping: "login", params: [email: email])

                    return
                }
                
                // does the user have a password?
                if (user.passwordHash == null) {
                    user.sendResetPasswordEmail()
                    redirect(mapping: "no-password", params: [email: email])
                    return
                } else {
                    println "has password"
                }
            }
        }
        
        render(view: "/login")
    }
    
    def signup() {
        render(view: "/signup")
    }
    
    def signupStudent() {
        if (params.signup) { // TODO: redirect after POST
            def email = params.email
            def name = params.name
            def classID = params.classid
            
            def classroom = Classroom.findByClassCode(classID)
            
            if (! classroom) {
                render "That wasn't a valid class ID." // TODO: make this prettier
            } else {
                def user = new User(displayName: name, email: email, classroom: classroom)
                
                if (user.validate()) {
                    user.save()
                    userService.become(response, user)

                    redirect(mapping: "signupStudentSuccess")
                } else {
                    render "Please use valid information." // TODO: prettier
                    
                    user.errors.allErrors.each { error ->
                        println error
                    }
                }
            }
        } else {
            render(view: "/signupStudent")
        }
    }
    
    def signupTeacher() {
        if (params.signup) { // TODO: redirect after POST
            def email = params.email
            def name = params.name
            
            def user = new User(displayName: name, email: email)
            
            if (user.validate()) {
                user.save()
                userService.become(response, user)

                def classroom = userService.createClassroom(user)

                user.setClassroom(classroom)
                user.save()

                redirect(mapping: "signupTeacherSuccess")
            } else {
                render "Please use valid information." // TODO: prettier

                user.errors.allErrors.each { error ->
                    println error
                }
            }
        } else {
            render(view: "/signupTeacher")
        }
    }
    
    def signupTeacherSuccess() {
        render(view: "/signupTeacherSuccess")
    }
    
    def signupStudentSuccess() {
        render(view: "/signupStudentSuccess")
    }
    
    def invest() {
        def num = Math.floor(params.num.toDouble())
        def ticker = params.ticker
        def stock = financeService.getStocks([ticker])[ticker]
        
        def success = false
        
        if (num > 0) {
            success = request.user.purchaseStocks(stock, num)
        }
        
        if (success) {
            new UserAlert(type: "success", title: "You've successfully made a purchase!", message: "You're now the owner of ${num.toInteger()} share${num != 1 ? "s" : ""} of ${stock.getName()} stock.").add(flash)
            redirect(mapping: "portfolio")
        } else {
            redirect(mapping: "invest", params: [ticker: ticker])
        }
    }
    
    def sell() {
        def num = Math.floor(params.num.toDouble()).toInteger()
        def ticker = params.ticker
        def stock = financeService.getStocks([ticker])[ticker]
        
        def success = false
        
        if (num > 0) {
            success = request.user.sellStocks(stock, num)
        }
        
        if (success) {
            new UserAlert(type: "success", title: "You've successfully made a sale!", message: "You've sold ${num.toInteger()} share${num != 1 ? "s" : ""} of ${stock.getName()} stock.").add(flash)
            redirect(mapping: "portfolio") 
        } else {
            redirect(mapping: "sell", params: [ticker: ticker])
        }
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
