define(['jquery'],
		function($) {
			function WholeSale() {
			}

			WholeSale.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
					},
					
					submit : function() {
						var form = $("#searchWholeSale");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_wholesale_html").replaceWith(html);
							$this.initTable();
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
					
					updateWholeSaleStatus : function(e) {
						if (confirm("Are you sure to update this wholesale status ?") == false) {
					        return;
					    } 
						var iid = $(e).data('wholesaleid');
						var statusid = $(e).attr('value');
						var url = wholesaleRoutes.controllers.manager.wholesale.WholeSale.updateStatus(iid, statusid).url;
						var $this = this;
						$.post(url, function(json) {
							var result = json['result'];
							if (result == true) {
							    alert("修改成功!");
								$this.submit();
							} else {
								alert("修改失败");
							}
						}, "json");
					}
			};
			
			return WholeSale;

});

