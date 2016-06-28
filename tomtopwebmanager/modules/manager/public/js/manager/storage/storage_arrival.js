$(function(){
	$("#search_storage_submit").click(function(){
		searchArrival(1,10);
	});
	/*$("#addArrivalBtn").bind("click",function(){
		if($.trim($("#addTargetArrival").html())!=""){
			
		}
		$("#addTargetArrival").empty();
		$("#addArrivalTem").children().clone().appendTo("#addTargetArrival");
		$("#addTargetArrival").show();
	});*/
	
});
function valid(type,storageName){
	var validResult=false;
	if($.trim(type)!="" && $.trim(storageName)!="" ){
		validResult=true;
	}
	return validResult;
};
function searchArrival(pageParam,pageSizeParam){
	var type=$("#storageHidder").val();
	var storageName=$("#storageName").val();
	var page=$.trim(pageParam)==""?1:pageParam;
	var pageSize=$.trim(pageSizeParam)==""?10:pageSizeParam;
	if(valid(type,storageName)){
		//http://localhost:9000/sysadmin/storage/storageSeach?type=2&cstorageName=USA
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

function closeStorageArrival(){
	$("#addTargetArrival").hide();
};

function pageClick(pageNum){
	searchArrival(pageNum,10);
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