<!doctype html>
<html>
  <head>
    <r:require modules="page_home" />
    <meta name="layout" content="main" />
    <title>trenders.org: a simple stock market sim for students &amp; teachers</title>
  </head>
  <body>
    <div id="flag">
      <p>trenders.org is an educational site where you can learn about personal investing and personal finance alone or as part of a class.</p>
      
      <ul>
        <%-- lessons --%>
        <li>
          <a href="${createLink(mapping: "about_lessons")}" class="homeIcon" id="homeIcon-lessons">
            <div class="flagIcon" id="flagIconLessons"><div>Lessons</div></div>
          </a>
        </li>
        
        <%-- stock sim --%>
        <li>
          <a href="${createLink(mapping: "about_stocksim")}" class="homeIcon" id="homeIcon-stocksim">
            <div class="flagIcon" id="flagIconStockSim"><div>Stock Simulator</div></div>
          </a>
        </li>
        
        <%-- classrooms --%>
        <li>
          <a href="${createLink(mapping: "about_classrooms")}" class="homeIcon" id="homeIcon-classrooms">
            <div class="flagIcon" id="flagIconClassrooms"><div>Classrooms</div></div>
          </a>
        </li>
      </ul>
      
      <div class="clear"></div>
    </div>
  </body>
</html>
