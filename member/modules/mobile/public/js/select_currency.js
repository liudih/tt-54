$(function(){
	
	$(".lineBlock.aciClick.lbBox").click(function(e) {
		var code=$(this).children("p:last-child").attr("code");
		var dataObject = dataObject || {};
		dataObject['currencyCode'] = code;	
		var json=JSON.stringify(dataObject);
		$.ajax({
			url : js_routes.controllers.base.Regional.regionalSettings().url,
			type : "post",
			data : json,
			contentType : "application/json",
			success : function(data) {
				window.location.reload();
			}
		});
	})
})



