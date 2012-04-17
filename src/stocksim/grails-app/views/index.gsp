<!doctype html>
<html>
  <head>
    <r:require modules="page_home" />
    <meta name="layout" content="main" />
    <title>trenders.org: a simple stock market sim for students &amp; teachers</title>
  </head>
  <body>
    <!--
    ${resource(dir: "images", file: "trending.png")}
    ${resource(dir: "images", file: "classrooms.png")}
    ${resource(dir: "images", file: "lessons.png")}
    -->
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
    
    <div class="sidebar">
      <h1>Get started</h1>
      <p>Signing up is quick, easy, and doesn’t require an email or password; click below to get started.</p>
    
      <h1>Have an iPad?</h1>
      <p>This site is optimized for use on iPads. Visit this site to install the trenders.org app.</p>
      
      <h1>Browse</h1>
      <ul>
        <finance:eachSector>
          <li><a href="browse?s=${finance.sector(url: "true")}"><finance:sector url="false" /></a></li>
        </finance:eachSector>
      </ul>
    </div>
    
    <div class="column3">
      <h1>Market Movement</h1>
      <p><strong>Dow Jones Industrial Average</strong></p>
      <g:set var="chartWidth" value="329" />
      <g:set var="chartHeight" value="90" />
      <div id="chart" style="background-image: url('http://charts.reuters.com/reuters/enhancements/chartapi/chart_api.asp?width=${chartWidth}&height=${chartHeight}&symbol=.DJI&duration=5&headertype=none'); width: ${chartWidth}px; height: ${chartHeight}px;">
        <p>Chart powered by Reuters.</p>
      </div>
      
      
      <p><strong>S&amp;P 500</strong></p>
      <g:set var="chartWidth" value="329" />
      <g:set var="chartHeight" value="90" />
      <div id="chart" style="background-image: url('http://charts.reuters.com/reuters/enhancements/chartapi/chart_api.asp?width=${chartWidth}&height=${chartHeight}&symbol=.INX&duration=5&headertype=none'); width: ${chartWidth}px; height: ${chartHeight}px;">
        <p>Chart powered by Reuters.</p>
      </div>
      
      <h1>Trending</h1>
      <table id="trendingTable">
        <tr>
          <th style="width: 230px;">&nbsp;</th>
          <th style="width: 80px;">% Change</th>
          <th style="width: 80px;">Mkt. Cap</th>
        </tr>
        
        <tr>
          <th>Gainers</th>
          <th>&nbsp;</th>
          <th>&nbsp;</th>
        </tr>
        
        <finance:gainers num="4">
          <tr id="row-${finance.mover(req: "ticker").toUpperCase()}" class="stock">
            <td>${finance.mover(req: "name")}</td>
            <td class="green">${finance.mover(req: "change")}</td>
            <td>${finance.mover(req: "marketCap")}</td>
          </tr>
        </finance:gainers>
        
        <tr>
          <th>Losers</th>
          <th>&nbsp;</th>
          <th>&nbsp;</th>
        </tr>
        
        <finance:losers num="4">
          <tr id="row-${finance.mover(req: "ticker").toUpperCase()}" class="stock">
            <td>${finance.mover(req: "name")}</td>
            <td class="red">${finance.mover(req: "change")}</td>
            <td>${finance.mover(req: "marketCap")}</td>
          </tr>
        </finance:losers>
      </table>
    </div>
    
    <div class="column3">
      <h1>News &amp; Advice</h1>
      <ul class="stockList" style="margin-top: 7px !important;">
        <googleNews:articles query="finance" num="5">
          <li>
            <strong><a href="${googleNews.article(req: "link")}" class="gray" target="_blank"><g:trim elipses="true" content="${googleNews.article(req: "title")}" chars="45" /></a></strong><br />
            <span class="soft"><googleNews:article req="author" />, <g:toRecentTime date="${googleNews.article(req: "pubDate")}" />. <a class="small" href="${googleNews.article(req: "relatedLink")}" target="_blank">related</a></span><br />
            <p><g:trim elipses="true" content="${googleNews.article(req: "description")}" chars="150" /></p>
          </li>
        </googleNews:articles>
      </ul>
      <p class="small italics">from a Google News search for finance <g:link mapping="faq" fragment="google-news">what's this?</g:link></p>
      
    </div>
    
    <div class="clear"></div>
  </body>
</html>
