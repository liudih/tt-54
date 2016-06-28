define(['jquery','jqueryjson','jvalidate','jmetadata'],function($){
	$.metadata.setType("attr", "validate");
	
	$("#edit-attribute-modal").on("hidden", function() {
		$(this).removeData("modal");
	});
	
	$("a[tag=pageNum]").click(function(){
		var value=$(this).attr("value");
		$("#search_form > input[name=p]").val(value);
		$("#search_form").submit();
	});
	
	$("#saveBaseInfo").submit(function(){
		return false;
	});
	
	$("#saveBaseInfo").find("[name=submit]").click(function(){
		var url = $("#saveBaseInfo").attr("action");
		$.post(url, $("#saveBaseInfo").serialize(), function(){
			location.reload();
		});
	});
	
	$("#saveAttribute").submit(function(){
		return false;
	});
	
	$("#saveAttribute").find("[name=submit]").click(function(){
		var url = $("#saveAttribute").attr("action");
		$.post(url, $("#saveAttribute").serialize(), function(){
			location.reload();
		});
	});
	
	$("#search_form").find("[type=submit]").click(function(){
		$("#search_form").find("[name=p]").val("0");
		return true;
	});
	
	$("a[name^='editCategory']").each(function(){
		$(this).click(function () {
			var clistingid = $(this).attr('clistingid');
			$("input[name^='clistingid']").val(clistingid);
			var url =$(this).attr('href');
			$('#edit-category-modal .modal-body').html('加载中........');
			$.get(url, '', function(data){  
		        $('#edit-category-modal .modal-body').html(data);
		    })  
		});
		
	});	
	
	$("a[name^='editAttribute']").each(function(){
		$(this).click(function () {
			var url =$(this).attr('href');
			$('#edit-attribute-modal .modal-body').html('');
			$.get(url, '', function(data){  
		        $('#edit-attribute-modal .modal-body').html(data);
		    })  
		});
		
	});
	
	$("a[name^='editBaseInfo']").each(function(){
		$(this).click(function () {
			var url =$(this).attr('href');
			$('#edit-baseInfo-modal .modal-body').html(' ');
			$.get(url, '', function(data){  
		        $('#edit-baseInfo-modal .modal-body').html(data);
		    })  
		});
		
	});
	
	$("a[name^='editStorageMap']").each(function(){
		$(this).click(function () {
			var url =$(this).attr('href');
			$('#edit-saleDetails-modal .modal-body').html(' ');
			$.get(url, '', function(data){  
		        $('#edit-saleDetails-modal .modal-body').html(data);
		    })  
		});
		
	});
	
	$("a[name^='editSaleDetails']").each(function(){
		$(this).click(function () {
			var url =$(this).attr('href');
			$('#edit-saleDetails-modal .modal-body').html(' ');
			$.get(url, '', function(data){  
		        $('#edit-saleDetails-modal .modal-body').html(data);
		    })  
		});
		
	});
	
	$("a[name^='productCategoryMap']").each(function(){
		$(this).click(function () {
			var url =$(this).attr('href');
			$('#edit-productCategoryMapper-modal .modal-body').html(' ');
			$.get(url, '', function(data){  
		        $('#edit-productCategoryMapper-modal .modal-body').html(data);
		    })  
		});
		
	});
	
	$('#update-product-category').click(function(){
		var form = $('#categoryForm');
		var url = form.attr("action");
		var categoryids = [];
		var categorynames = [];
		var i = 0;
		$("#categeroytree :checked").each(function(){
			var id = $(this).attr('value');
			var categoryname = $(this).attr('catgoryname');
			categorynames[i] = categoryname;
			categoryids[i++] = id;
		});
		if (categoryids.length <= 0) {
			alert("请选择品类!");
			return false;
		}
		var listingid = $("input[name^='clistingid']").val();
		if (listingid == "") {
			alert("当前数据错误!");
			return false;
		}
		var data = {};
		var confirmMsg = "是否同时修改具有相同父sku的产品的所属品类为:" + categorynames + "?";
		if (confirm(confirmMsg) == false) {
			data['updateParentSku'] = false;
        } else {
        	data['updateParentSku'] = true;
        }
		data['listingid'] = listingid;
		data['categoryids'] = categoryids;
		$.ajax({
			url : url,
			type : "post",
			data : $.toJSON(data),
			contentType: "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				if (false != data) {
					var alterMsg = "sku:" + data + "修改所属品类为:"+ categorynames +"成功!";
					alert(alterMsg);
					var p = $("#search_form").find("input[name=p]").val();
					var q = $("#search_form").find("input[name=q]").val();
					self.location = js_productRoutes.controllers.manager.Product.baseList(q,p).url;
				} else {
					alert("修改失败!");
				}
			}
		});
	});
	
	$('#update-product-storage').click(function(){
		var form = $('#updateStorageForm');
		var url = form.attr("action");
		var storagesAdd = [];
		var storagenames = [];
		var storagesDel = [];
		var storagenamesDel = [];
		var i = 0;
		var j = 0;
		$("#storages :checked").each(function(){
			if ($(this).hasClass('chooseDefault')) {
				return true;
			}
			var id = $(this).attr('value');
			var storagename = $(this).attr('name');
			storagenames[i] = storagename;
			storagesAdd[i++] = id;
		});
		$("#storages .chooseDefault:unchecked").each(function(){
			var id = $(this).attr('value');
			var storagename = $(this).attr('name');
			storagenamesDel[j] = storagename;
			storagesDel[j++] = id;
		});
		if (jQuery.isEmptyObject(storagesAdd) && jQuery.isEmptyObject(storagesDel)) {
			alert("当前没有做出修改操作!");
			return false;
		}
		var confirmMsg = "是否";
		if (!jQuery.isEmptyObject(storagenames)){
			confirmMsg += "新增发货仓库:" + storagenames + " ";
		}
		if (!jQuery.isEmptyObject(storagenamesDel)){
			confirmMsg += "取消发货仓库:" + storagenamesDel + " ";
		}
		if (confirm(confirmMsg) == false) {
			return false;
        }
		var listingid = form.find("input[name^='listingid']").val();
		var csku = form.find("input[name^='csku']").val();
		if (listingid == "" || csku == "") {
			alert("当前数据错误!");
			return false;
		}
		var data = {};
		data["storagesAdd"] = storagesAdd;
		data["storagesDel"] = storagesDel;
		data["listingid"] = listingid;
		data["csku"] = csku;
		
		$.ajax({
			url : url,
			type : "post",
			data : $.toJSON(data),
			contentType: "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				if (false != data) {
					alert("操作成功!");
					var p = $("#search_form").find("input[name=p]").val();
					var q = $("#search_form").find("input[name=q]").val();
					self.location = js_productRoutes.controllers.manager.Product.baseList(q,p).url;
				} else {
					alert("修改失败!");
				}
			}
		});
	});
	
	$("a[name^='editSalePrice']").each(function(){
		$(this).click(function () {
			var url =$(this).attr('href');
			$('#edit-salePrice-modal .modal-body').html(' ');
			$.get(url, '', function(data){  
		        $('#edit-salePrice-modal .modal-body').html(data);
//		        initDatePlus();
		    })  
		});
		
	});
	
	$(document).ready(function() {
		require(['jquery', 'bootstrap-datetimepicker'], function(){
		});
	});
	
	$("#salePriceForm").validate({
		 submitHandler: function (form) {
			var startTime = new Date($('#saledatestart').val());
            var endTime = new Date($('#saledateend').val());
            if (startTime > endTime) {
            	alert("促销的开始时间大于结束时间,请重新选择");
            	return false;
            }
			var form = $("#salePriceForm");
			var url = form.attr("action");
			var $this = this;
			$.post(url, form.serialize(), function(data) {
				if (data == "true") {
					alert("修改成功!");
					var p = $("#search_form").find("input[name=p]").val();
					var q = $("#search_form").find("input[name=q]").val();
					self.location = js_productRoutes.controllers.manager.Product.baseList(q,p).url;
				} else {
					alert("修改失败!");
				}
			});
		 }
	});
});

