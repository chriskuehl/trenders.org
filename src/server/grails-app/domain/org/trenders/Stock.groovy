package org.trenders

class Stock {
    static mapping = {
        datasource "temp"
    }
    
    static constraints = {
        ticker unique: true, index: "Ticker_Idx"
        
        name nullable: true, index: "Name_Idx"
        lastSale nullable: true
        prevClose nullable: true
        dayChange nullable: true
        dayChangePercent nullable: true
        open nullable: true
        yearTarget nullable: true
        dayRange nullable: true
        yearRange nullable: true
        marketCap nullable: true, index: "MarketCap_Idx"
        peRatio nullable: true
        sector index: "Sector_Idx"
    }
    
    String ticker
    String name
    double lastSale = 0 // USED TO BE "value"
    String prevClose
    String dayChange
    String dayChangePercent
    double open = 0
    double yearTarget = 0
    String dayRange
    String yearRange
    long marketCap
    double peRatio = 0
    String exchange // USED TO BE "market"
    int ipoYear = 0
    String sector
    String industry
}
