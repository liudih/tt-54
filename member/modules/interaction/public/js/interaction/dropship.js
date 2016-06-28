//批量输入添加
$("#batch_add_dropship_product").on("click", function() {
	var skus = $("#textarea_skus").val();
	//skus = skus.replace(/(^\s*)|(\s*$)/g, ""); //去除前后空格
	skus = skus.replace(/\s/g, ""); //去除所有空格
	if(skus == ""){
		alert("please input sku ");
		return false;
	}
	var leftCount = $("#dropship_left_count").val();
	var data = JSON.stringify({
		"skus" : skus,
		"leftCount": leftCount
	});
	
	$.ajax({
		url : add_dropship.controllers.interaction.Dropship.batchAddDropshipProduct().url,
		type : "POST",
		data : data,
		contentType : "application/json",
		success : function(data) {
			if(data.errorCode =="over"){
				alert("At least you can add " + leftCount +" sku ,but your input is more than that ! ");
			} 
			if(data.errSkus.length==0){
				alert("add success !");
				window.location.href = add_dropship.controllers.interaction.Dropship.dropshipWishlist().url;
			} else {
				alert(data.errSkus + "\n the skus above add failure,The reason may be that you have successfully added the SKU, or that the sku does not exist. !");
				window.location.href = add_dropship.controllers.interaction.Dropship.dropshipWishlist().url;
			}
		}
	});
})


//批量删除
$("#remove_all_dropship").on("click",function(){
	var ids = getCheckedIds();
	if(ids.length>0){
		var idString = ids.join("-");
		var data = JSON.stringify({"ids" : idString});
		$.ajax({
			url : dropship_wishlist.controllers.interaction.Dropship.batchDeleteDropshipProduct().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data == "success"){
					for(var i=0;i<ids.length;i++){
						$("#tr_" + ids[i]).remove();
					}
					alert("delete success !")
				} else {
					alert("delete failure, please refresh and try again !");
				}
			}
		});
	} else {
		alert("please select at least one product !");
		return false;
	}
});

//单个删除
$("input[name=single_remove]").on("click",function(){
	var id = $(this).attr("tag");
	var data = JSON.stringify({"id" : id});
	$.ajax({
		url : dropship_wishlist.controllers.interaction.Dropship.deleteDropshipProduct().url,
		type : "POST",
		data : data,
		contentType : "application/json",
		success : function(data) {
			if(data == "success"){
				$("#tr_" + id).remove();
			} else {
				alert("delete failure");
			}
		}
	});
});

//批量设置状态
$("a[name=set_dropship_true]").on("click",function(){
	var ids = getCheckedIds();
	if(ids.length>0){
		var leftCount = $("#dropship_left_count").val();
		if(leftCount < ids.length){
			alert("At least you can add " + leftCount + " product to dropship !");
			return false;
		}
		var idString = ids.join("-");
		var data = JSON.stringify({"ids" : idString});
		$.ajax({
			url : dropship_wishlist.controllers.interaction.Dropship.batchSetDropshipProduct().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data == "success"){
					for(var i=0;i<ids.length;i++){
						$("#status_" + ids[i]).text("YES");
					}
					alert("add success !")
				} else {
					alert("add failure, please refresh and try again !");
				}
			}
		});
	} else {
		alert("please select at least one product !");
		return false;
	}
});


$("a[name=put_off]").on("click",function(){
	var id= $(this).attr("tag");
	var idJson = JSON.stringify({"id":id});
	$.ajax({
		url : dropship_list.controllers.interaction.Dropship.dropshipPutOff().url,
		type : "POST",
		data : idJson,
		contentType : "application/json",
		success : function(data){
			if(data =="success"){
				$("#tr_" + id).remove();
				window.location.reload();
			} else {
				alert("put off  failure, please refresh and try again !");
			}
		}
	});
});


$("#batch_put_off").on("click",function(){
	var ids= getCheckedIds();
	if(ids.length>0){
		var idString = ids.join("-");
		var data = JSON.stringify({"ids" : idString});
		$.ajax({
			url : dropship_list.controllers.interaction.Dropship.batchPutOffDropshipProduct().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data == "success"){
					window.location.reload();
				} else {
					alert("delete failure, please refresh and try again !");
				}
			}
		});
	} else {
		alert("please select at least one product !");
		return false;
	}
});

function getCheckedIds(){
	var ids = new Array();
	$("span[name=dropship_checkbox]").each(function(){
		var nodeclass = $(this).attr("class");
		if(nodeclass == "afters"){
			var id = $(this).attr("tag");
			ids.push(id);
		}
	});
	return ids;
}