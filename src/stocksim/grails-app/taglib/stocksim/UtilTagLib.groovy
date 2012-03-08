package stocksim

import com.ocpsoft.pretty.time.PrettyTime

class UtilTagLib {
    def addToCollection = { attrs ->
        attrs.collection.add(attrs.element)
    }
    
    def toRecentTime = { attrs ->
        def date = new Date(attrs.date.toString())
        out << new PrettyTime().format(date)
    }
}
