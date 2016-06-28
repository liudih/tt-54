define(['jquery', './model/Categorylabel', '../imagepreview', 'jqueryjson'],function($, Categorylabel, imagepreview){
	var categorylabelobj = new Categorylabel();
	
	var imageObj = new imagepreview();
	
	$("#search_categorylabelbase_submit").click(function(){
		categorylabelobj.submit();
	});
	
	$("a[tag=pageSize]").click(function(){
		categorylabelobj.changePageSize(this);
		categorylabelobj.submit();
	});
	
	$("a[tag=pageNum]").click(function(){
		categorylabelobj.changePageNum(this);
		categorylabelobj.submit();
	});
	
	$('#categoryLabelForm').submit(function(){
		categorylabelobj.addCategoryLabel();
		return false;
	});
	
	$('#type').change(function(){
		categorylabelobj.changeSelect();
	});
	
	$('#siteId').change(function(){
		categorylabelobj.changeSelect();
	});
	
	$('#oderlist a.delete').on('click', function (e) {
		categorylabelobj.deleteCategoryLabel(this);
    });
	
	$(".prvimage").change(function(){
		var id = $(this).attr('id');
		imageObj.validateImage(this, 'prv' + id);
	});
});

$(function(){
	$("#add-categorylabel-modal").on("hidden", function() {
	    $(this).removeData("bs.modal");
	});
});