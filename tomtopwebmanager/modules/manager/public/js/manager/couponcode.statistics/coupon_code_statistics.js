$(document).ready(function(){
	$("#search_id").click(function(){
		var couponCode_value=$("#couponCode_Id").val();
		if(couponCode_value==""){
			alert("Please enter Coupon Code !");
			return false;
		}
		return true;
	});
	
	$("#download_couponCode").click(function(){
		var form = $("#searchCouponCode");
		var icouponruleid = form.find("select[name='icouponruleid']").val();
		var ccode= form.find("input[name='ccode']").val();
		var url = couponCodeInit.controllers.manager.CouponCode.downloadCouponCodes(icouponruleid,ccode).url;
		document.getElementById("ifile").src=url;
	});
});