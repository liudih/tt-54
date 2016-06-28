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
	var url = "/cart/cartdropdata";
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
function deldrop(cid,sid){
	$(".topSearch_Hidebox").show();
	var url = "/cart/delcartitem";
	var list = [];
	var cids = cid.split(",");
	for(var i=0;i<cids.length;i++){
		if(cids[i]!="" && cids[i].length>1){
			var map = {};
			map['clistingid'] = cids[i];
			var ssid = isNaN(sid) ? 1 : sid;
			map['storageid'] = ssid;
			list[list.length] = map;
		}
		var dd = $.toJSON(list);
	}
	
	$.ajax({
		url : url,
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		async : true,
		success : function(data) {
			if (data.result == "success") {
				$("#drop-" + cid).hide();
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
