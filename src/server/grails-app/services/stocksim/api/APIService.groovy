package stocksim.api

class APIService {
    def lyricService
    
    def generateResponse(def category, def item, def action, def params) {
        def response = [:]
        def startTime = (new Date()).getTime()
        
        // process for a response
        
        // add standard stuff on all requests
        response.apiDate = (new Date()).toString()
        response.apiTime = (new Date()).getTime() - startTime
        response.apiLyric = lyricService.getLine()
        
        // feed back the response so it can be processed and output by some controller
        response
    }
}
