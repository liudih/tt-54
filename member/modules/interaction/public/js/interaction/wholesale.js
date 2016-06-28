$(document).ready(function() {
	$("#theform1").validate({
		submitHandler : function(form) {
			tijiao1();
		},
		rules : {
			cname : {
				required : true,
				maxlength:50
			},
			cphone : {
				required : true,
				maxlength:60
			},
			cemail : {
				required : true,
				email : true
			},
			ftargetprice : {
				required : true,
				number : true,
				maxlength:15
			},
			iquantity : {
				required : true,
				digits : true,
				maxlength:9
			},
			ccountrystate : {
				required : true,
				maxlength:100
			},
			ccompany : {
				required : true,
				maxlength:60
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
		               url:theRoutes2.controllers.base.Captcha.checkCaptcha().url,
		               data:{
		            	   captcha:function(){
		            		   return $("#captcha2").val();
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

function tijiao1(){
	var Box = $("#wholesaleBox");
	var form = $("#theform1");
	var a1 = Box.find("input[name='ftargetprice']:eq(0)").val();
	var a2 = Box.find("input[name='cname']:eq(0)").val();
	var a3 = Box.find("textarea[name='cinquiry']:eq(0)").val();
	var a4 = Box.find("input[name='iquantity']:eq(0)").val();
	if(a1=="" || isNaN(a1)){
		alert("Price must be numeric!");
		return false;
	}
	if(a4=="" || isNaN(a4)){
		alert("Quantity must be numeric!");
		return false;
	}
	if(a2=="" || a3==""){
		alert("data is not null!");
		return;
	}
	$.ajax({
		url : theRoutes2.controllers.interaction.Wholesale.addData().url,
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