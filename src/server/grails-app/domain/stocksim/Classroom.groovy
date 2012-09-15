package stocksim

class Classroom {
    // TODO: implement options
    static hasMany = [users: User]
    User teacher
    
    
    def config = org.codehaus.groovy.grails.commons.ConfigurationHolder.config
    
    // class options
    // TODO: implement class options, trade delay, password
    String classCode = null
    double tradeFee = config.stocksim.trading.fee
    int tradeDelay = config.stocksim.trading.delay
    boolean showNames = true
    //String password = null
    boolean open = true
}
