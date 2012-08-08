<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <content tag="noRobots">true</content>
    
    <title>User Control</title>
  </head>
  <body>
    <div id="page-body" role="main">
      <h1>User Control</h2>
      
      <h2>Users</h2>
      <table>
        <tr>
          <th>Email</th>
          <th>Confirmed</th>
          <th>Class Code</th>
          <th>&nbsp;</th>
        </tr>
        
        <g:each var="user" in="${admin.getUsers()}">
          <tr>
            <td>${user.getEmail()}</td>
            <td>${user.isEmailConfirmed()}</td>
            <td>${user.getClassroom() ? user.getClassroom().getClassCode() : "<em>none</em>"}</td>
            <td><g:link action="become" params="${[user: user.getId()]}">Become</g:link></td>
          </tr>
        </g:each>
      </table>
      
      <h3>Add User</h3>
      <form method="POST" action="${createLink(action: "addUser")}">
        <p><label for="email">Email:</label> <input type="text" name="email" id="email" value="" /></p>
        <p><input type="submit" value="Add" /></p>
      </form>
      
      <h2>User Sessions</h2>
      <table>
        <tr>
          <th>User ID</th>
          <th>Email</th>
          <th>Token Hash</th>
          <th>&nbsp;</th>
        </tr>
        
        <g:each var="userSession" in="${admin.getUserSessions()}">
          <tr>
            <td>${userSession.getUser().getId()}</td>
            <td>${userSession.getUser().getEmail()}</td>
            <td>${userSession.getSessionTokenHash()}</td>
            <td><g:link action="invalidateSession" params="${[session: userSession.getId()]}">Invalidate</g:link></td>
          </tr>
        </g:each>
      </table>
      
      <h2>Classrooms</h2>
      <table>
        <tr>
          <th>Teacher ID</th>
          <th>Class Code</th>
        </tr>
        
        <g:each var="classroom" in="${admin.getClassrooms()}">
          <tr>
            <td>${classroom.getTeacher().getId()}</td>
            <td>${classroom.getClassCode()}</td>
          </tr>
        </g:each>
      </table>
      
      <h3>Create Classroom</h3>
      <form method="POST" action="${createLink(action: "createClassroom")}">
        <p><label for="classroomEmail">Email:</label> <input type="text" name="email" id="classroomEmail" value="" /></p>
        <p><input type="submit" value="Add" /></p>
      </form>
    </div>
  </body>
</html>