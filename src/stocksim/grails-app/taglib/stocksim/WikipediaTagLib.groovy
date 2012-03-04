package stocksim

class WikipediaTagLib {
    static namespace = "wikipedia"
    def wikipediaService
    
    def summary = { attrs, body ->
        def title = attrs.title
        
        def source = wikipediaService.getArticleSource(title)
        
        if (source != null) {
            def content = wikipediaService.getSummaryFromHTML(wikipediaService.cleanupHTML(source)).replaceAll("<(.|\n)*?>", "")
            
            if (attrs.maxChars == null || content.length() <= attrs.maxChars.toInteger()) {
                out << content
            } else {
                def trimmedContent = content.substring(0, attrs.maxChars.toInteger())
                trimmedContent = trimmedContent.substring(0, trimmedContent.lastIndexOf(" ")) + "..."
                out << trimmedContent
            }
            
            out << " <a target=\"_blank\" href=\"${wikipediaService.getURL(title)}\">(read more)</a>"
        }
    }
}
