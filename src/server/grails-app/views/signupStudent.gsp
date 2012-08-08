<!doctype html>
<html>
  <head>
    <r:require modules="page_signup" />
    <meta name="layout" content="main" />
    
    <title>Get Started: Student</title>
  </head>
  <body>
    <h1>Get Started: Student</h1>
    <p>We just need a little bit of information before we can get you started as a student.</p>
    
    <form method="POST">
      <table>
        <tr>
          <th>Email:</th>
          <td><input type="text" name="email" value="" /></td>
        </tr>
        
        <tr>
          <th>Name:</th>
          <td><input type="text" name="name" value="" /></td>
        </tr>
        
        <tr>
          <th>Class ID:</th>
          <td><input type="text" name="classid" value="" /></td>
        </tr>
      </table>
      
      <input type="submit" name="signup" value="Join Classroom" />
    </form>
  </body>
</html>