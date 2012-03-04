package stocksim

import groovy.util.XmlParser

class WikipediaService {
    def getArticleSource(def title) {
        title = URLEncoder.encode(title.replace(" ", "_"))
        def url = "http://en.wikipedia.org/wiki/Special:Export/$title"
        def content = new XmlParser().parseText(url.toURL().text)
        def source = content.page[0].revision[0].text[0].text()
        
        source
    }
}
