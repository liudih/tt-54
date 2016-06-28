$(function() {
	init_Ckeditor();
})
$().ready(function() {
	$("#comment_form").validate({
		submitHandler: function(form) {
			formSubmit.call(this,form);
		},
		rules : {
			ctitle : {
				required:true,
				maxlength:600,
			},
			captcha:{
				required : true,
				maxlength:5,
				remote:{
		               type:"post",
		               dataType:'json',
		               url: product_comment.controllers.base.Captcha.checkCaptcha().url,
		               data:{
		            	   captcha:function(){
		            		   return $("#captcha").val();
		            		   }
		            	   }
		               } 
			}
		},
		messages : {
			ctitle : {
				required : "Title is required !",
				maxlength : "Title should no more than {0} characters."
			},
			captcha:{
				required : "Verification code is require !",
				maxlength:jQuery.validator.format("Please enter no more than {0} characters !"),
				remote:"please check your captcha !"
			}
		},
		errorPlacement: function(error, element) {  
		   $(element).parent().next().html(error);
		}
	});
});

function formSubmit(form){
	var textareaId = "comment_textarea";
	var question = CKEDITOR.instances[textareaId].getData();
	var actionUrl = $(form).attr("action");
	if(question == ""){
	   $("#ia_content_error").text("Comment is required");
	   return false;
	} else if(question.length>600){
	   $("#ia_content_error").text("Your comment should no more than 600 characters !");
	   return false;
	}
	$("input[name=cquestion]").val(question);	
	$.ajax({
		url: actionUrl,
		data: $(form).serialize(),
		type:"POST",
		success: function(data){
			if(data.resultCode == "success"){
				location.reload();
			}else {
				alert("save error");
			}
		}
	});
}


