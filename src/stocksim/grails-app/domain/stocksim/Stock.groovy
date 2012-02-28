package stocksim

/*
    Stocks are either born with data or not; data is typically provided via
    StockService
    
    If not born with data, attempts to access data via getters (use these!)
    will result in data being fetched
    
    Getters (getWhatever) will not fetch data, only return null
    Getters (tryGetWhatever) will fetch data if necessary, so only use these when
        you can afford it
*/

class Stock {
    String tickerSymbol
    String market // either "nasdaq" or "nyse"
    
    // data
    double price
    
    Stock(String tickerSymbol, String market, double price) {
        this.tickerSymbol = tickerSymbol
        this.market = market
        
        this.price = price
    }
}
