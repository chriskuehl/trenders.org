<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>trenders.org: a simple stock market sim for students &amp; teachers</title>
  </head>
  <body>
    <div id="page-body" role="main">
      <h1>Welcome to trenders.org</h1>
      
      <%-- 
      <finance:stocks tickers="['aapl', 'goog', 'yhoo']">
      <ul>
      <li>AAPL: <finance:stock ticker="aapl" req="price" /></li>
      <li>GOOG: <finance:stock ticker="goog" req="price" /></li>
      <li>YHOO: <finance:stock ticker="yhoo" req="price" /></li>
      </ul>
      </finance:stocks> --%>

      <h2>Controllers:</h2>
      <ul>
        <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
          <li><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
        </g:each>
      </ul>
    </div>
  </body>
</html>
