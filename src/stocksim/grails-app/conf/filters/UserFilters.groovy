package filters

class UserFilters {
    def filters = {
        checkUserFilters(controller: "*", action: "*") {
            before = {
                println "Checking the user!"
            }
        }
    }
}

