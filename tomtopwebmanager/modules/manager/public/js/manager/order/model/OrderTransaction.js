define(['jquery'],
		function($) {
			function OrderTransaction() {
			}
			OrderTransaction.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
						$(".order_ll_TTBoxAll").hover(
							  function () {
									var divx = $(this).children(".order_ll_TxT");
									var order_h = divx.height();
									divx.css({"height":order_h-2})
									if(order_h>90){
										$(".coupon_ll_code").css({"padding-top":order_h/4})
									}
							  },
							  function () {
								  var divx = $(this).children(".order_ll_TxT");
								  divx.css({"height":"auto"})
								   $(".coupon_ll_code").css({"padding-top":"0"})
							  }
					   );
					},
					
					submit : function() {
						$('.waiting_ll').show();
						var form = $("#searchTransactionOrders");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_orders_html").replaceWith(html);
							$('.waiting_ll').hide();
							$this.initTable();
							$this.validatePrice();
							$("form").on("submit", function(){
								$this.updateOrderPrice(this);
								return false;
							});
						}, "html");
					},
					
					changePageSize : function(e) {
						$("a[tag=pageSize]").attr("href", "javascript:;");
						$("a[tag=pageSize]").removeAttr("class");
						$(e).attr("class", "order_ll_aActi");
						$(e).removeAttr("href");
						var status = $(e).attr("value");
						$("input[name=pageSize]").val(status);
					},
					
					changePageNum : function(e) {
						$("a[tag=pageNum]").attr("href", "javascript:;");
						$("a[tag=pageNum]").removeAttr("class");
						$(e).attr("class", "showAc");
						$(e).removeAttr("href");
						var status = $(e).attr("value");
						$("input[name=pageNum]").val(status);
					}
			};
			
			return OrderTransaction;

});

