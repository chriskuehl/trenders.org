package filters

class AdminFilters {
    def filters = {
        noCachingFilter(uri: "/admin/**") {
            before = {
                if (grails.util.GrailsUtil.getEnvironment().equals(org.codehaus.groovy.grails.commons.GrailsApplication.ENV_PRODUCTION)) {
                    render(view: "/denied")    
                    return false
                }
            }
        }
    }
}

