package stocksim

class EmailService {
    // TODO: support the actual SES API instead of using their SMTP bridge
    // TODO: investigate whether we are better off using something besides SES
    //       (for example, Mandrill looks promising)
    
    // TODO: support HTML emails
    // TODO: some logic to check that we aren't spamming (e.g. was an identical
    //       message sent within the past 15 minutes?; have we sent more than 5
    //       messages within the past minute?; etc)
    def sendMail(def to, def subject, def body) {
        // add mail message to the message queue
        new MailMessage(from: "trenders.org <chris@trenders.org>", to: to, subject: subject, body: body).save()
        
        // trigger sending all unsent emails in the queue
        ProcessMailQueueJob.triggerNow()
    }
}