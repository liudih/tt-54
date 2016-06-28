$(document).on("click","a[id^=collect_faq_]",function(){
	var id = $(this).attr("tag");
	var count = $(this).attr("count");
	var isOpen = $("#faq_reply_" + id).css("display");
	if(isOpen == "none"){
		$(this).text("Collapse");
		$("#faq_reply_" + id).css("display","block");
	} else {
		$(this).text("Replies("+ count +")");
		$("#faq_reply_" + id).css("display","none");
	}
});

//faq翻页
$(function(){
	var totalPages = $("#faqs_total_page").val();
	var currentPage = $("#faqs_current_page").val();
	init(totalPages,currentPage);
});

$(document).on("click","li[id^=faqs_page_]",function(){
	var totalPages = $("#faqs_total_page").val();
	var sku = $("#hidden_sku").val();
	var pageSize = $("#faqs_page_size").val();
	var listingId = $("#faqs_listingId").val();
	var selectedPage = $(this).find("a").text();
	getPage(listingId,sku,selectedPage,pageSize,totalPages);
});

$(document).on("click","#faqs_pre_page",function(){
	var selectedPage =  $(this).attr("preValue");
	if(undefined != selectedPage){
		var totalPages = $("#faqs_total_page").val();
		var pageSize = $("#faqs_page_size").val();
		var listingId = $("#faqs_listingId").val();
		var sku = $("#hidden_sku").val();
		getPage(listingId,sku,selectedPage,pageSize,totalPages);
	}
});

$(document).on("click","#faqs_next_page",function(){
	var selectedPage =  $(this).attr("nextValue");
	if(undefined != selectedPage){
		var totalPages = $("#faqs_total_page").val();
		var pageSize = $("#faqs_page_size").val();
		var listingId = $("#faqs_listingId").val();
		var sku = $("#hidden_sku").val();
		getPage(listingId,sku,selectedPage,pageSize,totalPages);
	}
});

//显示当前页的显示样式
function init(totalPages,currentPage){
	$("li[id^=faqs_page_]").each(function(){
		var node = $(this).find("span");
		if(undefined != node){
			var id = node.text();
			node.replaceWith("<a href='javascript:void(0)'>"+ id +"</a>");
		}
	});
	$("#faqs_pre_page").removeAttr("href");
	$("#faqs_next_page").removeAttr("href");
	if(currentPage == 1 && currentPage != totalPages){
		$("#faqs_pre_page").removeAttr("preValue");
		$("#faqs_next_page").attr({href:"javascript:void(0)", nextValue: parseInt(currentPage) + 1 });
	}else if(currentPage != 1 && currentPage == totalPages){
		$("#faqs_next_page").removeAttr("nextValue");
		$("#faqs_pre_page").attr({href:"javascript:void(0)", preValue: parseInt(currentPage) - 1 });
	}else if(currentPage  == totalPages == 1 ){
		$("#faqs_next_page").removeAttr("nextValue");
		$("#faqs_pre_page").removeAttr("preValue")
	}else{
		$("#faqs_pre_page").attr({href:"javascript:void(0)", preValue: parseInt(currentPage) - 1 });
		$("#faqs_next_page").attr({href:"javascript:void(0)", nextValue: parseInt(currentPage) + 1 });
	}
	$("#faqs_page_" + currentPage).find("a").replaceWith("<span class='current'>" + currentPage + "</span>");
};

function getPage(listingId,sku,selectedPage,pageSize,totalPages){
	var dataObject = dataObject || {};
	dataObject.listingId = listingId;
	dataObject.page=selectedPage;
	dataObject.pageSize=pageSize;
	dataObject.sku=sku;
	var jsonData = JSON.stringify(dataObject);
	$.ajax({
		url:  faqs.controllers.interaction.ProductFaq.getFaqs().url,
		type: "POST",
		data: jsonData,
		contentType:"application/json" ,
		success : function(data){
			  $("#product_faqs_tbody").children().remove();
			  $("#product_faqs_tbody").append(data);
			  init_Ckeditor();
			  init(totalPages,selectedPage);
		}
	});
};

$(document).on("click","a[id^=comment_reply_]",function(){
	var isLogin = $(this).attr("tag");
	if(isLogin==false){
		return false;
	}
	var nodeId =$(this).attr("id");
	var id = nodeId.replace("comment_reply_","");
	var name = $("#answer_" + id).find(".reply_name").text();
	var ckeditorId = $(this).closest(".reply_box").attr("id").replace("faq_reply_","");
	var textareaId ="textarea_content_"+ ckeditorId;
	CKEDITOR.instances[textareaId].setData("Quote From " + name + ": ");
});

//提交faq回答
$(document).on("click","input[id^=reply_sumbmit_]",function(){
	var id = $(this).attr("id").replace("reply_sumbmit_","");
	var title = $("#faq_title_" + id).text();
	var question = $("#faq_question_" + id).text();
	
	var listingId = $("#hidden_listingid").val();
	var sku = $("#hidden_sku").val();
	var email = $("#hidden_email").val();
	var date = $("#create_date").val();
	var textareaId = "textarea_content_" + id;
	var answer =  CKEDITOR.instances[textareaId].getData();
	if(answer == ""){
		return false;
	} else if(answer.length > 600){
		alert("No more than 600 characters !");
		return false;
	}
	answer = answer.replace(/&nbsp;/g," ");
	answer = $.trim(answer);
	var dataObject = dataObject || {};
	dataObject.listingId = listingId;
	dataObject.sku = sku;
	dataObject.email = email;
	dataObject.date = date;
	dataObject.answer = answer;
	dataObject.title = title;
	dataObject.question = question;
	
	var jsonData = JSON.stringify(dataObject);
	
	$.ajax({
		url:  faqs.controllers.interaction.ProductFaq.reply().url,
		type: "POST",
		data: jsonData,
		contentType:"application/json" ,
		success : function(data){
			if(data == "success"){
				location.reload() 
			}else{
				$("faq_reply_" + id).append("<span id='submit_error'>error</span>");
			}
		}
	});
	
});