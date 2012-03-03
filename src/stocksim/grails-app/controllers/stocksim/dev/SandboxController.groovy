package stocksim.dev

import stocksim.*
import stocksim.exception.*

class SandboxController {
    static defaultAction = "viewstocks"
    def userService
    
    def newUser() {
        def token = userService.generateTokenHash()
        render "Got: ${token}"
    }
    
    def viewstocks() {
        if (! params.stocks) {
            // there's a better way to do this...
            render "<form method=\"get\"><input type=\"text\" name=\"stocks\" /><input type=\"submit\" /></form>";
        } else {
            def stocks = params.stocks.split(",")
            render "Stocks to check:<br />"
            
            try {
                Stock[] stockObjects = FinanceService.getStocks(stocks)
                
                render "<ul>"
                
                stockObjects.each { stock ->
                    render "<li>${stock.getTickerSymbol()}: ${stock.getPrice()}</li>"
                }
                
                render "</ul>"
            } catch (BadTickerSymbolException ex) {
                render ex.getMessage()
            }
        }
    }
}