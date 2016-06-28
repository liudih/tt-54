define(['jquery','../model/DropShip','jqueryjson'],function($,DropShip){
	var dropship = new DropShip();
	
	$("#search_dropship_submit").click(function(){
		dropship.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		dropship.changePageSize(this);
		dropship.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		dropship.changePageNum(this);
		dropship.submit();
	});
	
	$(document).ready(function() {
		dropship.initTable();
	});
	
	$(document).on("click","#updatestatus li",function(){
		dropship.updateDropShipStatus(this);
	});
	
	$(document).on("click","#updatelevel li",function(){
		dropship.updateDropShipLevel(this);
	});
});

