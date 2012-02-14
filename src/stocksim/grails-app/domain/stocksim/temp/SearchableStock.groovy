package stocksim.temp

class SearchableStock {
    static mapping = {
        datasource "temp"
    }
    
    String ticker
    String name
    double lastSale
    double marketCap
    int ipoYear
    String sector
    String industry
    String market
    
    static constraints = {
        ticker unique: true
        name unique: false
        industry unique: false
        sector unique: false
        market unique: false
    }
}