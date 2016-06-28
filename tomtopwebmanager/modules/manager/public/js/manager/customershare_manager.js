$(function(){
	$("a[tag=pageNum]").click(function(){
		var value=$(this).attr("value");
		$("#search_form > input[name=page]").val(value);
		$("#search_form").submit();
	});
	//给相应的标点赋值
	$("#email").val(GetQueryString("email"));
	$("#url").val(GetQueryString("url"));
	$("#type").val(GetQueryString("type"));
	$("#country").val(GetQueryString("country"));
	
	$("#downloadCustomerShare").click(function(){
			var email=$("#email").val();
			var url=$("#url").val();
			var type=$("#type").val();
			var country=$("#country").val();
			var url = js_CustomerShare.controllers.manager.CustomerShareManagerController.exportCustomerShare(email,url,type,country).url;
			//var url = couponCode.controllers.manager.CouponCode.downloadCouponCodes(email,url,type,country).url;
			document.getElementById("ifile").src=url;
	});

});
function GetQueryString(name)
{
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
}