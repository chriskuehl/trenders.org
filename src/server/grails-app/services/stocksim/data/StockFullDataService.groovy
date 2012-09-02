package stocksim.data

class StockFullDataService {
    def stockDataService
    
    // for each existing ticker, we need to get the info
    def update() {
        def tickerList = stockDataService.getTickerList()
    }
}
