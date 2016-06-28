/*
	product deatil page function
*/
define(['jquery'],function ($) {

    function ModelDetail() {
    }
   
    ModelDetail.prototype = {
  
        bundelSaleCalculatePrice : function () {
			var totalprice = 0;
			var items = 0;
		    $("#bundle-sale div[class^=chooseBox]").each(function() {
				var lid = $(this).attr("id");
				if($(this).hasClass("chooseSelected")){
					var price =  parseFloat($("#oriprice"+lid).attr('oriprice'));
					totalprice = totalprice + price;
					items ++ ;
				}
			});
		    
		    var currency = $("#total-price-1").attr('currency') || '';
			var isJPY = 'JPY' == currency;
			totalprice = (isJPY ? Math.round(parseFloat(totalprice)) : parseFloat(totalprice).toFixed(2));
			
		    $("#bundel-sale-totalprice").text(totalprice);
		    $("#bundel-sale-selected").text(items);
        },
		
		addToCart : function(list, button, backcall) {
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
						$(".viewCart").click(function(){location.href='/cart'});
						$(".continueShopping").click(function(){$('.addTo_cartHide').hide()});
						$(".addTo_close").click(function(){
							$(this).parents(".addTo_cartHide").css({"display":"none"})
						});
					}else if(result=="no-enough"){
						$(".addTo_cartHide").hide();
						pophtml("Error","Out of stock!");
					}else {
						pnode.find(".fail-add").show();
					}
				}
			});
		}
    };

    return ModelDetail;
});
