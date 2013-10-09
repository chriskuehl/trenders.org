package org.trenders

class GoogleNewsTagLib {
    static namespace = "googleNews"
    
    def googleNewsService
    
    def articles = { attrs, body ->
        request.googleNewsArticles = googleNewsService.getNewsArticles(attrs.query)
        def max = Math.min(attrs.num.toInteger(), request.googleNewsArticles.size())
        
		if (max > 0) {
			for (i in (1..max)) {
				request.googleNewsCurrentArticle = i - 1
				out << body()
			}
		}
    }
    
    def article = { attrs, body ->	
        out << request.googleNewsArticles[request.googleNewsCurrentArticle][attrs.req]
    }
}
