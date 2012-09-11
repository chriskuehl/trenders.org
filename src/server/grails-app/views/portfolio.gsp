<finance:stocks tickers="${request.user.getOwnedTickers()}">
  <!doctype html>
  <html>
    <head>
      <meta name="layout" content="main" />

      <title>My Portfolio</title>
    </head>
    <body>
      <h1>My Portfolio</h1>
      <p>Your portfolio is currently worth $<strong>${request.user.getPrettyPortfolioValue()}</strong>. You've spent $<strong>${request.user.getPrettyMoneySpentOnPortfolio()}</strong> on it.</p>

      <h2>Currently Owned Assets</h2>

      <g:if test="${request.user.getOwnedStocks().size() > 0}">
        <table>
          <tr>
            <th>Company</th>
            <th># of Shares</th>
            <th>Current Price</th>
            <th>Total Spent</th>
            <th>Current Value</th>
            <th>Gain</th>
            <th colspan="2">&nbsp;</th>
          </tr>

          <g:each in="${request.user.getOwnedStocks()}" var="stock">
            <tr>
              <td><g:link mapping="stock" params="${[ticker: stock.getTicker()]}">${finance.stock(ticker: stock.getTicker(), req: "name")}</g:link></td>
              <td>${stock.getQuantity()}</td>
              <td>$${finance.stock(ticker: stock.getTicker(), req: "lastSale").toDouble().trunc(2)}</td>
              <td>$${stock.getTotalSpent().toDouble().trunc(2)}</td>
              <td>$${(finance.stock(ticker: stock.getTicker(), req: "lastSale").toDouble() * stock.getQuantity().toDouble()).trunc(2)}</td>
              <td>$${((finance.stock(ticker: stock.getTicker(), req: "lastSale").toDouble() * stock.getQuantity().toDouble()) - stock.getTotalSpent().toDouble()).round(2)}</td>
              <td><g:link mapping="sell" params="${[ticker: stock.getTicker()]}">Sell</g:link></td>
              <td><g:link mapping="invest" params="${[ticker: stock.getTicker()]}">Buy More</g:link></td>
            </tr>
          </g:each>
        </table>
      </g:if>

      <g:else>
        <p>You don't currently have any assets in your portfolio.</p>
      </g:else>
    </body>
  </html>
</finance:stocks>