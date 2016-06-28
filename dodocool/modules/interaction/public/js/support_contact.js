$(document).ready(function() {
	 $("#contactForm").validate({
		 submitHandler: function(form){   // 表单提交句柄,为一回调函数，带一个参数：form
			 var url = $(form).attr("action");
			 $.ajax({  
	                    type: 'post',  
	                    url: url ,  
	                    data: $(form).serialize(),
	                    success: function(data){  
	                      if(data.resultCode == "success"){
	                    	  alert("Your request has been successfully submitted, and we will reply as soon as possible.");
	                    	  location.href = "/";
	                      } else {
	                    	  alert("something error ");
	                    	  return fase;
	                      }
	                    }
			 }); // 提交表单
	     },
	        
		     rules: {
		    	 rootCategory: {
					required:true,
					maxlength:50
				},
					
				nextCategory: {
					required:true,
					maxlength:50
				},
				
				clistingid: {
					required:true,
					changeSku:true,
					maxlength:50
				},
				
				ctitle: {
					required:true,
					maxlength:50
				},
				
				cquestion: {
					required:true,
					maxlength:50
				},
				
				cmemberemail: {
					required:true,
					email:true,
					maxlength:50
				},
				
				confirm_email: {
					required:true,
					email:true,
					maxlength:50,
					equalTo: "#cmemberemail"
				}
				
		  },
		 
			messages: {
				rootCategory: {
					required: "Please select something!",
					maxlength:"Please enter a valid email address!"
				} ,
				
				nextCategory: {
					required: "Please select something!",
					maxlength:"Please enter a valid email address!"
				} ,
				
				clistingidx: {
					required: "Please select something!",
					maxlength:"Please enter a valid email address!"
				} ,
				
				ctitle: {
					required: "This field is required!",
					maxlength:"Please enter a valid email address!"
				} ,

				cquestion: {
					required: "This field is required!",
					maxlength:"Please enter a valid email address!"
				} ,
				
				cmemberemail: {
					required: "This field is required!",
					maxlength:"Please enter a valid email address!"
				} ,
				
				confirm_email: {
					required: "Repeat your email!",
					equalTo: "Enter the same email as above!"
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
			     if (element.is(":radio"))
			         error.appendTo(element.parent().next().next());
			     else if (element.is(":checkbox"))
			         error.appendTo(element.next());
			     else 
			    	 $(element).after(error);
			 },
			 
			 success: function (e,element) {
				 $(e).removeClass("edit_error");
                 $("#regsubmit").removeClass('noAgree').removeAttr('disabled');
		     }
	 });
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

jQuery.validator.addMethod("changeSku", function(value, element) { 
	var sku = $('#clistingid').find("option:selected").data('sku');
	if ('' == sku) {
		return false;
	}
	$('#csku').val(sku);
	return true;
	
});

$(document).on("change","#rootCategory",function(){
	var root = $('#rootCategory').val();
	if (root == '') {
		return false;
	}
	var url = contactRoutes.controllers.interaction.Contact.getNextCategory(root).url;
	$.get(url, function(html) {
		$("#nextCategory").replaceWith(html);
	}, "html");
});

$(document).on("change","#nextCategory",function(){
	var root = $('#nextCategory').val();
	if (root == '') {
		return false;
	}
	var url = contactRoutes.controllers.interaction.Contact.getProduct(root).url;
	$.get(url, function(html) {
		$("#clistingid").replaceWith(html);
	}, "html");
});