/*
	page setting;
 */
define(['jquery','../model/Detail','../model/BundleMutil','../model/ProductAttribute','jqueryjson'],function($,Detail,BundleMutil,ProductAttribute){
	/*
	 * bundel sale
	 */
	var id = "bundle-sale";
	var detailObj = new Detail();
	var bundleMutilObj = new BundleMutil();
	var productAttribute = new ProductAttribute();
	$("#" + id + " div[class^=chooseBox]").each(function() {
		var lid = $(this).attr("id");
		$("#" + lid).on("click", function(e) {
			var clistingid = $(this).attr("value");
			if(	$(this).attr("class").indexOf("chooseSelected")>=0 && !$(this).hasClass("chooseDefault")){
				var mainclistingid = $("#main_clistingid_bundle").attr("value");
				bundleMutilObj.selectModel(clistingid, lid, mainclistingid);
			} else {
				$("#multiatri_" + lid).css('display','none');  
			}
			
			if ($(this).attr("tag") == "false") {
				detailObj.bundelSaleCalculatePrice();
			} else {
				e.preventDefault();
			}
		});
	});

	/*
	 * discount price timer1
	 */
	var seconds = parseInt($("#sale-price").attr("tag"));
	if (seconds > 0) {
		detailObj.timer(seconds, $("#sale-price"));
	} else {
		$("#sale-price").parent().parent().hide();
	}
	
	function getTotal(price,qty){
		var currency = $("#total-price-1").attr('currency') || '';
		var isJPY = 'JPY' == currency;
		var totalPrice = (isJPY ? parseInt(price * qty) : parseFloat(price * qty).toFixed(2));
		$("#total-price-1").html(totalPrice);
		var symbol = $('#priceSymbol').val();
		$("#total-price-2").html(symbol + totalPrice);
	}

	$("#cart-button-qty-sub").on("click", function(e) {
		var qty = parseInt($("#cart-button-qty").val());
		var totalPrice = parseFloat($("#total-price").val());
		qty -= 1;
		if (qty < 1) {
			return;
		}
		getTotal(totalPrice,qty);
		$("#cart-button-qty").val(qty);
	});

	$("#cart-button-qty-add").on("click", function(e) {
		var qty = 1;
		var showqty = $("#cart-button-qty").val();
		if(showqty!="" && !isNaN(showqty)){
			qty = parseInt(showqty);
		}
		qty += 1;
		if(qty>999){
			return;
		}
		var totalPrice = parseFloat($("#total-price").val());
		getTotal(totalPrice,qty);
		$("#cart-button-qty").val(qty);
	});
	
	$("#cart-button-qty").keyup(function() {
		var qty = $(this).val();
		if (qty=="" || isNaN(qty)) {
			qty = 1;
			$(this).val(qty);
		}
		qty = parseInt(qty);
		if(qty > 999) {
			qty = 999;
			$(this).val(qty);
		}
		if(qty < 1) {
			qty = 1;
			$(this).val(qty);
		}
		var totalPrice = parseFloat($("#total-price").val());
		getTotal(totalPrice,qty);
	});

	/*
	 * player
	 */
	$("#membervideoslist div[type=button]").each(function() {
		var lid = $(this).attr("id");
		$("#" + lid).on("click", function(e) {
			var vurl = $(this).children("iframe").attr("src");
			$("#playwindow").attr("src", vurl);
		});
	});

	$("#memberphotolist div[type=button]").each(function() {
		var pid = $(this).attr("id");
		$("#" + pid).on("click", function(e) {
			var purl = $(this).children("img").attr("src");
			$("#photos").attr("src", purl);
		});
	});

	$("#publicvideoslist div[type=button]").each(function() {
		var vid = $(this).attr("id");
		$("#" + vid).on("click", function(e) {
			var pvurl = $(this).children("iframe").attr("src");
			$("#publicVideo").attr("src", pvurl);
		});
	});
	
//	$(document).ready(function(){
//		var listingid = $('#attributeShow').attr('value');
//		productAttribute.selectModel(listingid);
//	});
	
	$(".showAttributePic").hover(
			  function () {
				    var src = $(this).data('bigimageurl');
				  	$("#attributePic").prev().hide();
				    $("#attributePic").attr('src', src);
				    $("#attributePic").addClass('img-responsive');
				    $("#attributePic").show();
			  },
			  function () {
				  $("#attributePic").hide();
				  $("#attributePic").prev().show();
			  }
	);
	
	
	function showVideoDlg(){
		var $dlg= $("#videodlg");
		$dlg.show();
		mH =$dlg.find(".scrollWZ").height();
		mY =$dlg.find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			$dlg.find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			$dlg.find(".scrollBox").css({"overflow-y":"auto"})
		}
	};
});


