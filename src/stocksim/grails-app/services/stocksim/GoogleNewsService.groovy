package stocksim

import stocksim.info.*
import groovy.util.XmlParser

class GoogleNewsService {
    def getNewsArticles(def query) {
        def url = "http://news.google.com/news?q=${URLEncoder.encode(query)}&output=rss".toURL()
        def xml = new XmlParser().parseText(url.getText())
        
        def articles = xml.channel[0].item
        def articleObjects = []
        
        articles.each { article ->
            def description = cleanDescription(article.description.text())
            def title = article.title.text()
            def author = title.substring(title.lastIndexOf("-") + 1)
            title = title.substring(0, title.lastIndexOf("-") - 1)
            def relatedLink = WikipediaService.between(description, "<a class=\"p\" href=\"", "\"")
            
            print description
            
            description = WikipediaService.between(description, "</font></b></font><br /><font size=\"-1\">", "</font>").replaceAll("<[^>]*>", "")
            description = description.substring(0, description.length() - 4)
            
            def articleObject = new GoogleNewsArticle(
                    title: title,
                    author: author,
                    link: article.link.text(),
                    pubDate: new Date(article.pubDate.text()),
                    relatedLink: relatedLink,
                    description: description
            )
            
            articleObjects.add(articleObject)
        }
        
        articleObjects
    }
    
    def cleanDescription(def description) {
        description
    }
}
