//删除单个商品
function delCart(lid) {
	var url = cartRoutes.controllers.cart.Cart.delCart(lid, "").url;
	$.ajax({
		url : url,
		type : "GET",
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.result == "success") {
				var csku = $("#cart_item_num" + lid + " .csku:eq(0)").val();
				if (csku == "X01") {
					$(".dropOrder").removeClass("bulkrateActi");
				}

				$("#cart_item_num" + lid).remove();
				sumAllPrice();
			}
		},
		complete : function() {
		}
	});
}
// 删除捆绑商品
function deleteItemList(itemID, listingID, isMain) {
	var url = cartRoutes.controllers.cart.Cart.deleteItemList(itemID,
			listingID, isMain).url;
	$.get(url, function(data) {
		if (data.result == "success") {
			refreshCartTable();
		}
	}, "json");
}
// 减少数量
$(".qty_num_Reduction").click(function() {
	var n = $(this).next().find("input:eq(0)");
	var num = parseInt(n.val());
	if (num > 1) {
		num = num - 1;
	}
	n.val(num);
	submitNum($(this), num); // 更新数量
});
// 增加数量
$(".qty_num_add").click(function() {
	var n = $(this).prev().find("input:eq(0)");
	var num = parseInt(n.val());
	if (num < 9999) {
		num = num + 1;
	}
	n.val(num);
	submitNum($(this), num); // 更新数量
});

function sumItemTotal(num, node) {
	var itemid = node.attr("clstag");
	var price1 = $("#cart_item_num" + itemid + " .newPrice span:eq(0)").html();
	var price2 = parseFloat(price1);
	var allprice = num * price2;
	allprice = allprice.toFixed(2);
	var allpricenode = $("#cart_item_num" + itemid + " .pro_total span:eq(0)");
	allpricenode.html(allprice);
}

// 失去焦点的数量判断
$(".quantity-text").blur(function() {
	var n = this.value;
	if (isNaN(n) || n < 1 || n > 9999) {
		n = 1;
	}
	this.value = n;
	submitNum($(this).parent().next(), n);
});

// 按下Enter键
$(".quantity-text").keydown(function(event) {
	if (event.keyCode == 13) {
		var n = this.value;
		if (isNaN(n) || n < 1 || n > 9999) {
			n = 1;
		}
		this.value = n;
		submitNum($(this).parent().next(), n);
	}
});

// 计算总价
function sumAllPrice() {
	savetotal();// 计算折扣的价格
	refreshTotal();
}

function submitNum(node, num) {
	var cid = node.attr("clstag");
	var url = cartRoutes.controllers.cart.Cart.editNum(cid, num).url;
	$
			.ajax({
				url : url,
				type : "get",
				async : false,
				dataType : "json",
				success : function(data) {
					if (data.result == "success") {
						$("#cart_item_num" + cid + " .pro_total")
								.each(
										function(i, e) {
											var n = $(this);
											if (i == 0) {
												n
														.find("span:eq(0)")
														.html(
																parseFloat(
																		data["main"].price)
																		.toFixed(
																				2));
											} else {
												n
														.prev()
														.find("li:eq(0)")
														.html(
																parseInt(data[n
																		.attr("data")].quantity)
																		* num);
												n
														.find("span:eq(0)")
														.html(
																(parseFloat(data[n
																		.attr("data")].price) * num)
																		.toFixed(2));
											}
										});
						sumAllPrice();
					} else if (data.result == "notenough") {
						node.parent().find("input:eq(0)").val(data.oldnum);
						alert("Low stocks!");
					} else {
						alert("update error!");
					}
				},
				complete : function() {
				}
			});
}

// remove all
function removeall() {
	var url = cartRoutes.controllers.cart.Cart.delCart(0, "yes").url;
	$.ajax({
		url : url,
		type : "GET",
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.result == "success") {
				location.reload();
			}
		},
		complete : function() {
		}
	});
}

function hideTip(node) {
	$(node).parent().hide();
}

