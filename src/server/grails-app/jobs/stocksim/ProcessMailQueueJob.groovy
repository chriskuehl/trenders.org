package stocksim

class ProcessMailQueueJob {
    def startDelay = 15000
    def timeout = 30 * 1000 // every 30 seconds
    
    def mailService

    def execute() {
        def unsentMessages = MailMessage.findAllBySent(false)
        
        unsentMessages.each { message ->
            println "Sending mail message: ${message}"

            mailService.sendMail {
                //multipart true

                to message.to
                from message.from
                subject message.subject
                //html 'this is <b>some</b> text'
                body message.body
            }

            message.sent = true
            message.save()
        }
    }
}