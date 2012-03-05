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
        <p><wikipedia:summary title="${title}" maxChars="400" /></p>
        <p class="small italics">from Wikipedia's article on ${title.endsWith(".") ? title : title + "."} <g:link mapping="faq" fragment="wikipedia">what's this?</g:link></p>
      </div>

    </body>
  </html>
</finance:stocks>