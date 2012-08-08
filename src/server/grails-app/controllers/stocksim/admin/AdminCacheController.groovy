package stocksim.admin

import stocksim.*

class AdminCacheController {
    def servletContext
    def searchService
    
    def index() {
        render(view: "/admin/cache/index")
    }
    
    def browse() {
        render(view: "/admin/cache/browse")
    }
    
    def flush() {
        servletContext["cache"] = [:]
        UpdateIndicesJob.triggerNow()
        
        new UserAlert(type: "success", title: "All done!", message: "The cache has been flushed.").add(flash)
        redirect(action: "index")
    }
    
    def flushSearchCache() {
        searchService.updateCache()
        
        new UserAlert(type: "success", title: "All done!", message: "The search cache has been flushed. It may take a few seconds for search to start working again.").add(flash)
        redirect(action: "index")
    }
}