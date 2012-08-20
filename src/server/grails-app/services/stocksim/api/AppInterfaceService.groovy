package stocksim.api

import stocksim.*
import stocksim.api.category.*

class AppInterfaceService {
    def lyricService
    def userService
    
    def classroomInterfaceService
    def userInterfaceService
    def portfolioInterfaceService
    def infoInterfaceService
    
    def generateResponse(def category, def item, def action, def params) {
        def response = [:]
        def startTime = (new Date()).getTime()
        
        // see if the request has an appropriate token, and if so, find the user
        // they're trying to login as
        def user = null
        
        def sessionToken = params.token
        
        if (sessionToken) {
            user = userService.getUserForSessionToken(sessionToken)
        }
        
        // process for a response
        def categoryService = getCategoryService(category)
        
        if (categoryService == null || item.startsWith("_")) {
            response.apiCode = AppInterface.codes.NOT_AVAILABLE
        } else {
            // does a closure exist for the item requested?
            def publicMethod = categoryService[item]
            def authMethod = categoryService["_" + item]
            
            def method = publicMethod
            def requiresAuth = false
            
            if (authMethod != null) {
                method = authMethod
                requiresAuth = true
            }
            
            if (method != null) {
                // does that method require authorization? (starts with semicolon)
                def authorized = requiresAuth ? (user != null) : true
                
                if (authorized) {
                    // mark the response as "ok" (API methods can override this later if
                    // they want)
                    response.apiCode = AppInterface.codes.OK
                    categoryService[item](response, action, params)
                } else {
                    response.apiCode = AppInterface.codes.LOGIN_FIRST
                }
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