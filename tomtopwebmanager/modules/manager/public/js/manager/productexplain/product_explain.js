define(['jquery','jqueryjson'],function($){
	$('#iwebsiteid').change(function(){
		getProductExplainMsg();
	});
	
	$('#ilanguageid').change(function(){
		getProductExplainMsg();
	});
	
	function getProductExplainMsg() {
		$('#productmanager_modle').show();
		var websiteid = $("#iwebsiteid").val();
		var languageid = $("#ilanguageid").val();
		var url = productExplainRoutes.controllers.manager.ProductExplain.productExplainEdit(websiteid, languageid).url;
		if ("" != websiteid && "" != languageid) {
			$.get(url, function(data){
				$("#productExplainEdit_modle").replaceWith(data);
				onsubmitForm();
				deleteProductExplain();
			}, "html");
		} else {
			$("#productExplainEdit_modle").empty();
		}
	};
	
	function onsubmitForm(){
		$("button[id^=productExplainForm_update]").click(function(){
			var id = $(this).data('id');
			var form = $("#update-product_explain" + id).find('form');
			var siteId = form.find("select[name='iwebsiteid']").val();
			var ilanguageid= form.find("select[name='ilanguageid']").val();
			var ctype = form.find("select[name='ctype']").val();
			if(siteId =="" || ilanguageid == "" || ctype == ""){
				alert("数据不能为空");
				return false;
			}
			for ( instance in CKEDITOR.instances ) {
				 CKEDITOR.instances[instance].updateElement();
			}
			var url = form.attr("action");
			$.post(url, form.serialize(), function(html) {
				if (jQuery.isEmptyObject(html)) {
					alert("保存失败!");
					return false;
				}
				alert("保存成功!");
				$("#productExplainEdit_modle").replaceWith(html);
				onsubmitForm();
				deleteProductExplain();
			}, "html");
	    });
	};
	
	$('#productExplainForm_add').click(function() {
		var form = $("#add-product_explain").find('form');
		var siteId = form.find("select[name='iwebsiteid']").val();
		var ilanguageid= form.find("select[name='ilanguageid']").val();
		var ctype = form.find("select[name='ctype']").val();
		if(siteId =="" || ilanguageid == "" || ctype == ""){
			alert("数据不能为空");
			return false;
		}
		for ( instance in CKEDITOR.instances ) {
			 CKEDITOR.instances[instance].updateElement();
		}
		var url = form.attr("action");
		$.post(url, form.serialize(), function(html) {
			if (jQuery.isEmptyObject(html)) {
				alert("保存失败!");
				return false;
			}
			alert("保存成功!");
			$('#add-product_explain').hide();
			$("#productExplainEdit_modle").replaceWith(html);
			onsubmitForm();
			deleteProductExplain();
		}, "html");
	});
	
	//删除
	function deleteProductExplain(){
		$('a[name=delete_product_explain]').on("click", function(){
			var id = $(this).data('id');
			console.info(id);
			var data = JSON.stringify({"id":id});
			if (confirm("确定删除??")){
				$.ajax({
					url : productExplainRoutes.controllers.manager.ProductExplain.productExplainDelete().url,
					type : "POST",
					data : data,
					contentType : "application/json",
					success : function(data) {
						if(data.result == true){
							alert("操作成功!!");
							getProductExplainMsg();
						}else{
							alert("操作失败!!");
						} 
					}
				});
			}else {
				return ;
			}
		});
	}
});

