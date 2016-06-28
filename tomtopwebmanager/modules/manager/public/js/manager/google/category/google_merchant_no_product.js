$(function(){
	$("#showBtn").click(function(){
		var sku=$("#csku").val();
		var language=$("#clanguage").val();
		var targetcountry=$("#ctargetcountry").val();
		if($.trim(sku)=="" && $.trim(language)=="" && $.trim(targetcountry)=="" ){
			alert("请输入查询条件");
			return;
		}
		$("#searchNoPublishBtn").click();
	});
});

