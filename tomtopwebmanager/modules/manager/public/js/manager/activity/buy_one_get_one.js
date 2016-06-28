define([ 'jquery', 'jqueryjson' ], function($) {
$(document).ready(function() {
		
		require(['jquery', 'bootstrap-datetimepicker'], function(){
			 
			 $('input[name=dfromdate]').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", // 选择日期后，文本框显示的日期格式
				　　autoclose:true // 选择日期后自动关闭
				  }).on('changeDate', function(ev){
					  var startTime = $(this).val();
					  $('input[name=dtodate]').datetimepicker('setStartDate', startTime);
					  $(this).datetimepicker('hide');
				  });
			 $('input[name=dtodate]').datetimepicker({
				　　format: "yyyy-mm-dd hh:ii:ss", // 选择日期后，文本框显示的日期格式
				　　autoclose:true // 选择日期后自动关闭
				  });
			 
			 $('input[name=qty]').each(function(){
				 var aid = $(this).data('id');
				 $("#qty"+aid).attr("readOnly",true);
			 });
			 
			 $('input[name=lock]').each(function(){
				 var aid = $(this).data('id');
				 $("#lock"+aid).val('unlock');
			 });
			 
			 
			 
		});
		
		$('td[name=aid]').each(function(){
			var aid = $(this).data('id');
			var visible = $(this).data('visible');
			if(visible==false){
				$("#update"+aid).attr("disabled", true);
            	$("#stop"+aid).attr("disabled", true);
			}else{
				$("#open"+aid).attr("disabled", true);
			}
		});
	});

