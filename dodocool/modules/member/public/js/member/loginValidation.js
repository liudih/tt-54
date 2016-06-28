$(document)
		.ready(
				function() {
					$("#loginForm")
							.validate(
									{
										// 执行ajaxsubmit,当表单通过验证，提交表单。回调函数有个默认参数form
										submitHandler : function(form) {// 表单提交句柄,为一回调函数，带一个参数：form
											form.submit();// 提交表单
										},

										rules : {

											email : {
												required : true,
												email : true,
												maxlength : 50
											},

											password : {
												required : true,
												minlength : 6,
												maxlength : 20,
												rangelength : [ 6, 20 ]
											}
										},

										messages : {

											email : {
												required : "Provide a email.",
												email : "The email you've entered is invalid. Please check your email and try again.",
												maxlength : "Please enter a valid email address!"
											},

											password : {
												required : "Provide a password.",
												minlength : jQuery.validator
														.format("Enter at least {0} characters."),
												maxlength : jQuery.validator
														.format("Please enter no more than {0} characters!"),
												rangelength : $.validator
														.format("Please enter a value between {6} and {20} characters long!")
											}
										},

										// 错误提示
										errorClass : "error_style",

										highlight : function(error, element) {
											$(element).addClass("errorInp");
											$(element).siblings('.rightUse')
													.remove();
											$("#regsubmit").addClass('noAgree')
													.attr('disabled', 'true');
										},

										unhighlight : function(element) {
											$(element).next('label').remove();
											$(element).removeClass('errorInp');
										},

										errorPlacement : function(error,
												element) {
											$('#submit').removeClass(
													"log_sinIn");
											$(element).after(error);
										},

										success : function(e, element) {
											$('#submit').addClass("log_sinIn");
											e.removeClass('edit_error')
													.addClass('suceess');
										}

									});

				});

// email限制输入框最多50
jQuery.fn.maxLength = function(max) {
	this
			.each(function() {
				var type = this.tagName.toLowerCase();
				var inputType = this.type ? this.type.toLowerCase() : null;
				if (type == "input" && inputType == "text"
						|| inputType == "password") {
					// Apply the standard maxLength
					this.maxLength = max;
				} else if (type == "textarea") {
					this.onkeypress = function(e) {
						var ob = e || event;
						var keyCode = ob.keyCode;
						var hasSelection = document.selection ? document.selection
								.createRange().text.length > 0
								: this.selectionStart != this.selectionEnd;
						return !(this.value.length >= max
								&& (keyCode > 50 || keyCode == 32
										|| keyCode == 0 || keyCode == 13)
								&& !ob.ctrlKey && !ob.altKey && !hasSelection);
					};
					this.onkeyup = function() {
						if (this.value.length > max) {
							this.value = this.value.substring(0, max);
						}
					};
				}
			});
};
$("#email").maxLength(51);

// password限制输入最多20
jQuery.fn.maxLength = function(max) {
	this
			.each(function() {
				var type = this.tagName.toLowerCase();
				var inputType = this.type ? this.type.toLowerCase() : null;
				if (type == "input" && inputType == "text"
						|| inputType == "password") {
					this.maxLength = max;
				} else if (type == "textarea") {
					this.onkeypress = function(e) {
						var ob = e || event;
						var keyCode = ob.keyCode;
						var hasSelection = document.selection ? document.selction
								.createRange().text.length > 0
								: this.selectionStart != this.selectionEnd;
						return !(this.value.length >= max
								&& (keyCode > 50 || keyCode == 32
										|| keyCode == 0 || keyCode == 13)
								&& !ob.altKey && !hasSelection);
					};
					this.onkeyup = function() {
						if (this.value.length > max) {
							this.value = this.value.substring(0, max);
						}
					};
				}
			});
};
$("#password").maxLength(21);