$(function(){
	$("#edit-attribute-modal").on("hidden", function() {
	    $(this).removeData("bs.modal");
	});
	
	
	$("#edit-category-modal").on("hidden.bs.modal", function() {
		 $(this).removeData("modal");
	});
	
	$("#edit-baseInfo-modal").on("hidden", function() {
		 $(this).removeData("bs.modal");
	});
	
	$("#edit-salePrice-modal").on("shown", function() {
		$('#saledatestart').datetimepicker({
			　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
			　　autoclose:true //选择日期后自动关闭 
			  }).on('changeDate', function(ev){
				  var startTime = $('#saledatestart').val();
				  $('#saledateend').datetimepicker('setStartDate', startTime);
				  $('#saledatestart').datetimepicker('hide');
			  });
		 $('#saledateend').datetimepicker({
			　　format: "yyyy-mm-dd hh:ii:ss", //选择日期后，文本框显示的日期格式 
			　　autoclose:true //选择日期后自动关闭 
		 }).on('changeDate', function(ev){
			  var endTime = $('#saledateend').val();
			  $('#saledatestart').datetimepicker('setEndDate', endTime);
			  $('#saledateend').datetimepicker('hide');
		  });
	});
	
	$("#edit-saleDetails-modal").on("hidden", function() {
		$(this).removeData("modal");
	});
	
	$("#edit-productCategoryMapper-modal").on("hidden", function() {
		$(this).removeData("modal");
	});
});