package stocksim.data

import stocksim.*

class StockDataHelperService {
    def getTickerList() {
        Stock.findAll().collect { it.ticker.toLowerCase() }
    }
}
