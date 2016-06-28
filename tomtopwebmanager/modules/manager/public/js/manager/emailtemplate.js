$(document).on("change","select[name=select_language]",function(){
	var languageId = parseInt($(this).val());
	$("#language_hidden_value").val(languageId);
	emailTemplateFormSubmit();
});

$(document).on("change","select[name=select_website]",function(){
	var websiteId = parseInt($(this).val());
	$("#website_hidden_value").val(websiteId);
	emailTemplateFormSubmit();
});

$(document).on("change","select[name=select_ctype]",function(){
	var type = $(this).val();
	$("#type_hidden_value").val(type);
	emailTemplateFormSubmit();
});

function  emailTemplateFormSubmit(){
	$("#email_template_form").submit();
}

//删除
$(document).on("click","a[name=delete_email_template]",function(){
	var id = $(this).attr("tag");
	var title = $(this).parent().prevAll("td[title=email_template_title]").text();
	var r=confirm("你确定要删除" + title + "吗 ？");
	var data = JSON.stringify({"id":id});
	if (r==true){
		$.ajax({
			url : email_template.controllers.manager.EmailTemplate.deleteEmailTemplate().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode == 0){
					$("#tr_" + id).remove();
				}else if(data.errorCode){
					alert("request error");
				} 
			}
		});
	}else {
		return ;
	}
});

//编辑修改
$(document).on("click","button[id^=update-email-template-content-btn]",function(){
	var id = $(this).attr("tag");
	var ctitle = $("#edit-email-template" + id).find("input[name=ctitle]").val();
	if(null==ctitle || ctitle==""){
		alert("邮件模板标题不能为空");
		return false;
	}
	$("form").submit(function(){
		 for ( instance in CKEDITOR.instances ) {
			 CKEDITOR.instances[instance].updateElement();
		 }
    });
});

//添加邮件模板
$(document).on("click","#add-email-template-content-btn",function(){
	var ctitle = $("#add-new-email-template").find("input[name=ctitle]").val();
	if(null==ctitle || ctitle==""){
		alert("邮件模板标题不能为空");
		return false;
	}
	$("form").submit(function(){
		 for ( instance in CKEDITOR.instances ) {
			 CKEDITOR.instances[instance].updateElement();
		 }
    });
});

$(document).on("click","input[name=show_template_variable]",function(){
	var node = $(this);
	$(this).nextAll("div[name=email_template_variables]").remove();
	var ilanguage = $(this).prevAll("select[name=ilanguage]").find("option:selected").val();;
	var iwebsite = $(this).prevAll("select[name=iwebsiteid]").find("option:selected").val();;
	var ctype = $(this).prevAll("select[name=ctype]").find("option:selected").text();;
	var data =JSON.stringify({"ctype":ctype});
	$.ajax({
		url : email_template.controllers.manager.EmailTemplateVariable.getEmailTemplateVariable().url,
		type : "POST",
		data : data,
		contentType : "application/json",
		success : function(data) {
			var divHtml="<div name='email_template_variables'><table class='table table-striped table-hover table-bordered'><thead><tr><th width='150'>变量</th><th>变量备注</th></thead><tbody>";
			for(var i=0;i<data.length;i++){
				divHtml = divHtml + "<tr><td width='150'>"+data[i].cname+"</td>" + "<td>"+data[i].cremark+"</td></tr>";
			}
			divHtml = divHtml + "</tbody></table></div>"
			node.nextAll("input[name=ctitle]").after(divHtml);
		}
	});
});

$(document).on("click","a[name=page_a]",function(){
	var pageValue = $(this).attr("value");
	var websiteId = $("#website_hidden_value").val();
	var ahref = email_template.controllers.manager.EmailTemplate.manageEmailTemplate(pageValue,websiteId).url; 
	location.href = ahref;
});




