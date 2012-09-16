package stocksim.api.category

import stocksim.*

class PortfolioInterfaceService {
    def financeService
    
    public def _current = { response, action, params, user ->
        def assets = []
        
        user.getOwnedStocks().each { ownedStock ->
            def stock = financeService.getStock(ownedStock.getTicker())
            
            assets.add([
                company: [
                    ticker: stock.ticker,
                    name: stock.name,
                    lastSale: stock.lastSale,
                    marketCap: stock.marketCap,
                    ipoYear: stock.ipoYear,
                    sector: stock.sector,
                    industry: stock.industry,
                    exchange: stock.exchange
                ],
                
                numShares: ownedStock.quantity,
                totalSpent: ownedStock.totalSpent.toDouble().trunc(2),
                currentValue: stock.lastSale * ownedStock.quantity,
                gain: ((stock.lastSale * ownedStock.quantity) - ownedStock.totalSpent.toDouble()).round(2)
            ])
        }
        
        response.totalSpent = user.getPrettyMoneySpentOnPortfolio()
        response.totalWorth = user.getPrettyPortfolioValue()
        
        response.assets = assets
    }
    
    public def _history = { response, action, params, user ->
        def events = []
        
        user.getOrderedHistoryEvents().each { event ->
            def stock = financeService.getStock(event.getTicker())
            
            events.add([
                date: event.getDate().toString(),

                company: [
                    ticker: stock.ticker,
                    name: stock.name,
                    lastSale: stock.lastSale,
                    marketCap: stock.marketCap,
                    ipoYear: stock.ipoYear,
                    sector: stock.sector,
                    industry: stock.industry,
                    exchange: stock.exchange
                ],

                action: event.getWasPurchase() ? "purchase" : "sale",
                quantity: event.getQuantity(),
                balanceChange: event.getMoney().toDouble()
            ])
        }
        
        response.events = events
    }
}