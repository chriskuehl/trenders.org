$(function() {
	// hovering over user icons
	$(".userIconsNoScript").removeClass("userIconsNoScript");
	$("#userIcons li a").hover(function() {
		$(this).stop(true).animate({
			opacity: 0.7
		}, 100);
	}, function() {
		$(this).stop(true).animate({
			opacity: 1
		}, 100);
	});
	
	// hovering over switch language
	$(".changeLanguageBarNoScript").removeClass("changeLanguageBarNoScript");
	$("#changeLanguageBar").hover(function() {
		$(this).stop(true).animate({
			color: "#D9E5E9"
		}, 100);
	}, function() {
		$(this).stop(true).animate({
			color: "#9AACB2"
		}, 100);
	});
	
	// search bar helper text
	$(".searchBar").inputLabel("Search for stocks...", {color: "#777777"});
	
	// autocomplete for the search bar
	$(".searchBar").keyup(function() {
		var query = $.trim($(this).val());
		
		if (query.length <= 0) {
			return $("#autoComplete").fadeOut(200);
		}
		
		var pos = $(this).offset();
		var height = $(this).outerHeight()
		
		// get results
		$.get("search/json", {query: query}, function(results) {
			if ($("#autoComplete").length == 0) {
				$("<div />").attr("id", "autoComplete").appendTo($("body")).css({
					position: "absolute",
					zIndex: "100",
					left: pos.left,
					top: pos.top + height,
					border: "solid 2px #CCC",
					backgroundColor: "rgba(255, 255, 255, 0.8)",
					padding: "5px",
					display: "none"
				});
				
				$("#autoComplete").fadeIn(200);
			} else if (! $("#autoComplete").is(":visible")) {
				$("#autoComplete").fadeIn(200),css({
					left: pos.left,
					top: pos.top + height
				});
			}
			
			var ac = $("#autoComplete");
			
			
			if (results.length <= 0) {
				return ac.fadeOut(200);
			}
			
			var src = "<ul>";
			
			for (var resultIndex in results) {
				var result = results[resultIndex];
				src += "<li>" + result.name + " (" + result.ticker + ")</li>";
			}
			
			src += "</ul>";
			ac.html(src);
		});
	});
});