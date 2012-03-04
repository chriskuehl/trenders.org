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
    
    // a regex doesn't work very well since templates can be embedded inside
    // other templates, so we'll just do it sift through each character and keep
    // track of our depth
    def stripTemplates(def source, def maxChars) {
        def numChars = 0
        def lastChar = null
        def depth = 0
        def finalSourceBuffer = new StringBuffer()
        
        // this is faster than closures for very long articles, and I can use a
        // break if we hit the max limit
        for (int i = 0; i < source.length(); i ++) {
            char curChar = source.charAt(i) // faster to avoid Groovy here and just go with primitives

            if (curChar == "{" && lastChar == "{") {
                depth ++
                lastChar = null
            } else if (curChar == "}" && lastChar == "}") {
                depth --
                lastChar = null
            } else {
                if (depth <= 0 && lastChar != null) {
                    finalSourceBuffer << lastChar
                    numChars ++

                    if (numChars >= maxChars) {
                        break
                    }
                }

                lastChar = curChar
            }
        }
        
        def finalSource = finalSourceBuffer.toString()
        finalSource = finalSource.replace("()", "")
        finalSource
    }
    
    def convertToHTML(def source) {
        
    }
}
