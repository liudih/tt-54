define([ 'jquery', 'jqueryjson' ], function($) {
	$(document).ready(function() {
		require(['bootstrap-datetimepicker'], function(){
			$('#start_date').datetimepicker({
				format : "yyyy-mm-dd hh:ii",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#start_date').val();
				$('#end_date').datetimepicker('setStartDate', startTime);
				$('#start_date').datetimepicker('hide');
			});
			$('#end_date').datetimepicker({
				format : "yyyy-mm-dd hh:ii",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#end_date').val();
				$('#start_date').datetimepicker('setEndDate', startTime);
				$('#end_date').datetimepicker('hide');
			});
			$('#search_startDate').datetimepicker({
				minView: "month",
				maxView: "year",
				format : "yyyy-mm-dd",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#search_startDate').val();
				$('#search_endDate').datetimepicker('setStartDate', startTime);
				$('#search_startDate').datetimepicker('hide');
			});
			
			$('#search_endDate').datetimepicker({
				minView: "month",
				maxView: "year",
				format : "yyyy-mm-dd",
				autoclose : true
			}).on('changeDate', function(ev) {
				var startTime = $('#search_endDate').val();
				$('#search_startDate').datetimepicker('setEndDate', startTime);
				$('#search_endDate').datetimepicker('hide');
			});
			
			$('#upddate').datetimepicker({
				format : "yyyy-mm-dd hh:ii:ss",
				autoclose : true
			});
			
			$('#ddate').datetimepicker({
				format : "yyyy-mm-dd hh:ii:ss",
				autoclose : true
			});
		});
	});
	
	$(document).on("change","select[name=select_status]",function(){
		var type = $(this).val();
		if(type == 3){
			$("#search_sku").val("");
			$("#search_email").val("");
			$("#search_content").val("");
			$("#search_hidden_sku").val("");
			$("#search_hidden_email").val("");
			$("#search_hidden_content").val("");
		}
		$("#review_status_hidden_value").val(type);
	});
	
	$(document).on("change","select[name=websiteid]",function(){
		var websiteId = $(this).val();
		$("#search_hidden_siteId").val(websiteId);
	});
	
	$(document).on("click","#prePage",function(){
		var prePage = parseInt($(this).attr("tag")) -1;
		$("#page_hidden_value").val(prePage);
		reviewsFormSubmit();
	});

	$(document).on("click","#nextPage",function(){
		var nextPage = parseInt($(this).attr("tag")) + 1;
		$("#page_hidden_value").val(nextPage);
		reviewsFormSubmit();
	});

	$(document).on("click","a[name=pa]",function(){
		var page = parseInt($(this).attr("tag"));
		$("#page_hidden_value").val(page);
		reviewsFormSubmit();
	});

	$("#search").click(function(){
		var sku = $("#search_sku").val();
		sku = textTrim(sku);
		$("#search_hidden_sku").val(sku);
		var email = $("#search_email").val();
		email = textTrim(email);
		$("#search_hidden_email").val(email);
		var startDate = $("#search_startDate").val();
		startDate = textTrim(startDate);
		$("#search_hidden_startDate").val(startDate);
		var endDate = $("#search_endDate").val();
		endDate = textTrim(endDate);
		$("#search_hidden_endDate").val(endDate);	
		$("#page_hidden_value").val(1);
		var content = $("#search_content").val();
		$("#search_hidden_content").val(content);
		reviewsFormSubmit();
	});

	function  reviewsFormSubmit(){
		$("#reviews_manger_form").submit();
	}
	
  // 全选
  $("#checkAll").on("click", function () {
      $("input[type=checkbox]").each(function(){
    	  $(this).parent().addClass("checked");
      })
  });

  // 全不选
  $("#checkNone").on("click", function () {
      $("input[type=checkbox]").each(function(){
      	 $(this).parent().removeClass("checked");
      })
  });
  
  //反选
  $("#checkRevise").on("click", function () {
	  $("input[type=checkbox]").each(function(){
		 var node =  $(this).parent();
		 var isChecked = node.attr("class");
		 if(isChecked == undefined || isChecked ==""){
			 node.addClass("checked");
		 } else {
			 node.removeClass();
		 }
	  })
  });
  
  //批量审核
  $("#review_batch_vertify").on("change",function(){
	  var type = $(this).val();
	  $("#batch_type").val(type);
  });
  
  $("#batch_submit").on("click",function(){
	  var batchType = $("#batch_type").val();
	  if(batchType == 0 ){
		  alert("请选择操作类型");
		  return false;
	  }
	  var ids = new Array();
	  $("input[type=checkbox]").each(function(){
		  var node =  $(this).parent();
		  var isChecked = node.attr("class");
			 if(isChecked == "checked"){
				 var childnode = node.find("input[type=checkbox]");
				 var id = childnode.val();
				 ids.push(id);
			 }
	  });
	  if(ids.length==0){
		  alert("请至少选择一条要审核的评论");
		  return false;
	  }
	  var allpoints = $("#allpoints").val();
	  if(parseInt(allpoints)>10){
			alert("Too many points!");
			return false;
		}
	  var idsString = ids.join("-");
	  var data = JSON.stringify({"ids":idsString,
		  "type": batchType,
		  "points":allpoints});
	  
	  $.ajax({
			url : reviews.controllers.manager.Review.batchVerify().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data=="success"){
					alert("操作成功");
					window.location.reload();
				} else {
					alert("操作失败 (可能评论时间 大于现在时间)");
				}
			}
		});
  });
  
  function textTrim(str){ //删除左右两端的空格
		return str.replace(/(^\s*)|(\s*$)/g, "");
  } 
});


