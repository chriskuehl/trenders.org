package org.trenders

class MailMessage {
    static mapping = {
        // this is necessary because "from" is reserved in H2 and "to" is reserved
        // in MySQL
        // (see http://www.h2database.com/html/advanced.html for list of 
        //  reserved words and http://jira.grails.org/browse/GRAILS-8140
        //  for an explanation of why this works)
        from column: 'fromAddr'
        to column: 'toAddr'
        
        // "body" is typically a 255-length varchar unless we specify otherwise
        body type: 'text'
    }

    String from
    String to
    String subject
    String body
    
    boolean sent = false
}
