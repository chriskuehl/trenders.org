package org.trenders

class LayoutTagLib {
    static namespace = "layout"
    
    def genTitle = { attrs, body ->
        def title = body()
        
        if (title.indexOf("trenders.org") <= (- 1)) {
            title += " &ndash; trenders.org"
        }
        
        out << title
    }
}
