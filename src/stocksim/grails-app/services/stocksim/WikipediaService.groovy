package stocksim

import groovy.util.XmlParser

class WikipediaService {
    def getArticleSource(def title, def failOnRedirect) {
        try {
            title = URLEncoder.encode(title.replace(" ", "_"))
            def url = "http://en.wikipedia.org/wiki/Special:Export/$title"
            def content = new XmlParser().parseText(url.toURL().text)
            def source = content.page[0].revision[0].text[0].text()
            
            if (source.startsWith("#REDIRECT ")) {
                if (failOnRedirect) {
                    return null
                }
                
                def target = source.substring(source.indexOf("[[") + 2)
                target = target.substring(0, target.indexOf("]]"))
                
                return getArticleSource(target, true) // if there's another redirect, give up
            }
            
            return source
        } catch (Exception ex) {
            ex.printStackTrace()
            return null
        }
    }
}
