define(
		[ 'jquery', '../model/Summary' ],
		function($, Summary) {
			var summaryObj = new Summary();

			summaryObj.refreshShippingMethod($(
					"input[name='address_radio']:checked").val());

			summaryObj.init();

			$("input[name='address_radio']").click(function() {
				var addressId = $(this).val();
				summaryObj.refreshShippingMethod(addressId);
			});

			$(function() {
				var addressid = $('input[name=hidden_radio_value]').val();
				Listeners.fireListenner('addresschang', addressid);
			});

			$("#place_order")
					.submit(
							function() {
								var flag = true;
								var isOrder = $("input[name=isOrder]").val();
								if (isOrder == "1") {
									return true;
								}
								var checkCartUrl = orderRoutes.controllers.cart.Cart
										.checkCart().url;
								$
										.ajax({
											type : "GET",
											url : checkCartUrl,
											async : false,
											dataType : "json",
											success : function(data) {
												if (data.length > 0) {
													flag = false;
													pophtml("Error","Some product was out of stock,redirect to cart!");
													location.href = orderRoutes.controllers.cart.Cart
															.cartview().url;
												}
											}
										});
								return flag;
							});

		});
