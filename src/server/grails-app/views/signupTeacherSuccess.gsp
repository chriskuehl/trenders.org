<!doctype html>
<html>
  <head>
    <r:require modules="page_signup" />
    <meta name="layout" content="main" />
    
    <title>Get Started: Teacher</title>
  </head>
  <body>
    <h1>Get Started: Teacher</h1>
    <p>Your class has been successfully created. The class ID to give your students is:</p>
    <div style="border: dashed 2px rgb(150, 150, 150); color: rgb(30, 30, 30); font-size: 16px; padding: 5px; font-weight: bold;">${request.user.getClassroom().getClassCode()}</div>
  </body>
</html>