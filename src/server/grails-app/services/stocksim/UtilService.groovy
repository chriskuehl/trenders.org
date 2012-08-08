package stocksim

import java.text.DecimalFormat

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
    
    def formatBigNumber(def number) {
        number = Double.valueOf(number)
        
        def units = [
            [1000, "K"],
            [1000000, "M"],
            [1000000000, "B"]
        ]
        
        def ret = number
        
        units.each { unit ->
            if (unit[0] < number) {
                ret = (Math.floor(number / unit[0] * 10) / 10) + unit[1]
            }
        }
        
        return ret
    }
    
    def makePretty(def number) {
        def formatter = new DecimalFormat("#,###")

        formatter.format(number)
    }
}
