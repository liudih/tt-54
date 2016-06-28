define(
		[ 'jquery' ],
		function() {

			function ModelSummary() {
			}

			ModelSummary.prototype = {

				init : function() {
					var t = this;
					t
							.updateShippingInfo($("input[name='shippingMethodIdValue']:checked"));
					t.getTotal();
					$(".cancel").click(function() {
						$('.pu_pop').hide();
					});
					$("input[name='shippingMethodIdValue']").change(function() {
						t.updateShippingInfo(this);
						t.getTotal();
					});
					$("#place_order")
							.submit(
									function() {
										if ($(".ContinueSpay").hasClass(
												"ContinueNo")) {
											return false;
										}
										var addressID = $(
												"input[name='addressId']")
												.val();
										var billID = $(
												"input[name='order_address_radio']:checked")
												.val();
										var url = orderRoutes.controllers.order.OrderProcessing
												.checkAddress(
														parseInt(addressID),
														parseInt(billID)).url;
										var b = true;
										$.ajax({
											url : url,
											type : "GET",
											async : false,
											success : function(data) {
												b = data.res;
												if (!b) {
													$(".pu_pop").find(
															"p.pu_popTxt")
															.text(data.msg);
													$(".pu_pop").show();
												} else {
													$(".pu_pop").hide();
												}
											},
											dataType : "json"
										});
										return b;
									});
					t.beforeSubmit();
				},

				beforeSubmit : function() {
					var t = this;
					var isOrder = $("input[name=isOrder]").val();
					if (isOrder == "1") {
						var isSelect = $("input[name=isSelect]").val();
						if (isSelect == "false") {
							t.checkBillAddress();
							return;
						}
					}
					t.checkShippingMethod();
				},

				checkBillAddress : function() {
					var billAd = $("input[name=order_address_radio]:checked")
							.val();
					if (billAd == undefined || billAd == null || billAd == "") {
						$("#shippingMethodErr").hide();
						$("table.shippingAs").addClass("ErrBorder");
						$(".ContinueSpay").addClass("ContinueNo");
					} else {
						$("#shippingMethodErr").hide();
						$("table.shippingAs").removeClass("ErrBorder");
						$(".ContinueSpay").removeClass("ContinueNo");
					}
				},

				checkShippingMethod : function() {
					var shippingMethodId = $("input[name='shippingMethodId']")
							.val();
					var addressId = $("#place_order > input[name='addressId']")
							.val();
					var b = true;
					if (shippingMethodId == undefined
							|| shippingMethodId == null
							|| shippingMethodId == "") {
						$("#shippingMethodErr").show();
						b = false;
					} else {
						$("#shippingMethodErr").hide();
					}
					if (addressId == undefined || addressId == null
							|| addressId == "") {
						$("#addressErr").show();
						$("table.shippingAs").addClass("ErrBorder");
						b = false;
					} else {
						$("#addressErr").hide()
						$("table.shippingAs").removeClass("ErrBorder");
					}
					if (!b && !$(".ContinueSpay").hasClass("ContinueNo")) {
						$(".ContinueSpay").addClass("ContinueNo");
					} else if (b && $(".ContinueSpay").hasClass("ContinueNo")) {
						$(".ContinueSpay").removeClass("ContinueNo");
					}
				},

				getTotal : function() {
					var total = 0;
					$("[name='total_fragment']").each(function() {
						var text = $(this).text().replace(',', '');
						var price = parseFloat(text);
						if (!isNaN(price)) {
							total += price;
						}
					});
					if (total < 0)
						total = 0;
					
					var currency = $("#grand_total").attr('currency') || '';
					var isJPY = 'JPY' == currency;
					
					$("#grand_total").text((isJPY ? total : total.toFixed(2)));
				},

				updateShippingInfo : function(e) {
					var id = $(e).val();
					$("input[name='shippingMethodId']").val(id);
					var billID = $("input[name='order_address_radio']:checked")
							.val();
					$("input[name='billId']").val(billID);
					var price = $(e).attr("price");
					var currency = $("#total_fragment_shipping").attr('currency') || '';
					var isJPY = 'JPY' == currency;
					price = (isJPY ? parseInt(price) : price);
					$("#total_fragment_shipping").html(price);
					var img = $("#img_name_" + id + " img").clone();
					var span = $(e).next("span").text();
					$("#total_fragment_shipping_context").text(span);
					$("#total_fragment_shipping_context").prepend(img);
					this.beforeSubmit();
				},

				refreshShippingMethod : function(addressId) {
					$("#place_order > input[name='addressId']").val(addressId);
					var url = null;
					var isOrder = $("input[name=isOrder]").val();
					if (isOrder == "0") {
						var cartId = $("input[name='cartId']").val();
						url = js_ShippingAddressRoutes.controllers.order.ShippingAddress
								.refreshShippingMethod(cartId,
										parseInt(addressId)).url;
					} else {
						var isSelect = $("input[name=isSelect]").val();
						if (isSelect == "false") {
							return 0;
						}
						var orderNumber = $("input[name=orderID]").val();
						url = js_ShippingAddressRoutes.controllers.order.ShippingAddress
								.refreshByOrder(orderNumber,
										parseInt(addressId)).url;

					}
					var t = this;
					$.get(url, function(html) {
						$("#shipping_method").replaceWith(html);
						t.init();
					}, "html");
				}

			}

			return ModelSummary;

		});