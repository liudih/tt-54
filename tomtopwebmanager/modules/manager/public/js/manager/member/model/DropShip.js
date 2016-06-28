define(['jquery'],
		function($) {
			function DropShip() {
			}

			DropShip.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
					},
					
					submit : function() {
						var form = $("#searchDropShip");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_dropship_html").replaceWith(html);
							$this.initTable();
//							$("form").on("submit", function(){
//								$this.updatePassword(this);
//								return false;
//							});
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
					
					updateDropShipStatus : function(e) {
						if (confirm("Are you sure to update this dropship status ?") == false) {
					        return;
					    } 
						var iid = $(e).data('dropshipid');
						var statusid = $(e).attr('value');
						var url = dropShipRoutes.controllers.manager.DropShip.updateStatus(iid, statusid).url;
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
					},
					
					updateDropShipLevel : function(e) {
						if (confirm("Are you sure to update this dropship level ?") == false) {
					        return;
					    } 
						var iid = $(e).data('dropshipid');
						var statusid = $(e).attr('value');
						var url = dropShipRoutes.controllers.manager.DropShip.updateLevel(iid, statusid).url;
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
					}
					
			};
			
			return DropShip;

});

