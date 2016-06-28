	 jQuery.validator.addMethod("same", function(value, element) {  
         return this.optional(element) || same(value);  
     }, "you input the same passowrd!");  

     function same(pwd) {  
         var oldPwd = $("#cpassword").val();  
         if (oldPwd == pwd)  
             return false;  
         else  
             return true;  
     }  
	var options={
			ignore : "",
			focusInvalid : false,
			errorClass : "edit_error",
			errorElement : "label",
			rules : {
				cpassword : {
					required : true,
					remote:{
			               type:"post",
			               dataType:'json',
			               url:passwordRoutes.controllers.member.Register.translatePassword().url,
			               data:{
			            	   cpassword:function(){
			            		   return $("#cpassword").val();
			            		   }
			            	   }
			               } 
					
				},
				cnewpassword: {
					required: true,
					minlength: 6,
					maxlength: 20,
					rangelength:[6,20],
					dsimple : true,
					wsimple : true,
					Wsimple : true,
					same:true  
				},
				ccnewpassword : {
					required: true,
					minlength: 6,
					maxlength: 20,
					equalTo:"#cnewpassword"
				},
				captcha:{
					required : true,
					remote:{
			               type:"post",
			               dataType:'json',
			               url:passwordRoutes.controllers.base.Captcha.checkCaptcha().url,
			               data:{
			            	   captcha:function(){
			            		   return $("#findpwd-captcha").val();
			            		   }
			            	   }
			               } 
				}
				
			},
			messages : {
				cpassword : {
					required : "password is required.",
					remote : "your password is error."
				},
				cnewpassword : {
					required: "Provide a password",
					minlength: jQuery.validator.format("Please enter at least {0} characters."),
					maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
					rangelength: $.validator.format("Please enter a value between {6} and {20} characters long.")
				},
				ccnewpassword : {
					required: "Repeat your password",
					minlength: jQuery.validator.format("Please enter at least {0} characters."),
					maxlength: jQuery.validator.format("Please enter no more than {0} characters."),
					equalTo: "Enter the same password as above."
				},
				captcha:{
					required : "captcha is required.",
					remote:"please check your captcha"
				}
			},
			highlight : function( element,error) {
				$(element).addClass("edit_INerror");
				var $eleform=$(element).parents('.form-element');
				$eleform.removeClass('rights');
				$eleform.addClass('error');
			},
			errorPlacement: function(error, element) {
				$("#psubmit").attr('disabled', true);
		        var errorMsg=$(error).html();
		        $(element).parents('.form-element').find('span.edit_error').html(errorMsg);
		     },
			unhighlight : function(element, error, valid) {
				$(element).removeClass("edit_INerror");
				var $eleform=$(element).parents('.form-element');
				$eleform.removeClass('error');
				$eleform.addClass('rights');
			},
			success : function() {
				$("#psubmit").attr('disabled', false);
			}
		};
	$('#password-form').validate(options);
	var profile_options={
			ignore : "",
			focusInvalid : false,
			errorClass : "edit_error",
			errorElement : "label",
			rules : {
				cfirstname : {
					required : true
					
				},
				clastname : {
					required : true
				}
				
			},
			messages : {
				cfirstname : {
					required : "firstname is required."
				},
				clastname : {
					required : "lastname is required"
				}
			},
			highlight : function(error, element) {
				
				$(element).addClass("edit_INerror");
			},

			errorPlacement : function(error, element) {
				
				$("#profile_submit").attr('disabled', true);
				$(element).addClass("edit_INerror");
				$(element).after(error);
			},
			unhighlight : function(element, error, valid) {
				
				$(element).removeClass("edit_INerror");
				$(element).next('.edit_error').remove();
			},
			success : function() {
				$("#profile_submit").attr('disabled', false);
			}
		};
 $('#profile-form').validate(profile_options);
 $(document).ready(function () {
     var myDate = new Date();
     $("#dateSelector").DateSelector({
             ctlYearId: 'idYear',
             ctlMonthId: 'idMonth',
             ctlDayId: 'idDay',
             defYear: myDate.getFullYear(),
             defMonth: (myDate.getMonth()+1),
             defDay: myDate.getDate(),
             minYear: 1800,
             maxYear: (myDate.getFullYear())
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
 
 (function ($) {
	    //SELECT控件设置函数
	    function setSelectControl(oSelect, iStart, iLength, iIndex) {
	        oSelect.empty();
	        for (var i = 0; i < iLength; i++) {
	            if ((parseInt(iStart) + i) == iIndex)
	                oSelect.append("<option selected='selected' value='" + (parseInt(iStart) + i) + "'>" + (parseInt(iStart) + i) + "</option>");
	            else
	                oSelect.append("<option value='" + (parseInt(iStart) + i) + "'>" + (parseInt(iStart) + i) + "</option>");
	        }
	    }
	    $.fn.DateSelector = function (options) {
	        options = options || {};
	 
	        //初始化
	        this._options = {
	            ctlYearId: null,
	            ctlMonthId: null,
	            ctlDayId: null,
	            defYear: 0,
	            defMonth: 0,
	            defDay: 0,
	            minYear: 1882,
	            maxYear: new Date().getFullYear()
	        }
	 
	        for (var property in options) {
	            this._options[property] = options[property];
	        }
	 
	        this.yearValueId = $("#" + this._options.ctlYearId);
	        this.monthValueId = $("#" + this._options.ctlMonthId);
	        this.dayValueId = $("#" + this._options.ctlDayId);
	 
	        var dt = new Date(),
	        iMonth = parseInt(this.monthValueId.attr("data") || this._options.defMonth),
	        iDay = parseInt(this.dayValueId.attr("data") || this._options.defDay),
	        iMinYear = parseInt(this._options.minYear),
	        iMaxYear = parseInt(this._options.maxYear);
	                 
	        this.Year = parseInt(this.yearValueId.attr("data") || this._options.defYear) || dt.getFullYear();
	        this.Month = 1 <= iMonth && iMonth <= 12 ? iMonth : dt.getMonth() + 1;
	        this.Day = iDay > 0 ? iDay : dt.getDate();
	        this.minYear = iMinYear && iMinYear < this.Year ? iMinYear : this.Year;
	        this.maxYear = iMaxYear && iMaxYear > this.Year ? iMaxYear : this.Year;
	 
	        //初始化控件
	        //设置年
	        setSelectControl(this.yearValueId, this.minYear, this.maxYear - this.minYear + 1, this.Year);
	        //设置月
	        setSelectControl(this.monthValueId, 1, 12, this.Month);
	        //设置日
	        var daysInMonth = new Date(this.Year, this.Month, 0).getDate(); //获取指定年月的当月天数[new Date(year, month, 0).getDate()]
	        if (this.Day > daysInMonth) { this.Day = daysInMonth; };
	        setSelectControl(this.dayValueId, 1, daysInMonth, this.Day);
	 
	        var oThis = this;
	        //绑定控件事件
	        this.yearValueId.change(function () {
	            oThis.Year = $(this).val();
	            setSelectControl(oThis.monthValueId, 1, 12, oThis.Month);
	            oThis.monthValueId.change();
	        });
	        this.monthValueId.change(function () {
	            oThis.Month = $(this).val();
	            var daysInMonth = new Date(oThis.Year, oThis.Month, 0).getDate();
	            if (oThis.Day > daysInMonth) { oThis.Day = daysInMonth; };
	            setSelectControl(oThis.dayValueId, 1, daysInMonth, oThis.Day);
	        });
	        this.dayValueId.change(function () {
	            oThis.Day = $(this).val();
	        });
	    }
	})(jQuery);
