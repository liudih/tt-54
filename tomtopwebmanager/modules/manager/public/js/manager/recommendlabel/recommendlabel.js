define(['jquery', './model/Recommendlabel', '../imagepreview', 'jqueryjson'],function($, Recommendlabel, imagepreview){
	var recommendlabelobj = new Recommendlabel();
	
	$("#search_recommendlabelbase_submit").click(function(){
		recommendlabelobj.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		recommendlabelobj.changePageSize(this);
		recommendlabelobj.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		recommendlabelobj.changePageNum(this);
		recommendlabelobj.submit();
	});
	
});
