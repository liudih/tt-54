$(".viewCart").click(function(){location.href='/cart'});
$(".continueShopping").click(function(){$('.addTo_cartHide').hide()});

//------------右侧按钮加入购物车------------------
$(".rightPaly_addToCart").click(function(){
	var clistingid = $("#clistingid").attr("value");
	if (clistingid == "") {
		return false;
	}
	var qty = $("#cart-button-qty").val();
	pnode = $(this).parent();
	pnode.find(".success-add").hide();
	pnode.find(".fail-add").hide();
	pnode.find(".loading").show();
	pnode.find(".success-qty").html(qty);
	
	var totalPrice = $("#total-price-1").html();
	var symbol = $('#priceSymbol').val();
	var storageid = $(".colorOverf .rightDown_SJ:eq(0)").attr("data-id");
	pnode.find(".total-price-2").html(symbol + totalPrice);
   	var list = [];
   	var map = {};
	//map['bismain'] = "false";
	map['clistingid'] = clistingid;
	map['qty'] = qty;
	storageid = storageid!=null ? storageid : 1;
	map['storageid'] = storageid;
	list[0] = map;
	addToCart(list,this,null);
});

$("#buynow").on("click", function() {
	var clistingid = $("#clistingid").attr("value");
	if (clistingid == "") {
		return false;
	}
	var qty = $("#cart-button-qty").val();
	var storageid = $(".colorOverf .rightDown_SJ:eq(0)").attr("data-id");
	storageid = storageid!=null ? storageid : 1;
	var list = [];
   	var map = {};
	map['clistingid'] = clistingid;
	map['qty'] = qty;
	map['storageid'] = storageid;
	list[0] = map;
	addToCart(list,this,function(){
		self.location = cartRoutes.controllers.cart.Cart.cartview().url;
	});
});

//------------捆绑加入购物车------------------

$("#bundel-sale-addtocart").on("click",function(e){
	initCartPanel();
   	var data = "";
   	var list = [];
   	var i = 0;
   	var mainlistingid = "";
   	var storageid = $(".colorOverf .rightDown_SJ:eq(0)").attr("data-id");
	storageid = storageid!=null ? storageid : 1;
   	$("#bundle-sale div[class^=chooseBox]").each(function() //multiple checkbox的name  
	{ 
   		if(	$(this).attr("class").indexOf("chooseSelected")>=0){
			var clistingid = $(this).attr("value");
	 		var map = {};
	 		if("true" == $("#"+clistingid).attr("tag")){
	 			//map['bismain'] = "true";
	 			mainlistingid = clistingid;
	        }
	 		map['clistingid'] = clistingid;
	 		map['qty'] = 1;
	 		map['storageid'] = storageid;
	 		list[i] = map;
	 		i++;
   		}
	 });
     var totalPrice = $("#bundel-sale-totalprice").text();
     var symbol = $('#priceSymbol').val();
	 
	 var $span = $(".add_toCart_result:eq(0)");
	 var cloneSpan = $span.clone(true);
	 $("#hiddenCartResult_2").html(cloneSpan);
	 $("#hiddenCartResult_2 .total-price-2").html(symbol + totalPrice);
   	 $("#hiddenCartResult_2 .productTotal").html(i);
	 $("#hiddenCartResult_2 .success-qty").html(i);
	 $("#hiddenCartResult_2 .loading").show();
	 $("#hiddenCartResult_2 .success-add").hide();
	 addToCart(list, this, function(){
		 $("#hiddenCartResult_2 .success-add").show();
		 $("#hiddenCartResult_2 .loading").hide();
		 $("#hiddenCartResult_2 .fail-add").hide();
	 });
});

$("#bundel-sale-buyitnow").on("click", function(e) {
	var data = "";
   	var list = [];
   	var i = 0;
   	var storageid = $(".colorOverf .rightDown_SJ:eq(0)").attr("data-id");
	storageid = storageid!=null ? storageid : 1;
   	$("#bundle-sale div[class^=chooseBox]").each(function(){ 
   		if(	$(this).attr("class").indexOf("chooseSelected")>=0){
			var clistingid = $(this).attr("value");
	 		var map = {};
//	 		if("true" == $("#"+clistingid).attr("tag")){
//	 			map['bismain'] = "true";
//	        }
	 		map['clistingid'] = clistingid;
	 		map['qty'] = 1;
	 		map['storageid'] = storageid;
	 		list[i] = map;
	 		i++;
   		}
	 });
   	addToCart(list, this, function(){
   		self.location = cartRoutes.controllers.cart.Cart.cartview().url;
	});
});

//公共加入购物车
function addToCart(list, button, backcall) {
	//common click
	$(".viewCart").click(function(){location.href='/cart'});
	$(".continueShopping").click(function(){$('.addTo_cartHide').hide()});
	$(".addTo_close").click(function(){
		$(this).parents(".addTo_cartHide").css({"display":"none"})
	});
	
	var dd = $.toJSON(list);
   	//var url = cartRoutes.controllers.cart.Cart.saveCart(data).url;
	$.ajax({
		url: "/cart/savecartitem",
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		success : function(data) {
			var pnode = $(button).parent();
			pnode.find(".loading").hide();
			var result = data.result;
			if ("success" == result) {
				if(backcall!=null){
					backcall();
					return;
				}
				pnode.find(".success-add").show();
			}else if(result=="no-enough"){
				$(".addTo_cartHide").hide();
				pophtml("Error","Out of stock!");
			}else {
				pnode.find(".fail-add").show();
			}
		}
	});
}

function initCartPanel() {
	$("#success-add").hide();
   	$("#fail-add").hide();
   	$("#loading").show();
}