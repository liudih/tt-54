$(document).ready(function() {
	$("#scores").css({"width":"100%"});
	$(".product_Reviews em").click(function () {
		var index = $(this).index()+1;
		var n = $(this).parent();
		if(n.attr("id") == "price"){
			$("#iprice").val(index);
		}else if(n.attr("id") == "quality"){
			$("#iquality").val(index);
		}else if(n.attr("id") == "usefulness"){
			$("#iusefulness").val(index);
		}else if(n.attr("id") == "shipping"){
			$("#ishipping").val(index);
		}
		var price = $("#iprice").val()*1;
		var quality = $("#iquality").val()*1;
		var usefulness = $("#iusefulness").val()*1;
		var shipping = $("#ishipping").val()*1;
		var wdes=price+quality+usefulness+shipping; 
		var wds = wdes*5;
		$("#scores").css({"width":wds+"%"});
		var wd = wdes/4;
		$("#foverallrating").val(wd);
	 
	});
	function submitform(){
		var form = $("#writeReviewForm");
		$.ajax({
			url: form.attr("action"),
			dataType: "json",
			type:"post",
			data: form.serialize(),
			success: function(data){
				if(data.result=="success"){
					pophtml("Success","Success!");
				}
				if(data.result=="no-order"){
					pophtml("Error","You have commented!");
				}
				var u = $("#backurl").attr("data");
				location.href = u;
			}
		});
	}
	$("#writeReviewForm").validate({
		submitHandler: function(form){
			submitform();
		},
		rules: {
			nickName:{required:true, maxlength:20, isNickName:true},
	    	ccomment:{required:true, minlength:20, maxlength:2000},
			captcha:{
				remote:{
				type:"POST",
				dataType:'json',
	            url:js_reviewRoutes.controllers.base.Captcha.checkCaptcha().url,
	            data:{
	            	    captcha:function(){
	            	    	return $("#captcha").val();
	            	    }
	                }
	            },
				required:true,
				maxlength:5
			},
			
			maxlength:{
				maxlength:600
			},
			
			videoTitle:{
				maxlength:50
			}
	  },
	  messages: {
			nickName:{
				required: "Please enter an name!",
				maxlength:jQuery.validator.format("Please enter no more than {0} characters!")
			},
			
			ccomment:{
				required:"This reviews is required!",
				minlength:jQuery.validator.format("At least {0} characters")
			},
			
			captcha:{
				remote:"please check your captcha!",
				required:"captcha is require!",
				maxlength:jQuery.validator.format("Please enter no more than {0} characters!")
			},
			
			videoUrl:{
				maxlength:jQuery.validator.format("Please enter no more than {0} characters!")
			},
			
			videoTitle:{
				maxlength:jQuery.validator.format("Please enter no more than {0} characters!")
			}
		},
		// 错误提示
		errorClass: "edit_error", 
		highlight: function (element) { 
			// 错误方法调用
			$(element).siblings('.rightUse').remove() ;
			$("#regsubmit").addClass('noAgree').attr('disabled','true');
	     },

	     unhighlight: function (element) { 
	    	$(element).siblings('label').remove() ;
	    	$(element).next('.edit_error').removeClass('edit_error').addClass('');
	     },

		errorPlacement: function(error, element) {
		        $(element).after(error);
		 },
		 
		 success: function (e,element) {
			 $(e).removeClass("edit_error");
             // $("#regsubmit").removeClass('noAgree').removeAttr('disabled');
	     }
	});
		　
});

//nickName格式效验 
jQuery.validator.addMethod("isNickName",function(value,element){
	return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);   
}, "Please enter the correct format!");

jQuery.fn.maxLength = function(max){
	this.each(function(){
		var type = this.tagName.toLowerCase();
		var inputType = this.type? this.type.toLowerCase() : null;
		if(type == "input" && inputType == "text" || inputType == "password"){
			this.maxLength = max;
		}
		else if(type == "textarea"){
			this.onkeypress = function(e){
				var ob = e || event;
				var keyCode = ob.keyCode;
				var hasSelection = document.selection? document.selection.createRange().text.length > 0 : this.selectionStart != this.selectionEnd;
				return !(this.value.length >= max && (keyCode > 50 || keyCode == 32 || keyCode == 0 || keyCode == 13) && !ob.ctrlKey && !ob.altKey && !hasSelection);
			};
			this.onkeyup = function(){
				if(this.value.length > max){
					this.value = this.value.substring(0,max);
				}
			};
		}
	});
};
$("#nickName").maxLength(21);
$("#ccomment").maxLength(2001);
$("#captcha").maxLength(5);
$("#videoTitle").maxLength(51);
$("#videoUrl").maxLength(601);

