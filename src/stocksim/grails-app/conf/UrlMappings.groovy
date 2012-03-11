class UrlMappings {

	static mappings = {
                "/search" (controller: "search", action: "browse")
                "/search/json" (controller: "search", action: "json")
                
		"/"(view:"/index")
        
                name stock: "/stock/$ticker" {
                    view = "/stock"
                }
                
                name faq: "/faq" {
                    view = "/faq"
                }
                
                // errors
		"500"(view:'/error')
		"404"(view:'/error')
	}
}
