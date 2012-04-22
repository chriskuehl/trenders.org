<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>My Classroom</title>
  </head>
  <body>
    <h1>My Classroom</h1>
    <p>You're currently a member of a classroom managed by ${request.user.getClassroom().getTeacher().getEmail()}.</p>
    
    <h2>Class Members</h2>
    <g:if test="${request.suser.getClassmates().size() > 1}">
      <table>
        <tr>
          <td>Name</td>
          <td>Email</td>
          <td>Current Balance</td>
          <td>Portfolio Value</td>
        </tr>
        
        
        <g:each in="${request.suser.getClassmates()}" var="user">
          <g:if test="${user.getClassroom().getTeacher() != user}">
            <tr>
              <td>${user.getDisplayName()}</td>
              <td>${user.getEmail()}</td>
              <td>${user.getPrettyBalance()}</td>
              <td>${user.getPrettyPortfolioValue()}</td>
            </tr>
          </g:if>
        </g:each>
      </table>
    </g:if>
    
    <g:else>
      <p>There aren't any students in this classroom (yet!).
    </g:else>
  </body>
</html>