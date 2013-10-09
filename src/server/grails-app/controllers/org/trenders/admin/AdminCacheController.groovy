package org.trenders.admin

import org.trenders.*

class AdminCacheController {
    def servletContext
    def searchService
    def stockDataService
    
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
    
    def flushStockData() {
        stockDataService.updateStockData(true)
        
        new UserAlert(type: "success", title: "All done!", message: "The stock data cache has been flushed. It may take a few seconds for everything to start working again.").add(flash)
        redirect(action: "index")
    }
}
