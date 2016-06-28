$(document).ready(function() {
	$("#theform2").validate({
		submitHandler : function(form) {
			tijiao2();
		},
		rules : {
			cemail : {
				required : true,
				email : true,
				maxlength:100
			},
			cinquiry : {
				required : true,
				maxlength:2000
			},
			captcha:{
				required : true,
				maxlength:5,
				remote:{
		               type:"post",
		               dataType:'json',
		               url:theRoutes3.controllers.base.Captcha.checkCaptcha().url,
		               data:{
		            	   captcha:function(){
		            		   return $("#captcha3").val();
		            		   }
		            	   }
		               } 
			}
		},
		messages: {
			cemail: {
				required: "This field is required!",
				maxlength:"Please enter a valid email address!"
			} ,
			captcha:{
				required : "captcha is require!",
				maxlength:jQuery.validator.format("Please enter no more than {0} characters!"),
				remote:"please check your captcha!"
			}
		},
		//错误提示
		errorClass: "edit_error", 
		highlight: function (element) { 
			$(element).removeClass("edit_error");
	     },
	     /*unhighlight: function (element) { 
	    	//成功方法调用
	    	$(element).siblings('label').remove() ;
	    	$(element).after($('<label class="rightUse"></label>'));
	    	$(element).removeClass('errorInp');
	    	//$(element).next('.edit_error').removeClass('edit_error').addClass('');
	     },*/
		errorPlacement: function(error, element) {
			if(element.attr("name")=="captcha"){
				$(element).next().after(error);
			}else{
				$(element).after(error);
			}
		 },
		 success: function (e,element) {
			 $(e).removeClass("edit_error");
	     }
	 });
});

function tijiao2(){
	var Box = $("#reportBox");
	var form = $("#theform2");
//	var a1 = parent.find("input[name='flowerprice']:eq(0)").val();
//	var a2 = parent.find("input[name='csourceurl']:eq(0)").val();
	var a3 = Box.find("textarea[name='cinquiry']:eq(0)").val();
	if(a3==""){
		alert("data is not null!");
		return;
	}
	$.ajax({
		url : theRoutes3.controllers.interaction.ReportError.addData().url,
		type : "POST",
		data: form.serialize(),
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.result == "success") {
				Box.hide();
				form[0].reset();
			}else if(data.result == "wrongcaptcha"){
				form.find(".captchatd .edit_error").remove();
				form.find(".captchatd:eq(0)").append('<label class="edit_error ajaxerr">please check your captcha!</label>');
			}else{
				alert(data.result);
			}
		},
		complete : function() {
			changeCaptcha(form[0]);
		}
	});
}