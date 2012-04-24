<g:set var="ticker" value="${params.ticker.toLowerCase()}" />
<finance:stocks tickers="${allTickers}">
  <g:set var="title" value="${finance.stock(ticker: ticker, req: "name")}" />
  <g:set var="simpleTitle" value="${finance.simpleName(name: title)}" />
  <g:set var="price" value="${finance.stock(ticker: ticker, req: "price")}" />
  <!doctype html>
  <html>
    <head>
      <meta name="layout" content="main" />

      <title>Invest in ${simpleTitle}</title>
    </head>
    <body>
      <h1>Invest in ${titls}</h1>
      <p>You can purchase up to <strong></strong> shares of ${simpleTitle}.</p>
    </body>
  </html>
</finance:stocks>