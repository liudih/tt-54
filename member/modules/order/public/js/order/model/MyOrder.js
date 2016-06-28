define(
		[ 'jquery', '../../lib/jquery.simplePagination', 'jqueryjson' ],
		function() {
			function ModelMyOrder() {
			}

			ModelMyOrder.prototype = {
				init : function() {
					this.createPage();
					this.removeEvent()
				},

				afterSubmit : function() {
					this.createPage();
					this.removeEvent();
					this.deleteEvent();
					this.restoreEvent();
				},

				removeEvent : function() {
					var t = this;
					$("#remove_all_order").click(function() {
						var ids = new Array();
						$("a[class=rubbish]").each(function() {
							ids.push(parseInt($(this).attr("value")));
						});
						t.updateOrder(ids, false);
					});
					$("a.rubbish[name=remove_order]").click(function() {
						var ids = new Array();
						ids.push(parseInt($(this).attr("value")));
						t.updateOrder(ids, false);
					});
				},

				deleteEvent : function() {
					var t = this;
					$("#delete_all_order").click(function() {
						var ids = new Array();
						$("a[name=delete_order]").each(function() {
							ids.push(parseInt($(this).attr("value")));
						});
						t.updateOrder(ids, false);
					});
					$("a[name=delete_order]").click(function() {
						var ids = new Array();
						ids.push(parseInt($(this).attr("value")));
						t.updateOrder(ids, false);
					});
				},
				
				restoreEvent : function() {
					var t = this;
					$("#restore_all_order").click(function() {
						var ids = new Array();
						$("a[name=restore_order]").each(function() {
							ids.push(parseInt($(this).attr("value")));
						});
						t.updateOrder(ids, true);
					});
					$("a[name=restore_order]").click(function() {
						var ids = new Array();
						ids.push(parseInt($(this).attr("value")));
						t.updateOrder(ids, true);
					});
				},

				updateOrder : function(ids, isRestore) {
					var t = this;
					var isShow = parseInt($("input[name=isShow]").val());
					var url;
					if (isRestore) {
						url = memberOrderRoutes.controllers.order.member.MemberOrder
								.restore($.toJSON(ids)).url;
					} else if (isShow == 1) {
						url = memberOrderRoutes.controllers.order.member.MemberOrder
								.remove($.toJSON(ids)).url;
					} else if (isShow == 2) {
						url = memberOrderRoutes.controllers.order.member.MemberOrder
								.delete($.toJSON(ids)).url;
					}
					$.post(url, function() {
						t.submit();
					});
				},

				submit : function() {
					var form = $("#searchOrders");
					var url = form.attr("action");
					var t = this;
					$("#search_orders_html").hide();
					$("#search_loading_gif").show();
					$.post(url, form.serialize(), function(html) {
						$("#search_orders_html").replaceWith(html);
						$("#search_loading_gif").hide();
						t.afterSubmit();
					}, "html");
				},

				changePageSize : function(e) {
					$("a[tag=pageSize]").attr("href", "javascript:;");
					$("a[tag=pageSize]").removeAttr("class");
					$(e).attr("class", "showAc");
					$(e).removeAttr("href");
					var status = $(e).attr("value");
					$("input[name=pageSize]").val(status);
					$("input[name=pageNum]").val(1);
					this.submit();
				},

				changeIsShow : function() {
					$("input[name=status]").removeAttr("value");
					$("input[name=isShow]").val("2");
				},

				changeTab : function(e) {
					var value = $(e).attr("value");
					var statusValue = $("select[name=status_value]");
					if (value != undefined) {
						statusValue.attr("disabled", "true");
					} else {
						statusValue.removeAttr("disabled");
					}
					statusValue.get(0).selectedIndex = 0;
					$("input[name=status]").val(value);
					$("input[name=pageNum]").removeAttr("value");
					$("input[name=isShow]").val("1");
					$("input[name=name]").removeAttr("value");
				},

				changeStatus : function(value) {
					$("input[name=status]").val(value);
				},
				
				queryString:function (param,name) {
					   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
					   var r = param.match(reg);
					   if (r!=null) return (r[2]); return "";
				},
				
				download : function() {
					var form = $("#searchOrders");
					var form = $("#searchOrders");
					var interval = this.queryString(form.serialize(),'interval');
					var status = this.queryString(form.serialize(),'status_value');
					var orderNumber = this.queryString(form.serialize(),'orderNumber');
					var productName = this.queryString(form.serialize(),'productName');
					var transactionId = this.queryString(form.serialize(),'transactionId');
					var firstName = this.queryString(form.serialize(),'firstName');
					var url = memberOrderRoutes.controllers.order.dropshipping.MemberDropShippingOrder
							.download(interval, status, orderNumber, productName, transactionId, firstName).url;
					document.getElementById("ifile").src=url;
				},
				
				createPage : function() {
					var t = this;
					$('#light-pagination').pagination({
						pages : $("input[name=page_count_value]").val(),
						currentPage : $("input[name=page_number_value]").val(),
						hrefTextPrefix : 'javascript:void(',
						hrefTextSuffix : ')',
						cssStyle : 'light-theme',
						selectOnClick : true,
						onPageClick : function(pageNumber, event) {
							$("input[name=pageNum]").val(pageNumber);
							t.submit();
						}
					});
				}
			}

			return ModelMyOrder;
		});