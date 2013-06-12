package stocksim

class TeacherController {
    def beforeInterceptor = {
        if (! request.user || ! (request.user.isTeacher() || request.user.isAdmin()) {
            render "You are not authorized to view this page."
            return false
        }
    }

    def updateInformation() {
        if (params.dataType == "updateEmail") {
            def user = User.findByEmail(params.email)
            user.setEmail(params.newEmail)
            user.save(flush: true)
        }
        
        render "OK"
    }
}
