define(['jquery','../model/Order','jqueryjson'],function($, Order){
	var orderStatus_index;
	var orderObj = new Order();
	
	$(document).on("click",".dropdown-menu li",function(){
		orderObj.changeOrderStatue(this);
	});
	
	$("#search_orders_submit").click(function(){
		$("input[name=pageNum]").val(1);
		orderObj.submit();
	});
	
	$(document).on("click","#payment_commit_id",function(){		
		orderObj.Update_Payment_Confirm();
	});

	
	$("#download_order").click(function(){
		orderObj.download();
	});
	
	$("#download_order_details").click(function(){
		orderObj.downloadDetails();
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
	
	orderObj.validatePrice();
	
	$("form").on("submit", function(){
		orderObj.updateOrderPrice(this);
		return false;
	});
	
	$(".number").keyup(function() {
		var value = $(this).val();
		value = value.match(/\d+/);
		if (value != null && value != 0) {
			value = parseInt(value);
		}
		$(this).val(value);
	});
	

	$("a[tag=paypal]").mouseenter(function(){
		orderObj.getPaypalMsg(this);
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

