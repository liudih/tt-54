define(['jquery','../model/FAQEvalute'], function($, FAQEvalute){
	var faqEvaluteObj = new FAQEvalute();
	$("#product_faq_evalute a").click(function(){
		faqEvaluteObj.addEvalute(this);
	});
});