//选择国家
$(document).on("click","li[name=country_li]",function(){
	//var id = $(this).parent().attr("tag");
	var countryCode = $(this).attr("code");
	var countryValue = $(this).attr("cid");
	if(countryCode=='US'){
		 //根据选中国家，联动省
		 // 根据国家联动省的功能先注释
		 var countryValueJson = {countryValue:countryValue};
		 $.ajax({
				url : "/member/allprovince", 
				type : "POST",
				data : JSON.stringify(countryValueJson),
				contentType : "application/json",
				async: false,
				success : function(provinceJson) {
					if(null != provinceJson){
						var provinceOption="";
						var provinceDate = provinceJson.provinces;
						$("li[name=province_li]").each(function(){
							 $(this).remove();
						});
						$.each(provinceDate, function(j,pitem){
							provinceOption = provinceOption + "<li name='province_li'><span name='province' value='"+ pitem.iid  +"'>"+ pitem.cname +"</span></li>";
						});
						$("ul[name=province_list]").html(provinceOption);
						$("#province_list_id").css('display','block');	
						$("#div_province_id").show();//州下来按钮显示
						$(document).on("click","li[name=province_li]",function(){
							var provinceText = $(this).find("span[name=province]").text();
							var cprovince = $(this).parent().siblings('input[type="text"]');
							cprovince.val(provinceText);
							cprovince.next(".edit_error").remove();
							cprovince.removeClass("edit_INerror");
							$(this).parent(".province_list").css({"display":"none"});
						});
					}
			 }
		 }); 
	}else{	
		$("#div_province_id").hide();
		//$(this).parents('form').find('.current_province').val('');
		$("#province_list_id").css('display','none');
		$("li[name=province_li]").remove();
	}
});
//切换州
function toggleState(){
	$("#province_list_id").toggle();
};