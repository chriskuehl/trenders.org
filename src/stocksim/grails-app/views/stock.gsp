<g:set var="ticker" value="${params.ticker.toLowerCase()}" />
<finance:stocks tickers="${[ticker]}">
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
        <h1><finance:stock ticker="${ticker}" req="name" /></h1>
        <p><wikipedia:summary title="${finance.stock(ticker: ticker, req: "name")}" maxChars="400" /></p>
      </div>

    </body>
  </html>
</finance:stocks>