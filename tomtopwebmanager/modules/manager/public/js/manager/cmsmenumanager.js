 
	initCmsMenuTree(); 
				
	function initCmsMenuTree() {
	    $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
	    $('.tree li.parent_li > span').on('click', function (e) {
	    	$("#cmsmenutree").find('span').attr('style', '');
	    	$(this).attr('style', 'background-color:red;');
	        var children = $(this).parent('li.parent_li').find(' > ul > li');
	        if (children.is(":visible")) {
	            children.hide('fast');
	            $(this).attr('title', 'Expand this branch').find(' > i').addClass('icon-plus-sign').removeClass('icon-minus-sign');
	        } else {
	            children.show('fast');
	            $(this).attr('title', 'Collapse this branch').find(' > i').addClass('icon-minus-sign').removeClass('icon-plus-sign');
	        }
	        
	        var flag = $('#addChildren_Or_Menumessage_flag').val();
	        if(typeof flag == "undefined" || flag ==0){
	        	$('#cmsmenu_message_manager').click();
        	}else if(flag ==1){
        		$('#cmsmenu_children_manager').click();
        	}else{
        		$('#cmsmenu_more_language_manager').click();
        	}
	        e.stopPropagation();
	    });
	}
				
	
	
	$('#cmsmenu_children_manager').unbind('click');
	$('#cmsmenu_children_manager').on('click',function(){
		//判断是否有树节点选中//
		//提示
		var id = $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').attr("id");
		if(typeof id == "undefined")
		{
			alert('请选择父级目录后再添加，谢谢。');
			return;
		}
		$("#cmsmenuedit_modle").empty();
		var url = '/sysadmin/cms/editMenu?iparentid='+id;
		$.get(url, function(data){
			$("#cmsmenuedit_modle").replaceWith(data);
		}, "html");
	});
	
	
	$('#cmsmenu_message_manager').unbind('click');
	$('#cmsmenu_message_manager').on('click',function(){
		//判断是否有树节点选中//
		//提示
		var id = $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').attr("id");
		if(typeof id == "undefined")
		{
			alert('请先选择目录信息后再修改，谢谢。');
			return;
		}
		$("#cmsmenuedit_modle").empty();
		var url = '/sysadmin/cms/editMessageMenu?iid='+id;
		$.get(url, function(data){
			$("#cmsmenuedit_modle").replaceWith(data);
		}, "html");
	});
	
	
	$('#delete_cmsmenu_manager').unbind('click');
	$('#delete_cmsmenu_manager').on('click',function(){
		//判断是否有树节点选中//
		//提示
		var id = $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').attr("id");
		if(typeof id == "undefined")
		{
			alert('请先选择需要删除的目录，谢谢。');
			return;
		}
		
		var text = $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').text();
		if (confirm("您确定要删除 "+text+" 目录吗?") == false) {
            return;
        }
		
		 var url="/sysadmin/cms/deleteCmsMenu";
		  $.ajax({
			   type: "GET",
			   url: url,
			   data: "iid="+id,
			   error: function(){},
			   success: function(data){
				   if(data.errorCode===0){
					   $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').remove();
					   alert("删除目录成功");

				   }else if(data.errorCode===1){
						 
				   }
			   }
			});
		
	});
	
	
	
	$('#cmsmenu_more_language_manager').unbind('click');
	$('#cmsmenu_more_language_manager').on('click',function(){
		//判断是否有树节点选中//
		//提示
		var id = $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').attr("id");
		if(typeof id == "undefined")
		{
			alert('请先选择目录信息后再修改，谢谢。');
			return;
		}
		$("#cmsmenuedit_modle").empty();
		
		var url = js_CmsMenuRoutes.controllers.manager.Cms.editCmsMenuLanguage(id).url;;
		$.get(url, function(data){
			$("#cmsmenuedit_modle").replaceWith(data);
		}, "html");
	});
	
	
	$(document).on("click","#add_cmsmenu_more_language_btn",function(){
		  //获取广告内容的值
		  var imenuid = $("input[name='iid']").val();
		  var cname = $("input[name='cname']").val();
		  var ilanguageid = $("#ilanguageid_cmsmenu  option:selected").val();
		  
		  if(typeof cname == "undefined" || cname=='' )
		  {
			  alert('请先填写目录后再添加，谢谢。');
			  return;
		  }
		  if(typeof ilanguageid == "undefined" || ilanguageid=='' || ilanguageid==-1)
		  {
				alert('请先选择语言后再添加，谢谢。');
				return;
		  }
		  var ilanguageidText = $("#ilanguageid_cmsmenu").find("option:selected").text();

		  
		  $.ajax({
              url: js_CmsMenuRoutes.controllers.manager.Cms.validateCmsMenuMoreLanguage(imenuid, ilanguageid).url,
              type: 'GET',
              data: {
              },
              success: function(data) {
              	if(data.errorCode===0){
              		$.ajax({
         			   type: "POST",
         			   url: js_CmsMenuRoutes.controllers.manager.Cms.addCmsMenuMoreLanguage(imenuid,cname,ilanguageid).url,
         			   error: function(){},
         			   success: function(msg){
         			      //返回数据，动态添加行
         				   var table= $("#cmsmenu_more_language_table");
         				   var vTr= "<tr id="+msg.iid+">" +
         				   		"<td>"+msg.iid+"</td>" +
         				   		"<td>"+imenuid+"</td>" +
         				   		"<td>"+cname+"</td>" +
         				   		"<td>"+ilanguageidText+"</td>" +
         				   		"<td><a href=\"javascript:deleteCmsMenu_MoreLanguage("+msg.iid+");\"><i class=\"icon-trash\"></i></a></td>"
         				   		"</tr>"
         				   table.append(vTr);
         			   }
         			});
              	}else{
              		alert('您所选择的语言，目录已翻译，请选择其他语言翻译，或者删除原来的再重新添加，谢谢。');
            		
            		return;
              	}
              }
		  });
		  
	  });
	  
	  
	 function deleteCmsMenu_MoreLanguage(id){
		 if (confirm("Are you sure to delete this row ?") == false) {
			 return;
		 }
		 
		  $.ajax({
			   type: "GET",
			   url: js_CmsMenuRoutes.controllers.manager.Cms.deleteCmsMenuMoreLanguage(id).url,
			   error: function(){},
			   success: function(data){
				   if(data.errorCode===0){
					   	alert("删除成功");
						$('#cmsmenu_more_language_table #'+data.iid+'').remove();

				   }else if(data.errorCode===1){
						 alert("删除失败");
				   }
			   }
			});
			 
	}
	 
	 
 	$('#cmsmenu_more_website_manager').unbind('click');
	$('#cmsmenu_more_website_manager').on('click',function(){
		//判断是否有树节点选中//
		//提示
		var id = $("#cmsmenutree").find("li").find('span[style="background-color:red;"]').attr("id");
		if(typeof id == "undefined")
		{
			alert('请先选择目录信息后再修改，谢谢。');
			return;
		}
		$("#cmsmenuedit_modle").empty();
		
		var url = js_CmsMenuRoutes.controllers.manager.Cms.editCmsMenuWebsite(id).url;;
		$.get(url, function(data){
			$("#cmsmenuedit_modle").replaceWith(data);
		}, "html");
	});
	
	
	$(document).on("click","#add_cmsmenu_more_website_btn",function(){
		  //获取广告内容的值
		  var imenuid = $("input[name='iid']").val();
		  var iwebsiteid = $("#iwebsiteid_cmsmenu  option:selected").val();
		  var device = $("#cdevice  option:selected").val();
		  
		  if(typeof iwebsiteid == "undefined" || iwebsiteid=='' || iwebsiteid==-1)
		  {
				alert('请先选择站点后再添加，谢谢。');
				return;
		  }
		  var iwebsiteidText = $("#iwebsiteid_cmsmenu").find("option:selected").text();

		  if(typeof device == "undefined" || device=='' || device==-1)
		  {
				alert('请先选择设备后再添加，谢谢。');
				return;
		  }
		  
		  $.ajax({
              url: js_CmsMenuRoutes.controllers.manager.Cms.validateCmsMenuMoreWebsite(imenuid, iwebsiteid, device).url,
              type: 'GET',
              data: {
              },
              success: function(data) {
              	if(data.errorCode===0){
              		$.ajax({
         			   type: "POST",
         			   url: js_CmsMenuRoutes.controllers.manager.Cms.addCmsMenuMoreWebsite(imenuid,iwebsiteid, device).url,
         			   error: function(){},
         			   success: function(msg){
         			      //返回数据，动态添加行
         				   var table= $("#cmsmenu_more_website_table");
         				   var vTr= "<tr id="+msg.iid+">" +
         				   		"<td>"+msg.iid+"</td>" +
         				   		"<td>"+msg.imenuid+"</td>" +
         				   		"<td>"+iwebsiteidText+"</td>" +
         				   		"<td>"+device+"</td>" +
         				   		"<td><a href=\"javascript:deleteCmsMenu_MoreWebsite("+msg.iid+");\"><i class=\"icon-trash\"></i></a></td>"
         				   		"</tr>"
         				   table.append(vTr);
         			   }
         			});
              	}else{
              		alert('您所选择的站点，目录已关联，谢谢。');
            		
            		return;
              	}
              }
		  });
		  
	  });
	  
	  
	 function deleteCmsMenu_MoreWebsite(id){
		 if (confirm("Are you sure to delete this row ?") == false) {
			 return;
		 }
		 
		  $.ajax({
			   type: "GET",
			   url: js_CmsMenuRoutes.controllers.manager.Cms.deleteCmsMenuMoreWebsite(id).url,
			   error: function(){},
			   success: function(data){
				   if(data.errorCode===0){
					   	alert("删除成功");
						$('#cmsmenu_more_website_table #'+data.iid+'').remove();

				   }else if(data.errorCode===1){
						 alert("删除失败");
				   }
			   }
			});
			 
	}
	 