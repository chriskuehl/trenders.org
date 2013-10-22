package org.trenders

class ProcessMailQueueJob {
	static triggers = { // start 15 seconds after bootstrap, repeat every 5 seconds
		simple name: "updateStocks", startDelay: 15000, repeatInterval: 5 * 1000
	}

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
