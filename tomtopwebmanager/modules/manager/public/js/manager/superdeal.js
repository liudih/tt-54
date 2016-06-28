$(document).on("change","select[name=select_rootCategory]",function(){
	var type = $(this).val();
	$("#rootCategoryId_hidden_value").val(type);
	superDealFormSubmit();
});

$(document).on("click","#prePage",function(){
	var prePage = parseInt($(this).attr("tag")) -1;
	$("#page_hidden_value").val(prePage);
	superDealFormSubmit();
});

$(document).on("click","#nextPage",function(){
	var nextPage = parseInt($(this).attr("tag")) + 1;
	$("#page_hidden_value").val(nextPage);
	superDealFormSubmit();
});

$(document).on("click","a[id^=page]",function(){
	var page = parseInt($(this).attr("tag"));
	$("#page_hidden_value").val(page);
	superDealFormSubmit();
});

$("#search").click(function(){
	var sku = $("#search_sku").val();
	if(sku != ""){
		sku = textTrim(sku);
	} else {
		return false;
	}
	$("#search_hidden_sku").val(sku);
	superDealFormSubmit();
});

function  superDealFormSubmit(){
	$("#super_deal_form").submit();
}

//删除
$(document).on("click","a[name=delete_superdeal]",function(){
	var id = $(this).attr("tag");
	var sku = $(this).parent().prevAll("td[name=sku]").text();
	var r=confirm("你确定要删除" + sku + "吗 ？");
	var data = JSON.stringify({"id":id});
	if (r==true){
		$.ajax({
			url : super_deal.controllers.manager.SuperDeal.deleteSuperDeals().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode == 0){
					$("#tr_" + id).remove();
				}else if(data.errorCode){
					alert("request error");
				} 
			}
		});
	}else {
		return ;
	}
});

$(document).on("click","#show_product_information",function(){
	var node = $(this);
	$(this).nextAll("div[name=sku_information]").remove();
	var sku = $("input[name=csku]").val();
	var websiteid = $("#iwebsiteid").val();
	sku = textTrim(sku);
	if ("" == sku && "" == websiteid) {
		alert("请选择站点和sku!");
		return;
	}
	var data =JSON.stringify({"sku":sku, "websiteId": websiteid});
	$.ajax({
		url : super_deal.controllers.manager.SuperDeal.getSuperDealSkuInformation().url,
		type : "POST",
		data : data,
		contentType : "application/json",
		success : function(data) {
			if(data.errorCode != 1){
				var divHtml="<div name='sku_information'><table class='table table-striped table-hover table-bordered'><thead><tr><th width='150'>属性</th><th>值</th></thead><tbody>";
				divHtml = divHtml + "<tr><td width='150'>"+  "SKU" +"</td>" + "<td>"+data.returnData.sku+"</td></tr>";
				divHtml = divHtml + "<tr><td width='150'>"+ "ListingID" +"</td>" + "<td>"+data.returnData.listingId+"</td></tr>";
				divHtml = divHtml + "<tr><td width='150'>"+ "顶级品类" +"</td>" + "<td>"+data.returnData.rootCategoryName +"</td></tr>";
				divHtml = divHtml + "<tr><td width='150'>"+ "原价 "+"</td>" + "<td>"+data.returnData.price+"</td></tr>";
				divHtml = divHtml + "<tr><td width='150'>"+ "促销价" +"</td>" + "<td>"+data.returnData.salePrice+"</td></tr>";
				divHtml = divHtml + "<tr><td width='150'>"+ "开始时间" +"</td>" + "<td>"+data.returnData.beginDate+"</td></tr>";
				divHtml = divHtml + "<tr><td width='150'>"+ "结束时间"+"</td>" + "<td>"+data.returnData.endDate+"</td></tr>";
				divHtml = divHtml + "</tbody></table></div>"
				$("#show_product_information").after(divHtml);
				$("input[name=clistingid]").val(data.returnData.listingId);
				$("input[name=icategoryrootid]").val(data.returnData.rootCategoryId);
				$("input[name=icategoryrootName]").val(data.returnData.rootCategoryName);
				$("#hidden_sku").val(data.returnData.sku);
			} else {
				alert("没有找到该SKU的相关信息");
			}
		}
	});
});


