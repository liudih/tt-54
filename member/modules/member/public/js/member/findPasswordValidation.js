//checkEmail
$(document).ready(function(){
	$("#pform").validate({
		submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
			form.submit();  //提交表单   
		},  
		//错误提示
		errorClass: "suceess e_errorIN",
		rules:{
			email:{
				required: true,
				noexist: true,
				maxlength:50
			}
		},
		
		message:{
			email:{
				required: "This field is required!",
				noexist: "The Email Address was not found in our records; please try again!",
				maxlength:"Please enter a valid email address!"
			},
		     submitHandler: function (form) {
		        
		    	form.submit();
		     }
		},
		
		 
        highlight: function (element) { 
        	$(element).addClass('e_error');
        },
        
		 unhighlight: function (element) { 
			 $(element).removeClass('e_error');
		     },
		     
		     errorPlacement: function(error, element) {
				$(element).after(error);
			 },  
		   
		 //验证通过改变元素和提交按钮的样式
		  success: function (e,element) {
			  e.removeClass('e_errorIN').removeClass('e_error').addClass('suceess');
		     }
	});
});

//email限制输入最多50
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
$("#email").maxLength(51);

//ajax sync validate email is exists!
jQuery.validator.addMethod("noexist", function(value, element) { 
	var s= s || {};
	var flag=null;
	s.url= js_emailRoutes.controllers.member.Register.checkEmail(value).url;
	s.data={};
	s.type='get';
	s.async=false;
	s.success=function(data){
		if(data){
			if(data.errorCode===0){
				flag=false;
			}else if(data.errorCode===1){
				flag=true;
			}
		}
	}
	$.ajax(s);
    var result=this.optional(element) || flag; 
    return       result;
}); 

