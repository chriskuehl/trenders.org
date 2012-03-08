package stocksim

import com.ocpsoft.pretty.time.PrettyTime

class UtilTagLib {
    static returnObjectForTags = ["pickRandom"]
    
    def addToCollection = { attrs ->
        attrs.collection.add(attrs.element)
    }
    
    def toRecentTime = { attrs ->
        def date = new Date(attrs.date.toString())
        out << new PrettyTime().format(date)
    }
    
    def trim = { attrs ->
        def elipses = attrs.elipses == "true"
        def chars = attrs.chars.toInteger()
        def content = attrs.content
        
        if (content.length() <= chars) {
            out << content
        } else {
            content = content.substring(0, chars - 3).trim()
            
            if (content.endsWith(".") || content.endsWith(",")) {
                content = content.substring(0, content.length() - 1).trim()
            }
            
            out << content
            
            if (elipses) {
                out << "..."
            }
        }
    }
    
    def pickRandom = { attrs ->
        def array = attrs.array
        def max = Math.min(array.size(), attrs.num.toInteger())
        
        Collections.shuffle(array)
        
        def finalArray = []
        
        for (def i in (1..max)) {
            finalArray.add(array[i - 1])
        }
        
        finalArray
    }
}