$("input[tag=condition]").keyup(function(){
		var tt = $(this).val();
		if (isNaN(tt)){
			alert("只能输入数字");
			$(this).val("");
		}
});

$(document).on("click","#modify_search_condition",function(){
	var buttonText = $(this).text();
	if(buttonText != '保存'){
		$("input[tag=condition]").removeAttr("disabled");
		$("#modify_search_condition").text("保存");
	} else {
		var str ="";
		var priceRangeLower = $("#priceRangeLower").val();
		var priceRangeHigher =$("#priceRangeHigher").val();
		if(priceRangeLower === "" || priceRangeHigher ===""){
			str = str + "价格区间不能为空\n";
		} else {
			if(priceRangeHigher < priceRangeLower){
				str = str +  "价格区间第一个值不能大于第二个值\n";
			}
		}
		var discountRangeLower =  $("#discountRangeLower").val();
		var discountRangeHigher = $("#discountRangeHigher").val();
		if(discountRangeLower === "" || discountRangeHigher ===""){
			str = str + "折扣区间不能为空\n";
		} else if(discountRangeLower > 1 || discountRangeHigher > 1){
			str = str + "折扣区间不能大于1\n";
		} else 	if(discountRangeHigher < discountRangeLower){
			str = str +  "折扣区间第一个值不能大于第二个值\n";
		}
		var browseTimeRange =$("#browseTimeRange").val();
		var browseLimitPerLine =$("#browseLimitPerLine").val();
		var saleTimeRange =$("#saleTimeRange").val();
		var SaleLimitPerLine =$("#SaleLimitPerLine").val();
		if(browseTimeRange === ""){
			str = str + "浏览历史近天数不能为空\n";
		}
		if(browseLimitPerLine === ""){
			str =str + "浏览历史每条产品线限制数不能为空\n";
		}
		if(saleTimeRange === ""){
			str = str + "销售近天数不能为空\n";
		}
		if(SaleLimitPerLine === ""){
			str = str + "销售排名每条产品线限制条数不能为空\n";
		}
		if(str != ""){
			alert(str);
			return ;
		}
		var obj=new Object(); 
		obj.SuperDealPriceRangeLower = priceRangeLower;
		obj.SuperDealPriceRangeHigher = priceRangeHigher;
		obj.SuperDealBrowseLimitPerLine = browseLimitPerLine;
		obj.SuperDealBrowseTimeRange = browseTimeRange;
		obj.SuperDealSaleTimeRange = saleTimeRange;
		obj.SuperDealSaleLimitPerLine = SaleLimitPerLine;
		obj.SuperDealDiscountRangeLower = discountRangeLower;
		obj.SuperDealDiscountRangeHigher = discountRangeHigher;
		var data =JSON.stringify(obj);
		
		$.ajax({
			url : super_deal.controllers.manager.SuperDeal.changeSuperDealSearchCondition().url,
			type : "POST",
			data : data,
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode != 1){
					$("input[tag=condition]").attr("disabled","disabled");
					$("#modify_search_condition").text("修改查询条件");
					alert("保存成功！")
				} else {
					alert("保存错误！");
				}
			}
		});
	}
});

$(document).on("click","#add-super-deal-content-btn",function(){
	var sku1 = $("#input_sku").val();
	sku1 = textTrim(sku1);
	var sku2 = $("#hidden_sku").val();
	if(sku1 != sku2){
		alert("请先查看该sku的信息！");
		return false;
	}
	if(sku1 == ""){
		alert("sku不能为空 ！");
		return false;
	}
});

$(document).on("change","select[name=show_or_not]",function(){
	var id = $(this).attr("tag");
	var value = $(this).val();
	var bshow = "yes";
	if(value == 2){
		bshow = "no";
	}
	var data = JSON.stringify({"bshow":bshow,"id":id});
	$.ajax({
		url : super_deal.controllers.manager.SuperDeal.updateSuperDealBshow().url,
		type : "POST",
		data : data,
		contentType : "application/json",
		success : function(data) {
			if(data.errorCode != 1){
				alert("修改成功");
			} else {
				alert("修改失败");
			}
		}
	});
});

function textTrim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
} 