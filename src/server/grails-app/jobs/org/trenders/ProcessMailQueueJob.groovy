package org.trenders

class ProcessMailQueueJob {
    def startDelay = 15000
    def timeout = 1 * 1000 // every 5 seconds
    
    def mailService

    def execute() {
        def message = MailMessage.findBySent(false)
        
        if (message == null) {
            return
        }
        
        //unsentMessages.each { message ->
            if (message.sent) {
                println "Already sent: ${message}"
            } else {
                println "Sending mail message: ${message}"

                message.sent = true
                
                try {
                    message.save(flush: true)
                } catch (Exception ex) {
                    println "Abandoning ${message}, is dirty..."
                    return false
                }

                mailService.sendMail {
                    //multipart true

                    to message.to
                    from message.from
                    subject message.subject
                    //html 'this is <b>some</b> text'
                    body message.body
                }
            }
        //}
    }
}
