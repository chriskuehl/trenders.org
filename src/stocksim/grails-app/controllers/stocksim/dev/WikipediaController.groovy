package stocksim.dev

class WikipediaController {
    def wikipediaService
    
    def index() {
        render "<form method=\"GET\"><input type=\"text\" value=\"${params.article ? params.article : ""}\" name=\"article\" /></form>"
        render "<br />"
        
        if (params.article) {
            render wikipediaService.cleanupHTML(
                wikipediaService.getArticleSource(params.article, false), 1000
            )
        }
    }
}
