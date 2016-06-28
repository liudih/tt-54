$(function(){
	$(".firThisBox a").each(function(i,e){
		var jdom = $(this);
		var dname = jdom.attr("data-name");
		var dval = jdom.attr("data-value");
		var isexist = $("#searchbar input[name='"+dname+"'][value='"+dval+"']").length>0;
		if(isexist){
			jdom.find("span:eq(0)").addClass("afters");
			jdom.parent().addClass("thisParAci");
		}
	});
})
/**
 * 进入时先remove过滤条件，再判断是否添加相应过滤参数
 * 过滤条件只删除自己类型的 hidden input
 */
$(".firThisBox a").click(function(){
	var jdom = $(this);
	if(jdom.hasClass("arrayLess")||jdom.hasClass("arrayMore")||jdom.hasClass("arrayLine") ){
		return ;
	}
	var iname = jdom.attr("data-name");
	var dval = jdom.attr("data-value");
	
	if(dval == 'freeShipping'){
		$("#searchbar").find("input[name='productType'][value='freeShipping']").remove();
	}else if(dval == 'new'){
		$("#searchbar").find("input[name='productType'][value='new']").remove();
	}else{
		$("#searchbar").find("input[name='"+iname+"'][value='"+dval+"']").remove();
	}

	//点击过滤条件时需要特殊处理
	var newnode = $(this).find("span:eq(0)");
	if(!newnode.hasClass('afters')){
		$("#searchbar").append('<input type="hidden" value="'+dval+'" name="'+iname+'">');
	}
	$("#p").val(1);
	$("#searchbar").submit();
});
