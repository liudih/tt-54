define(['jquery'],
		function($) {
			function Order() {
			}

			Order.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
						$("a[tag=paypal]").mouseenter(function(){
							$this.getPaypalMsg(this);
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
						var form = $("#searchOrders");
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
					},
					
					changeOrderStatue : function(e) {
						var statusid = $(e).attr('value');
						orderStatus_index=$(e).attr('id');
						if(statusid==2){
					        return;
						}
						if (confirm("Are you sure to update this order status ?") == false) {
					        return;
					    } 
						var transid = $(e).attr("transid");
						var url = orderRoutes.controllers.manager.Order.updateOrderStatus(orderStatus_index, statusid, transid).url;
						var $this = this;
						$.post(url, function(json) {
							var result = json['result'];
							if (result == true) {
								alert("修改成功");
								$this.submit();
							} else {
								alert("修改失败");
							}
						}, "json");
					},
					
					validatePrice : function() {
						$('.price').focusout(function() {
							var value = $(this).val();
							var reg=/\d+(\.\d{1,2})?/;
							value = value.match(reg);
							if (value != null && value != 0) {
								value = parseFloat(value);
							}
							$(this).val(value);
						});
					},
					
					updateOrderPrice : function(e) {
						var url = $(e).attr("action");
						var $this = this;
						$.post(url, $(e).serialize(), function(data) {
							var result = data['result'];
							if (result == true) {
								alert("修改成功");
								$this.submit();
							} else {
								alert("修改失败");
							}
						}, "json");
					},
					
					getPaypalMsg : function(e) {
						var ordernumber = $(e).data('ordernumber');
						var url = orderRoutes.controllers.manager.Order.getPaypalMessage(ordernumber).url;
						$.get(url, function(html) {
							$(e).siblings(".paypalMsg").replaceWith(html);
						}, "html");
					},
					
					download : function() {
						var form = $("#searchOrders");
						var siteId = form.find("select[name='siteId']").val();
						var paymentId= form.find("select[name='paymentId']").val();
						var status= form.find("input[name='status']").val();
						var orderNumber= form.find("input[name='orderNumber']").val();
						var email= form.find("input[name='email']").val();
						var transactionId= form.find("input[name='transactionId']").val();
						var start= form.find("input[name='start']").val();
						var end= form.find("input[name='end']").val();
						var vhost= form.find("select[name='vhost']").val();
						var paymentStart= form.find("input[name='paymentStart']").val();
						var paymentEnd= form.find("input[name='paymentEnd']").val();
						var code = form.find("input[name='code']").val();
						var queryType = null;
						if(typeof($("#dropshipOrderQuery").val()) != "undefined"){
							queryType = $("#dropshipOrderQuery").val();
						}
						if(typeof($("#wholesaleOrderQuery").val()) != "undefined"){
							queryType = $("#wholesaleOrderQuery").val();
						}
						var url = order.controllers.manager.Order.exportOrderList(siteId,paymentId,status,orderNumber, email,
								transactionId,start,end,vhost,paymentStart,paymentEnd,queryType,code).url;
						document.getElementById("ifile").src=url;
					},
					
					downloadDetails : function() {
						var form = $("#searchOrders");
						var siteId = form.find("select[name='siteId']").val();
						var paymentId= form.find("select[name='paymentId']").val();
						var status= form.find("input[name='status']").val();
						var orderNumber= form.find("input[name='orderNumber']").val();
						var email= form.find("input[name='email']").val();
						var transactionId= form.find("input[name='transactionId']").val();
						var start= form.find("input[name='start']").val();
						var end= form.find("input[name='end']").val();
						var vhost= form.find("select[name='vhost']").val();
						var paymentStart= form.find("input[name='paymentStart']").val();
						var paymentEnd= form.find("input[name='paymentEnd']").val();
						var code = form.find("input[name='code']").val();
						var queryType = null;
						if(typeof($("#dropshipOrderQuery").val()) != "undefined"){
							queryType = $("#dropshipOrderQuery").val();
						}
						if(typeof($("#wholesaleOrderQuery").val()) != "undefined"){
							queryType = $("#wholesaleOrderQuery").val();
						}
						var url = order.controllers.manager.Order.exportOrderDetailsList(siteId,paymentId,status,orderNumber, email,
								transactionId,start,end,vhost,paymentStart,paymentEnd,queryType,code).url;
						document.getElementById("ifile").src=url;
					},
					
					Update_Payment_Confirm:function(){
						
						var orderstatus=$("#orderstatus_id"+orderStatus_index).val();
						var orderTransaction=$("#orderTransaction_Id"+orderStatus_index).val();
						var orderid = $("#order_Id"+orderStatus_index).val();
						
						var url = orderRoutes.controllers.manager.Order.updateOrderStatus(orderid, orderstatus,orderTransaction).url;
						var $this = this;
						$.post(url, function(json) {
							var result = json['result'];
							if (result == true) {
								alert("修改成功");
								$this.submit();
								$("#payment_close_id").click();
							} else {
								alert("修改失败");
							}
						}, "json");
					}
			};
			
			return Order;

});

