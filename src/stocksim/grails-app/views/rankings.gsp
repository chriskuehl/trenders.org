<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>My Classroom</title>
  </head>
  <body>
    <h1>My Classroom</h1>
    <p>You're currently a member of a classroom managed by <strong>${request.user.getClassroom().getTeacher().getDisplayName()}</strong>. The class access code is:</p>
    <div style="border: dashed 2px rgb(150, 150, 150); color: rgb(30, 30, 30); font-size: 16px; padding: 5px; font-weight: bold;">${request.user.getClassroom().getClassCode()}</div>
    
    <h2>Class Members</h2>
    <g:if test="${request.user.getClassmates().size() > 1}">
      <table>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Current Balance</th>
          <th>Portfolio Value</th>
          <th>Total Assets</th>
        </tr>
        
        
        <g:each in="${request.user.getClassmates()}" var="user">
          <tr>
            <td>${user.getDisplayName()}</td>
            <td>${user.getEmail()}</td>
            <td>$${user.getPrettyBalance()}</td>
            <td>$${user.getPrettyPortfolioValue()}</td>
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