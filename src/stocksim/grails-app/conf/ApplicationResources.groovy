modules = {
    site {
        resource url: "http://fonts.googleapis.com/css?family=Cabin:500|Arimo:400italic,400,700", attrs: [type:"css"], disposition: 'head'
        
        resource url: "css/reset.css", disposition: 'head'
        resource url: "css/site.css", disposition: 'head'
        
        resource url: "js/jquery-1.7.1.min.js", disposition: 'head'
        resource url: "js/jquery-ui-1.8.16.min.js", disposition: 'head'
        resource url: "js/jquery.inputLabel.js", disposition: 'head'
        
        resource url: "js/site.js", disposition: 'head'
        resource url: "js/standalone.js", disposition: 'head'
    }
    
    page_home {
        resource url: "css/pages/home.css", disposition: "head"
        resource url: "js/pages/home.js", disposition: "head"
    }
    
    page_stock {
        resource url: "css/pages/stock.css", disposition: 'head'
    }
    
    page_search {
        resource url: "css/pages/search.css", disposition: "head"
    }
}