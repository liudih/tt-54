 
	$("#edit-cms-content-modal").on("hidden", function() {
	    $(this).removeData("modal");
	});
 
	$("#edit-cms-content-moreLanguage-modal").on("hidden", function() {
	    $(this).removeData("modal");
	});
   
	var options={
	   errorElement: 'span', 
	   errorClass: 'help-block help-block-error', 
	   focusInvalid: false, 
	   ignore: "", 
	     rules: {
	         ctitle: {
	             required: true
	         },
	         ckey: {
	             required: true
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
	    	 for ( instance in CKEDITOR.instances )
	    		 CKEDITOR.instances[instance].updateElement();
	    	 
			 var oneLevelCmsMenu = $("#oneLevelCmsMenu  option:selected").val();
	    	 var imenuid = 0;
	    	 if(typeof oneLevelCmsMenu == "undefined" || oneLevelCmsMenu ==-1){
				  alert("请选择相应的一级目录信息后再保存文章");
				  return;
			 }else{
				  imenuid = oneLevelCmsMenu;
			 }
	    	 var twoLevelCmsMenu = $("#twoLevelCmsMenu  option:selected").val();
	    	 if(typeof twoLevelCmsMenu != "undefined" && twoLevelCmsMenu !=-1){
	    		 imenuid = twoLevelCmsMenu;
	    	 }
	    	 
	    	 $("#imenuid").val(imenuid);
	    	 form.submit();
	 
	     }
	 };
   var addCmsContentForm=$("#addCmsContentForm");
   addCmsContentForm.validate(options);
		   
		   
		   
 var editoptions={
	   errorElement: 'span', 
       errorClass: 'help-block help-block-error', 
       focusInvalid: false, 
       ignore: "", 
	    rules: {
	         ctitle: {
	             required: true
	         },
	         ckey: {
	             required: true
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
	    	 for ( instance in CKEDITOR.instances )
	    		 CKEDITOR.instances[instance].updateElement();
	    	 
			 var twoLevelCmsMenu = $("#cms_content_menu_edit_2  option:selected").val();
			 var imenuid = 0;
	    	 if(typeof twoLevelCmsMenu == "undefined" || twoLevelCmsMenu ==-1){
				  alert("请选择相应的二级目录信息后再保存文章");
				  return;
			 }else{
				  imenuid = twoLevelCmsMenu;
			 }
	    	 
	    	  $("#imenuid_edit").val(imenuid);
	    		 
	    	form.submit();
	 
	     }
	 };
   var editCmsContentForm=$("#editCmsContentForm");
   editCmsContentForm.validate(editoptions);
   
 
   
   //添加文章时一级下拉联动二级下拉
	$(document).on("change","#iwebsiteid",function(){
	      //清除二级下拉列表
	      //要请求的二级下拉JSON获取页面
	      //将选中的一级下拉列表项的id传过去
	      $.getJSON(
	    		  js_CmsManagerRoutes.controllers.manager.Cms.getCmsMenuByLevelId(1, 0, this.value).url,
	    		  function (data) {
	    	  $("#oneLevelCmsMenu").empty();
	    	  $("#twoLevelCmsMenu").empty();
	    	  $("#oneLevelCmsMenu").append($("<option/>").text("--请选择--").attr("value","-1"));
	          //对请求返回的JSON格式进行分解加载
	          $(data).each(function () {
	              $("#oneLevelCmsMenu").append($("<option/>").text(this.cname).attr("value",this.iid));
	          });
	      });
	  });
	
   // 默认点击一级类型时加载文章类型数据
 
  
  
    //添加文章时一级下拉联动二级下拉
	$(document).on("change","#oneLevelCmsMenu",function(){
		
		 var siteId = $("#iwebsiteid  option:selected").val();
	      //清除二级下拉列表
	      //要请求的二级下拉JSON获取页面
	      //将选中的一级下拉列表项的id传过去
	      $.getJSON(
	    		  js_CmsManagerRoutes.controllers.manager.Cms.getCmsMenuByLevelId(2,this.value, siteId).url,
	    		  function (data) {
	    	  $("#twoLevelCmsMenu").empty();
	    	  $("#twoLevelCmsMenu").append($("<option/>").text("--请选择--").attr("value","-1"));
	          //对请求返回的JSON格式进行分解加载
	          $(data).each(function () {
	              $("#twoLevelCmsMenu").append($("<option/>").text(this.cname).attr("value",this.iid));
	          });
	      });
	  });
	
	
	//编辑文章内容时 一级下拉联动二级下拉
	$(document).on("change","#cms_content_menu_edit_1",function(){
		//清除二级下拉列表
		//要请求的二级下拉JSON获取页面
		//将选中的一级下拉列表项的id传过去
		$.getJSON(js_CmsManagerRoutes.controllers.manager.Cms.getCmsMenuByLevelId(2,this.value,1).url, function (data) {
			$("#cms_content_menu_edit_2").empty();
			$("#cms_content_menu_edit_2").append($("<option/>").text("--请选择--").attr("value","-1"));
			//对请求返回的JSON格式进行分解加载
			$(data).each(function () {
				$("#cms_content_menu_edit_2").append($("<option/>").text(this.cname).attr("value",this.iid));
			});
		});
	});
	
	 
	 $('.deletecms_content').each(function(index){
		  
		  $(this).on('click',function(e){
			   
			   if (confirm("Are you sure to delete this row ?") == false) {
				 return;
			   }
			 
			   $.ajax({
				   type: "GET",
				   url: js_CmsManagerRoutes.controllers.manager.Cms.deleteCmsContent($(this).data('id')).url,
				   error: function(){},
				   success: function(data){
					   if(data.errorCode===0){
							 $('#cmscontent_table #'+data.iid+'').remove();

					   }else if(data.errorCode===1){
							 alert("删除失败");
					   }
				   }
				});
		   });
	  });
	 
	 
 
	//cms多语言 编辑内容 时 需要根据语言变更内容与标题
	$(document).on("change","#ilanguageid_more_language",function(){
		
		var icmscontentid = $("input[name='icmscontentid_more_language']").val();
		
		var ilanguageid = $("#ilanguageid_more_language  option:selected").val();
		
		$.ajax({
            url: js_CmsManagerRoutes.controllers.manager.Cms.getCmsContentByLangIdAndContentId(icmscontentid, ilanguageid).url,
            type: 'POST',
            async:false,
            data: {
            },
            success: function(data) {
            	if(data ==''){
            		$("input[name='ctitle_more_language']").val('');
                	CKEDITOR.instances.cms_content_morelanguage.setData('');
            	}else{
            		$("input[name='ctitle_more_language']").val(data.ctitle);
            		CKEDITOR.instances.cms_content_morelanguage.setData(data.ccontent);
            	}
            }
		});
	});
		
		
	$(document).on("click","#add_cmscontent_more_language_btn",function(){
		$("form[name='addCmsContentMoreLanguageForm']").ajaxSubmit({
			
			type:'post',
			dataType:'json',
			beforeSerialize: function(){
				//在组装发送的表单值之前修改某些表单的值
				for ( instance in CKEDITOR.instances )
   	    		 CKEDITOR.instances[instance].updateElement();
			},
			beforeSubmit: function(){
				//可以做表单提交前的验证，不通过则可以阻止提交
			       //获取广告内容的值
				  var icmscontentid = $("input[name='icmscontentid_more_language']").val();
				  var ckey = $("input[name='ckey_more_language']").val();
				  var ctitle = $("input[name='ctitle_more_language']").val();
				  var ccontent = $("input[name='ccontent_more_language']").val();
				  
				  var ilanguageid = $("#ilanguageid_more_language  option:selected").val();
				  
				  if(typeof ctitle == "undefined" || ctitle=='' )
				  {
					  alert('请先填写目录后再添加，谢谢。');
					  return false;
				  }
				  if(typeof ilanguageid == "undefined" || ilanguageid=='' || ilanguageid==-1)
				  {
						alert('请先选择语言后再添加，谢谢。');
					  return false;
				  }
			  
				  //需要判定此文章是否已经翻译过，不能重复翻译
				  var returnValue = false;
				  $.ajax({
			            url: js_CmsManagerRoutes.controllers.manager.Cms.validateCmsContentMoreLanguage(icmscontentid, ilanguageid).url,
			            type: 'GET',
			            async:false,
			            data: {
			            },
			            success: function(data) {
			            	if(data.errorCode===0){
			            		returnValue = true;
			            	}else{
			            		alert('您所选择的语言，目录已翻译，请选择其他语言翻译，或者删除原来的再重新添加，谢谢。');
			            		returnValue = false;
			            	}
			            }
				});
				  
				return returnValue;
				
			},
			success:function(msg){
				var ilanguageidText = $("#ilanguageid_more_language").find("option:selected").text();
				//发送成功后的操作
				alert('保存成功'); 
          	   //返回数据，动态添加行
			   var table= $("#cmscontent_more_language_table");
			   var vTr= "<tr id="+msg.iid+">" +
			   		"<td>"+msg.iid+"</td>" +
			   		"<td>"+msg.icmscontentid+"</td>" +
			   		"<td>"+msg.ctitle+"</td>" +
			   		"<td>"+ilanguageidText+"</td>" +
			   		"<td><a href=\"javascript:deleteCmsContent_MoreLanguage("+msg.iid+");\"><i class=\"icon-trash\"></i></a></td>"
			   		"</tr>"
			   table.append(vTr);
			},
			error: function(XmlHttpRequest, textStatus, errorThrown){  
                alert( "保存失败");  
            }  
		});
		
	});
	  
	  
	 function deleteCmsContent_MoreLanguage(id){
		 if (confirm("Are you sure to delete this row ?") == false) {
			 return;
		 }
		 
		  $.ajax({
			   type: "GET",
			   url: js_CmsManagerRoutes.controllers.manager.Cms.deleteCmsContentMoreLanguage(id).url,
			   error: function(){},
			   success: function(data){
				   if(data.errorCode===0){
					   	alert("删除成功");
						$('#cmscontent_more_language_table #'+data.iid+'').remove();

				   }else if(data.errorCode===1){
						 alert("删除失败");
				   }
			   }
			});
			 
	}
  
 