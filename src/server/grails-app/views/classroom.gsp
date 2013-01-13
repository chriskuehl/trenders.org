<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>My Classroom</title>
  </head>
  <body>
    <h1>My Classroom</h1>
    <p>You're currently a member of a classroom managed by <strong>${request.user.getClassroom().getTeacher().getDisplayName()}</strong>. The class access code is:
      <span style="border: dashed 2px rgb(150, 150, 150); color: rgb(30, 30, 30); font-size: 16px; padding: 5px; font-weight: bold;">${request.user.getClassroom().getClassCode()}</span>
    </p>
    
    <h2>Class Members</h2>
    <g:if test="${request.user.getClassmates().size() > 1}">
      <table>
        <tr>
          <th>Rank</th>
          <th>Name</th>
          <th>Current Balance</th>
          <th>Portfolio Value</th>
          <th>Total Assets</th>
          <th>Largest Investment</th>
        </tr>
        
        <g:set var="i" value="${0}" />
        <g:each in="${request.user.getClassmatesByTotalAssets()}" var="user">
          <tr>
            <td>${(++ i)}.</td>
            <td>${user.getDisplayName()}</td>
            <td>$${user.getPrettyBalance()}</td>
            <td>$${user.getPrettyPortfolioValue()}</td>
            <td>$${user.getPrettyTotalAssets()}</td>
            <td>
              <g:set var="mostSpent" value="${user.getPrettyLargestInvestment()}" />
              
              <g:if test="${mostSpent == null}">
                <em>no investments</em>
              </g:if>
              <g:else>
                <g:link mapping="stock" params="${[ticker: mostSpent[0].getTicker()]}">${finance.simpleName(name: finance.stock(ticker: mostSpent[0].getTicker(), req: "name"))}</g:link> (${mostSpent[1]})
              </g:else>
            </td>
          </tr>
        </g:each>
      </table>
    </g:if>
    
    <g:else>
      <p>There aren't any students in this classroom (yet!).
    </g:else>
  </body>
</html>