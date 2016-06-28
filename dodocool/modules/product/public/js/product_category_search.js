$(document).on("click","a[name=next_page]",function(){
	var $node =$(this); 
	var categoryId = $(this).attr("tag");
	var page = $(this).attr("p");
	var object = jsonData || {};
	object.categoryId = categoryId;
	object.page = page;
	var jsonData = JSON.stringify(object);
	var url =  category_product.controllers.product.CategoryProduct.getNextPageCategoryProductData().url;
	$.ajax({
		url: url,
		data:jsonData,
		type:"POST",
		contentType:"application/json",
		success: function(data){
			var count = data.count;
			var html = data.html;
			$node.parent().before(html);
			$node.attr("p", parseInt(page)+1);
			if(count<8){
				$node.parent().remove();
			}
		}
	});
});
