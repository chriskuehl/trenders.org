package org.trenders

import groovy.json.JsonSlurper

class YahooQueryService {
    static def getResultsFromQuery(def query) {
        //println query
        String body = "http://query.yahooapis.com/v1/public/yql?format=json&env=http%3A%2F%2Fdatatables.org%2Falltables.env&q=${URLEncoder.encode(query)}".toURL().getText()
        
        def slurper = new JsonSlurper()
        slurper.parseText(body)
    }
    
    static boolean isAlphaNumeric(def test) {
        test ==~ /^[a-zA-Z0-9]*$/;
    }
    
    static boolean isAlphaNumericOrSlash(def test) {
        test ==~ /^[a-zA-Z0-9\\/]*$/;
    }
    
    static String makeAlphaNumeric(def str) {
        def result = ""
        
        for (def i = 0; i < str.length(); i ++) {
            def charAt = str[i]
            
            if (isAlphaNumeric(charAt) || charAt == " ") {
                result += charAt
            }
        }
        
        result
    }
}
