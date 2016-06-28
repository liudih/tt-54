
define(['jquery','jvalidate','jmetadata','jqueryjson'],function($){
	$('#search_title').on('click', function(){
		var languageId = $('#ilanguage').val();
		var titile = $('#title').val();
		var url = attachmentMapper.controllers.manager.attachment.ProductAttachment.getDescByLanguageIdAndTitle(languageId, titile).url;
		$.get(url, function(data){
			$("#attachmentDescs").replaceWith(data);
		}, "html");
	});
	
	$(document).on("click","#attachmentMapper a.delete",function(){
    	if (confirm("是否删除该映射信息?") == false) {
            return;
        } 
        var iid = $(this).data('id');
    	var url = attachmentMapper.controllers.manager.attachment.ProductAttachment.deleteProductAttachmentMapper(iid).url;
        $.ajax({
			url : url,
			type : "delete",
			success : function(data) {
				if (data['result'] == true) {
					alert("删除成功!");
					searchSubmit();
				} else {
					alert("删除失败，请重新操作!");
				}
			}
		});
	})
	
    $(document).on("click","[name=iattachmentdescid]:checkbox",function(){
		if ($(this).attr("check")) {
            $(this).removeAttr("check");
        } else {
            $(this).attr("check", true);
        }
	});
    
    $('#attachmentMapperEdit').submit(function(){
		var form = $("#attachmentMapperEdit");
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if (data['fail'] != null) {
				alert(data['fail']);
			} else if (data['result'] == 'true') {
				alert('保存成功!');
				
			} else {
				alert('保存失败!');
			}
		}, "json");
		return false;
	}); 
    
    $("#search_file_submit").click(function(){
		searchSubmit();
	});
	
	$("a[tag=pageSize]").click(function(){
		changePageSize(this);
		searchSubmit();
	});
	
	$("a[tag=pageNum]").click(function(){
		changePageNum(this);
		searchSubmit();
	});
	
	$("#select_file__use_for").on("change",function(){
		var value=$(this).val();
		$("input[name=clabel]").val(value);
	});
	
	function searchSubmit() {
		var form = $("#searchFile");
		var url = form.attr("action");
		$.post(url, form.serialize(), function(html) {
			$("#search_file_html").replaceWith(html);
			$("#searchFile").on("submit", function(){
				return false;
			});
			$("a[tag=pageNum]").click(function(){
				changePageNum(this);
				searchSubmit();
			});
		}, "html");
	}
	
	function changePageSize (e) {
		$("a[tag=pageSize]").attr("href", "javascript:;");
		$("a[tag=pageSize]").removeAttr("class");
		$(e).attr("class", "order_ll_aActi");
		$(e).removeAttr("href");
		var status = $(e).attr("value");
		$("input[name=pageSize]").val(status);
	}
	
	function changePageNum (e) {
		$("a[tag=pageNum]").attr("href", "javascript:;");
		$("a[tag=pageNum]").removeAttr("class");
		$(e).attr("class", "showAc");
		$(e).removeAttr("href");
		var status = $(e).attr("value");
		$("input[name=pageNum]").val(status);
	}
});
