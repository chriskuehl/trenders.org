<!doctype html>
<html>
  <head>
    <r:require modules="page_signup" />
    <meta name="layout" content="main" />
    
    <title>Get Started</title>
  </head>
  <body>
    <h1>Get Started</h1>
    <p>What type of account are you trying to create?</p>
    
    <a href="${createLink(mapping: "signupStudent")}" class="signupLink" id="signupStudent">Student</a>
    <a href="${createLink(mapping: "signupTeacher")}" class="signupLink" id="signupTeacher">Teacher</a>
  </body>
</html>