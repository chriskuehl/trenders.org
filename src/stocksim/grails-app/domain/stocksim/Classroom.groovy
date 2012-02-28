package stocksim

class Classroom {
    User teacher
    
    
    def config = org.codehaus.groovy.grails.commons.ConfigurationHolder.config
    
    // class options
    double tradeFee = config.stocksim.trading.fee
    int tradeDelay = config.stocksim.trading.delay
    boolean showNames = true
    String password = null
    boolean open = true
}
