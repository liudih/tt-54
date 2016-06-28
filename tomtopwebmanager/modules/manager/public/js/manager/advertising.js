
    $(function(){
    	var pageNo = $("input[name='pageNo']").val();
    	var totalPages = $("input[name='totalPages']").val();
       
        //初始化所需数据
        var options = {
            bootstrapMajorVersion:3,//版本号。3代表的是第三版本
            currentPage: pageNo, //当前页数
            numberOfPages: 10, //显示页码数标个数
            totalPages:totalPages, //总共的数据所需要的总页数
            itemTexts: function (type, page, current) {  
            //图标的更改显示可以在这里修改。
            switch (type) {  
                    case "first":  
                        return "first";  
                    case "prev":  
                        return "prev";  
                    case "next":  
                        return "next";  
                    case "last":  
                        return "last";  
                    case "page":  
                        return  page;  
                }                 
            }, 
            tooltipTitles: function (type, page, current) {
				//如果想要去掉页码数字上面的预览功能，则在此操作。例如：可以直接return。
                switch (type) {
		            case "first":
		                return "Go to first page";
		            case "prev":
		                return "Go to previous page";
		            case "next":
		                return "Go to next page";
		            case "last":
		                return "Go to last page";
		            case "page":
		                return (page === current) ? "Current page is " + page : "Go to page " + page;
		        }
            },
            onPageClicked: function (e, originalEvent, type, page) {
            	var ilanguageid = $("#ilanguageid_search  option:selected").val();
            	var iwebsiteid = $("#iwebsiteid_relation_search  option:selected").val();
            	var iposition = $("#iposition_search  option:selected").val();
            	var itype = $("#advert_itype_relation_search  option:selected").val();
            	
                //单击当前页码触发的事件。若需要与后台发生交互事件可在此通过ajax操作。page为目标页数。
	      		location.href = js_AdvertisingRoutes.controllers.manager.Advertising.searchAdvertisingPage(page,ilanguageid,iwebsiteid,iposition,itype).url;
            }
        };
        var element = $('#advertising-paginator');//获得数据装配的位置
        element.bootstrapPaginator(options);	//进行初始化
    });

	$("#addAdvertisingForm").validate({
			submitHandler: function(form){
				var iposition = $("#iposition  option:selected").val();
				var ilanguageid = $("#ilanguageid  option:selected").val();
				if(iposition ==-1){
					alert('位置不能为空！');
					return false;
				}
				if(ilanguageid ==-1){
					alert('语言不能为空！');
					return false;
				}
				form.submit();
			},
			rules: {
				iindex:{required:true, maxlength:5, digits:true},
				maxlength:{
					maxlength:600
				},
				
				videoTitle:{
					maxlength:50
				}
		  },
		  messages: {
			  iindex:{
					required: "Please enter an iindex!",
					maxlength:jQuery.validator.format("Please enter no more than {0} characters!")
			  }
				
			},
			// 错误提示
			errorClass: "edit_error", 
			highlight: function (element) { 
				// 错误方法调用
				$(element).siblings('.rightUse').remove() ;
				$("#regsubmit").addClass('noAgree').attr('disabled','true');
		     },

		     unhighlight: function (element) { 
		    	$(element).siblings('label').remove() ;
		    	$(element).next('.edit_error').removeClass('edit_error').addClass('');
		     },

			errorPlacement: function(error, element) {
			        $(element).after(error);
			 },
			 
			 success: function (e,element) {
				 $(e).removeClass("edit_error");
	             // $("#regsubmit").removeClass('noAgree').removeAttr('disabled');
		     }
		});
	   
 
	   
	   $("#edit-advertising-modal").on("hidden", function() {
		    $(this).removeData("modal");
		});
	   
	   $("#edit-advertising-relation-modal").on("hidden", function() {
		   $(this).removeData("modal");
	   });
	   
	   
	  function delcallbackAdvertising(data){
		  
		 if(data.errorCode===0){
			 $('#advertising_table #'+data.iid+'').remove();

		 }else if(data.errorCode===1){
			 
		 }
	  }
	 $('.deleteAdvertising').each(function(index){
		  
		  $(this).on('click',function(e){
			  
			  if (confirm("Are you sure to delete this row ?") == false) {
                  return;
              }
			  var s = s || {};
			   
			   s.type="get";
			   s.url=js_AdvertisingRoutes.controllers.manager.Advertising.deleteAdvertising($(this).data('id')).url;
			   s.success=delcallbackAdvertising;
			   $.ajax(s);
			   
		   });
	  });
	 
	 
	 function deleteAdvertising_content(id){
		 if (confirm("Are you sure to delete this row ?") == false) {
			 return;
		 }
		 
		  $.ajax({
			   type: "GET",
			   url: js_AdvertisingRoutes.controllers.manager.Advertising.deleteAdvertContent(id).url,
			   error: function(){},
			   success: function(data){
				   if(data.errorCode===0){
						 $('#advert_content_table #'+data.iid+'').remove();

				   }else if(data.errorCode===1){
						 
				   }
			   }
			});
		 
	 }
	 
	 
	 function deleteAdvertising_relation(id){
		 if (confirm("Are you sure to delete this row ?") == false) {
			 return;
		 }
		 $.ajax({
			 type: "GET",
			 url: js_AdvertisingRoutes.controllers.manager.Advertising.deleteAdvertRelation(id).url,
			 error: function(){},
			 success: function(data){
				 if(data.errorCode===0){
					 $('#advertising_relation_table #'+data.iid+'').remove();
					 
				 }else if(data.errorCode===1){
					 
				 }
			 }
		 });
		 
	 }
	  
		  
	$(document).on("click","#add_advertcontent_btn",function(){
		
		$("form[name='editAdvertContetForm']").ajaxSubmit({
			
			type:'post',
			dataType:'json',
			beforeSerialize: function(){
				 
			},
			beforeSubmit: function(){
				var iadvertisingid = $("input[name='iadvertisingid']").val();
				var ilanguageid = $("#ilanguageid_content  option:selected").val();
				var iindex = $("input[name='iindex']").val();
				$("input[name='ilanguageid']").val(ilanguageid);
				if(ilanguageid == -1){
					alert('语言不能为空');
					return false;
				}
				if(isNaN(iindex)){
					alert('排序序号只能为数字');
					return false;
				}
				
				//需要判定此广告此语言是否已经存在 ，不能重复存在
				var returnValue = false;
				$.ajax({
			            url: js_AdvertisingRoutes.controllers.manager.Advertising.validateAdvertContent(iadvertisingid, ilanguageid).url,
			            type: 'GET',
			            async:false,
			            data: {
			            },
			            success: function(data) {
			            	if(data.errorCode===0){
			            		returnValue = true;
			            	}else{
			            		alert('您所选择的语言，广告已关联，请选择其他语言，或者删除原来的关联后再重新添加，谢谢。');
			            		returnValue = false;
			            	}
			            }
				});
				  
				return returnValue;
			},
			success:function(msg){
				  var ilanguageidText = $("#ilanguageid_content").find("option:selected").text();
				 //返回数据，动态添加行
				   var table= $("#advert_content_table");
				   var vTr= "<tr id="+msg.iid+">" +
				   		"<td>"+msg.iadvertisingid+"</td>" +
				   		"<td>"+msg.ctitle+"</td>" +
				   		"<td>"+msg.chrefurl+"</td>" +
				   		"<td>"+ilanguageidText+"</td>" +
				   		"<td>"+msg.bstatus+"</td>" +
				   		"<td><a href=\"javascript:deleteAdvertising_content("+msg.iid+");\"><i class=\"icon-trash\"></i></a></td>"
				   		"</tr>"
				   table.append(vTr);
			},
			error: function(XmlHttpRequest, textStatus, errorThrown){  
                alert( "保存失败");  
            }  
		});
		  
	  });
	  
	  
	  
	  $(document).on("change","#itype",function(){
		  var itype = $("#itype  option:selected").val();
		  if(itype==1){
			  $("#advert_ilanguageid_relation").hide();
			  $("#advert_category_relation").hide();
			  $("#advert_cbusinessid_relation").show();
		  }else if(itype==2){
			  $("#advert_ilanguageid_relation").show();
			  $("#advert_category_relation").show();
			  $("#advert_cbusinessid_relation").hide();
		  }else{
			  $("#advert_ilanguageid_relation").hide();
			  $("#advert_category_relation").hide();
			  $("#advert_cbusinessid_relation").hide();
			  
		  }
		 
	  });
	  $(document).on("click","#add_advertrelation_btn",function(){
		  var itype = $("#itype  option:selected").val();
		  var iwebsiteid = $("#iwebsiteid  option:selected").val();
		  var cbusinessid = $("input[name='cbusinessid']").val();
		  
		  var oneLevelCategory = $("#oneLevelCategory  option:selected").val();
		  var twoLevelCategory = $("#twoLevelCategory  option:selected").val();
		  var threeLevelCategory = $("#threeLevelCategory  option:selected").val();
		  
		  
		  if(itype== -1){
			  alert("请选择相应的广告类型后再保存");
			  return;
		  }else if(itype== 1){
			  //产品类型用默认取到的值
		  }else if(itype==2){
			  if(oneLevelCategory == -1){
				  alert("请选择相应的品类信息后再保存");
				  return;
			  }
			  if(typeof threeLevelCategory == "undefined" && typeof twoLevelCategory == "undefined" && typeof oneLevelCategory == "undefined"){
				  alert("请选择相应的品类信息后再保存");
				  return;
				   
			  }else if(typeof threeLevelCategory != "undefined" && threeLevelCategory !=-1){
				  cbusinessid = threeLevelCategory;
			  }else if(typeof twoLevelCategory != "undefined" && twoLevelCategory !=-1){
				  cbusinessid = twoLevelCategory;
			  }else if(typeof oneLevelCategory != "undefined" && oneLevelCategory !=-1){
				  cbusinessid = oneLevelCategory;
			  }
		  }else{
			  cbusinessid = '';
		  }
		if(iwebsiteid == -1){
			alert("请选择相应的站点信息后再保存");
			return;
		}
		  
		$("#cbusinessid").val(cbusinessid);
		var cdevice = $("#cdevice  option:selected").val();
		
		if(typeof cdevice == "undefined" || cdevice=='' || cdevice=='-1'){
			  alert("请选择相应的设备信息后再保存");
			  return;
			   
		}
		
		$("form[name='editAdvertRelationForm']").ajaxSubmit({
					
			type:'post',
			dataType:'json',
			beforeSerialize: function(){
				 
			},
			beforeSubmit: function(){
				 
			},
			success:function(msg){
				//获取广告内容的值
				  var iadvertisingid = $("input[name='iadvertisingid']").val();
				  var itypeText = $("#itype").find("option:selected").text();
				  var iwebsiteidText = $("#iwebsiteid").find("option:selected").text();
				  var cbusinessid = $("input[name='cbusinessid']").val();
				 //返回数据，动态添加行
				  var table= $("#advertising_relation_table");
				  var trStr= "<tr id="+msg.iid+">" +
				  "<td>"+iadvertisingid+"</td>" +
				  "<td>"+itypeText+"</td>" +
				  "<td>"+iwebsiteidText+"</td>" +
				  "<td>"+cbusinessid+"</td>" +
				  "<td>"+cdevice+"</td>" +
				  "<td><a href=\"javascript:deleteAdvertising_relation("+msg.iid+");\"><i class=\"icon-trash\"></i></a></td>"
				  "</tr>"
				  
				  table.append(trStr);
			},
			error: function(XmlHttpRequest, textStatus, errorThrown){  
	            alert( "保存失败");  
	        }  
		});
		  
	});

	  
		
		 
	  //一级下拉联动二级下拉
	  $(document).on("click","#oneLevelCategory",function(){
		  //品类根据站点与语言选择查询，先验证
		  var ilanguageid = jQuery("#ilanguageid_relation  option:selected").val();
		  if(typeof ilanguageid == "undefined" || ilanguageid==-1)
		  {
			  alert("请先选择语言！！！");
			  
			  return;
		  }
		  
		  var iwebsiteid = jQuery("#iwebsiteid  option:selected").val();
		  
		  if(typeof iwebsiteid == "undefined" || iwebsiteid==-1)
		  {
			  alert("请先选择站点！！！");
			  
			  return;
		  }

	  });

	  
	  // 品类是根据语言与站点选择而改变的
	  $(document).on("change","#iwebsiteid",function(){
		  var iwebsiteid = jQuery("#iwebsiteid  option:selected").val();
		  var ilanguageid = jQuery("#ilanguageid_relation  option:selected").val();
		  //要请求的一级品类JSON获取页面
		  $("#oneLevelCategory").empty();
		  $.getJSON(
			js_AdvertisingRoutes.controllers.manager.Advertising.getCategoryList(iwebsiteid,ilanguageid).url,
			function (data) {
			  $("#oneLevelCategory").append($("<option/>").text("--请选择--").attr("value","-1"));
		      //对请求返回的JSON格式进行分解加载
		      $(data).each(function () {
		          $("#oneLevelCategory").append($("<option/>").text(this.cname).attr("value",this.icategoryid));
		      });
		  });
	  });

	$(document).on("change","#ilanguageid_relation",function(){
		  var iwebsiteid = jQuery("#iwebsiteid  option:selected").val();
		  var ilanguageid = jQuery("#ilanguageid_relation  option:selected").val();
		  //要请求的一级品类JSON获取页面
		  $("#oneLevelCategory").empty();
		  $.getJSON(
			js_AdvertisingRoutes.controllers.manager.Advertising.getCategoryList(iwebsiteid,ilanguageid).url,
			function (data) {
		      //对请求返回的JSON格式进行分解加载
		      $(data).each(function () {
		          $("#oneLevelCategory").append($("<option/>").text(this.cname).attr("value",this.icategoryid));
		      });
		  });
	  });
	  
	  //一级下拉联动二级下拉
	$(document).on("change","#oneLevelCategory",function(){
		  
		 var iwebsiteid = jQuery("#iwebsiteid  option:selected").val();
		  var ilanguageid = jQuery("#ilanguageid_relation  option:selected").val();
	      //清除二级下拉列表
	      $("#twoLevelCategory").empty();
	      $("#twoLevelCategory").append($("<option/>").text("--请选择--").attr("value","-1"));
	      //要请求的二级下拉JSON获取页面
	  	
	      //将选中的一级下拉列表项的id传过去
	      $.getJSON(
	    	js_AdvertisingRoutes.controllers.manager.Advertising.getChildCategoriesByCategoryId(iwebsiteid,ilanguageid,this.value).url, 
	    	function (data) {
	          //对请求返回的JSON格式进行分解加载
	          $(data).each(function () {
	              $("#twoLevelCategory").append($("<option/>").text(this.cname).attr("value",this.icategoryid));
	          });
	      });
	  });
	  
	  
	  //二级下拉联动三级下拉
	 $(document).on("change","#twoLevelCategory",function(){
		 
		 var iwebsiteid = jQuery("#iwebsiteid  option:selected").val();
		  var ilanguageid = jQuery("#ilanguageid_relation  option:selected").val();
	      //清除三级下拉列表
	      $("#threeLevelCategory").empty();
	      $("#threeLevelCategory").append($("<option/>").text("--请选择--").attr("value","-1"));
	      //要请求的三级下拉JSON获取页面
	      //将选择的二级下拉列表项的id传过去
	      $.getJSON(
	    	   js_AdvertisingRoutes.controllers.manager.Advertising.getChildCategoriesByCategoryId(iwebsiteid,ilanguageid,this.value).url, 
	    	   function (data) {
		          //对请求返回的JSON格式进行分解加载
		          $(data).each(function () {
		              $("#threeLevelCategory").append($("<option/>").text(this.cname).attr("value",this.icategoryid));
		       });
	      });

	  });
	 
			     
