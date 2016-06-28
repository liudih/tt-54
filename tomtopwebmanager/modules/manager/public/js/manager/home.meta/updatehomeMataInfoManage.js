$(document).on("click","#updateSeoId",function(){
	$('.update-seo').unbind();
	$('.update-seo').submit(function(){
		var form = $(this);
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if(data['dataMessages']==5){
				alert("Change Success！");
				 var url = seoList.controllers.manager.Seo.getSeoList().url;
				 window.location = url;
			}else if(data['dataMessages']==6){
				alert("Change failed！");
				return false;
			}else {
				return false;
			}
		});
	}); 

});
