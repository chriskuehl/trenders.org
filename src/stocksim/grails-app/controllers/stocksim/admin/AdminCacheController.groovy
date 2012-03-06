package stocksim.admin

import stocksim.*

class AdminCacheController {
    def servletContext
    
    def index() {
        render(view: "/admin/cache")
    }
    
    def flush() {
        servletContext["cache"] = null
        
        new UserAlert(type: "success", title: "All done!", message: "The cache has been flushed.").add(flash)
        redirect(action: "index")
    }
}
