var checkflag=false;
var options={
		   errorElement: 'span', 
	       errorClass: 'help-inline', 
	       focusInvalid: false,
	       ignore: "", 
           
	     rules: {
	    	  ctitle: {
	    	      required: true
	    	  },
	    	 
	    	  curl: {
	    	      required: true,
	    	      url:true
	    	  },
	    	  bfile: {
	    	      required: true
	    	  }
	     },
	     messages:{
	    	 ctitle: "请输入广告标题",
	    	 curl: {
	    		 required:"请输广告连接网址",
	    		 url:"请输入合法的网址"
	    	 },
	         dwidth:{
	        	 required:"请输广告宽度",
	        	 number:"请输入合法的数字"
	         },
	        dheight:{
	        	 required:"请输广告高度",
	        	 number:"请输入合法的数字"
             },
	        bfile:{
	        	required:"请选择图片文件"
	        }
	     },
	     errorPlacement: function(error, element) {
	    	   
	    	    if(element[0].name==='bfile'){
	    	    	$('#file-error-msg').html('').append(error);
	    	    }else{
	    	    	element.after(error);
	    	    }
	    	    
	     },
	     highlight: function (element) { 
	         var parens=$(element).parents('.tab-pane');
	         var tabid=parens.attr('id');
	         var target=$('.tabbable').find('a[data-toggle=tab][href=#'+tabid+']');
	         if(!checkflag){
	        	 target.click();
	        	 checkflag=true;
	        	 return;
	         }
	         
	     },

	     unhighlight: function (element) {
	    	 checkflag=false;
	         
	     },

	     success: function (label) {
	        
	     },
	     submitHandler: function (form) {
	    	form.submit();
	     },
	     error:function(){

	     }

    }
	var $addform=$("#add-banner-form");
	var $editform=$("#edit-banner-form");
	$addform.validate(options);
	delete options.rules.bfile;
	delete options.messages.bfile;
	$editform.validate(options); 
	function fnUploadError(file,element){
		var msg=file.name+'不是图片文件格式';
		$('#file-error-msg').html('').append('<span class="help-inline">'+msg+'</span>');
		$('.fileupload').removeClass('fileupload-exists').addClass('fileupload-new');
		element.val('');
	} 
	function fnChange($element,$input){
		var target=$input.data('target');
		if(target==undefined){
			return;
		}
		var val=$input.val();
		if(val===''){
			$(target).attr('disabled',true)
		}else{
			$(target).attr('disabled',false)
			
		}
		
	}	
	function fnUploadSuccess(file,element){
		$('#file-error-msg').html('');
		return false;
	}
	var color_options={
			layout:'hex',
			submit:0,
			colorScheme:'dark',
			onChange:function(hsb,hex,rgb,el,bySetColor) {
				$(el).css('border-color','#'+hex);
				$('#edit_previewcolor').css('background-color','#'+hex);
				if(!bySetColor) $(el).val('#'+hex);
			}
		}	
   $("#edit-banner-modal").on("hidden", function() {
	    $(this).removeData("modal");
	}).on("show", function() {
		var $custom=$('.edit_custom');
	    if(!$custom.hasClass('en')){
	    	var $bgcolor=$('#edit_picker');
	    	$bgcolor.colpick(color_options).keyup(function(){
				$(this).colpickSetColor(this.value);
			});
	    	
			$custom.addClass('en');
	    }
	});
  var total=0;
  var QueryParam=function(){
	  this.storage=[];
  };
  QueryParam.prototype.add=function(name,value){
	  if(typeof name!='string'){
		  return;
	  }
	  for(var i in this.storage){
		  if(value==this.storage[name]){
			  return;
		  }
		  
	  }
	  this.storage[name]=value;
  }
  QueryParam.prototype.get=function(name){
	  return this.storage[value];
  }
  var queryParam=new QueryParam;
  function loadData(o){
	  if(o!=undefined){
		  queryParam.add(o.name,o.value)
	  }
	  var oSettings = table.fnSettings();
      table.fnClearTable(0);
      table.fnDraw();
  }
  
  function up(iid){
	  var url=js_bannerRoutes.controllers.manager.Banner.up(iid).url;
	  $.post(url,{},function(data,status,xhr){
		  if(status=='success' && data.errorCode=='2'){
			  loadData();
		  }
	  });
  }
  function down(iid){
	  var url=js_bannerRoutes.controllers.manager.Banner.down(iid).url;
	  $.post(url,{},function(data,status,xhr){
		  if(status=='success' && data.errorCode=='2'){
			  loadData();
		  }
	  });
  }
  function del(iid){
	  var url=js_bannerRoutes.controllers.manager.Banner.delete(iid).url;
	  $.post(url,{},function(data,status,xhr){
		  if(status=='success' && data.errorCode=='2'){
			  loadData();
		  }
	  });
  }
  var table = $('#banner_table').dataTable({
      "aLengthMenu": [
          [5, 15, 20, -1],
          [5, 15, 20, "All"]
      ],
      "iDisplayLength": 5,
      "sPaginationType": "bootstrap",
      "oLanguage": {
    	   "sProcessing": "正在加载中......",
           "sLengthMenu": "每页显示 _MENU_ 条记录",
           "sZeroRecords": "对不起，查询不到相关数据！",
           "sEmptyTable": "表中无数据存在！",
           "sInfo": "当前显示 _START_ 到 _END_ 条，共 _TOTAL_ 条记录",
           "sInfoFiltered": "数据表中共为 _MAX_ 条记录",
           "sSearch": "搜索",
           "sInfoEmpty" : "没有数据",
           "sZeroRecords" : "没有数据",
          "oPaginate": {
        	  "sFirst": "首页",
              "sPrevious": "上一页",
              "sNext": "下一页",
              "sLast": "末页"
          }
      },
      "aoColumns":[ 
               	{ "mData": "iindex", 'sClass':'center'},
               	{ "mData": "ctitle", 'sClass':'center'},
               	{ "mData":null,"mRender":function(data,type,aaData){
               	     var html='';
	               	 if(aaData.iindex<total){
	               	   html+='<a class="btn btn-sm  green" href="javascript:void(0)" data-index="'+aaData.iindex+'" data-order data-action="up"><i class="icon-angle-up"></i></a>';
	               	 }else{
	               	   html+='<a class="btn btn-sm  default" href="javascript:void(0)" ><i class="icon-angle-up"></i></a>';
	               	 }
	                 if(aaData.iindex>1){
	                	  html+='<a class="btn btn-sm  red" href="javascript:void(0)" data-index="'+aaData.iindex+'" data-order data-action="down"><i class="icon-angle-down"></i></a>';
	                 }else{
	                	  html+='<a class="btn btn-sm  default" href="javascript:void(0)" ><i class="icon-angle-down"></i></a>';
	                 }
	                 
          	         return html;
               	 }
               	},
            	{ "mData": "website", 'sClass':'center'},
               	{ "mData": "language", 'sClass':'center'},
               	
               	{ "mData":null,"mRender":function(data,type,aaData){
               		        return '<img src="'+js_bannerRoutes.controllers.manager.Banner.at(aaData.iid).url+'" alt="'+aaData.ctitle+'" style="max-height:46px;max-width:182px;">'
						   }
               	},
             	{ "mData": "dcreatedate", 'sClass':'center'},
               	{"mData":null, "mRender":function(data,type,aaData){
       		        return '<a class="edit" href="'+js_bannerRoutes.controllers.manager.Banner.editForm(aaData.iid).url+'" data-toggle="modal" '+
                           'data-target="#edit-banner-modal">编辑</a>'
				   }
       	        },
       	        { "mData":null,"mRender":function(data,type,aaData){
       	        	
     		        return '<a class="delete" href="javascript:void(0)" data-id="'+aaData.iid+'"  data-action="del" >删除</a>'
				   }
     	        }
               ],
      "bProcessing":true,
      "bServerSide":true, 
      "sAjaxSource":js_bannerRoutes.controllers.manager.Banner.list().url, 
      "fnServerParams": function ( aoData ) {
    	  var start,totail,page,pageSize;
    	  for(var i in aoData){
    		  if(aoData[i].name=='iDisplayStart'){
    			  start=aoData[i].value;
    		  }
    		  if(aoData[i].name=='iDisplayLength'){
    			  totail=aoData[i].value;
    			  pageSize=aoData[i].value;
    		  }
    	  }
    	  page=(start==0)?1:start/totail+1;
          aoData.push( { "name": "page", "value": page } );
          aoData.push( { "name": "pageSize", "value": pageSize } );
          for(var i in queryParam.storage){
        	  var storage=queryParam.storage[i]
        	  aoData.push( { "name":i, "value": storage } );
          }
      },
      "fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
    	  
          oSettings.jqXHR = $.ajax( {
            "dataType": 'json',
            "type": "GET",
            "url": sSource,
            "data": aoData,
            "success": function(data,status,xhr){
            	total=data.iTotalRecords
            	fnCallback(data,status,xhr);
            	
             }
          } );
        }
      
  });
  $(document).on('click','a[data-order]',function(e){
	  var index=$(this).data('index');
	  var action=$(this).data('action')
	  window[action](index);
	  e.preventDefault();
  });
  
  $(document).on('click','a[data-action=del]',function(e){
	  var id=$(this).data('id');
	  del(id);
	  e.preventDefault();
  });
  