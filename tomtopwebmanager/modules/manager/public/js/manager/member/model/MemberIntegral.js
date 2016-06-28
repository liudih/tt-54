define(['jquery'],
		function($) {
			function MemberIntegral() {
			}

			MemberIntegral.prototype = {
					initTable: function() {
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
							$this.valideIntegral();
						});
					},
					
					submit : function() {
						var form = $("#searchMemberIntegral");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_memberIntegrals_html").replaceWith(html);
							$this.initTable();
							$("form").unbind('submit');
							$("form").on("submit", function(){
								$this.editMemberIntegral(this);
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
					
					valideIntegral : function() {
						$(".intragal").focusout(function() {
							var value = $(this).val();
							value = value.match(/^-?\d+$/);
							if (value != null && value != 0) {
								value = parseInt(value);
							}
							$(this).val(value);
						});
					},
					
					editMemberIntegral : function(e) {
						var url = $(e).attr("action");
						var $this = this;
						$.post(url, $(e).serialize(), function(data) {
							var result = data['result'];
							if (result == true) {
								alert("操作成功");
								$this.submit();
							} else {
								alert("操作失败");
							}
						}, "json");
					}
					
			};
			
			return MemberIntegral;

});

