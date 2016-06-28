define(['jquery','../model/OrderTransaction','jqueryjson'],function($, OrderTransaction){
	var orderObj = new OrderTransaction();
	
	$("#search_orders_submit").click(function(){
		$("input[name=pageNum]").val(1);
		orderObj.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		$("input[name=pageNum]").val(1);
		orderObj.changePageSize(this);
		orderObj.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		orderObj.changePageNum(this);
		orderObj.submit();
	});
	
	$(document).ready(function() {
		orderObj.initTable();
		require(['jquery', 'bootstrap-datetimepicker'], function(){
			 $('#orderdatestart').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
				　　autoclose:true //选择日期后自动关闭 
				  }).on('changeDate', function(ev){
					  var startTime = $('#orderdatestart').val();
					  $('#orderdateend').datetimepicker('setStartDate', startTime);
					  $('#orderdatestart').datetimepicker('hide');
				  });
			 $('#orderdateend').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
				　　autoclose:true //选择日期后自动关闭 
				  });
			  $('#paymentDateStart').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
				　　autoclose:true //选择日期后自动关闭 
			  }).on('changeDate', function(ev){
				  var startTime = $('#paymentDateStart').val();
				  $('#paymentDateEnd').datetimepicker('setStartDate', startTime);
				  $('#paymentDateStart').datetimepicker('hide');
			  });
			  $('#paymentDateEnd').datetimepicker({
		　  	  	  format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
			　  	  autoclose:true //选择日期后自动关闭 
			  });
		});
	});
});

