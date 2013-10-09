package org.trenders.dev

import org.trenders.*

class GoogleNewsController {
    def googleNewsService
    
    def index() {
        render "<form method=\"GET\"><input type=\"text\" value=\"${params.query ? params.query : ""}\" name=\"query\" /></form>"
        render "<br />"
        
        if (params.query) {
            def articles = googleNewsService.getNewsArticles(params.query)
            
            articles.each { article ->
                render "<b>${article.title}</b><br />link: ${article.link}<br />author: ${article.author}<br />pub date: ${article.pubDate}<br />related: ${article.relatedLink}<br />\n\n\n${article.description}\n\n\n<br /><br /><br />"
            }
        }
    }
}
