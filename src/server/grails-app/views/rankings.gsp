<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>Classroom Rankings</title>
  </head>
  <body>
    <h1>Classroom Rankings</h1>
    <g:if test="${request.user.getClassmates().size() > 1}">
      <table>
        <tr>
          <th>Name</th>
          <th>Total Assets</th>
        </tr>
        
        
        <g:each in="${request.user.getClassmatesByTotalAssets()}" var="user">
          <tr>
            <td>${user.getDisplayName()}</td>
            <td>$${user.getPrettyTotalAssets()}</td>
          </tr>
        </g:each>
      </table>
    </g:if>
    
    <g:else>
      <p>There aren't any students in this classroom (yet!).
    </g:else>
  </body>
</html>