$(document).ready(function() {
	$("#theform").validate({
		 submitHandler: function(form){
			 tijiao();
         },
	     rules: {
	    	 flowerprice: {
				required:true,
				number:true,
				maxlength:12
			},
			csourceurl:{
				required:true,
				url:true,
				maxlength:200
			},
			cemail:{
				required:true,
				email:true,
				maxlength:100
			},
			cinquiry:{
				required:true,
				maxlength:2000
			},
			captcha:{
				required : true,
				maxlength:5,
				remote:{
		               type:"post",
		               dataType:'json',
		               url:theRoutes.controllers.base.Captcha.checkCaptcha().url,
		               data:{
		            	   captcha:function(){
		            		   return $("#captcha-pricematch").val();
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
			confirm_password: {
				required: "Repeat your password!",
				minlength: jQuery.validator.format("Please enter at least {0} characters!"),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters!"),
				equalTo: "Enter the same password as above!"
			},
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

function tijiao(){
	var form = $("#theform");
	$.ajax({
		url : theRoutes.controllers.interaction.PriceMatch.addData().url,
		type : "POST",
		data: form.serialize(),
		dataType : "json",
		async : true,
		success : function(data) {
			if (data.result == "success") {
				$("#pricematchBox").hide();
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

$("input[name='captcha']").keydown(function(){
	$(this).parent().find(".ajaxerr:eq(0)").remove();
});
