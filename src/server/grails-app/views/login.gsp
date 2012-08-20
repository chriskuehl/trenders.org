<!doctype html>
<html>
  <head>
    <r:require modules="page_signup" />
    <meta name="layout" content="main" />
    
    <title>Login</title>
  </head>
  <body>
    <div class="alert alertInfo">
      <g:img dir="images" file="alert-info.png" alt="Warning" />
      <strong>Beta testers:</strong> If you signed up before September 2012, you haven't created a password yet. That's ok&ndash;just enter your email and hit "Login". We'll send you an email with a link you can click to login.
    </div>
    
    <h1 style="margin-top: 30px;">Login</h1>
    
    <form action="${createLink(mapping: "login")}" method="POST" style="margin-top: 20px; margin-bottom: 20px;">
      <table>
        <tr>
          <th>Email:</th>
          <td><input type="email" style="width: 200px;" name="email" placeholder="joe&#64;example.com" /></td>
        </tr>

        <tr>
          <th>Password</th>
          <td>
            <input type="password" style="width: 200px;" name="password" /><br />
            <span style="font-size: 10px; font-style: italic;">(if you have one)</span>
          </td>
        </tr>

        <tr>
          <th>&nbsp;</th>
          <td><input type="submit" name="login" value="Login &raquo;" /></td>
        </tr>
      </table>
    </form>
    
    <hr />
    <p>Need an account? No problem&ndash;you can sign up <g:link mapping="signup">here</g:link>.</p>
  </body>
</html>