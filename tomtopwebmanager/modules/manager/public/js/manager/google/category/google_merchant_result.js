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
	$("#backMerchantProductBtn").click(function(){
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
			pushBackProductMerchants(nodeidList);
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
	
	$("#repullBackMerchantProductBtn").click(function(){
		if($("#selectCheckbox tr input")!=null && $("#selectCheckbox tr input").size()>0){
			/*var nodeidList="";
			$.each($("#selectCheckbox tr input"),function(index,item){
				if($(item).parent().attr("class")=="checked"){
					nodeidList=$(item).attr("id")+"_"+nodeidList;
				}
			});
			if($.trim(nodeidList)==""){
				alert("请选择刊登产品");
				return;
			}*/
			//repullBackProductMerchants(nodeidList);
			//var a=new Array();a.push("aaa");alert(a);
			var params="";
			$.each($("#selectCheckbox tr input"),function(index,item){
				if($(item).parent().attr("class")=="checked"){
					var id=$(item).parents("tr").attr("id");
					if($.trim(id)!=""){
						params=params+"_"+id;
					}
				}
			});
			if($.trim(params)==""){
				alert("请选择刊登产品");
				return;
			}
			pullMerchantsProductByProductCnodeidList(params);
		}
	});
	function deleteProductMerchants(params){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_ConfigGoogleFeeds.controllers.manager.google.category.GoogleFeeds.deleteGoogleBackRecords(params).url,
			success:function(data){
				location.reload();
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error"); 
			}
		});
	}
	function pushBackProductMerchants(params){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_ConfigGoogleFeeds.controllers.manager.google.category.GoogleFeeds.pushSelectMerchantProduct(params).url,
			success:function(data){
				if(data!=null && "N"!=data){
					$.each($("#selectCheckbox tr input"),function(index,item){
						$.each(data,function(_index,_item){//封装的值
							if($(item).attr("id")==_index){
								$(item).parent().parent().parent().parent().find(".skuResult").html(_item);
							}
						});
					});
					alert("complete");
				}else{
					alert("params error");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error"); 
			}
		});
	}
	function repullBackProductMerchants(params){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_ConfigGoogleFeeds.controllers.manager.google.category.GoogleFeeds.pullMerchantsProductByIdList("","",params,"").url,
			success:function(data){
					alert(data);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error"); 
			}
		});
	}
	function pullMerchantsProductByProductCnodeidList(params){
		$.ajax({
			 type: "POST",
			async:false,
			//dataType:"xml",
			url:js_ConfigGoogleFeeds.controllers.manager.google.category.GoogleFeeds.pullMerchantsProductByProductCnodeidList(params).url,
			success:function(data){
					alert(data);
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


