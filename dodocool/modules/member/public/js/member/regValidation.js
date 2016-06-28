$(document).ready(function() {
	 $("#regForm").validate({
		 submitHandler: function(form){   // 表单提交句柄,为一回调函数，带一个参数：form
			 form.submit(); // 提交表单
	         },
	        
		     rules: {
		    	 firstname: {
					required:true,
					maxlength:50
				},
					
				lastname: {
					required:true,
					maxlength:50
				},
			 
				email: {
					required:true,
					email:true,
					noexist:true,
					maxlength:50
				},
				
				passwd: {
					required: true,
					minlength: 6,
					maxlength: 20,
					rangelength:[6,20],
					dsimple : true,
					wsimple : true,
					Wsimple : true,
// nosame:"#eml"
				},
				
				confirm_password: {
					required: true,
					minlength: 6,
					maxlength: 20,
					equalTo: "#passwd"
				},
				
				captcha:{
					required : true,
					maxlength:5,
					remote:{
			               type:"post",
			               dataType:'json',
			               url:js_emailRoutes.controllers.base.Captcha.checkCaptcha().url,
			               data:{
			            	   captcha:function(){
			            		   return $("#captcha").val();
			            		   }
			            	   }
			               } 
				}
				
		  },
		 
			messages: {
				firstname: {
					required: "This field is required!",
					maxlength:"Please enter a valid email address!"
				} ,

				lastname: {
					required: "This field is required!",
					maxlength:"Please enter a valid email address!"
				} ,
				
				email: {
					required: "This field is required!",
					noexist:'An account already exists for this email address!',
					maxlength:"Please enter a valid email address!"
				} ,
				
				passwd: {
					required: "Provide a password!",
					minlength: jQuery.validator.format("Please enter at least {0} characters!"),
					maxlength: jQuery.validator.format("Please enter no more than {0} characters!"),
					rangelength: $.validator.format("Please enter a value between {6} and {20} characters long!"),
// nosame:"Your password and account information too coincidence, stolen risk,
// please change a password!"
				},
				
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
			// 错误提示
			errorClass: "error_style", 
			highlight: function (element) { 
				// 错误方法调用
				$(element).addClass("errorInp");
				$(element).siblings('.rightUse').remove() ;
				$("#regsubmit").addClass('noAgree').attr('disabled','true');
		     },

		     unhighlight: function (element) { 
		    	// 成功方法调用
		    	$(element).next('label').remove() ;
		    	$(element).removeClass('errorInp');
		     },

			errorPlacement: function(error, element) {
				console.info("errorPlacement!!!!");
			        $(element).after(error);
			 },
			 
			 success: function (e,element) {
				 console.info("success!!!!");
				 $(e).removeClass("edit_error");
                 $("#regsubmit").removeClass('noAgree').removeAttr('disabled');
		     }
	 });
});
// email是否与passwd相等
jQuery.validator.addMethod("nosame",function(value,element){
	var email = $("#eml").val();
	var passwd = $("#passwd").val();
	var l =  $('#l')[0];
	l.style.background="#b1ddff";
	  return this.optional(element) ||(email!=passwd)?true:false;  
});

// 邮箱格式效验
jQuery.validator.addMethod("email",function(value,element){
	var zip = /^([a-zA-Z0-9]+[\_|\-|\.]?)+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,20}){1,20})$/;    
    return this.optional(element) || (zip.test(value));    
  }, "Please enter a valid email address.");  

// email限制输入最多50
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
$("#eml").maxLength(51);

// 限制密码长度20
jQuery.fn.maxLength = function(max){
    this.each(function(){
        var type = this.tagName.toLowerCase();
        var inputType = this.type? this.type.toLowerCase() : null;
        if(type == "input" && inputType == "text" || inputType == "password"){
            // Apply the standard maxLength
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
// 用法
$('#passwd').maxLength(21);

jQuery.fn.maxLength = function(max){
    this.each(function(){
        var type = this.tagName.toLowerCase();
        var inputType = this.type? this.type.toLowerCase() : null;
        if(type == "input" && inputType == "text" || inputType == "password"){
            // Apply the standard maxLength
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
// 用法
$('#confirm_password').maxLength(21);

// 验证密码强度
$('#passwd').keyup(function () {
	　　if ($(this).val() != "") {
	　　　　var strongRegex = new RegExp("^(?=.{6,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
	　　　　var mediumRegex = new RegExp("^(?=.{6,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
	　　　　var enoughRegex = new RegExp("^(?=.{6,}).*", "g");
				 function reset(){
			  	 		$('#l').css('background','#c5c4c4');
			    	    $('#m').css('background','#c5c4c4');
			    	    $('#h').css('background','#c5c4c4');
			     }
				 
	　　　　if  (false == enoughRegex.test($(this).val()) ) {
	　　　　　　// 密码小于六位的时候，密码强度图片都为灰色
							reset();
	　　　　　　 $('#l').css('background','#b1ddff');
	　　　　}
	　　　　else if (strongRegex.test($(this).val())) {
	　　　　　　// 强,密码为八位及以上并且字母数字特殊字符三项都包括
						reset();	
	　　　　　　$('#l').css('background','#b1ddff');
						$('#m').css('background','#3ca5f6');
						$('#h').css('background','#1077c7');
	　　　　}
	　　　　else if (mediumRegex.test($(this).val())) {
	　　　　　　// 中等,密码为七位及以上并且字母、数字、特殊字符三项中有两项，强度是中等
						reset();
	　　　　　　$('#l').css('background','#b1ddff');
					    $('#m').css('background','#3ca5f6');
	　　　　}
					else {
	　　　　　　// 弱,如果密码为6为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的
						reset();
	　　　　　　$('#l').css('background','#b1ddff');
	　　　　}      
	　　}
	});
// 提示密码 '[A-Z]|[a-z]|[0-9]|[!,%,&,@,#,$,/^,/*,/?,_,~]'//Mima
// ^^(?=.*\d)|(?=.*[a-z])|(?=.*[A-Z]).{6,8}$
jQuery.validator.addMethod("dsimple", function(value, element) { 
    var passwd = /[A-Z]|[a-z]|[!,%,&,@,#,$,^,*,?,_,~]/; 
    return passwd.test(value) || this.optional(element); 
}, "The password you entered is too simple.");

jQuery.validator.addMethod("wsimple", function(value, element) { 
    var passwd = /[A~Z]|[0-9]|[!,%,&,@,#,$,^,*,?,_,~]/; 
    return passwd.test(value) || this.optional(element); 
}, "The password you entered is too simple.");

jQuery.validator.addMethod("Wsimple", function(value, element) { 
	var passwd = /[a~z]|[0-9]|[!,%,&,@,#,$,^,*,?,_,~]/; 
	return passwd.test(value) || this.optional(element); 
}, "The password you entered is too simple.");

// ajax获取邮箱是否注册
jQuery.validator.addMethod("noexist", function(value, element) { 
	var s= s || {};
	var flag=null;
	s.url= js_emailRoutes.controllers.member.Register.checkEmail(value).url;
	s.data={};
	s.type='get';
	s.async=false;
	s.success=function(data){
		if(data){
			if(data.errorCode===1){
				flag=false;
			}else if(data.errorCode===0){
				flag=true;
			}
		}
	}
	$.ajax(s);
    var result=this.optional(element) || flag; 
    return       result;
}); 

// captcha限制输入最多5
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
$("#captcha").maxLength(5);











