package stocksim

import groovy.json.JsonSlurper

class GoogleFinanceService {
    // in reality we are using this for the indicies more than stocks
    // since we use YQL to get stocks
    // 
    // .DJI = Dow Jones Industrial Average
    // .INX = S&P 500
    static def getInfoForAsset(def stock) {
        String body = "http://finance.google.com/finance/info?client=ig&q=INDEXDJX%3A${URLEncoder.encode(stock)}".toURL().getText()
        
        // this isn't returning proper JSON; for some reason, the first line
        // starts with " // ", but we can fix it and then parse it as JSON
        body = body.substring(3) // remove " // [" at the beginning
        
        // slurp it
        def slurper = new JsonSlurper()
        def result = slurper.parseText(body)[0]
        
        // minor number fixes
        result.l = result.l.replace(",", "")
        result.l_cur = result.l_cur.replace(",", "")
        
        
        result
    }
}
