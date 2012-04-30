package filters

class AdminFilters {
    def dependsOn = [UserFilters]
    def request
    
    def filters = {
        noCachingFilter(uri: "/admin/**") {
            before = { 
                if (! (request.user != null && request.user.getIsAdmin())) {
                    println "Not authorized!"
                    render(view: "/denied")    
                    return false
                }
            }
        }
    }
}

