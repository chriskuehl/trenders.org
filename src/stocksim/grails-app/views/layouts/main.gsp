<!doctype html>
<html>
	<head>
                <r:require modules="site" />

                <base href="<g:createLink />" />

		<!-- site-wide -->
                <r:layoutResources />
                
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="width=1024" />
		
		<!-- page-specific -->
                
                <title><layout:genTitle><g:layoutTitle default="trenders.org" /></layout:genTitle></title>
                <g:layoutHead />
	</head>
	
	<body>
		<div id="header">
			<div id="topBar">
				<div id="topBarShadow" class="pageWidth centered">
					<a href="/" id="logo">trenders.org</a>
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
                                                                <a id="changeLanguageBar" class="changeLanguageBarNoScript" href="/changeLanguage?lang=en">Usar español</a>
                                                        </div>
                                                </div>
                                        </user:ifLoggedIn>
				</div>
			</div>
			
			<div id="blueBar"></div>
			
			<div id="tabBar">
				<div class="pageWidth centered">
                                        <%-- 
					<div class="marketIndex marketDown">
						<strong>Dow</strong> 12,965.69 (<span class="marketDown">&ndash;0.2%</span>)
					</div>
					
					<div class="marketIndex marketUp">
						<strong>S&amp;P 500</strong> 1,362.21 (<span class="marketUp">+1.3%</span>)
					</div> --%>
                                        <finance:index index="DJI" title="Dow" />
                                        <finance:index index="INX" title="S&amp;P 500" />
				
					<ul id="navBar">
						<li class="blueNavBar" id="searchTab">
							<form method="GET" action="/search" autocomplete="off">
								<input type="text" class="searchBar" name="query" value="" />
							</form>
						</li>
						<li><a href="#">Sit Amet</a></li>
						<li><a href="#">Ipsum Dolor</a></li>
						<li><a href="#">Lorem</a></li>
					</ul>
				</div>
			</div>
		</div>
		
		<div id="container">
			<div id="content" class="centered">
                                <g:layoutBody/>
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
