define(['jquery','../model/Member','jqueryjson'],function($,Member){
	var memberObj = new Member();
	
	$("#search_members_submit").click(function(){
		memberObj.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		memberObj.changePageSize(this);
		memberObj.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		memberObj.changePageNum(this);
		memberObj.submit();
	});
	
	$("form").on("submit", function(){
		memberObj.updatePassword(this);
		return false;
	});
	
	$(document).ready(function() {
		memberObj.initTable();
		require(['jquery', 'bootstrap-datetimepicker'], function(){
			 $('#memberdatestart').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
				　　autoclose:true //选择日期后自动关闭 
				  }).on('changeDate', function(ev){
					  var startTime = $('#orderdatestart').val();
					  $('#orderdateend').datetimepicker('setStartDate', startTime);
					  $('#orderdatestart').datetimepicker('hide');
				  });
			 $('#memberdateend').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
				　　autoclose:true //选择日期后自动关闭 
				  });
		});
	});
});

