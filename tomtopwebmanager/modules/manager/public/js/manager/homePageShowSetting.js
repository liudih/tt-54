$(document).on("click","#select_label_type",function(){
	var selectedValue = $(this).val();
	$("#search_hidden_labelType").val(selectedValue);
});

$(document).on("click","#select_site",function(){
	var selectedValue = $(this).val();
	$("#search_hidden_siteId").val(selectedValue);
});

$(document).on("click","#search",function(){
	var siteId = $("#search_hidden_siteId").val();
	if("" == siteId){
		$("#search_hidden_siteId").val(1);
	}
	var sku = $("#search_sku").val();
	sku = $.trim(sku);
	$("#search_hidden_sku").val(sku);
	$("#page_hidden_value").val(1);
	$("#productlabel_manger_form").submit();
});

$(document).on("click","a[name=page_a]",function(){
	var pageValue = $(this).attr("value");
	var websiteId = $("#search_hidden_siteId").val();
	var type= $("#search_hidden_labelType").val();
	var ahref = product_label.controllers.manager.HomePageProductShowSetting.pageProductShowSetting(pageValue,websiteId,"",type).url; 
	location.href = ahref;
});