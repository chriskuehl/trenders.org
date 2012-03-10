package stocksim

class UtilService {
    def trim(def content, def chars, def elipses) {
        def str = ""
        
        if (content.length() <= chars) {
            str += content
        } else {
            content = content.substring(0, chars - 3).trim()
            
            if (content.endsWith(".") || content.endsWith(",")) {
                content = content.substring(0, content.length() - 1).trim()
            }
            
            str += content
            
            if (elipses) {
                str += "..."
            }
        }
        
        str
    }
}
