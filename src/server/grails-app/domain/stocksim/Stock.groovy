// this is created via FinanceServer#getStocks

package stocksim

class Stock {
    static mapping = {
        datasource "temp"
    }
    
    static constraints = {
        ticker unique: true
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