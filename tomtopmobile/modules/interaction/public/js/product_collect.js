
var collecturl = ProductCollectRoutes.controllers.interaction.Collect.collect().url;
var membercollecturl = ProductCollectRoutes.controllers.interaction.Collect.getcollect().url;
var loginurl = ProductCollectRoutes.controllers.member.Login.login().url+ "?backUrl="+escape(location.href);

function product_collect(lid,node) {
	var jdom = $(node);
	if(jdom.hasClass("redHeart")){
		return;
	}
	var action = "add";
	$.get(collecturl, {
		"lid" : lid,
		"action" : action
	}, function(data) {
		if (data.result == "nologin") {
			location.href = loginurl;
			return;
		}
		if (data.result == "success") {
			if (action == "add") {
				jdom.addClass("redHeart");
				$("#collectwishlistcount").html(data.count);
			  
				//var text = jdom.html();
				//var num = parseInt(text.substring(1,text.length-1))+1;
				//jdom.html("("+num+")");
			} 
		} else {
			alert(data.result);
		}
	}, "json");
}
