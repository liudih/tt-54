define([ 'jquery', '../model/TopicPage' ], function($, TopicPage) {
	var topicPageObj = new TopicPage();
	$("div.attML_BOX.newArivals_sp span").click(function() {
		topicPageObj.ajaxRefresh(this);
	});
});