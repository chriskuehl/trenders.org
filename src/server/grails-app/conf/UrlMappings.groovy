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
        
        name portfolio: "/portfolio" {
            controller = "viewProxy"
            proxyView = "/portfolio"
        }
        
        name history: "/history" {
            controller = "viewProxy"
            proxyView = "/history"
        }
        
        name trending: "/trending" {
            controller = "viewProxy"
            proxyView = "/trending"
        }
        
        name news: "/news" {
            controller = "viewProxy"
            proxyView = "/news"
        }
        
        name rankings: "/rankings" {
            controller = "viewProxy"
            proxyView = "/rankings"
        }
        
        name classroom: "/classroom" {
            controller = "viewProxy"
            proxyView = "/classroom"
        }
        
        name invest: "/invest/$ticker" {
            controller = "viewProxy"
            proxyView = "/invest"
        }
        
        name sell: "/sell/$ticker" {
            controller = "viewProxy"
            proxyView = "/sell"
        }
        
        name doInvest: "/doInvest" {
            controller = "user"
            action = "invest"
        }
        
        name doSell: "/doSell" {
            controller = "user"
            action = "sell"
        }
        
        // lessons
        name lessons: "/lessons" {
            controller = "lessons"
            action = "index"
        }
        
        name lesson: "/lessons/$cat/$lesson" {
            controller = "lessons"
            action = "view"
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
        "/admin" (controller: "viewProxy", proxyView: "admin/index")
        "/admin/cache/$action" (controller: "adminCache")
        "/admin/user/$action" (controller: "adminUser")
        
        // dev pages
        "/dev/googleNews/$action" (controller: "googleNews")
        "/dev/sandbox/$action" (controller: "sandbox")
        "/dev/searchableStock/$action/$id?" (controller: "searchableStock")
        "/dev/wikipedia/$action" (controller: "wikipedia")
       
        // api
        "/api/$apiCategory" (controller: "JSObjectAPI", action: "handleRequest", apiItem: "", apiAction: "")
        "/api/$apiCategory/$apiItem" (controller: "JSObjectAPI", action: "handleRequest", apiAction: "")
        "/api/$apiCategory/$apiItem/$apiAction" (controller: "JSObjectAPI", action: "handleRequest")
        
        // errors
        "500"(view:'/error')
        "404"(view:'/error')
    }
}
