<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    
    <title>Teacher Panel: Classroom Configuration</title>
  </head>
  <body>
    <h1>Teacher Panel: Classroom Configuration</h1>
    <p>You're currently a member of a classroom managed by <strong>${request.user.getClassroom().getTeacher().getDisplayName()}</strong>. The class access code is:
      <span style="border: dashed 2px rgb(150, 150, 150); color: rgb(30, 30, 30); font-size: 16px; padding: 5px; font-weight: bold;">${request.user.getClassroom().getClassCode()}</span>
    </p>
    
    <h2>Class Members</h2>
    <g:if test="${request.user.getClassmates().size() > 1}">
      <table>
        <tr>
          <th>Display Name</th>
          <th>Email Address</th>
          <th>Last Password Change</th>
          <th>&nbsp;</th>
        </tr>
        
        <g:set var="i" value="${0}" />
        <g:each in="${request.user.getClassmates()}" var="user">
          <tr>
            <td><a href="#" data-user="${user.getEmail()}" data-action="editDisplayName" class="actionIcon edit" title="Change Display Name">&nbsp;</a> ${user.getDisplayName()}</td>
            <td><a href="#" data-user="${user.getEmail()}" data-name="${user.getDisplayName()}" data-action="editEmail" data-current-value="${user.getEmail()}" class="actionIcon edit" title="Change Email Address">&nbsp;</a> ${user.getEmail()} </td>
            <td><a href="#" data-user="${user.getEmail()}" data-action="editPassword" class="actionIcon edit" title="Change Password">&nbsp;</a> ${user.getLastPasswordChange() == null ? "Never" : user.getLastPasswordChange()}</td>
            <td>
              <g:if test="${request.user.getEmail() != user.getEmail()}">
                <a href="#" data-user="${user.getEmail()}" data-action="delete" class="actionIcon delete" title="Delete Student">&nbsp;</a>
              </g:if>
            </td>
          </tr>
        </g:each>
      </table>
    </g:if>
    <g:else>
      <p>There aren't any students in your classroom (yet!).
    </g:else>
  
    <script>
      $(document).ready(function() {
        $(".actionIcon").click(function(e) {
          var studentEmail = $(this).data("user");
          var studentName = $(this).data("name");
          var action = $(this).data("action");
          var currentValue = $(this).data("currentValue");
          
          if (action == "editDisplayName") {
            alert("Editing a student's display name isn't supported yet.");
          } else if (action == "editEmail") {
            var newEmail = prompt("Enter a new email address for student \"" + studentName + "\".", currentValue);
            
            if (newEmail) {
              var j = $.post("teacher/updateEmail", {email: studentEmail, newEmail: newEmail});
              j.success(function() {
                window.location = window.location;
              });
              j.error(function() {
                alert("Error updating email. Is that a real email address? Is it already in use?")
              });
            }
          } else if (action == "editPassword") {
            alert("Changing a student's password isn't supported yet.");
          } else if (action == "delete") {
            alert("Removing students from your classroom isn't supported yet.");
          }
          
          e.preventDefault();
          return false;
        });
      });
    </script>
  </body>
</html>