package stocksim

class WikipediaTagLib {
    static namespace = "wikipedia"
    def wikipediaService
    def cacheService
    
    def summary = { attrs, body ->
        def title = attrs.title
        def source = cacheService.fetchFromCache("wikipedia", title, 60 * 24)
        
        if (source == null) { // it wasn't in the cache
            source = wikipediaService.getArticleSource(title)
            cacheService.storeInCache("wikipedia", title, source)
        }
        
        if (source != null) {
            def content = wikipediaService.getSummaryFromHTML(wikipediaService.cleanupHTML(source)).replaceAll("<(.|\n)*?>", "")
            
            if (attrs.maxChars == null || content.length() <= attrs.maxChars.toInteger()) {
                out << content
            } else {
                def trimmedContent = content.substring(0, attrs.maxChars.toInteger())
                trimmedContent = trimmedContent.substring(0, trimmedContent.lastIndexOf(" "))
                
                if (trimmedContent.endsWith(",") || trimmedContent.endsWith(".")) {
                    trimmedContent = trimmedContent.substring(0, trimmedContent.length() - 1)
                }
                
                trimmedContent += "..."
                out << trimmedContent
            }
            
            out << " <a target=\"_blank\" href=\"${wikipediaService.getURL(title)}\">(read more)</a>"
            out << body()
        }
    }
}