//预览图片
 $("#cadvertisingimages").change(function(){
	 preImg("cadvertisingimages",'imgPre');
	 $("#imgPre").css("display","block");
 });
 
//预览图片
 $("#cbgimages").change(function(){
	 preImg("cbgimages",'bgimgPre');
	 $("#bgimgPre").css("display","block");
 });
	 
 function getFileUrl(sourceId) {
	 var url;
	 if (navigator.userAgent.indexOf("MSIE")>=1) { // IE
	 url = document.getElementById(sourceId).value;
	 } else if(navigator.userAgent.indexOf("Firefox")>0) { // Firefox
	 url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
	 } else if(navigator.userAgent.indexOf("Chrome")>0) { // Chrome
	 url = window.URL.createObjectURL(document.getElementById(sourceId).files.item(0));
	 }
	 return url;
}

 function preImg(sourceId, targetId) {
	 var url = getFileUrl(sourceId);
	 var imgPre = document.getElementById(targetId);
	 imgPre.src = url;
 } 
  
 
 
 
	//  编辑内容 时 需要根据语言变更内容
	$(document).on("change","#ilanguageid_content",function(){
			
		var iadvertisingid = $("input[name='iadvertisingid']").val();
		var ilanguageid = $("#ilanguageid_content option:selected").val();
		$("input[name='ilanguageid']").val(ilanguageid);
		
		$.ajax({
		    url: js_AdvertisingRoutes.controllers.manager.Advertising.getAdvertContentByAdvertIdAndLangId(iadvertisingid, ilanguageid).url,
		    type: 'GET',
		    async:false,
		    data: {
		    },
		    success: function(data) {
		     	if(data ==''){
		     		$("input[name='ctitle']").val('');
		     		$("input[name='chrefurl']").val('');
		     		$("input[name='iindex']").val('');
		     		$("input[name='cbgimageurl']").val('');
		     		$("input[name='cbgcolor']").val('');
		     		$("#cbgimages_proxy").attr("src",'');
		     	}else{
		     		$("input[name='ctitle']").val(data.ctitle);
		     		$("input[name='chrefurl']").val(data.chrefurl);
		     		$("input[name='iindex']").val(data.iindex);
		     		$("input[name='cbgimageurl']").val(data.cbgimageurl);
		     		$("input[name='cbgcolor']").val(data.cbgcolor);
		     		
		     		if(data.bstatus){
		     			$("#bstatus_true").attr("checked",'checked');
		     			$("#bstatus_false").removeAttr("checked");
		     		}else{
		     			$("#bstatus_false").attr("checked",'checked');
		     			$("#bstatus_true").removeAttr("checked");
		     		}
		     		
		     		$("#cbgimages_proxy").attr("src",'/img/'+data.cbgimageurl);
		     	}
		    }
		});
	});
  
	                   		
	                   		