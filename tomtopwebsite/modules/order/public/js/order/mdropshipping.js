require([ './common' ], function(common) {
	$(function() {
		digitalInput();
		var type = $("input[name=html_type]").val();
		if (type == "dropshipping_confirm") {
			getTotal();
		}
	});

	$("input[name=search]").keyup(function() {
		var v = $(this).val().toLowerCase();
		var regstr = v + ".*";
		var reg = new RegExp(regstr, "i");
		$("#countrylist_ul li").each(function(i) {
			$(this).hide();
		});
		$("#countrylist_ul li").each(function(i) {
			if ($.trim($(this).html()) != "") {
				if (reg.test($(this).children("span").html().toLowerCase())) {
					$(this).show();
				} else {
					$(this).hide();
				}
			}
		});
	});

	$("li[name=country_li]").click(function() {
		var cc = $(this).attr("countryCode");
		$("input[name=countrysn]").val(cc);
		var clazz = $(this).attr("class");
		var text = $(this).children("span[name=country]").text();
		var h3 = $(this).parents("div.select_country").children("h3");
		h3.removeClass();
		h3.addClass(clazz);
		h3.children("span").text(text);
	});

	$("li.placeAdd").click(function() {
		var ul = $("#new_product_input").children("ul");
		ul.clone().insertBefore("#new_product_button");
		digitalInput();
	});

	$("li.placeReduction").click(function() {
		var ul = $("#new_product_button").prev("ul").remove();
	});

	function initSkuQty() {
		var productULs = $("#simple_dropshipping_form").children(
				"ul[name=product_ul]");
		var sku = "";
		var qty = "";
		for (var i = 0; i < productULs.length; i++) {
			var ul = productULs[i];
			var skuValue = $(ul).find("input[name=sku_input]").val();
			var qtyValue = $(ul).find("input[name=qty_input]").val();
			sku += skuValue;
			qty += qtyValue;
			if (i < productULs.length - 1) {
				sku += ",";
				qty += ",";
			}
		}
		$("#simple_dropshipping_form").children("input[name=sku]").val(sku);
		$("#simple_dropshipping_form").children("input[name=qty]").val(qty);
	}

	$("#simple_dropshipping_form").submit(function() {
		var b = true;
		initSkuQty();
		$("#address_table").find("input[tag=address_field]").each(function() {
			var value = $(this).val();
			if (value == null || value.trim() == "") {
				$(this).parent("td").find("p.edit_error").show();
				b = false;
			} else {
				$(this).parent("td").find("p.edit_error").hide();
			}
		});
		if(!b){
			return b;
		}
		//判断库存和停售状态
		var sku = $("#csku_1").val();
		var qty = $("#qty_1").val();
		$.ajax({
			url: "/checkout/checkproductstatus",
			type: "get",
			dataType: "json",
			async: false,
			data:{
				"sku" : sku,
				"qty" : qty
			},
			success:function(data){
				if(data.result=="success"){
					b = true;
				}else{
					pophtml("Error",data.result);
					b = false;
				}
			}
		});
		return b;
	});

	function digitalInput() {
		$("input.digital").keyup(function() {
			var value = $(this).val();
			value = value.match(/\d+/);
			if (value != null) {
				value = parseInt(value);
			}
			$(this).val(value);
		});
	}

	$("#place_dropShipping_form").submit(function() {
		var table = $("#dropShippingOrder_table");
		var idSpans = table.find("span.afters[name=orderID_span]");
		var ids = "";
		var shippingMethodIDs = "";
		if (idSpans.length == 0) {
			pophtml("Error","Please select order!");
			return false;
		}
		for (var i = 0; i < idSpans.length; i++) {
			var e = $(idSpans[i]);
			var id = e.attr("value");
			var shippingID = $("#" + id + "_shippingMethod").val();
			ids += id;
			shippingMethodIDs += shippingID;
			if (i < idSpans.length - 1) {
				ids += ",";
				shippingMethodIDs += ",";
			}
		}
		$("input[name=ids]").val(ids);
		$("input[name=shippingMethodIDs]").val(shippingMethodIDs);
		return true;
	});

	$("select[name=shippingMethodID]").change(function() {
		getTotal();
	});

	function getTotal() {
		var table = $("#dropShippingOrder_table");
		var idSpans = table.find("span.afters[name=orderID_span]");
		idSpans.each(function() {
			var id = $(this).attr("value");
			var select_op = $("#" + id + "_shippingMethod option:selected");
			var s_price = parseFloat(select_op.attr("price"));
			var total_e = $("#" + id + "_rowTotal");
			var subTotal = parseFloat(total_e.attr("value"));
			if (!isNaN(s_price) && !isNaN(subTotal)) {
				var total = s_price + subTotal;
				total_e.text(total.toFixed(2));
			}
		});
		var total = 0.0;
		for (var i = 0; i < idSpans.length; i++) {
			var e = $(idSpans[i]);
			var id = e.attr("value");
			var total_e = $("#" + id + "_rowTotal");
			var rowTotal = parseFloat(total_e.text());
			if (!isNaN(rowTotal)) {
				total += rowTotal;
			}
		}
		$("#dropshipping_total").text(total.toFixed(2));
	}
});