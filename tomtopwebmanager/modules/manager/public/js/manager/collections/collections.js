define([ 'jquery' ], function($) {
	$(document).ready(function() {
		require(['bootstrap-datetimepicker'], function(){
			$('#dcreatedate').datetimepicker({minView: "month",
				format : "yyyy-mm-dd", // 选择日期后，文本框显示的日期格式
				autoclose : true
				// 选择日期后自动关闭
			});
		});
	});
});

$(function(){
	$("#edit-collections-modal").on("shown", function(){
		$('#edit-collections-modal').find('#dcreatedate').datetimepicker({minView: "month",
			format : "yyyy-mm-dd", // 选择日期后，文本框显示的日期格式
			autoclose : true
		// 选择日期后自动关闭
		});
	});
});