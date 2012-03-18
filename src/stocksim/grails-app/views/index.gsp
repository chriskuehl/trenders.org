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
            <div class="flagIcon" id="flagIconLessons">Lessons</div>
            <p><strong>Lessons</strong></p>
            <p>Learn the basics about investing and the stock market.</p>
          </a>
        </li>
        
        <%-- stock sim --%>
        <li>
          <a href="${createLink(mapping: "about_stocksim")}" class="homeIcon" id="homeIcon-stocksim">
            <div class="flagIcon" id="flagIconStockSim">Stock Simulator</div>
            <p><strong>Stock Simulator</strong></p>
            <p>Start with a virtual $50k and invest it in the stock market.</p>
          </a>
        </li>
        
        <%-- classrooms --%>
        <li>
          <a href="${createLink(mapping: "about_classrooms")}" class="homeIcon" id="homeIcon-classrooms">
            <div class="flagIcon" id="flagIconClassrooms">Classrooms</div>
            <p><strong>Classrooms</strong></p>
            <p>Setup a classroom and monitor your students’ progress.</p>
          </a>
        </li>
      </ul>
      
      <div class="clear"></div>
    </div>
  </body>
</html>
