package stocksim.api.category

import stocksim.*

class PortfolioInterfaceService {
    def financeService
    
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