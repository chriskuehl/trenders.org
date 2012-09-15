package filters

import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.util.GrailsUtil

class AdminFilters {
    def dependsOn = [UserFilters]
    def request
    
    def filters = {
        adminFilter(uri: "/admin/**") {
            before = {
                if (! (request.user != null && request.user.getIsAdmin()) && GrailsUtil.getEnvironment().equals(GrailsApplication.ENV_PRODUCTION)) {
                    render(view: "/denied")    
                    return false
                }
            }
        }
        
        devFilter(uri: "/dev/**") {
            before = {
                if (! (request.user != null && request.user.getIsAdmin()) && GrailsUtil.getEnvironment().equals(GrailsApplication.ENV_PRODUCTION)) {
                    render(view: "/denied")    
                    return false
                }
            }
        }
    }
}