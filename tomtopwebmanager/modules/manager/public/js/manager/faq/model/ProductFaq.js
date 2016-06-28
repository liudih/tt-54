define(['jquery'],
		function($) {
			function ProductFaq() {
			}

			ProductFaq.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
					},
					
					submit : function() {
						$('.waiting_ll').show();
						var form = $("#searchPosts");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_post_html").replaceWith(html);
							$('.waiting_ll').hide();
							$this.initTable();
							$("form").on("submit", function(){
								$this.updateAnswer(this);
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
						if (confirm("Are you sure to update this order status ?") == false) {
					        return;
					    } 
						var orderid = $(e).attr('id');
						var statusid = $(e).attr('value');
						var url = orderRoutes.controllers.manager.Order.updateOrderStatus(orderid, statusid).url;
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
					
					updateAnswer : function(e) {
						var url = $(e).attr("action");
						console.info(url);
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
						var status= form.find("select[name='status']").val();
						var orderId= form.find("input[name='orderId']").val();
						var email= form.find("input[name='email']").val();
						var transactionId= form.find("input[name='transactionId']").val();
						var start= form.find("input[name='start']").val();
						var end= form.find("input[name='end']").val();
						var vhost= form.find("select[name='vhost']").val();
						var url = order.controllers.manager.Order.downloadOrderList(siteId,paymentId,status,orderId, email,transactionId,start,end,vhost).url;
						document.getElementById("ifile").src=url;
					}
			};
			
			return ProductFaq;

});

