define(['jquery'],
		function($) {
			function Order() {
			}

			Order.prototype = {
					initTable: function() {
						require(['jquery', 'jquery.dataTables.min'], function(){
						    $('#memberlist').dataTable( {
						    	"oLanguage": {
						             "sSearch": "搜索:",
						             "sLengthMenu": "每页显示 _MENU_ 条记录",
						                 "sZeroRecords": "Nothing found - 没有记录",
						             "sInfo": "显示第  _START_ 条到第  _END_ 条记录,一共  _TOTAL_ 条记录",
						             "sInfoEmpty": "显示0条记录",
						             "oPaginate": {
						                 "sPrevious": " 上一页 ",
						                 "sNext":     " 下一页 ",
						                 }
						             },
						             //"sPaginationType": "full_numbers",
						             "bPaginate": false, //是否分页显示
						             "bInfo": false,
						             "bAutoWidth":true,
						             "bProcessing":true,
						             "bRetrieve":true,
						             "bLengthChange": true,
						             "iCookieDuration": 60*60*24, // 一天
						             "sScrollX": "100%",
						             //"bScrollCollapse": true,
						             //"pagingType":   "full_numbers",
						             //"iDisplayLength": 10,
						             'aLengthMenu': [[10, 20, 50, -1], [10, 20, 50, "All"]],
						    } );
						});
						var $this = this;
						$("a[tag=pageNum]").click(function(){
							$this.changePageNum(this);
							$this.submit();
						});
					},
					
					submit : function() {
						var form = $("#searchMember");
						var url = form.attr("action");
						var $this = this;
						$.post(url, form.serialize(), function(html) {
							$("#search_members_html").replaceWith(html);
							$this.initTable();
							$("form").on("submit", function(){
								$this.updatePassword(this);
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
					
					updatePassword : function(e) {
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
					}
					
			};
			
			return Order;

});

