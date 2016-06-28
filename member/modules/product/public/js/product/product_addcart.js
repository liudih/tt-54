$(".viewCart").click(function(){location.href="http://cart.tomtop.com"});
$(".continueShopping").click(function(){$(this).closest(".addTo_cartHide").hide()});
$(".rightPaly_addToCart").click(function(){
	var savecarturl = '/cart/savecartitem';
	var n = $(this);
	var pnode = $(this).closest(".product_item");
	var pricehtml = pnode.find(".pricehtml:eq(0)").val();
	n.parent().find(".total-price-2:eq(0)").html(pricehtml);
	var list = [];
   	var map = {};
	map['clistingid'] = pnode.find(".plistingid:eq(0)").val();
	map['qty'] = 1;
	map['storageid'] = 1;
	list[0] = map;
	var dd = $.toJSON(list);
	n.parent().find(".success-add").hide();
	n.parent().find(".fail-add").hide();
	n.parent().find(".loading").show();
	
//	$.ajax({
//		url: savecarturl,
//		type: "POST",
//		dataType: "json",
//		contentType: "application/json",
//		data: dd,
//		success : function(data) {
//			var result = data.result;
//			if ("success" == result) {
//				n.parent().find(".success-add").show();
//			} else if(result=="no-enough"){
//				pophtml("Error","Out of stock!");
//			}else{
//				n.parent().find(".fail-add").show();
//			}
//		},
//		complete:function(){
//			n.parent().find(".loading").hide();
//		}
//	});
	$.ajax({
		type: "GET",
		url: "http://cart.tomtop.com/cart-api/savecartitem/jsonp?_="+Math.random(),
		data:{
			data: encodeURIComponent(dd)
		},
		contentType: 'application/json',
		dataType:'jsonp',
		jsonp: "jsoncallback",
        jsonpCallback:"success_jsonpCallback",
		success : function(data) {
			if (data.ret==1) {
				n.parent().find(".success-add").show();
			}else{
				n.parent().find(".fail-add").show();
			}
		},
		complete:function(){
			n.parent().find(".loading").hide();
		}
	 });
});