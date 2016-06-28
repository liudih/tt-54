define(['jquery','product/model/BundleMutil','jqueryjson'],function($,BundleMutil){
	
	var bundleMutilObj = new BundleMutil();
	$(".attributesSelection").on("click",function(e){
		$(".chooseProduct_color").hide();
		var listingid =  $(this).data('id');
		var lid = $(this).attr('id');
		var mainlistingid = $(this).data('mainid');
		console.info(mainlistingid);
		bundleMutilObj.getMutilModel(listingid, lid, mainlistingid);
	});
	
	$(".bundel-sale-addtocart").on("click",function(e){
		if ($(this).hasClass('grayInt')) {
			return;
		}
		var listingid = $(this).data('listingid');
		$("#success-add"+listingid).hide();
	   	$("#fail-add"+listingid).hide();
	   	$("#loading"+listingid).show();
		var listingid = $(this).data('listingid');
		var data = "";
	   	var list = [];
	   	var i = 0;
	   	var mainlistingid = "";
	   	var clas = ".addtocart" + listingid;
	   	
	   	$(clas).each(function() //multiple checkbox的name  
	   			{ 
	   					var clistingid = $(this).attr("value");
	   			 		var map = {};
	   			 		if(true == $(this).data("tag")){
	   			 			map['bismain'] = "true";
	   			 			mainlistingid = clistingid;
	   			        }
	   			 		map['clistingid'] = clistingid;
	   			 		map['qty'] = 1;
	   			 		map['storageid'] = 1;
	   			 		list[i] = map;
	   			 		i++;
	   		   		
	   			 });
	   	var totalPrice = $("#bundel-sale-totalprice"+listingid).text();
	    //var symbol = $('#priceSymbol').val();
		$("#total-price-2"+listingid).html(totalPrice);
	   	$("#productTotal"+listingid).html(i);
		$("#success-qty"+listingid).html(i);
	   	
		submitCart(list, function(){
			$("#loading"+listingid).hide();
			$("#success-add"+listingid).show();
		});
	});
	
	$(".bundel-sale-buyitnow").on("click", function(e) {
		if ($(this).hasClass('grayInt')) {
			return;
		}
		var listingid = $(this).data('listingid');
		var data = "";
	   	var list = [];
	   	var i = 0;
	   	var mainlistingid = "";
	   	var clas = ".addtocart" + listingid;
	   	
		$(clas).each(function(){ 
			var clistingid = $(this).attr("value");
		 		var map = {};
		 		if(true == $(this).data("tag")){
		 			map['bismain'] = "true";
		 			mainlistingid = clistingid;
		        }
		 		map['clistingid'] = clistingid;
		 		map['qty'] = 1;
		 		map['storageid'] = 1;
		 		list[i] = map;
		 		i++;
		 });
		submitCart(list, function(){
			self.location = cartRoutes.controllers.cart.Cart.cartview().url;
		});
	});
	
	function submitCart(list, backcall){
		//common click
		$(".viewCart").click(function(){location.href='/cart'});
		$(".continueShopping").click(function(){$('.addTo_cartHide').hide()});
		$(".addTo_close").click(function(){
			$(this).parents(".addTo_cartHide").css({"display":"none"})
		});
		var dd = $.toJSON(list);
	   	 $.ajax({
	   		url: "/cart/savecartitem",
			type: "POST",
			dataType: "json",
			contentType: "application/json",
			data: dd,
			success : function(data) {
				var result = data.result;
				if ("success" == result) {
					if(backcall!=null){
						backcall();
						return;
					}
				}else if(result=="no-enough"){
					$(".addTo_cartHide").hide();
					alert("Out of stock!");
				}else {
					$("#loading"+listingid).hide();
					$("#fail-add"+listingid).show();
				} 
			}
		});
	}
	
});