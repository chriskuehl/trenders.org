<g:set var="ticker" value="${params.ticker.toLowerCase()}" />
<finance:stocks tickers="${[ticker]}">
  <g:set var="title" value="${finance.stock(ticker: ticker, req: "name")}" />
  <g:set var="simpleTitle" value="${finance.simpleName(name: title)}" />
  <g:set var="price" value="${finance.stock(ticker: ticker, req: "value")}" />
  <!doctype html>
  <html>
    <head>
      <meta name="layout" content="main" />

      <title>Sell Stock in ${simpleTitle}</title>
    </head>
    <body>
      <h1>Sell Stock in ${title}</h1>
      <p>You currently own <strong><user:getNumberOwned ticker="${ticker}" /></strong> shares of ${simpleTitle}.</p>
      <form action="${createLink(mapping: "doSell")}" method="POST">
        <table>
          <tr>
            <th>Current Balance:</th>
            <td>$${request.user.getPrettyBalance()}</td>
          </tr>

          <tr>
            <th>Flat Trading Fee:</th>
            <td>&ndash; $8.95</td>
          </tr>

          <tr>
            <th>Stock Price:</th>
            <td>+ $${price} x <input type="text" style="width: 50px;" id="num" name="num" value="0" /> (<span id="cost"></span>)</td>
          </tr>

          <tr>
            <th>Resulting Balance:</th>
            <td>= <span id="balance"></span></td>
          </tr>
        </table>
        
        <input type="hidden" name="ticker" value="${ticker}" />
        <input id="submit" name="submit" type="submit" value="Sell" />
      </form>
      
      <script>
        var startBalance = ${request.user.getBalance() - 8.95};
        var price = ${price};
        var max = <user:getNumberOwned ticker="${ticker}" />;
        
        $(function() {
          recalc();
          
          $("#num").keydown(recalc);
          $("#num").keyup(recalc);
          $("#num").change(recalc);
          
          $("#submit").click(function(e) {
            if (! confirm("Are you sure you want to sell these shares?")) {
              e.preventDefault();
              return false;
            }
          });
        });

        function recalc() {
          //var num = Math.min(max, Math.max(0, Math.floor($("#num").val())));
          var num = Math.max(0, Math.floor($("#num").val()));
          
          if (isNaN(num)) {
            num = 0;
          }

          var cost = (num * price);
          $("#cost").html("$" + cost.toFixed(2));
          
          var balance = (startBalance + cost);
          $("#balance").html("$" + balance.toFixed(2));
          
          if (num < 1 || num > max) {
            $("#num").css({color: "red"});
            $("#submit").attr("disabled", "disabled");
          } else {
            $("#num").removeAttr("style");
            $("#submit").removeAttr("disabled");
          }
        }
      </script>
    </body>
  </html>
</finance:stocks>