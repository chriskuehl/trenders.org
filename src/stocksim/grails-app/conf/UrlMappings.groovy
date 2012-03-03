class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        
		"/"(view:"/index")
                "/stock"(view:"/stock")
                
                // errors
		"500"(view:'/error')
		"404"(view:'/error')
	}
}
