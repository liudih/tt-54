//checkEmail
$(document).ready(function(){
	$("#resetForm").validate({
		submitHandler: function(form){   //表单提交句柄,为一回调函数，带一个参数：form   
			form.submit();  //提交表单   
		},  
		
		rules:{
			passwd: {
				required: true,
				minlength: 6,
				maxlength: 20,
				rangelength:[6,20],
				dsimple : true,
				wsimple : true,
				Wsimple : true
			},
			
			confirm_password: {
				required: true,
				minlength: 6,
				maxlength: 20,
				equalTo: "#passwd"
			}
	  },
	 
		messages: {
			passwd: {
				required: "Provide a password",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				rangelength: $.validator.format("Please enter a value between {6} and {20} characters long.")
			},
			
			confirm_password: {
				required: "Repeat your password",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				equalTo: "Enter the same password as above."
			}
		},
		
		//错误提示
		errorClass:'edit_error',
		validClass:'rightUse',
		highlight: function (element) { 
			//错误方法调用
			$(element).addClass("errorInp");
			$(element).siblings('.rightUse').remove() ;
			//$(element).next('.rightUse').removeClass('rightUse').addClass('edit_error');
	     },

	     unhighlight: function (element) { 
	    	//成功方法调用
	    	 	$(element).siblings('label').remove() ;
		    	$(element).after($('<label class="rightUse"></label>'));
	    	$(element).removeClass('errorInp');
//	    	$(element).next('.edit_error').removeClass('edit_error').addClass('rightUse');
	     },

		errorPlacement: function(error, element) {
			$(element).after(error);
		 },
		 
		 success: function (e,element) {
			  $('#resetsubmit').removeAttr('disabled').removeClass('noAgree');
	     }
	});
});
//提示密码 '[A-Z]|[a-z]|[0-9]|[!,%,&,@,#,$,/^,/*,/?,_,~]'//Mima ^^(?=.*\d)|(?=.*[a-z])|(?=.*[A-Z]).{6,8}$
jQuery.validator.addMethod("dsimple", function(value, element) { 
    var passwd = /[A-Z]|[a-z]|[!,%,&,@,#,$,^,*,?,_,~]/; 
    return passwd.test(value) || this.optional(element); 
}, "The password you entered is too simple.");
jQuery.validator.addMethod("wsimple", function(value, element) { 
    var passwd = /[A-Z]|[0-9]|[!,%,&,@,#,$,^,*,?,_,~]/; 
    return passwd.test(value) || this.optional(element); 
}, "The password you entered is too simple.");
jQuery.validator.addMethod("Wsimple", function(value, element) { 
    var passwd = /[0-9]|[a-z]|[!,%,&,@,#,$,^,*,?,_,~]/; 
    return passwd.test(value) || this.optional(element); 
}, "The password you entered is too simple.");

//限制密码长度20
jQuery.fn.maxLength = function(max){
    this.each(function(){
        var type = this.tagName.toLowerCase();
        var inputType = this.type? this.type.toLowerCase() : null;
        if(type == "input" && inputType == "text" || inputType == "password"){
            //Apply the standard maxLength
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
//用法
$('#passwd').maxLength(21);

jQuery.fn.maxLength = function(max){
    this.each(function(){
        var type = this.tagName.toLowerCase();
        var inputType = this.type? this.type.toLowerCase() : null;
        if(type == "input" && inputType == "text" || inputType == "password"){
            //Apply the standard maxLength
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
//用法
$('#confirm_password').maxLength(21);

//验证密码强度
$("#passwd").keyup(function () {
	　　if ($(this).val() != "") {
	　　　　var strongRegex = new RegExp("^(?=.{6,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
	　　　　var mediumRegex = new RegExp("^(?=.{6,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
	　　　　var enoughRegex = new RegExp("^(?=.{6,}).*", "g");
	
				 function reset(){
//			  	 		$('#l').css('background','#c5c4c4');
//			    	    $('#m').css('background','#c5c4c4');
//			    	    $('#h').css('background','#c5c4c4');
			     }
				 
	　　　　if (false == enoughRegex.test($(this).val())) {
	　　　　　　//密码小于六位的时候，密码强度图片都为灰色
							reset();
//	　　　　　　 $('#l').css('background','#b1ddff');
							$('#l').addClass('highFirst');
	　　　　}
	　　　　else if (strongRegex.test($(this).val())) {
	　　　　　　//强,密码为八位及以上并且字母数字特殊字符三项都包括
						reset();	
						$('#l').addClass('highFirst');
//	　　　　　　$('#l').css('background','#b1ddff');
						$('#m').css('background','#3ca5f6');
						$('#h').css('background','#1077c7');
	　　　　}
	　　　　else if (mediumRegex.test($(this).val())) {
	　　　　　　//中等,密码为七位及以上并且字母、数字、特殊字符三项中有两项，强度是中等
						reset();
//	　　　　　　$('#l').css('background','#b1ddff');
						$('#l').addClass('highFirst');
						$('#m').addClass('highCenter');
//					    $('#m').css('background','#3ca5f6');
	　　　　}
					else {
	　　　　　　//弱,如果密码为6为及以下，就算字母、数字、特殊字符三项都包括，强度也是弱的
						reset();
//	　　　　　　$('#l').css('background','#b1ddff');
						$('#l').addClass('highFirst');
	　　　　}      
	　　}
	});
