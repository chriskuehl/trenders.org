package stocksim.api

import grails.converters.JSON

class JSObjectAPIController {
    def APIService
    
    def handleRequest() {
        def category = params.apiCategory
        def item = params.apiItem
        def action = params.apiAction
        
        def response = APIService.generateResponse(category, item, action, params)
        
        render response as JSON
    }
}
