$(function(){
	$(".dis").click(function(){
		var id = $(this).data('id');
		var status;
		var url = "/sysadmin/coupon/relation/updateStatus";
		if($("#dis"+id).html().trim()=='禁用'){
			status = false;
			var param = param||{};
			param.id = id;
			param.status = status;
			$.get(url,param,function(data){
				if(data){
					alert('修改成功！');
					$("#dis"+id).html('启用');
				}
			});
		}else{
			status = true;
			var curl = "/sysadmin/coupon/relation/checkStatus";
			var param = param || {};
			param.sku = $("#sku"+id).html().trim();
			$.get(curl,param,function(data){
				if(data){
					alert('已存在启用的sku!');
					return false;
				}else{
					var p = p||{};
					p.id = id;
					p.status = status;
					$.get(url,p,function(data){
						if(data){
							alert('修改成功！');
							$("#dis"+id).html('禁用');
						}
					});
				}
			});
		}
		
	});
	
	$(".del").click(function(){
		var id = $(this).data('id');
		var url = "/sysadmin/coupon/relation/delRelation";
		var param = param||{};
		param.id = id;
		$.get(url,param,function(data){
			if(data){
				alert('删除成功！');
				$("#tr"+id).remove();
			}
		});
	});
	
	$("#addF").click(function(){
		var sku = $("#csku").val();
		if(sku==''){
			alert('请填写sku!');
			return false;
		}
		var url = "/sysadmin/coupon/relation/checkSku";
		var param = param||{};
		param.sku = sku;
		$.get(url,param,function(data){
			if(data){
				alert('sku无效！');
				return false;
			}else{
				var status = $("#isEnabled").val();
				if(status=='true'){
					var p = p || {};
					p.sku = sku;
					var curl = "/sysadmin/coupon/relation/checkStatus";
					$.get(curl,p,function(data){
						if(data){
							alert('已存在启用的sku!');
							return false;
						}else{
							$("#relation-add-form").submit();
						}
					});
				}else{
					$("#relation-add-form").submit();
				}
				
			}
		});
		
	});
	
});