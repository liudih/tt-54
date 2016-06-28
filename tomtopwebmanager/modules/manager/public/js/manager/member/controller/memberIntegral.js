define(['jquery','../model/MemberIntegral','jqueryjson'],function($,MemberIntegral){
	var memberObj = new MemberIntegral();
	
	$("#search_membersIntegral_submit").click(function(){
		memberObj.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		memberObj.changePageSize(this);
		memberObj.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		memberObj.changePageNum(this);
		memberObj.submit();
	});
	
	$("form").on("submit", function(){
		memberObj.editMemberIntegral(this);
		return false;
	});
	
	$(document).ready(function() {
		memberObj.initTable();
	});
	
	memberObj.valideIntegral();
});

