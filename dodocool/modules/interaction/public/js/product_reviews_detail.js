//商品详情页面评论翻页
$(function(){
	var totalPages = $("#reviews_total_page").val();
	var currentPage = $("#reviews_current_page").val();
	init(totalPages,currentPage);
});

$(document).on("click","li[id^=reviews_page_]",function(){
	var totalPages = $("#reviews_total_page").val();
	var pageSize = $("#reviews_page_size").val();
	var listingId = $("#reviews_listingId").val();
	var selectedPage = $(this).find("a").text();
	getPage(listingId,selectedPage,pageSize,totalPages);
});

$(document).on("click","#reviews_pre_page",function(){
	var selectedPage =  $(this).attr("preValue");
	if(undefined != selectedPage){
		var totalPages = $("#reviews_total_page").val();
		var pageSize = $("#reviews_page_size").val();
		var listingId = $("#reviews_listingId").val();
		getPage(listingId,selectedPage,pageSize,totalPages);
	}
});

$(document).on("click","#reviews_next_page",function(){
	var selectedPage =  $(this).attr("nextValue");
	if(undefined != selectedPage){
		var totalPages = $("#reviews_total_page").val();
		var pageSize = $("#reviews_page_size").val();
		var listingId = $("#reviews_listingId").val();
		getPage(listingId,selectedPage,pageSize,totalPages);
	}
});

//显示当前页的显示样式
function init(totalPages,currentPage){
	$("li[id^=reviews_page_]").each(function(){
		var node = $(this).find("span");
		if(undefined != node){
			var id = node.text();
			node.replaceWith("<a href='javascript:void(0)'>"+ id +"</a>");
		}
	});
	$("#reviews_pre_page").removeAttr("href");
	$("#reviews_next_page").removeAttr("href");
	if(currentPage == 1 && currentPage != totalPages){
		$("#reviews_pre_page").removeAttr("preValue");
		$("#reviews_next_page").attr({href:"javascript:void(0)", nextValue: parseInt(currentPage) + 1 });
	}else if(currentPage != 1 && currentPage == totalPages){
		$("#reviews_next_page").removeAttr("nextValue");
		$("#reviews_pre_page").attr({href:"javascript:void(0)", preValue: parseInt(currentPage) - 1 });
	}else if(currentPage  == totalPages == 1 ){
		$("#reviews_pre_page").removeAttr("preValue")
		$("#reviews_next_page").removeAttr("nextValue");
	}else{
		$("#reviews_pre_page").attr({href:"javascript:void(0)", preValue: parseInt(currentPage) - 1 });
		$("#reviews_next_page").attr({href:"javascript:void(0)", nextValue: parseInt(currentPage) + 1 });
	}
	$("#reviews_page_" + currentPage).find("a").replaceWith("<span class='current'>" + currentPage + "</span>");
};

function getPage(listingId,selectedPage,pageSize,totalPages){
	var dataObject = dataObject || {};
	dataObject.listingId = listingId;
	dataObject.page=selectedPage;
	dataObject.pageSize=pageSize;
	var jsonData = JSON.stringify(dataObject);
	$.ajax({
		url:  reviews.controllers.interaction.ProductReviews.getReviews().url,
		type: "POST",
		data: jsonData,
		contentType:"application/json" ,
		success : function(data){
			  $("#product_reviews_tbody").children().remove();
			  $("#product_reviews_tbody").append(data);
			  init(totalPages,selectedPage);
		}
	});
};