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
          <th>&nbsp;</th>
        </tr>
        
        <g:each var="user" in="${admin.getUsers()}">
          <tr>
            <td>${user.getEmail()}</td>
            <td>${user.isEmailConfirmed()}</td>
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
          <th>Email</th>
          <th>Token Hash</th>
        </tr>
        
        <g:each var="userSession" in="${admin.getUserSessions()}">
          <tr>
            <td>${userSession.getUser().getEmail()}</td>
            <td>${userSession.getSessionTokenHash()}</td>
          </tr>
        </g:each>
      </table>
    </div>
  </body>
</html>