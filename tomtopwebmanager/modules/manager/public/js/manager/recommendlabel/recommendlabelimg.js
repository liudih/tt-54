define(['jquery', './model/Recommendlabel', '../imagepreview', 'jqueryjson'],function($, Recommendlabel, imagepreview){
	var imageObj = new imagepreview();
	
	$(".prvimage").change(function(){
		var id = $(this).attr('id');
		imageObj.validateImage(this, 'prv' + id);
	});
});

