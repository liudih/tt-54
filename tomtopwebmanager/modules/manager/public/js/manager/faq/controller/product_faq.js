define(['jquery','../model/ProductFaq','jqueryjson'],function($, ProductFaq){
	var productFaqObj = new ProductFaq();
	
	$("#search_post_submit").click(function(){
		$("input[name=pageNum]").val(1);
		productFaqObj.submit();
	});
	
	$("form").on("submit", function(){
		console.info('in in in');
		productFaqObj.updateAnswer(this);
		return false;
	});
	
	$("a[tag=pageSize]").click(function(){
		$("input[name=pageNum]").val(1);
		productFaqObj.changePageSize(this);
		productFaqObj.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		productFaqObj.changePageNum(this);
		productFaqObj.submit();
	});
	
	
	
	$("#savaanswer").click(function(){
		console.info('in in in search_post_submit');
		productFaqObj.updateAnswer(this);
		return false;
	});
	
	$(".number").keyup(function() {
		var value = $(this).val();
		value = value.match(/\d+/);
		if (value != null && value != 0) {
			value = parseInt(value);
		}
		$(this).val(value);
	});
	
	$("a[tag=paypal]").mouseenter(function(){
		productFaqObj.getPaypalMsg(this);
	});
});