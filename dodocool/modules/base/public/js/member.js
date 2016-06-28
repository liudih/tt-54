$().ready(function() {

 $("#profile-form").validate({
	 rules : {
		  caccount : {
				minlength: 6,
				maxlength: 50,
				rangelength:[6,50],
				Wname:true
		  },
		  cfirstname : {
				required : true,
				minlength: 6,
				maxlength: 50,
				rangelength:[6,50],
		  		Wname:true
			},
			clastname : {
				required : true,
				minlength: 6,
				maxlength: 50,
				rangelength:[6,50],
				Wname:true
			}
		},
		messages : {
			  caccount : {
					minlength: jQuery.validator.format("Please enter at least {0} characters."),
					maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
					rangelength: $.validator.format("Please enter a value between {6} and {50} characters long.")
			  },
			cfirstname : {
				required : "firstname is required.",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				rangelength: $.validator.format("Please enter a value between {6} and {50} characters long.")
			},
			clastname : {
				required : "lastname is required",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				rangelength: $.validator.format("Please enter a value between {6} and {50} characters long.")
			}
		},
		errorPlacement: function(error, element) {
			$("#profile_submit").attr('disabled', true);
	        var errorMsg=$(error).html();
	        $(element).next().html(errorMsg);
	     },
		success : function() {
			$("#profile_submit").attr('disabled', false);
		}
    });
 
 //验证修改密码
 $("#password-form").validate({
	focusInvalid : false,
	 rules : {
			cpassword : {
				required : true,
				minlength: 6,
				maxlength: 20,
				rangelength:[6,20]
			},
			cnewpassword: {
				required: true,
				minlength: 6,
				maxlength: 20,
				rangelength:[6,20],
				dsimple : true,
				wsimple : true
			},
			cpasswd : {
				required: true,
				minlength: 6,
				maxlength: 20,
				equalTo:"#cnewpassword"
			}
		},
		messages : {
			cpassword : {
				required : "Password is required.",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				rangelength: $.validator.format("Please enter a value between {6} and {20} characters long.")
			},
			cnewpassword : {
				required: "Provide a password.",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				rangelength: $.validator.format("Please enter a value between {6} and {20} characters long.")
			},
			cpasswd : {
				required: "Repeat your password.",
				minlength: jQuery.validator.format("Please enter at least {0} characters."),
				maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
				equalTo: "Please enter the same password."
			}
		},
		errorPlacement: function(error, element) {
			$('#result_message').html("");
			$("#psubmits").attr('disabled', true);
	        var errorMsg=$(error).html();
	        $(element).next().html(errorMsg);
	     },
		success : function() {
			$("#psubmits").attr('disabled', false);
		},
		submitHandler:function(form){
			var form = $("#password-form");
			var url = form.attr("action");
			var $this = this;
			$.post(url, form.serialize(), function(data) {			
				var message = "";
				if(data['result'] == 3) {
					message = data.errorMessage;
					$('#result_message').html(message);
				} else if (data['result'] == 2) {
					message = data.errorMessage;
					$('#result_message').html(message);
				}else {
					 var url = updatepasswd.controllers.member.Login.login().url;
					 window.location = url;
				}
			}, "json");
		}
    });

 	//使用正则表达式验证密码
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
 
	 
	// 字母和数字的验证  
	 jQuery.validator.addMethod("Wname", function(value, element) {  
	     var chrnum = /^([a-zA-Z0-9_]+)$/;  
	     return this.optional(element) || (chrnum.test(value));  
	 }, "Can only enter numbers and letters and underscores."); 
});