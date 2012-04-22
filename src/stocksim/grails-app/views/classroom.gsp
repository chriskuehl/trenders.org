<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>My Classroom</title>
  </head>
  <body>
    <h1>My Classroom</h1>
    <p>You're currently a member of a classroom managed by ${request.user.getClassroom().getTeacher().getEmail()}</p>
    
    <h2>Class Members</h2>
    <g:if test="${true}">
      <table>
        <tr>
          
        </tr>
        
        <g:each in="${user.getClassmates()}" var="user">
          
        </g:each>
      </table>
    </g:if>
    
    <g:else>
      <p>There aren't any students in this classroom (yet!).
    </g:else>
  </body>
</html>