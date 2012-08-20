<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>${rUser.passwordHash == null ? "Create" : "Reset"} Password</title>
  </head>
  <body>
    <h1>${rUser.passwordHash == null ? "Create" : "Reset"} Password</h1>
    
    <form method="POST">
      <table>
        <tr>
          <th>Email:</th>
          <td><input type="email" style="width: 200px;" name="email" disabled="disabled" value="${rUser.email}" /></td>
        </tr>

        <tr>
          <th>New Password:</th>
          <td>
            <input type="password" style="width: 200px;" name="password" />
          </td>
        </tr>

        <tr>
          <th>&nbsp;</th>
          <td><input type="submit" name="change" value="${rUser.passwordHash == null ? "Create" : "Change"} Password &raquo;" /></td>
        </tr>
      </table>
    </form>
  </body>
</html>