<!doctype html>
<html>
  <head>
    <r:require modules="page_signup" />
    <meta name="layout" content="main" />
    
    <title>Get Started</title>
  </head>
  <body>
    <h1>Get Started</h1>
    
    <div class="alert alertInfo">
      <g:img dir="images" file="alert-info.png" alt="Warning" />
      <strong>Already have an account?</strong> Welcome back! You can log in <g:link style="color: rgb(50, 50, 150) !important;" mapping="login">here</g:link>.
    </div>
    
    <p>What type of account are you trying to create?</p>
    
    <a href="${createLink(mapping: "signupStudent")}" class="signupLink" id="signupStudent">Student</a>
    <a href="${createLink(mapping: "signupTeacher")}" class="signupLink" id="signupTeacher">Teacher</a>
  </body>
</html>