<!DOCTYPE html>
<html>
	<head>
                <r:require modules="site" />

                <base href="${createLink(absolute: true, mapping: "home")}" />

		<%-- site-wide --%>
                <r:layoutResources />
                
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=1024" />
                <meta http-equiv="X-UA-Compatible" content="IE=9" />
                
                <g:if test="${pageProperty(name: "page.noRobots") == "true"}">
                  <meta name="robots" content="noindex" />
                </g:if>		
                
                <g:if test="${pageProperty(name: "page.canonical") != null}">
                  <link rel="canonical" href="${pageProperty(name: "page.canonical")}" />
                </g:if>
                
                <%-- page-specific --%>
                <title><layout:genTitle><g:layoutTitle default="trenders.org" /></layout:genTitle></title>
                <g:layoutHead />
                
                <%-- google analytics --%>
                <g:if env="production">
                  <script>
                    var _gaq = _gaq || [];
                    _gaq.push(['_setAccount', 'UA-29735034-1']);
                    _gaq.push(['_trackPageview']);

                    (function() {
                      var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
                      ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
                    })();
                  </script>
                </g:if>
	</head>
	
	<body>
		<div id="header">
			<div id="topBar">
				<div id="topBarShadow" class="pageWidth centered">
					<a href="./" id="logo">trenders.org</a>
                                        <user:ifLoggedIn>
                                                <div id="userBar">
                                                        <ul id="userIcons" class="userIconsNoScript">
                                                                <li><a href="#" id="portfolioIcon">Portfolio</a></li>
                                                                <li><a href="#" id="historyIcon">History</a></li>
                                                                <li><a href="#" id="lessonsIcon">Lessons</a></li>
                                                                <li><a href="#" id="searchIcon">Search</a></li>
                                                                <li><a href="#" id="trendingIcon">Trending</a></li>
                                                                <li><a href="#" id="inNewsIcon">In News</a></li>
                                                                <li><a href="#" id="myClassIcon">My Class</a></li>
                                                                <li><a href="#" id="rankingsIcon">Rankings</a></li>
                                                                <li><a href="#" id="logOutIcon">Log Out</a></li>
                                                        </ul>

                                                        <div class="clear"></div>

                                                        <div id="userStatusBar">
                                                                <p>
                                                                        <strong>Welcome, John Doe</strong><br />
                                                                        <strong>Current Balance:</strong> $143,293
                                                                </p>
                                                                <a id="changeLanguageBar" class="changeLanguageBarNoScript" rel="nofollow" href="/changeLanguage?lang=en">Usar español</a>
                                                        </div>
                                                </div>
                                        </user:ifLoggedIn>
				</div>
			</div>
			
			<div id="blueBar"></div>
			
			<div id="tabBar">
				<div class="pageWidth centered">
                                        <finance:index index="DJI" title="Dow" />
                                        <finance:index index="INX" title="S&amp;P 500" />
				
					<ul id="navBar">
						<li class="blueNavBar" id="searchTab">
							<form method="GET" action="${createLink(controller: "search", action: "browse")}" autocomplete="off">
								<input type="text" class="searchBar" name="q" value="" />
							</form>
						</li>
						<li><a href="#">Join</a></li>
						<li><a href="#">Getting Started</a></li>
						<li><a href="#">About</a></li>
					</ul>
				</div>
			</div>
		</div>
		
		<div id="container">
			<div id="content" class="centered ${pageProperty(name: "page.hasColumns") == "true" ? "contentWithColumns" : ""}">
                                <%-- noscript warning --%>
				<noscript>
                                        <user:displayAlert type="error" title="Sorry to bug you," message="but your browser currently has JavaScript disabled. Please enable it and refresh the page for a better experience." />
				</noscript>
                                
                                <%-- alert boxes from other pages --%>
                                <g:each var="alert" in="${flash.alerts}">
                                        <g:render template="/userAlert/userAlert" model="${[alert: alert]}" />
                                </g:each>
                                  
                                <g:layoutBody />
                                
                                <g:if test="${pageProperty(name: "page.hasColumns") == "true"}">
                                  <div class="clear"></div>
                                </g:if>
                        </div>
                </div>
		
		<!-- footer -->
		<div id="footer">
			<div class="pageWidth centered" id="footerInner">
				<p>
					Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent et augue a magna elementum commodo.<br />
					Phasellus a est sed tellus luctus imperdiet. <a href="#">Vivamus erat nibh</a>, tincidunt at auctor sit amet, tristique non tortor.<br />
					Donec hendrerit vulputate tortor sit amet rhoncus.
				</p>
				
				<!-- footer links -->
				<ul>
					<li><strong>Donec hendrerit</strong></li>
					<li><a href="#">Lorem ipsum</a></li>
					<li><a href="#">Dolor sit</a></li>
					<li><a href="#">Amet consectetur</a></li>
				</ul>
				
				<ul>
					<li><strong>Vulputate tortor</strong></li>
					<li><a href="#">Luctus imperdiet</a></li>
					<li><a href="#">Tristique non</a></li>
					<li><a href="#">Tortor donec hendr</a></li>
				</ul>
				
				<ul>
					<li><strong>Consectetur adipiscing</strong></li>
					<li><a href="#">Lorem ipsum</a></li>
					<li><a href="#">Dolor sit</a></li>
					<li><a href="#">Amet consectetur</a></li>
				</ul>
				
				<div class="clear"></div>
			</div>
		</div>
                
                <r:layoutResources />
	</body>
</html>
