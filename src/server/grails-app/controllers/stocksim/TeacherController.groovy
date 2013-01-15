package stocksim

class TeacherController {
    def updateInformation() {
        if (params.dataType == "updateEmail") {
            def user = User.findByEmail(params.email)
            user.setEmail(params.newEmail)
            user.save(flush: true)
        }
        
        render "OK"
    }
}