Listeners.addListener('addresschang', function(addressId) {
	refreshAllShippingMethod(addressId);
});

$(function() {
	var addressid = $('input[name=hidden_radio_value]').val();
	Listeners.fireListenner('addresschang', addressid);
	setShippingMethodEvent();
	setShippingMethodChangeEvent();
	setSubmitEvent();
	$(".cancel").click(function() {
		$('.pu_pop').hide();
	});
});

function refreshAllShippingMethod(addressId) {
	$("table[table-type='order-product']").each(function() {
		var orderId = $(this).attr("table-data");
		refreshShippingMethod(addressId, orderId);
	});
	calculateTotal();
	validForm();
}

function refreshShippingMethod(addressId, orderId) {
	var url = orderRoutes.controllers.order.ShippingAddress
			.refreshPreparaoryShipping(orderId, addressId).url;
	if (addressId != undefined && addressId != null && addressId != "") {
		$.get(url, function(data) {
			$("table[table-data='" + orderId + "']").find(
					"ul.selectShippingMethod_ul").replaceWith(data);
		}, "html");
	}
}

function setShippingMethodEvent() {
	$("div").delegate(".selectShippingMethod_click", "click", function(e) {
		$(this).siblings(".selectShippingMethod_none").toggle();
		if ($(this).parent().hasClass("selectShippingMethod_none")) {
			$(this).parent().toggle();
		}
		e.stopPropagation();
	});
}

function setShippingMethodChangeEvent() {
	$("div").delegate(".selectShippingMethod_con", "click", function() {
		var box = $(this).parents(".selectShippingMethod_box");
		var old_value = box.children(".selectShippingMethod_con");
		var none_box = box.children(".selectShippingMethod_none");
		none_box.append(old_value);
		box.prepend(this);
		calculateTotal();
		validForm();
	});
}

function calculateTotal() {
	var superTotal = 0.0;
	var shippingTotal = 0.0;
	var isJPY;
	$("table[table-type='order-product']")
			.each(
					function() {
						var currency = $(this).attr('currency') || '';
						isJPY = ('JPY' == currency);
						var shippingPriceDIV = $(this)
								.find(
										"li.selectShippingMethod_box > div.selectShippingMethod_click");
						var shippingPrice = parseFloat(shippingPriceDIV.find(
								"strong[strong-type='shipping-price']").text());
						$(this).find("span[span-type='order-shipping-price']")
								.text((isJPY ? shippingPrice : shippingPrice.toFixed(2)));
						var subtotal = parseFloat($(this).find(
								"span[span-type='order-subTotal']").text());
						var total = shippingPrice + subtotal;
						$(this).find("strong[strong-type='order-total']").text(
								(isJPY ? total : total.toFixed(2)));
						shippingTotal += shippingPrice;
						superTotal += parseFloat((isJPY ? total : total.toFixed(2)));
					});
	$("#total_fragment_shipping").text((isJPY ? shippingTotal : shippingTotal.toFixed(2)));
	var discount = parseFloat($("#order-total-discount").text());
	if (isNaN(discount)) {
		discount = 0;
	}
	$("#grand_total").text((isJPY ? (superTotal + discount) : (superTotal + discount).toFixed(2)));
}

function validForm() {
	if (checkAddress()) {
		checkShippingMethod();
	}
}

function checkAddress() {
	var billId = $("input[name=order_address_radio]:checked").val();
	var addressId = $("input[name='address_radio']:checked").val();
	if (billId == undefined || billId == null || billId == "") {
		$("#addressErr").show();
		$("table.shippingAs").addClass("ErrBorder");
		$(".ContinueSpay").addClass("ContinueNo");
		return false;
	} else {
		$("#addressErr").hide();
		$("table.shippingAs").removeClass("ErrBorder");
		$(".ContinueSpay").removeClass("ContinueNo");
		return true;
	}
}

function checkShippingMethod() {
	var shippingMethodId = $("input[name='shippingMethodId']").val();
	var b = true;
	$("table[table-type='order-product']").each(function() {
		var shippingPriceDIV = $(this).find("li.selectShippingMethod_box ");
		if (shippingPriceDIV == undefined || shippingPriceDIV == null) {
			b = false;
		}
	});
	if (!b) {
		$("#shippingMethodErr").show();
		$(".ContinueSpay").addClass("ContinueNo");
		return false;
	} else if (b) {
		$("#shippingMethodErr").hide();
		$(".ContinueSpay").removeClass("ContinueNo");
		return true;
	}
}

function setSubmitEvent() {
	$("#place_order")
			.submit(
					function() {
						if ($(".ContinueSpay").hasClass("ContinueNo")) {
							return false;
						}
						var addressId = $("input[name='address_radio']:checked").val();
						var billAddressId = $(
								"input[name='order_address_radio']:checked").val();
						var url = orderRoutes.controllers.order.OrderProcessing
								.checkAddress(parseInt(addressId),
										parseInt(billAddressId)).url;
						var b = true;
						$.ajax({
							url : url,
							type : "GET",
							async : false,
							success : function(res) {
								b = res.res;
								if (!b) {
									$(".pu_pop").find("p.pu_popTxt").text(
											res.msg);
									$(".pu_pop").show();
								} else {
									$(".pu_pop").hide();
								}
							},
							dataType : "json"
						});
						var data = {};
						var shippingIDs = new Array();
						$("table[table-type='order-product']")
								.each(
										function() {
											var map = {};
											var orderID = $(this).attr(
													"table-data");
											var shippingPriceDIV = $(this)
													.find(
															"li.selectShippingMethod_box > div.selectShippingMethod_click");
											var shippingID = shippingPriceDIV
													.attr("div-data");
											map["orderID"] = orderID;
											map["shippingID"] = shippingID;
											shippingIDs.push(map);
										});
						var msg = $("textarea[name='message']").val();
						var cartID = $("div[div-type='cart-info']").attr(
								"div-data");
						data["addressID"] = addressId;
						data["shippingIDs"] = shippingIDs;
						data["billAddressID"] = billAddressId;
						data["message"] = msg;
						data["cartID"] = cartID;
						var dataString = JSON.stringify(data);
						$("#form-data").val(dataString);
						return b;
					});
}