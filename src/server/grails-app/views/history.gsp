<g:set var="events" value="${request.user.getOrderedHistoryEvents()}" />
<g:set var="tickers" value="${request.user.getAllHistoryCompanies()}" />
<finance:stocks tickers="${tickers}">
  <!doctype html>
  <html>
    <head>
      <meta name="layout" content="main" />

      <title>My History</title>
    </head>
    <body>
      <h1>My History</h1>

      <g:if test="${events.size() > 0}">
        <p>Here is a history of your activity on the stock market:</p>
        <table>
          <tr>
            <th>Date</th>
            <th>Company</th>
            <th>Action</th>
            <th>Quantity</th>
            <th>Balance Change</th>
          </tr>

          <g:each in="${events}" var="event">
            <tr>
              <td>${event.getDate()}</td>
              <td><g:link mapping="stock" params="${[ticker: event.getTicker()]}">${finance.stock(ticker: event.getTicker(), req: "name")}</g:link></td>
              <td>${event.getWasPurchase() ? "Purchase" : "Sale"}</td>
              <td>${event.getQuantity()}</td>
              <td style="color: ${event.getWasPurchase() ? "red" : "green"};">$${event.getMoney().toDouble().trunc(2)}</td>
            </tr>
          </g:each>
        </table>
      </g:if>

      <g:else>
        <p>You haven't done anything yet.</p>
      </g:else>
    </body>
  </html>
</finance:stocks>