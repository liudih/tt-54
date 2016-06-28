$(function(){
	$("#selectProductBtn").click(function(){
		if($("#selectCheckbox tr input")!=null && $("#selectCheckbox tr input").size()>0){
			var _selectFlag=true;
			$.each($("#selectCheckbox tr input"),function(index,item){
				if($(item).parent().attr("class")!="checked"){
					_selectFlag=false;
				}
				//$(item).parent().addClass("checked");
			});
			$.each($("#selectCheckbox tr input"),function(index,item){
				//全选
				if(_selectFlag){
					$(item).parent().removeClass();
					$("#selectProductBtn").val("全选");
				//全部取消	
				}else{
					$(item).parent().addClass("checked");
					$("#selectProductBtn").val("取消全选");
				}
			});
		}
	});
	$("#merchantProductBtn").click(function(){
		if($("#selectCheckbox tr input")!=null && $("#selectCheckbox tr input").size()>0){
			var nodeidList="";
			$.each($("#selectCheckbox tr input"),function(index,item){
				if($(item).parent().attr("class")=="checked"){
					nodeidList=$(item).attr("id")+"_"+nodeidList;
				}
			});
			if($.trim(nodeidList)==""){
				alert("请选择刊登产品");
				return;
			}
			pushProductMerchants(nodeidList);
		}
	});
	$("#deleteProductBtn").click(function(){
		if($("#selectCheckbox tr input")!=null && $("#selectCheckbox tr input").size()>0){
			var nodeidList="";
			$.each($("#selectCheckbox tr input"),function(index,item){
				if($(item).parent().attr("class")=="checked"){
					nodeidList=$(item).attr("id")+"_"+nodeidList;
				}
			});
			if($.trim(nodeidList)==""){
				alert("请选择所需要删除的产品");
				return;
			}
			deleteProductMerchants(nodeidList);
		}
	});
	function deleteProductMerchants(params){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.deleteMerchantProductConfigData(params).url,
			success:function(data){
				location.reload();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error"); 
			}
		});
	}
	function pushProductMerchants(params){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.pushMerchantProductConfigData(params).url,
			success:function(data){
				if(data!=null && "N"!=data){
					$.each($("#selectCheckbox tr input"),function(index,item){
						$.each(data,function(_index,_item){//封装的值
							if($(item).attr("id")==_index){
								$(item).parent().parent().parent().parent().find(".skuResult").html(_item);
							}
						});
					});
				}else{
					alert("params error");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error"); 
			}
		});
	}
});

function pageSubmit(index){
	$("#formpage").attr("value",index);
	$("#searchBtn").click();
}