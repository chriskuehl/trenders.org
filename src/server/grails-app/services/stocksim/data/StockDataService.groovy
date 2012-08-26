package stocksim.data

class StockDataService {
    def stockDataListService
    
    def updateStockList() {
        stockDataListService.update()
    }
}