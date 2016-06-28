// 更改产品页面buynow跳转链接
$(function(){
	$("li:first-child").addClass("active");
})
$(document).on("click","#clearfixId li a",function(){
	var href = $(this).attr("value"); 
	$("#buynow").attr("href",href);
//	if ($(this).data('country')=="United States") {
//		$('#add-to-cart').show();
//		$('#buynow').hide();
//	} else {
		$('#buynow').show();
		$('#add-to-cart').hide();
//	}
})



			
