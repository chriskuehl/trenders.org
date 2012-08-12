package stocksim.api

import stocksim.api.category.*

class AppInterfaceService {
    def lyricService
    
    def classroomInterfaceService
    def userInterfaceService
    def portfolioInterfaceService
    def infoInterfaceService
    
    def generateResponse(def category, def item, def action, def params) {
        def response = [:]
        def startTime = (new Date()).getTime()
        
        // process for a response
        def categoryService = getCategoryService(category)
        
        if (categoryService == null) {
            response.apiCode = AppInterface.codes.NOT_AVAILABLE
        } else {
            // does a closure exist for the item requested?
            if (categoryService[item] != null) {
                // mark the response as "ok" (API methods can override this later if
                // they want)
                response.apiCode = AppInterface.codes.OK
                categoryService[item](response, action, params)
            } else {
                response.apiCode = AppInterface.codes.NOT_AVAILABLE
            }
        }
        
        // add standard stuff on all requests
        response.apiDate = (new Date()).toString()
        response.apiTime = (new Date()).getTime() - startTime
        response.apiLyric = lyricService.getLine()
        
        // feed back the response so it can be processed and output by some controller
        response
    }
    
    def getCategoryService(def category) {
        def categories = [
            classroom: classroomInterfaceService,
            user: userInterfaceService,
            portfolio: portfolioInterfaceService,
            info: infoInterfaceService
        ]
        
        categories[category]
    }
}