<!doctype html>
<html>
  <head>
    <meta name="layout" content="main" />
    <title>About This Site</title>
  </head>
  <body>
	<h1>About This Site</h1>
    <p>trenders.org is an open-source project started by <a href="http://www.linkedin.com/pub/chris-kuehl/31/220/1b4" target="_blank">Chris Kuehl</a>. All source code is available under an MIT license on <a href="https://github.com/chriskuehl/trenders.org">GitHub</a>. For information on contributing to this project, see the README.</p>
    
	<p>Follow trenders.org on GitHub:</p>
	<iframe src="http://ghbtns.com/github-btn.html?user=chriskuehl&repo=trenders.org&type=watch&count=true&size=large"
  allowtransparency="true" frameborder="0" scrolling="0" width="170" height="30" style="margin-bottom: 20px;"></iframe>
	
    <h2>Site Platform</h2>
    <p>This site runs on <a target="_blank" href="http://grails.org/">Grails</a>, a nifty web framework for the nifty programming language <a target ="_blank" href="http://groovy.codehaus.org/">Groovy</a>, which has the incredibly convenient property of running on the JVM. The application is hosted on the <a target="_blank" href="http://jetty.codehaus.org/jetty/">Jetty</a> servlet engine. <a target="_blank" href="https://www.varnish-cache.org/">Varnish</a> proxies requests to and from the Jetty server, and enables us to serve static resources extremely quickly.</p>
    <p>A <a target="_blank" href="http://www.mysql.com/">MySQL server</a> acts as the datasource for this application.</p>
    
    <h2>APIs and Data Sources</h2>
    <p>We use publicly-available APIs to get data pertaining to companies or to the stock market. The following sites or services provide us with data:</p>
    <ul>
      <li><a target="_blank" href="http://finance.yahoo.com/">Yahoo! Finance</a>, through their CSV data dumps, is used for company financial information</li>
      <li><a target="_blank" href="http://news.google.com/">Google News</a> is used to provide relevant news articles on company pages and on the home page</li>
      <li><a target="_blank" href="http://www.reuters.com/">Reuters</a> is used to provide graphs for historical (past week) data for stock indices and for individual stocks</li>
      <li><a target="_blank" href="http://finance.google.com/">Google Finance</a>, through their JSON API, is used to provide information on stock market indices (DJI, S&amp;P 500)</li>
      <li>The <a target="_blank" href="http://en.wikipedia.org/">English Wikipedia</a> is used to provide short summaries of companies on their information pages</li>
      <li>The <a target="_blank" href="http://nasdaq.com/">NASDAQ stock exchange</a> company CSV download provides an updated list of all companies on both the NASDAQ and NYSE stock exchanges</li>
    </ul>
    
    <h2>Public API and Data Feeds</h2>
    <p>trenders.org does contain a comprehensive API that can be used to control a user's portfolio or to get general financial data; in fact, we use this for our mobile applications. While there's nothing preventing you from using the API yourself, this isn't recommended, supported, or (to the extent that we can control you) permitted. We don't make any money off this site, and we're paying for the servers out of our own pockets.</p>
    <p>
	  It's nothing personal, but if we notice someone is using our API on the trenders.org domain, we'll probably take
	  steps to try to stop that. We just can't afford to pay for the extra traffic. I've tried to provide all of our data
	  sources in a convenient manner because I know from experience how hard it can be to get all this information from public
	  sources. Additionally, all of the code that powers this site is available for free on
	  <a href="https://github.com/chriskuehl/trenders.org">GitHub</a>, and you're more than welcome to adapt that for your own
	  project.
	</p>
    
    <h2>Graphics</h2>
    <p>We use a number of graphics which have been licensed under terms permitting their use on non-commercial websites. A huge thanks to these people:</p>
    <ul>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/27849/24/folder_yellow_icon">Folder Icon</a> by Visual Pharm</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/45350/24/history_icon">History Icon</a> by Iconshock</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/63127/24/lessons_icon">Lessons Icon</a> by Custom Icon Design</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/23505/24/magnifier_original_search_zoom_icon">Search Icon</a> by Oliver Scholtz</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/17224/24/chart_graph_graphic_line_icon">Chart Icon</a> by Visual Pharm</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/53973/24/blue_news_newspapers_icon">News Icon</a> by Sketchdock</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/45036/24/teacher_icon">Teacher Icon</a> by Iconshock</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/70642/24/_icon">Medal Icon</a> by Custom Icon Design</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/30010/24/log_out_system_icon">Log Out Icon</a> by Hbons</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/10249/48/accept_check_ok_tick_icon">Accept Icon</a> by Liam McKay</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/10277/48/cancel_close_icon">Cancel Icon</a> by Liam McKay</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/10279/48/ball_blue_circle_icon">Blue Circle Icon</a> by Liam McKay</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/8829/32/2%2B2%3D4_blackboard_calculate_education_math_school_icon?r=1">Blackboard Icon</a> by Oxygen Team</li>
      <li><a target="_blank" href="http://www.iconfinder.com/icondetails/44907/128/graduated_man_student_user_icon">Graduated Icon</a> by Iconshock</li>
    </ul>
  </body>
</html>