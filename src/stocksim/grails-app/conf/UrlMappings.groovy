class UrlMappings {
    static mappings = {
        // home page
        name home: "/" {
            controller = "viewProxy"
            proxyView = "/index"
        }
        
        // user pages
        
        name signup: "/signup" {
            controller = "user"
            action = "signup"
        }
        
        name signupStudent: "/signupStudent" {
            controller = "user"
            action = "signupStudent"
        }
        
        name signupStudentSuccess: "/signupStudentSuccess" {
            controller = "user"
            action = "signupStudentSuccess"
        }
        
        name signupTeacher: "/signupTeacher" {
            controller = "user"
            action = "signupTeacher"
        }
        
        name signupTeacherSuccess: "/signupTeacherSuccess" {
            controller = "user"
            action = "signupTeacherSuccess"
        }
        
        name logout: "/logout" {
            controller = "user"
            action = "logout"
        }
        
        name classroom: "/classroom" {
            controller = "viewProxy"
            proxyView = "/classroom"
        }
        
        name invest: "/invest/$ticker" {
            controller = "viewProxy"
            proxyView = "invest"
        }
        
        // search
        "/search" (controller: "search", action: "browse")
        "/browse" (controller: "search", action: "sector")
        "/search/json" (controller: "search", action: "json")
        
        // stock page
        name stock: "/stock/$ticker" {
            controller = "viewProxy"
            proxyView = "/stock"
        }
        
        // content pages
        name faq: "/faq" {
            controller = "viewProxy"
            proxyView = "/faq"
        }
        
        name about: "/about" {
            controller = "viewProxy"
            proxyView = "/about"
        }
        
        // lessons
        
        // admin pages
        "/admin" (view: "admin/index")
        "/admin/cache/$action" (controller: "adminCache")
        "/admin/user/$action" (controller: "adminUser")
        
        // dev pages
        "/dev/googleNews/$action" (controller: "googleNews")
        "/dev/sandbox/$action" (controller: "sandbox")
        "/dev/searchableStock/$action/$id?" (controller: "searchableStock")
        "/dev/wikipedia/$action" (controller: "wikipedia")

        // errors
       // "500"(view:'/error')
       // "404"(view:'/error')
    }
}
