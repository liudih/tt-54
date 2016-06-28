$(document).ready(function() {
		var status = $('#status').val();
		if(status==2||status==0){
			$("#add-to-cart-button").hide();
			$("#buynow").hide();
			$("#soldout").show();
		}else{
			$("#add-to-cart-button").show();
			$("#buynow").show();
			$("#soldout").hide();
		}
});

var cartlist = [];
$(function(){
	showCartData();
	$(".topSearch_cart").mouseenter(function(){
	    $(".topSearch_Hidebox").show().delay(1000);
	    showCartData();
	});
	$(".topSearch_cart").mouseleave(function(){
		$(".topSearch_Hidebox").hide().delay(1000);
	});
});

function showCartData(){
	$(".scrollPRO").css("height","auto");
	$("#cartsCon").html("");
	$(".noneProduct:eq(0)").hide();
	$("#loadingimg").show();
	var url = routes.controllers.cart.Cart.getCartsJson().url;
	$.ajax({
		url: url,
		type: "get",
		cache : false,
		dataType: "json",
		success:function(data){
			if(data.result=="success" && data.size>0){
				if(data.size>3){
					$(".scrollPRO").css("height","285px");
				}else{
					$(".scrollPRO").css("height","auto");
				}
				$("#cartsCon").html(data.html);
			}else{
				$(".noneProduct:eq(0)").show();
				data['size']=0;
			}
			
			var cnum = parseInt(data.size);
			var btnval = $("#cartdropbtn").val();
			var re = /[(][0-9]+[)]/; 
			btnval = btnval.replace(re,"("+cnum+")");
			$("#cartdropbtn").val(btnval);
			$("#cartdropnum").html(cnum);
			//cartlist = list;	//存全局变量
		},
		complete:function(){
			 $("#loadingimg").hide();
		}
	});
}
function deldrop(cid){
	$(".topSearch_Hidebox").show();
	var url = routes.controllers.cart.Cart.delCart().url;
	$.ajax({
		url : url,
		data : {
			lisid : cid,
			isall : ""
		},
		type : "GET",
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.result == "success") {
				$("#drop" + cid).hide();
				var len = $("#cartsCon >.topSearch_information:visible").length;
				var btnval = $("#cartdropbtn").val();
				var re = /[(][0-9]+[)]/; 
				btnval = btnval.replace(re,"("+len+")");
				$("#cartdropbtn").val(btnval);
				$("#cartdropnum").html(len);
				if(len<3){
					$(".scrollPRO").css("height","auto");
				}
			}
		},
		complete : function() {
			// $("#loadingimg").hide();
		}
	});
};
