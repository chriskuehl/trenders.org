package stocksim.data

class StockDataHelperService {
    def getTickerList() {
        Stock.findAll().collect { it.ticker.toLowerCase() }
    }
}