$(function(){
	$("#cheP").click(function(){
		if(true === this.checked){
			var parentspu = $("#cparentspu").val().trim();
			var list = [];
		   	var map = {};
		   	map['parentspu'] = parentspu;
	 		list[0] = map;
			$.ajax({
				url : "/sysadmin/copy/getsku",
				type : "get",
				data:{data: $.toJSON(list)},
				dataType : "json",
				success : function(data) {
					$.each(data, function(i,item){
						var $checkbox = "<span name='sp_checkp'><input type='checkbox' value='" + item + "' name='cskuM'/>" + item + " </span>";
						$('#cskuP').append($checkbox);
					});
				}
				});
			}else{
				$("#cskuP").find("span[name=sp_checkp]").remove();
			}
		});
	
		$("#che").click(function(){
		if(true === this.checked){
			var parentspu = $("#csubspu").val().trim();
			var list = [];
		   	var map = {};
		   	map['parentspu'] = parentspu;
	 		list[0] = map;
	 		
			$.ajax({
				url : "/sysadmin/copy/getsku",
				type : "get",
				data:{data: $.toJSON(list)},
				dataType : "json",
				success : function(data) {
					$.each(data, function(i,item){
						var $checkbox = "<span name='sp_check'><input type='checkbox' value='" + item + "' name='csku'/>" + item + " </span>";
						$('#csku').append($checkbox);
					});
				}
				});
			}else{
				$("#csku").find("span[name=sp_check]").remove();
			}
		});
	
});
	
	
		$("#fprice").blur(function(){
			var price = $("#fprice").val();
			var sku = $("#cparentspu").val().trim();
			if(price!=''){
			   	var param = param || {};
			   	param['price'] = price;
			   	param['sku'] = sku;

				$.ajax({
					url:"/sysadmin/copy/getPrice",
					type:"get",
					data:param,
		               dataType:"json",
		               success : function(data) {
			            	   if(data!=true){
			            		   alert('价格不能高于原价或低于成本价！');
			            		   $("#sform").attr("disabled", true); 
			            	   }else{
			            		   $("#sform").attr("disabled", false); 
			            	   }
		            	   }
				});
			}
		}); 
		
		$('input[name=price]').blur(function(){
			var aid = $(this).data('id');
			var price = $("#price"+aid).val();
			var sku = $("#mspu"+aid).text();
			if(price!=''){
			   	var param = param || {};
			   	param['price'] = price;
			   	param['sku'] = sku;

				$.ajax({
					url:"/sysadmin/copy/getPrice",
					type:"get",
					data:param,
		               dataType:"json",
		               success : function(data) {
			            	   if(data!=true){
			            		   alert('价格不能高于原价或低于成本价！');
			            		   $("#update"+aid).attr("disabled", true); 
			            	   }else{
			            		   $("#update"+aid).attr("disabled", false); 
			            	   }
		            	   }
				});
			}
		}); 
		
		$('a[name=mspu]').click(function(){
				var aid = $(this).data('id');
				var spu = $("#mspu"+aid).text().trim();
				var list = [];
			   	var map = {};
			   	map['spu'] = spu;
			   	map['aid'] = aid;
		 		list[0] = map;
				$.ajax({
					url : "/sysadmin/showcopy/getsku",
					type : "get",
					data:{data: $.toJSON(list)},
					dataType : "json",
					success : function(data) {
							if(data!=null && data.length>0){
								var sku ="";
								$('#mspu'+aid).text('');
								$.each(data, function(i,item){
										sku = sku + " <span>" + item + " </span> ";
								});
								$('#mspu'+aid).append(sku);
							} else {
								return ;
							}
					}
					});
			});
		
		$("a[name=spu]").click(function(){
			var aid = $(this).data('id');
			var spu = $("#spu"+aid).text().trim();
			var list = [];
		   	var map = {};
		   	map['spu'] = spu;
		   	map['aid'] = aid;
	 		list[0] = map;
			$.ajax({
				url : "/sysadmin/showcopy/getsku",
				type : "get",
				data:{data: $.toJSON(list)},
				dataType : "json",
				success : function(data) {
					if(data!=null && data.length>0){
						var sku ="";
						$('#spu'+aid).text('');
						$.each(data, function(i,item){
								sku = sku + " <span>" + item + " </span> ";
						});
						$('#spu'+aid).append(sku);
					} else {
						return ;
					}
				}
				});
		});
		
		$(".update").click(function(){
			var aid = $(this).data('id');
			var mspu = $('#mspu'+aid).data('value');
			var spu = $('#spu'+aid).data('value');
			var price = $("#price"+aid).val().trim();
			var qty = $("#qty"+aid).val().trim();
			var startDate = $("#orderdatestart"+aid).val().trim();
			var endDate = $("#orderdateend"+aid).val().trim();
			var param = param || {};
			if($('#lock'+aid).val()=='lock'){
				param['qty'] = qty;
				param['spu'] = spu;
			}else{
				param['qty']=0;
				param['spu'] = null;
			}
		   	param['aid'] = aid;
		   	param['mspu'] = mspu;
		   	param['price'] = price;
		   	param['startDate'] = startDate;
		   	param['endDate'] = endDate;
		   	
		   	$.ajax({
				url : "/sysadmin/copy/update",
				type : "get",
				data:param,
				dataType : "json",
				success : function(data) {
					alert('success!');
					$("#lock"+aid).val('unlock');
					$("#qty"+aid).attr("readOnly",true);
				}
				});
		});
		
		$(".stop").click(function(){
			var aid = $(this).data('id');
			var status = false;
			var param = param || {};
		   	param['aid'] = aid;
		   	param['status'] = status;
			$.ajax({
                type:"get",
                url:"/sysadmin/copy/status",
                data:param,
                success:function(data){
                    if(data>0){
                    	$("#update"+aid).attr("disabled", true);
                    	$("#stop"+aid).attr("disabled", true);
                    	$("#open"+aid).attr("disabled", false);
                    } 
                }
          });
			
		});
		
		$(".open").click(function(){
			var aid = $(this).data('id');
			var status = true;
			var param = param || {};
		   	param['aid'] = aid;
		   	param['status'] = status;
			$.ajax({
                type:"get",
                url:"/sysadmin/copy/status",
                data:param,
                success:function(data){
                    if(data>0){
                    	$("#update"+aid).attr("disabled", false);
                    	$("#stop"+aid).attr("disabled", false);
                    	$("#open"+aid).attr("disabled", true);
                    } 
                }
          });
			
		});
		
		$('input[name=lock]').click(function(){
			var aid = $(this).data('id');
			var status = $('#lock'+aid).val();
			if(status == 'unlock'){
				$('#lock'+aid).val('lock');
				$("#qty"+aid).attr("readOnly",false);
			}
		});
		
		
		$('#cskuP,#csku').click(function(){
			$("#sform").attr("disabled", false);
		});
		
		$("#cparentspu").blur(function(){
			if($('#cparentspu').val()!=''){
				$("#sform").attr("disabled", false);
			}
		});
		
		$("#csubspu").blur(function(){
			if($('#csubspu').val()!=''){
				$("#sform").attr("disabled", false);
			}
		});
		
		$("#qty").blur(function(){
			if($('#qty').val()!=''){
				$("#sform").attr("disabled", false);
			}
		});
		
		$("#orderdatestart").blur(function(){
			if($('#orderdatestart').val()!=''){
				$("#sform").attr("disabled", false);
			}
		});
		
		$("#orderdateend").blur(function(){
			if($('#orderdateend').val()!=''){
				$("#sform").attr("disabled", false);
			}
		});
		
		$("#sform").click(function(){
			var skuIsCheck ;
			if($("#cheP").is(":checked")){
				$('#cskuP').find('input').each(function(){
					if(true === this.checked){
						$("#sform").attr("disabled", false);
						skuIsCheck = 'true';
						return false;
					}else{
						skuIsCheck = 'false';
					}
				});
				
				if(skuIsCheck=='false'){
					$("#sform").attr("disabled", true);
					alert('Please choose SKU');
					return false;
				}
			}
			
			if($("#che").is(":checked")){
				$('#csku').find('input').each(function(){
					if(true === this.checked){
						$("#sform").attr("disabled", false);
						skuIsCheck = 'true';
						return false;
					}else{
						skuIsCheck = 'false';
					}
				});
				
				if(skuIsCheck=='false'){
					$("#sform").attr("disabled", true);
					alert('Please choose SKU');
					return false;
				}
			}
			
			if($('#cparentspu').val()!=''){
				$("#sform").attr("disabled", false);
			}else{
				$("#sform").attr("disabled", true);
				alert('Please input mainProduct!');
				return false;
			}
			
			if($('#csubspu').val()!=''){
				$("#sform").attr("disabled", false);
			}else{
				$("#sform").attr("disabled", true);
				alert('Please input freeProduct!');
				return false;
			}
			
			if($('#qty').val()!=''){
				$("#sform").attr("disabled", false);
			}else{
				$("#sform").attr("disabled", true);
				alert('Please input Qty!');
				return false;
			}
			
			if($('#fprice').val()!=''){
				$("#sform").attr("disabled", false);
			}else{
				$("#sform").attr("disabled", true);
				alert('Please input price!');
				return false;
			}
			
			if($('#orderdatestart').val()!=''){
				$("#sform").attr("disabled", false);
			}else{
				$("#sform").attr("disabled", true);
				alert('Please choose startTime!');
				return false;
			}
			
			if($('#orderdateend').val()!=''){
				$("#sform").attr("disabled", false);
			}else{
				$("#sform").attr("disabled", true);
				alert('Please choose endTime!');
				return false;
			}
			
		});
		
		
});
	