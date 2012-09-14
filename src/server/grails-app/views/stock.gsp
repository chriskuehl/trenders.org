<g:set var="ticker" value="${params.ticker.toLowerCase()}" />
<g:set var="relatedAll" value="${finance.relatedStocks(ticker: ticker)}" />
<g:set var="related" value="${pickRandom(array: relatedAll, num: 7)}" />

<g:set var="allTickers" value="${relatedAll.clone()}" />
<g:addToCollection collection="${allTickers}" element="${ticker.toUpperCase()}" />

<finance:stocks tickers="${allTickers}">
  <g:set var="title" value="${finance.stock(ticker: ticker, req: "name")}" />
  <g:set var="simpleTitle" value="${finance.simpleName(name: title)}" />
  <!doctype html>
  <html>
    <head>
      <r:require modules="page_stock" />
      <content tag="hasColumns">true</content>
      <meta name="layout" content="main" />
      <title><finance:stock ticker="${ticker}" req="name" /> &ndash; ${ticker.toUpperCase()}</title>
    </head>
    
    <body>

      <%-- first column --%>
      <div class="column3">
        <h1>${simpleTitle}</h1>
        <p class="justify">
          <wikipedia:summary title="${title}" maxChars="400">
            <p class="small italics">from Wikipedia's article on ${simpleTitle.endsWith(".") ? simpleTitle : simpleTitle + "."} <g:link mapping="faq" fragment="wikipedia">what's this?</g:link></p>
          </wikipedia:summary>
        </p>
        
        
        <h2 class="understroked">Financial Overview</h2>
        <g:set var="financialInfo" value="${[
          ["Previous Close", "prevClose"],
          ["1-Year Target", "yearTarget"],
          ["Day Range", "dayRange"],
          ["Year Range", "yearRange"],
          ["Market Cap", null, formatBigNumber(num: finance.stock(ticker: ticker, req: "marketCap").toDouble())],
          ["P/E Ratio", "peRatio"]
        ]}" />
        
        <g:each var="info" in="${financialInfo}">
          <div class="infoRow">
            <div class="key">${info[0]}</div>
            <div class="value">
              <g:if test="${info[1] != null}">
                <finance:stock ticker="${ticker}" req="${info[1]}" />
              </g:if>
              <g:else>
                ${info[2]}
              </g:else>
            </div>
            <div class="clear"></div>
          </div>
        </g:each>
        
        <user:ifLoggedIn>
          <g:link mapping="invest" params="${[ticker: ticker]}" class="yellowButton">Invest</g:link>
        </user:ifLoggedIn>
        <user:ifNotLoggedIn>
          <p>Log in to invest in this company.</p>
        </user:ifNotLoggedIn>
      </div>
      
      <%-- second column --%>
      <div class="column3">
        <h3 id="lastTradeLabel">Last trading price:</h3>
        <div id="lastTrade"><finance:stock ticker="${ticker}" req="lastSale" /></div>
        <div id="lastTradeInfo">
          <span id="tradeMove" class="${finance.stock(ticker: ticker, req: "dayChangePercent").startsWith("-") ? "tradeMoveDown" : "tradeMoveUp"}"><finance:stock ticker="${ticker}" req="dayChange" /> (<finance:stock ticker="${ticker}" req="dayChangePercent" />)</span><br />
          from previous day
        </div>
        
        <div class="clear"></div>
        
        <a id="chartBack" target="_blank" href="http://finance.yahoo.com/q/bc?s=${ticker.toUpperCase()}+Basic+Chart">
          <g:set var="chartWidth" value="266" />
          <g:set var="chartHeight" value="150" />
          <div id="chart" style="background-image: url('http://charts.reuters.com/reuters/enhancements/chartapi/chart_api.asp?width=${chartWidth}&height=${chartHeight}&symbol=${ticker.toUpperCase()}.${finance.stock(ticker: ticker, req: "exchange") == "nasdaq" ? "O" : "N"}&duration=5&headertype=none'); width: ${chartWidth}px; height: ${chartHeight}px;">
            <p>Chart powered by Reuters.</p>
          </div>
          
          <user:userAgentChoose iOS="Tap" other="Click" /> to view more charts.
        </a>
        
        <h2 class="understroked">Recent News Articles</h2>
        <ul class="stockList" style="margin-top: 7px !important;">
          <googleNews:articles query="${simpleTitle.toString()}" num="5">
            <li>
              <strong><a href="${googleNews.article(req: "link")}" class="gray" target="_blank"><g:trim elipses="true" content="${googleNews.article(req: "title")}" chars="45" /></a></strong><br />
              <span class="soft"><googleNews:article req="author" />, <g:toRecentTime date="${googleNews.article(req: "pubDate")}" />. <a class="small" href="${googleNews.article(req: "relatedLink")}" target="_blank">related</a></span><br />
            </li>
          </googleNews:articles>
        </ul>
        <p class="small italics">from a Google News search for ${simpleTitle} <g:link mapping="faq" fragment="google-news">what's this?</g:link></p>
      </div>
      
      <%-- third column --%>
      <div class="column3">
        <h2 class="understroked">On the Web</h2>
        <p>Many sites offer advice and information on stocks. You might find the following sites useful when researching ${simpleTitle}.</p>
        <ul class="stockSiteLinks">
          <li><a target="_blank" href="http://www.investopedia.com/markets/stocks/${ticker.toUpperCase()}">Investopedia</a></li>
          <li><a target="_blank" href="http://finance.yahoo.com/q?s=${ticker.toUpperCase()}">Yahoo! Finance</a></li>
        </ul>
        
        <ul class="stockSiteLinks">
          <li><a target="_blank" href="http://www.google.com/finance?client=ob&q=${ticker.toUpperCase()}">Google Finance</a></li>
          <li><a target="_blank" href="http://money.cnn.com/quote/quote.html?symb=${ticker.toUpperCase()}">CNN Money</a></li>
        </ul>
        
        <div class="clear"></div>
        
        <h2 class="understroked">Related Companies</h2>
        <p>The following companies are in the same industry as ${simpleTitle}. You should familiarize yourself with competitors before investing in a business.</p>
      
        <ul class="stockList">
          <g:each var="relatedStock" in="${related}">
            <li>
              <g:link mapping="stock" params="${[ticker: relatedStock]}" class="block">
                <strong><finance:simpleName name="${finance.stock(ticker: relatedStock, req: "name")}" /></strong>
                <span class="soft">${relatedStock}</span>
                <br />

                <span class="stockStats">
                  <strong>Price:</strong> <finance:stock ticker="${relatedStock}" req="lastSale" />&nbsp;
                  <strong>P/E:</strong> <finance:stock ticker="${relatedStock}" req="peRatio" />&nbsp;
                  <strong>Market Cap:</strong> <finance:stock ticker="${relatedStock}" req="marketCap" />
                </span>
              </g:link>
            </li>
          </g:each>
        </ul>
      </div>
    </body>
  </html>
</finance:stocks>