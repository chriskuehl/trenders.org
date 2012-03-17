var mouseOverAutoComplete = false;

$(function() {
        // preload images
        if ($("a.yellowButton").length > 0) {
            preloadImages(["static/images/yellowbutton_middle-hover.png"]);
        }
        
        if ($("#flag").length > 0) {
            preloadImages(["static/images/black-25.png"]);
        }
        
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
        $("body").click(function() {
            if (mouseOverAutoComplete) {
                return;
            }
            
            $("#autoComplete").fadeOut(200);
        });
        
        $(".searchBar").click(function() {
            console.log("test");
            $(this).keyup(); 
        });
        
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
					top: pos.top + height + 3,
					display: "none"
				});
				
				$("#autoComplete").fadeIn(200).hover(function() {
                                    mouseOverAutoComplete = true;
                                }, function() {
                                    mouseOverAutoComplete = false;
                                })
			} else if (! $("#autoComplete").is(":visible")) {
				$("#autoComplete").fadeIn(200).css({
					left: pos.left,
					top: pos.top + height + 3
				});
			}
			
			var ac = $("#autoComplete");
			
			
			if (results.length <= 0) {
				return ac.fadeOut(200);
			}
			
			var src = "<ul>";
			
			for (var resultIndex in results) {
				var result = results[resultIndex];
				src += "<li class=\"";
                                
                                if (resultIndex == 0) {
                                    src += "first ";
                                }
                                
                                if (resultIndex == results.length - 1) {
                                    src += "last";
                                }
                                
                                src += "\"><a href=\"stock/" + result.ticker + "\">";
                                src += "<strong>" + result.name + "</strong><br />";
                                src += "<div class=\"autoCompleteTicker\">" + result.ticker + " &ndash; " + result.sector + "</div>";
                                src += "<div class=\"autoCompletePrice\">" + result.lastSale + "</div>";
                                src += "<div class=\"clear\"></div>";
                                src +="</a></li>";
			}
			
			src += "</ul>";
			ac.html(src);
		});
	});
});

// http://stackoverflow.com/questions/476679/preloading-images-with-jquery
function preloadImages(arrayOfImages) {
    $(arrayOfImages).each(function(){
        $('<img/>')[0].src = this;
    });
}