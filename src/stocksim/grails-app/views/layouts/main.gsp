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
                <meta name="apple-mobile-web-app-capable" content="yes" />
                
                <link rel="shortcut icon" type="image/png" href="${resource(file: "favicon.png")}" />
                <link rel="icon" type="image/png" href="${resource(file: "favicon.png")}" /> <%-- just in case --%>
                <link rel="apple-touch-icon-precomposed" type="image/png" href="${resource(file: "apple-touch-icon-precomposed.png")}" />
                <link rel="apple-touch-startup-image" sizes="768x1004" type="image/png" href="${resource(file: "apple-touch-startup-image.png")}" />
                <link rel="apple-touch-startup-image" sizes="1024x748" type="image/png" href="${resource(file: "apple-touch-startup-image-landscape.png")}" />
                
                
                <g:if test="${pageProperty(name: "page.refresh")}">
                  <meta http-equiv="refresh" content="${pageProperty(name: "page.refresh")}" />
                </g:if>	
                
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
                                                                <li><a href="${createLink(mapping: "portfolio")}" id="portfolioIcon">Portfolio</a></li>
                                                                <li><a href="${createLink(mapping: "history")}" id="historyIcon">History</a></li>
                                                                <li><a href="${createLink(mapping: "lessons")}" id="lessonsIcon">Lessons</a></li>
                                                                <li><a href="${createLink(controller: "search")}/?q=" id="searchIcon">Search</a></li>
                                                                <li><a href="${createLink(mapping: "trending")}" id="trendingIcon">Trending</a></li>
                                                                <li><a href="${createLink(mapping: "news")}" id="inNewsIcon">In News</a></li>
                                                                <li><a href="${createLink(mapping: "classroom")}" id="myClassIcon">My Class</a></li>
                                                                <li><a href="${createLink(mapping: "classroom-rankings")}" id="rankingsIcon">Rankings</a></li>
                                                                <li><a href="${createLink(mapping: "logout")}" id="logOutIcon">Log Out</a></li>
                                                        </ul>

                                                        <div class="clear"></div>

                                                        <div id="userStatusBar">
                                                                <p>
                                                                        <strong>Welcome, <user:displayName /></strong><br />
                                                                        <strong>Total Assets:</strong> $<user:totalAssets />
                                                                </p>
                                                                <%-- <a id="changeLanguageBar" class="changeLanguageBarNoScript" rel="nofollow" href="/changeLanguage?lang=en">Usar español</a> --%>
                                                        </div>
                                                </div>
                                        </user:ifLoggedIn>
                                        
                                        <user:ifNotLoggedIn>
                                          <a href="${createLink(mapping: "signup")}" id="signupButton">Get Started</a>
                                        </user:ifNotLoggedIn>
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
		<g:render template="/layouts/main/footer" />
                
                <r:layoutResources />
	</body>
</html>
