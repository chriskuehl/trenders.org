package filters

class UserFilters {
    def filters = {
        checkUserFilters(uri: "/**") {
            before = {
                println "Checking the user!"
            }
        }
    }
}