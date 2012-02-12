package stocksim.dev

import stocksim.*

class SandboxController {
    static defaultAction = "viewstock"
    
    def viewstock() {
        def nasdaq = new Market(title: "NASDAQ")
        def nyse = new Market(title: "NYSE")
        
        render nasdaq.getTitle()
    }
}