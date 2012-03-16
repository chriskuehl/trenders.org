<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>Admin Panel</title>
  </head>
  <body>
    <div id="page-body" role="main">
      <h1>Admin Panel</h1>
      <ul>
        <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
          <li><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>
        </g:each>
      </ul>
    </div>
  </body>
</html>
