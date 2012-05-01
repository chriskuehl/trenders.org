package filters

class AdminFilters {
    def dependsOn = [UserFilters]
    def request
    def adminsOnly = {
        if (! (request.user != null && request.user.getIsAdmin())) {
            render(view: "/denied")    
            return false
        }
    }
    
    def filters = {
        adminFilter(uri: "/admin/**") {
            before = adminsOnly
        }
        
        devFilter(uri: "/dev/**") {
            before = adminsOnly
        }
    }
}

