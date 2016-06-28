$(function(){
	var options={
		errorElement: 'span', 
		errorClass: 'help-inline', 
		focusInvalid: false, 
		ignore: "", 
		rules: {
			cusername: {required: true},
			cpasswd: {required: true},
	        cjobnumber: {required: true},
	        iadminroleid: {required: true,minlength: 1},
	        iwebsiteids: {required: true,minlength: 1},
	        cemail: {required: true,email: true},
	        cphone: {required: true}
    	},
     	messages:{
	    	cusername: "请输入名称",
	    	cpasswd: "请输入密码",
	    	cjobnumber: "请输入员工工号",
	    	cemail:{
	        	required:"请输入员工邮箱号",
	        	email:"请输入合法的邮箱"
	        },
	        cphone:"请输入电话号码",
        	iadminroleid: "请至少选择一个角色",
        	iwebsiteids: "请至少选择一个站点"
     	},
     	errorPlacement: function (error, element) { // render error placement for each input type
            if (element.attr("name") == "iadminroleid") { // for uniform checkboxes, insert the after the given container
                error.addClass("no-left-padding").insertAfter("#form_iadminroleide_error");
            } else if (element.attr("name") == "iwebsiteids"){
            	error.addClass("no-left-padding").insertAfter("#form_iwebsiteids");
            } else {
                error.insertAfter(element); // for other inputs, just perform default behavoir
            }
        },
	    highlight: function (element) {
	        $(element).closest('.form-group').addClass('has-error');
	    },
	    unhighlight: function (element) { 
	        $(element).closest('.form-group').removeClass('has-error'); 
	    },
	    success: function (label) {
	        label.closest('.form-group').removeClass('has-error'); 
	    },
	    submitHandler: function (form) {
	    	var jobNumber = $("input[name='cjobnumber']").val();
	    	var cusername = $("input[name='cusername']").val();
	    	//提交前先验证用户名、工号唯一性
	    	$.ajax({
	    		type: "GET",
	    		async: false,
	    		url: js_UserRoutes.controllers.manager.AdminUser.validateUserName(cusername).url,
	    		error: function(){},
	    		success: function(data){
	    			if(data){
						alert("The user name needs the only, please revise and then add the user！");
						return;
					}else{
			    		$.ajax({
							type: "GET",
							async: false,
							url: js_UserRoutes.controllers.manager.AdminUser.validateJobNumber(jobNumber).url,
							error: function(){},
							success: function(data){
								if(data){
									alert("The user number needs the only, please revise and then add the user！");
								}else{
									form.submit();
								}
							}
						});
					}
	    		}
	    	});
	    	
	    	
    		
		}
	};
	
	var editoptions={
			errorElement: 'span', 
			errorClass: 'help-inline', 
			focusInvalid: false, 
			ignore: "", 
			rules: {
		        iadminroleid: {required: true,minlength: 1},
		        iwebsiteids: {required: true,minlength: 1},
		        cemail: {required: true,email: true},
		        cphone: {required: true}
	    	},
	     	messages:{
		    	cemail:{
		        	required:"请输入员工邮箱号",
		        	email:"请输入合法的邮箱"
		        },
		        cphone:"请输入电话号码",
	        	iadminroleid: "请至少选择一个角色",
	        	iwebsiteids: "请至少选择一个站点"
	     	},
	     	errorPlacement: function (error, element) { // render error placement for each input type
	            if (element.attr("name") == "iadminroleid") { // for uniform checkboxes, insert the after the given container
	                error.addClass("no-left-padding").insertAfter("#form_iadminroleide_error");
	            } else if (element.attr("name") == "iwebsiteids"){
	            	error.addClass("no-left-padding").insertAfter("#form_iwebsiteids");
	            } else {
	                error.insertAfter(element); // for other inputs, just perform default behavoir
	            }
	        },
		    highlight: function (element) {
		        $(element).closest('.form-group').addClass('has-error');
		    },
		    unhighlight: function (element) { 
		        $(element).closest('.form-group').removeClass('has-error'); 
		    },
		    success: function (label) {
		        label.closest('.form-group').removeClass('has-error'); 
		    },
		    submitHandler: function (form) {
		    	 
				form.submit();
	    		
			}
		};
	var userform=$("#userform");
	var editform=$("#editform");
	userform.validate(options);
	editform.validate(editoptions);
	$("#edit-user-modal").on("hidden", function() {
		$(this).removeData("modal");
	});
	   
	function delcallback(data){
		  
		if(data.errorCode===0){
			$('#user_table #'+data.iid+'').remove();

		}else if(data.errorCode===1){
			 
		}
	 }
	 $('.delete').each(function(index){
		
		$(this).on('click',function(e){
			if (confirm("Are you sure to delete this row ?") == false) {
				 return;
			}
			var s = s || {};
			s.type="get";
			s.url=js_UserRoutes.controllers.manager.AdminUser.deleteUser($(this).data('id')).url;
			s.success=delcallback;
			$.ajax(s);
		 });
	});
	 
	 
	 $("#add-user-random-password-btn").on('click',function(e){
		var cpasswd = userform.find("[name='cpasswd']");
		cpasswd.empty();
		
		$.ajax({
			async:false,
			type: "GET",
			url:js_UserRoutes.controllers.manager.AdminUser.generatePassword(6).url,
			success:function(msg){
				cpasswd.val(msg);
			},
			error: function(){
				 
			},
		});
	 });
			 
 })