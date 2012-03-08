<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <content tag="noRobots">true</content>
    
    <title>Cache Control</title>
  </head>
  <body>
    <div id="page-body" role="main">
      <h1>Cache Browse: ${params.service}</h2>
      
      <p><g:link action="index">&laquo; Back to Cache Control</g:link></p>
      
      <table>
        <tr>
          <th>Key</th>
          <th>Value</th>
        </tr>
        
        <g:each var="key" in="${servletContext.cache[params.service].keySet()}">
          <tr>
            <td>${key}</td>
            <td>${servletContext.cache[params.service][key]}</td>
          </tr>
        </g:each>
      </table>
    </div>
  </body>
</html>