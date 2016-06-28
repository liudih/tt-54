$(function(){
	
	var options={
		errorElement: 'span', 
		errorClass: 'help-inline', 
		focusInvalid: false, 
		ignore: "", 
		rules: {
	        iadminroleid_fun: {required: true},
	        menu_function_check: {required: true,minlength: 1},
    	},
     	messages:{
     		iadminroleid_fun: "请选择角色",
     		menu_function_check: "请选择需要配置的权限后再保存"
     	},
     	errorPlacement: function (error, element) { // render error placement for each input type
            if (element.attr("name") == "menu_function_check") { // for uniform checkboxes, insert the after the given container
                error.addClass("no-left-padding").insertAfter("#form_menu_function_check_error");
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
    		 return false;
		}
	};

 
	
	
	$(document).on("change","#iadminroleid_fun",function(){
		var iroleid = $("#iadminroleid_fun  option:selected").val();
		$('span').removeClass("checked");
		
		if(iroleid == 2){
			$("#add-menu-role-function-btn").attr("disabled", true);
			$("#add-menu-role-function-btn").removeClass("btn blue");
			$("#add-menu-role-function-btn").addClass("btn");
		}else{
			$("#add-menu-role-function-btn").removeAttr("disabled");
			$("#add-menu-role-function-btn").removeClass("btn");
			$("#add-menu-role-function-btn").addClass("btn blue");
		}
		$.ajax({
			type: "GET",
			async:false,
			url: js_MenuRoleRoutes.controllers.manager.AdminRole.getMenuByRoleId(iroleid).url,
			error: function(){},
			success: function(data){
				var list = data;
				
				$(':checkbox[name="menu_function_check"]').map(function(){
				      var v =  $(this).val();
				      for(var i =0; i <list.length;i++){
				    	  if(v == list[i]){
				    		  $(this).parent().addClass("checked");
				    	  }
				      }
				});
			}
		});
	});
	
	
	
	$(document).on("click","#add-menu-role-function-btn",function(){
		
		var iroleId = $("#iadminroleid_fun  option:selected").val();
		if(typeof iroleId == "undefined" || iroleId=='' || iroleId==-1){
			alert('请先选择角色后再保存，谢谢。');
			return false;
		}
		var list = $('#menu-role-checkbox-list').find('span[class="checked"] :checkbox').map(function(){
		      return $(this).val();
			}).get().join(',');
		//以下 这种方式不能取到所有checked的checkbox，因为bootstrap是否选中是根据上层的 span的样式决定的
//		var list = $(':checkbox[name="menu_function_check"]:checked').map(function(){
//			return $(this).val();
//		}).get().join(',');

		if(typeof list == "undefined" || list==''){
			alert('请勾选角色权限后再保存，谢谢。');
			return false;
		}
		
		$("form[name='add-menu-role-function-form']").ajaxSubmit({
			type:'POST',
			dataType:'json',
			url: js_MenuRoleRoutes.controllers.manager.AdminRole.addMenuRole(iroleId, list).url,
			beforeSerialize: function(){
				 
			},
			beforeSubmit: function(){
 
			},
			success:function(msg){
				if(msg.errorCode==1){
					 alert( "保存失败"); 
            	}else{
            		alert( "保存成功！");
            	}
				
			},
			error: function(XmlHttpRequest, textStatus, errorThrown){  
                alert( "保存失败");  
            }  
		});
		
	});
	
	$("input[type=checkbox]").on("click", function(t) {
	     var id = this.id;
	     var checked = this.checked;
	     if(id =='parentid'){
	    	// 找到最顶级父级节点
	    	var parentDiv = $(this).parent().parent().parent();
	    	// 找到平级的checkboxlist
	    	var chklist = parentDiv.find(':checkbox[id="subid"]');

	    	$.each(chklist, function(i,v){
	    		if(checked){
	    			$(this).parent().addClass("checked");
	    		}else{
	    			$(this).parent().removeClass("checked");
	    		}
	    	});   
	    	
	     }else{
	    	 var parentDiv = $(this).closest('.control-group');
	    	 var chklist = parentDiv.find(':checkbox[id="parentid"]');
	    	 
	    	 $.each(chklist, function(i,v){
	    		if(checked){
	    			$(this).parent().addClass("checked");
	    		}else{
	    			var subchklist = parentDiv.find('span[class="checked"]');
	    			if(subchklist.length <= 1){
	    				//去掉选中的时候需要判定其他 的子元素是否选中
	    				$(this).parent().removeClass("checked");
	    			}
	    		}
	    	});   
	     }
	});
			 
 })