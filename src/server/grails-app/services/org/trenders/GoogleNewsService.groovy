package org.trenders

import org.trenders.info.*
import groovy.util.XmlParser
import java.text.SimpleDateFormat

class GoogleNewsService {
    def cacheService
    
    def getNewsArticles(def query) {
        def articleObjects = cacheService.fetchFromCache("googlenews-search", query, 60)
        
        if (true || articleObjects == null) {
            def url = "http://news.google.com/news?q=${URLEncoder.encode(query)}&output=rss".toURL()

            def xml = new XmlParser().parseText(url.getText())

            def articles = xml.channel[0].item
            articleObjects = []

            articles.each { article ->
                def description = cleanDescription(article.description.text())
                def title = article.title.text()
                def author = title.substring(title.lastIndexOf("-") + 1)
                title = title.substring(0, title.lastIndexOf("-") - 1)
                def relatedLink = WikipediaService.between(description, "<a class=\"p\" href=\"", "\"")

                description = WikipediaService.between(description, "</font></b></font><br /><font size=\"-1\">", "</font>").replaceAll("<[^>]*>", "")
                description = description.substring(0, description.length() - 4)

                def articleObject = new GoogleNewsArticle()

				def sdf = new SimpleDateFormat("EE, dd MMM yyyy HH:mm:ss zzz")
				def pubDate = sdf.parse(article.pubDate[0].text())

				articleObject.title = title
				articleObject.author = author
				articleObject.link = article.link.text()
				articleObject.pubDate =  pubDate
				articleObject.relatedLink = relatedLink
				articleObject.description = description

                articleObjects.add(articleObject)
            }
            
            cacheService.storeInCache("googlenews-search", query, articleObjects)
        }
        
        articleObjects
    }
    
    def cleanDescription(def description) {
        description
    }
}
