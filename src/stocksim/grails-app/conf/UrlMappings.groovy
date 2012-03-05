class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        
		"/"(view:"/index")
                "/stock/$ticker"(view:"/stock")
                name faq: "/faq" {
                    view = "/faq"
                }
                
                // errors
		"500"(view:'/error')
		"404"(view:'/error')
	}
}
