<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>Contact Us</title>
  </head>
  <body>
    <h1>Contact Us</h1>
    
    <form method="POST">
      <table>
        <tr>
          <th>Name:</th>
          <td><input type="text" name="name" value="${request.user ? request.user.displayName : ""}" /></td>
        </tr>
        
        <tr>
          <th>Email:</th>
          <td><input type="text" name="email" value="${request.user ? request.user.email : ""}" /></td>
        </tr>
        
        <tr>
          <th style="vertical-align: top;">Comments:</th>
          <td>
            <textarea name="comments" style="width: 400px; height: 200px;"></textarea>
          </td>
        </tr>
      </table>
      
      <input type="submit" name="sendMessage" value="Send Message" />
    </form>
  </body>
</html>