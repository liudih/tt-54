$(function() {
	init();
});

$("#place_order").submit(function() {
	beforeSubmit();
	if ($(".ContinueSpay").hasClass("ContinueNo")) {
		return false;
	}
	return true;
});

function init() {
	updateShippingInfo();
	changeShippingInfo();
	getTotal();
}

function changeShippingInfo() {
	$("input[name=shippingMethodIdValue]").change(function() {
		updateShippingInfo();
		getTotal();
	});
	getTotal();
}

function updateShippingInfo() {
	var e = $("input[name=shippingMethodIdValue]:checked");
	var id = e.val();
	$("input[name=shippingMethodId]").val(id);
	var price = e.attr("price") || "0.00";
	var currency = $("#total_fragment_shipping").attr('currency') || '';
	var isJPY = 'JPY' == currency;
	price = (isJPY ? parseInt(price) : price);
	$("#total_fragment_shipping").html(price);
	var img = $("#img_name_" + id + " img").clone();
	var span = e.next("span").text();
	$("#total_fragment_shipping_context").text(span);
	$("#total_fragment_shipping_context").prepend(img);
}

function getTotal() {
	var total = 0;
	$("[name='total_fragment']").each(function() {
		var text = $(this).text().replace(',', '');
		var price = parseFloat(text);
		if (!isNaN(price)) {
			total += price;
		}
	});
	if (total < 0) {
		total = 0;
	}
	var currency = $("#grand_total").attr('currency') || '';
	var isJPY = 'JPY' == currency;
	$("#grand_total").text(isJPY?total:total.toFixed(2));
	beforeSubmit();
}

function beforeSubmit() {
	var isOrder = $("input[name=isOrder]").val();
	if (isOrder == "1") {
		var isSelect = $("input[name=isSelect]").val();
		if (isSelect == "false") {
			return;
		}
		checkShippingMethod();
	}
}

function checkShippingMethod() {
	var shippingMethodId = $("input[name='shippingMethodId']").val();
	var addressId = $("#place_order > input[name='addressId']").val();
	var b = true;
	if (shippingMethodId == undefined || shippingMethodId == null
			|| shippingMethodId == "") {
		$("#shippingMethodErr").show();
		b = false;
	} else {
		$("#shippingMethodErr").hide();
	}
	if (!b && !$(".ContinueSpay").hasClass("ContinueNo")) {
		$(".ContinueSpay").addClass("ContinueNo");
	} else if (b && $(".ContinueSpay").hasClass("ContinueNo")) {
		$(".ContinueSpay").removeClass("ContinueNo");
	}
}
