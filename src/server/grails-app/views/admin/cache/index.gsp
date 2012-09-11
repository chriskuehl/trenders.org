<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <content tag="noRobots">true</content>
    
    <title>Cache Control</title>
  </head>
  <body>
    <div id="page-body" role="main">
      <h1>Cache Control</h1>
      <form method="POST" action="${createLink(action: "flush")}">
        <p><input type="submit" value="Flush Cache" /></p>
      </form>
      
      <form method="POST" action="${createLink(action: "flushStockData")}">
        <p><input type="submit" value="Flush Stock Data Cache (this will take some time)" /></p>
      </form>
      
      <h2>Cache Services</h2>
      <ul>
        <g:each var="service" in="${servletContext.cache.keySet()}">
          <li><g:link action="browse" params="${[service: service]}">${service}</g:link></li>
        </g:each>
      </ul>
    </div>
  </body>
</html>