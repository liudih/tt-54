define(['jquery','../model/WholeSale','jqueryjson'],function($,WholeSale){
	var wholeSale = new WholeSale();
	
	$("#search_wholesale_submit").click(function(){
		wholeSale.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		wholeSale.changePageSize(this);
		wholeSale.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		wholeSale.changePageNum(this);
		wholeSale.submit();
	});
	
	$(document).ready(function() {
		wholeSale.initTable();
	});
	
	$(document).on("click","#updatestatus li",function(){
		wholeSale.updateWholeSaleStatus(this);
	});
});

