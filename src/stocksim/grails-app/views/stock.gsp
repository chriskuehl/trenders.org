<g:set var="ticker" value="${params.ticker.toLowerCase()}" />
<finance:stocks tickers="${[ticker]}">
  <g:set var="title" value="${finance.stock(ticker: ticker, req: "name")}" />
  <!doctype html>
  <html>
    <head>
      <content tag="hasColumns">true</content>
      <meta name="layout" content="main" />
      <title><finance:stock ticker="${ticker}" req="name" /> &ndash; ${ticker.toUpperCase()}</title>
    </head>
    
    <body>
      <!-- first column -->
      <div class="column3">
        <h1>${title}</h1>
        <p class="justify"><wikipedia:summary title="${title}" maxChars="400" /></p>
        <p class="small italics">from Wikipedia's article on ${title.endsWith(".") ? title : title + "."} <g:link mapping="faq" fragment="wikipedia">what's this?</g:link></p>
        
        <h2 class="understroked">Financial Overview</h2>
        <g:set var="financialInfo" value="${[
          ["Last Close", "lastClose"],
          ["1-Year Target", "yearTarget"],
          ["Day Range", "dayRange"],
          ["Year Range", "yearRange"],
          ["Market Cap", "marketCap"],
          ["P/E Ratio", "peRatio"]
        ]}" />
        
        <g:each var="info" in="${financialInfo}">
          <div class="infoRow">
            <div class="key">${info[0]}</div>
            <div class="value"><finance:stock ticker="${ticker}" req="${info[1]}" /></div>
            <div class="clear"></div>
          </div>
        </g:each>
        
        <%-- <user:ifLoggedIn> --%>
          <a class="yellowButton" href="#">Invest</a>
        <%-- </user> --%>
      </div>
      
    </body>
  </html>
</finance:stocks>