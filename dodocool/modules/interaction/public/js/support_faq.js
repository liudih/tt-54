$(document).on("click","#search_faq",function(){
	var key = $('#key').val();
	if (key == '') {
		return false;
	}
	var url = faqRoutes.controllers.interaction.Faq.search(key).url;
	
	$.get(url, function(html) {
		$("#nextCategorySelect").replaceWith(html);
	}, "html");
	
});