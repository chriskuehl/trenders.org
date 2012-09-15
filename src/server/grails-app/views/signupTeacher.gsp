<!doctype html>
<html>
  <head>
    <r:require modules="page_signup" />
    <meta name="layout" content="main" />
    
    <title>Get Started: Teacher</title>
  </head>
  <body>
    <h1>Get Started: Teacher</h1>
    <p>We just need a little bit of information before we can get you started as a teacher.</p>
    
    <form method="POST">
      <table>
        <tr>
          <th>Name:</th>
          <td><input type="text" name="name" value="" /></td>
        </tr>
        
        <tr>
          <th>Email:</th>
          <td><input type="text" name="email" value="" /></td>
        </tr>
        
        <tr>
          <th>Password:</th>
          <td><input type="password" name="password" value="" /></td>
        </tr>
      </table>
      
      <input type="submit" name="signup" value="Create Classroom" />
    </form>
  </body>
</html>