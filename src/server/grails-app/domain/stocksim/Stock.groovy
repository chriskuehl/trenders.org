package stocksim

class Stock {
    static mapping = {
        datasource "temp"
    }
    
    static constraints = {
        ticker unique: true
        
        name nullable: true
        lastSale nullable: true
        prevClose nullable: true
        dayChange nullable: true
        dayChangePercent nullable: true
        open nullable: true
        yearTarget nullable: true
        dayRange nullable: true
        yearRange nullable: true
        marketCap nullable: true
        peRatio nullable: true
    }
    
    String ticker
    String name
    double lastSale // USED TO BE "value"
    String prevClose
    String dayChange
    String dayChangePercent
    double open
    double yearTarget
    String dayRange
    String yearRange
    String marketCap
    double peRatio
    String exchange // USED TO BE "market"
    int ipoYear
    String sector
    String industry
}