$(function() {
	// 设置收藏状态
	if (typeof (membercollecturl) != "undefined") {
		$.get(membercollecturl, {}, function(data) {
			var list = data;
			$(".showcollect").each(function(i, e) {
				for (var i = 0; i < list.length; i++) {
					if ($(e).attr("data") == list[i]) {
						$(e).addClass("redHeart");
					}
				}
			});
		}, "json");
	}
	savetotal();

	// 设置drop shipping order
	$(".csku").each(function(i, e) {
		if (this.value == "X01") {
			$(".dropOrder").addClass("bulkrateActi");
			return false;
		}
	});
});
// 计算 save total
function savetotal() {
	var saveprice = 0;
	$(".cartListUL").each(
			function(i, e) {
				var savep = $(this).find(".mainsaveprice:eq(0)").html();
				
				if (savep != null && savep != undefined) {
					savep = savep.replace(/,/g,'');
					saveprice += parseFloat(savep)
							* parseInt($(this).find(".quantity-text:eq(0)")
									.val());
				}
			});
	if (saveprice > 0) {
		var currency = $("#saveTotal").attr('currency') || '';
		var isJPY = 'JPY' == currency;
		$("#saveTotal").html((isJPY ? saveprice : saveprice.toFixed(2)));
	} else {
		$(".cart_total").hide();
	}
}

$(".dropOrder").click(function() {
	var jdom = $(this);
	if (jdom.hasClass("bulkrateActi")) {
		// $(this).removeClass("bulkrateActi");
	} else {
		var url = cartRoutes.controllers.cart.Cart.addDropShipping().url;
		$.ajax({
			url : url,
			type : "GET",
			dataType : "json",
			async : true,
			success : function(data) {
				if (data.result == "success") {
					jdom.addClass("bulkrateActi");
					location.reload();
				}
			},
			complete : function() {
			}
		});
	}
})

function skiporder() {
	var checkurl = cartRoutes.controllers.member.Login.checkSign().url;
	var ischeck = $(".ContinueSpay:eq(0)").hasClass("ContinueNo");
	if (ischeck) {
		return;
	}
	var checkCartUrl = cartRoutes.controllers.cart.Cart.checkCart().url;
	$.get(checkCartUrl, function(data) {
		if (data.length > 0) {
			for (var i = 0; i < data.length; i++) {
				$("[flag=" + data[i] + "]").addClass("outOfStock");
			}
		} else {
			$.get(checkurl,
					function(data) {
						var url = $("#spayOut").val();
						if (data != undefined && data) {
							location.href = url;
						} else {
							$(".blockPopup_box").show();
						}
					}, "json");
		}
	}, "json");
}

Number.prototype.toFixed = function(s) {
	changenum = (parseInt(this * Math.pow(10, s) + 0.5) / Math.pow(10, s))
			.toString();
	index = changenum.indexOf(".");
	if (index < 0 && s > 0) {
		changenum = changenum + ".";
		for (i = 0; i < s; i++) {
			changenum = changenum + "0";
		}

	} else {
		index = changenum.length - index;
		for (i = 0; i < (s - index) + 1; i++) {
			changenum = changenum + "0";
		}
	}
	return changenum;
}

function refreshTotal() {
	//modify by lijun
	window.location.reload();
	return;
	$.get(cartRoutes.controllers.cart.Cart.refreshTotal().url, function(html) {
		$("#cart_grand_total").replaceWith(html);
	}, "html");
}

function refreshCartTable() {
	$.get(cartRoutes.controllers.cart.Cart.refreshCartTable().url, function(
			html) {
		$("#cart_view_table").replaceWith(html);
		refreshTotal();
	}, "html");
}

$("input[name=cart_submit_extra]").click(function() {
	submit($(this));
});

function submit(e) {
	var p = e.next("p");
	var form = e.parent("form");
	var value = e.prev("input").val();
	var url = form.attr('action');
	$.ajax({
		url : url,
		data : form.serialize(),
		success : function() {
			var strong = form.prev(".cart_applied").find("strong");
			strong.text(value);
			p.hide();
			form.fadeOut("fast");
			form.prev(".cart_applied").fadeIn("show");
			refreshTotal();
		},
		error : function() {
			var strong = p.children("strong");
			$(strong).text(value);
			p.fadeIn("normal");
		}
	});
}

$("input[name=cart_cancel_extra]").click(function() {
	var form = $(this).parent("form");
	var url = form.attr('action');
	$.get(url, form.serialize(), function(data) {
		refreshTotal();
	});
});

$("input.digital").keyup(function() {
	var value = $(this).val();
	value = value.match(/\d+/);
	if (value != null) {
		value = parseInt(value);
	}
	$(this).val(value);
});

$("form.cart_apply input").keydown(
		function(e) {
			var keyNum = e.which || e.keyCode;
			if (keyNum == 13) {
				submit($(this).parent("form").children(
						"input[name=cart_submit_extra]"));
			}
		});
