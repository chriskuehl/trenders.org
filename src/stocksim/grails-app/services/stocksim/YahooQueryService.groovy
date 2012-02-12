package stocksim

import groovy.json.JsonSlurper

class YahooQueryService {
    static def getResultsFromQuery(String query) {
        String body = "http://query.yahooapis.com/v1/public/yql?format=json&env=http%3A%2F%2Fdatatables.org%2Falltables.env&q=${URLEncoder.encode(query)}".toURL().getText()
        
        def slurper = new JsonSlurper()
        def response = slurper.parseText(body)
        
        response
    }
    
    static boolean isAlphaNumeric(String test) {
        test ==~ /^[a-zA-Z0-9]*$/;
    }
}
