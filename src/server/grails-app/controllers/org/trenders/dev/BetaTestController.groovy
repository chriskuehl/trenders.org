package org.trenders.dev

import org.trenders.*

class BetaTestController {
    def emailService
    
    def index() {
        if (params.sendMessage) {
            def email = params.email
            def name = params.name
            def comments = params.comments
            
            def body = "From: ${name} <${email}>\n\n"
            body += comments + "\n\n"
            body += "User Agent: ${request.getHeader("User-Agent")}\n"
            body += "IP Address: ${request.getRemoteAddr()}"
            
            emailService.sendMail("chris@trenders.org", "Comments from ${name}", body)
            
            new UserAlert(type: "success", title: "Thank you!", message: "Your feedback will help us improve trenders.org. If we need more information, we'll get in touch.").add(flash)
            redirect(mapping: "home")
        } else {
            render(view: "/report")
        }
    }
}
