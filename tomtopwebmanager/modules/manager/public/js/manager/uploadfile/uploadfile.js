define(['jquery','jqueryjson','jqueryform'],function($){
	$("#filepath").change(function(){
		var value = $(this).val();
		$("#path").val(value);
	});
	
	$('#uploadfiles').on('submit',function(){
		var _url= $(this).attr('action');
		$('#result').empty();
		$('#result').append("导入中...");
		$(this).ajaxSubmit({  
            type:"post",  //提交方式  
            dataType:"html", //数据类型  
            url:_url, //请求url  
            error:function(err){
            	alert('保存失败');
            	$('#result').empty();
            	$('#result').append("导入失败！");
            },
            success:function(data){ //提交成功的回调函数  
            	$('#result').replaceWith(data);
            	$('#uploadfiles').resetForm();
            }  
        });
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
	
	$('#addFilePath').submit(function(){
		var form = $("#addFilePath");
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if (data['fail'] != null) {
				alert('当前路径已经存在!!');
			} else if (data['result'] == true) {
				alert('保存成功!');
				var url = uploadfileRoutes.controllers.manager.UploadFilePath.uploadFilePathManager().url;
				window.location = url;
			} else {
				alert('保存失败!');
			}
		}, "json");
		return false;
	}); 
	
	$('#fileMsg a.delete').on('click', function () {
		if (confirm("是否删除该文档信息?") == false) {
            return;
        } 
        var fileId = $(this).data('id');
        var url = uploadFileRoutes.controllers.manager.UploadFile.deleteUploafile(fileId).url;
        $.ajax({
			url : url,
			type : "post",
			success : function(data) {
				if (data['result'] == true) {
					alert("删除成功!");
					searchSubmit();
				} else {
					alert("删除失败，请重新操作!");
				}
			}
		});
    });
    
    $('#pathtable a.delete').on('click', function () {
		if (confirm("是否删除该路径信息?") == false) {
            return;
        } 
        var iid = $(this).data('id');
        var url = uploadfileRoutes.controllers.manager.UploadFilePath.deleteUploadFilePath(iid).url;
        $.ajax({
			url : url,
			type : "post",
			success : function(data) {
				if (data['result'] == true) {
					alert("删除成功!");
					var url = uploadfileRoutes.controllers.manager.UploadFilePath.uploadFilePathManager().url;
					window.location = url;
				} else {
					alert("删除失败，请重新操作!");
				}
			}
		});
    });
    
    $(document).on("click","#attachment a.delete",function(){
        	if (confirm("是否删除该文档信息?") == false) {
                return;
            } 
            var iid = $(this).data('id');
        	var url = uploadFileRoutes.controllers.manager.attachment.AttachmentManager.deleteAttachment(iid).url;
            $.ajax({
    			url : url,
    			type : "delete",
    			success : function(data) {
    				if (data["fair"] != null) {
    					alert(data["fair"]);
    				}
    				if (data['result'] == true) {
    					alert("删除成功!");
    					searchSubmit();
    				} else {
    					alert("删除失败，请重新操作!");
    				}
    			}
    		});
    })
    
});
/*
function deletePath(lid) {
	if (confirm("是否删除该文档信息?") == false) {
        return;
    } 
    var url = uploadFileRoutes.controllers.manager.download.UploadDownloadFile.deleteUploaPath(lid).url;
    $.ajax({
		url : url,
		type : "delete",
		success : function(data) {
			if (data['result'] == true) {
				alert("删除成功!");
			} else {
				alert("删除失败，请重新操作!");
			}
		}
	});
}*/
