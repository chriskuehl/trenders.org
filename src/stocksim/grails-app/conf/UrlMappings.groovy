class UrlMappings {
    static mappings = {
        // home page
        "/" (view:"/index")
        
        // search
        "/search" (controller: "search", action: "browse")
        "/search/json" (controller: "search", action: "json")
        
        // stock page
        name stock: "/stock/$ticker" {
            view = "/stock"
        }
        
        // content pages
        name faq: "/faq" {
            view = "/faq"
        }
        
        // admin pages
        "/admin/cache/$action" (controller: "adminCache")
        
        // dev pages
        "/dev/googleNews/$action" (controller: "googleNews")
        "/dev/sandbox/$action" (controller: "sandbox")
        "/dev/searchableStock/$action/$id?" (controller: "searchableStock")
        "/dev/wikipedia/$action" (controller: "wikipedia")

        // errors
        "500"(view:'/error')
        "404"(view:'/error')
    }
}
