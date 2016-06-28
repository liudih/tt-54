$(function(){
	$("#search_storage_submit").click(function(){
		searchDefault(1,10);
	});
	/*$("#addDefaultBtn").bind("click",function(){
		if($.trim($("#addTargetArrival").html())!=""){
			
		}
		$("#addTargetDefault").empty();
		$("#addDefaultTem").children().clone().appendTo("#addTargetDefault");
		$("#addTargetDefault").show();
	});
	*/
});
function valid(type,storageName){
	var validResult=false;
	if($.trim(type)!="" && $.trim(storageName)!="" ){
		validResult=true;
	}
	return validResult;
};
function searchDefault(pageParam,pageSizeParam){
	var type=$("#storageHidder").val();
	var storageName=$("#storageName").val();  
	var page=$.trim(pageParam)==""?1:pageParam;
	var pageSize=$.trim(pageSizeParam)==""?10:pageSizeParam;
	if(valid(type,storageName)){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_StorageManager.controllers.manager.storage.StorageManagerController.storageParentSubList(type,storageName,page,pageSize).url,
			success:function(data){   
				$("#storage_arrival_table_list").html(data);  
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				
			}
		});
		
	}
}
function closeStorageDefault(){
	$("#addTargetDefault").hide();
};
function pageClick(pageNum){
	searchDefault(pageNum,10);
};
function deleteRecord(id,idRecord){
	var type=$("#storageHidder").val();
	if(delValid(type,id)){
		$.ajax({
			type: "GET",
			url:js_StorageManager.controllers.manager.storage.StorageManagerController.deleteStorageRecord(type,id).url,
			success:function(data){  
				if($.trim(data)==""){
					$("#"+idRecord).empty();
				}else{
					alert(data);
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				
			}
		});
		
	}
}
function delValid(type,id){
	return ($.trim(type)!="" && id>0)?true:false;
}
function showDialog(id){
	var targetId=$("#"+id).parent().attr("data-target");
	$(targetId).show();
};