define(['jquery','../imagepreview','jvalidate','jmetadata','jqueryjson'],function($, imagepreview){
	var imageObj = new imagepreview();
	$.metadata.setType("attr", "validate");
	
	// 图片类型验证     
	jQuery.validator.addMethod("isImage", function(value, element) {     
	    var tel = /.(gif|jpg|jpeg|png|gif|jpg|png)$/;  
	    return this.optional(element) || (tel.test(value));  
	}, "图片类型必须是.gif,jpeg,jpg,png中的一种"); 
	
	/*$("#submitChoose").click(function(){
		var websiteid = $("#websiteid").val();
		var languageid = $("#languageid").val();
		$("#categorytree_modle").empty();
		$("#categoryedit_modle").empty();
		var url =categoryRoutes.controllers.manager.Category.getCategoryList(websiteid, languageid).url;
		$.get(url, function(data){
			$("#categorytree_modle").replaceWith(data);
			initTree(); 
			getCategoryManagerChoose();
		}, "html");
	});*/
	
	$('#websiteid').change(function(){
		getCategoryTree();
	});
	
	$('#languageid').change(function(){
		getCategoryTree();
	});
	
	$('.isImage').change(function(){
		var id = $(this).attr('id');
		imageObj.validateImage(this, 'prv' + id);
	})
	
	function getCategoryTree() {
		$('#category_manager_choose').hide();
		var websiteid = $("#websiteid").val();
		var languageid = $("#languageid").val();
		var url =categoryRoutes.controllers.manager.Category.getCategoryList(websiteid, languageid).url;
		$("#categorytree_modle").empty();
		$("#categoryedit_modle").empty();
		if ("" != websiteid && "" != languageid) {
			$("#categorytree_modle").append("<span>请稍候..........</span>");
			$.get(url, function(data){
				$("#categorytree_modle").replaceWith(data);
				getCategoryManagerChoose();
			}, "html");
		} else {
			$("#categorytree_modle").append("<span>当前选择没有类别</span>");
		}
	}
	
	function getCategoryManagerChoose(){
		$("#categorytree").find("li").find('span[class="title"]').bind("click", function(){
			var id = $(this).attr("id");
			var languageid = $("#languageid").val();
			$('#category_manager_choose').show();
			$("#categoryedit_modle").empty();
			$('#category_message_manager').unbind('click');
			$('#category_message_manager').on('click',function(){
				getCategoryMessage(id, languageid);
			});
			$('#category_attribute_manager').unbind('click');
			$('#category_attribute_manager').on('click',function(){
				getCategoryAttributeManager(id, languageid);
			});
		});
	}
	
	function getCategoryMessage(id, languageid){
		$('#category_manager_choose').hide();
		var languageid = $("#languageid").val();
		var websiteid = $("#websiteid").val();
		var url = categoryRoutes.controllers.manager.Category.categoryEdit(id, languageid, websiteid).url;
		window.open(url,"_blank");
		$("#categoryForm").validate();
	}
	
	function getCategoryAttributeManager(id, languageid) {
		var url = categoryRoutes.controllers.manager.Category.getCategoryAttributeManager(id, languageid).url;
		window.open(url,"_blank");
	}
	
	function bindClickEventToCategorySumbit(){
		$("#categorySubmit").click(function(){
			var $form = $("#categoryForm");
			var url = $form.attr('action');
			$.post(url, $form.serializeArray(), function(data){
				alert("success");
			});
		});
	}
	
	$("#iposition").keyup(function() {
		var value = $(this).val();
		value = value.match(/\d+/);
		if (value != null && value != 0) {
			value = parseInt(value);
		}
		$(this).val(value);
	});
	
	$("#categoryForm").validate();
	
	$(".number").focusout(function() {
		var value = $(this).val();
		value = value.match(/^-?\d+$/);
		if (value != null && value != 0) {
			value = parseInt(value);
		}
		$(this).val(value);
	});
	
	$("form").submit(function(){
		$("#categoryForm").validate(); 
		 for ( instance in CKEDITOR.instances ) {
			 CKEDITOR.instances[instance].updateElement();
//				 var data = CKEDITOR.instances[instance].getData();
//				 if (data == "") {
//					 return false;
//				 }
		 }
			    
    });
});

