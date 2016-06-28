$(function(){
	//子类目高亮选中
	var cate = parseInt($(".category:eq(0)").val());
	$(".subCate_nav li").removeClass("aciCss");
	$(".subCate_nav li").each(function(i,e){
		var node = $(this);
		var ca = parseInt(node.attr("data"));
		if((ca==cate) || (isNaN(cate) && node.attr("data")=="all") ){
			node.addClass("aciCss");
			return ;
		}
	});
	if($(".subCate_nav .aciCss").length==0){
		$(".subCate_nav li:eq(0)").addClass("aciCss");
	}
	//排序高亮显示
	$(".stortNav_ul li").each(function(i,e){
		var sortcls = $(this).attr("data-name");
		var isshow = $("."+sortcls).length>0;
		if(isshow){
			$(this).addClass("aciCss");
		}
	});
	//高亮div的左边距离
	var nowDivLeft = $(".subCate_nav .aciCss:eq(0)").position().left;
	//屏幕中间的位置
	var mid = screen.availWidth/2;
	//滚动条左边宽度
	var sl = $(".subCate_navLine:eq(0)").scrollLeft();
	//高亮类目居中
	if((nowDivLeft-sl) > mid){
		$(".subCate_navLine:eq(0)")[0].scrollLeft = (nowDivLeft);
	}
	//过滤选中
	var isf = $("#searchbar input[value='freeShipping']").length>0;
	var isn = $("#searchbar input[value='new']").length>0;
	var isprice = $("#searchbar input[name='pricerange']").length>0;
	if(isf){
		$(".slider_buttonClick:eq(0)").addClass("slider_buttonOpen");
	}
	if(isn){
		$(".slider_buttonClick:eq(1)").addClass("slider_buttonOpen");
	}
	if(isprice){
		var v = $("#searchbar input[name='pricerange']:eq(0)").val();
		v = decodeURIComponent(v);
		var arr = v.split(":");
		$('#minPrice').val(arr[0]);
		$('#maxPrice').val(arr[1]);
	}
});
$(".subCate_nav li").click(function(){
	var val = $(this).attr("data");
	$(".category").remove();
	if(val=="all"){
		$("#searchbar").submit();
		return;
	}
	$("#searchbar").append('<input class="sortCls category" type="hidden" name="category" value="'+val+'" />');
	$("#searchbar").submit();
});
$(".stortNav_ul li").click(function(i,e){
	//删除已存在的排序属性
	$(".sortCls").each(function(i,e){
		var node = $(this);
		if(!node.hasClass("category") && !node.hasClass("subcategory") &&
				!node.hasClass("pricerange") && !node.hasClass("productType") ){
			node.remove();
		}
	});
	var jdom = $(this);
	var iname = jdom.attr("data-name");
	var dval = jdom.attr("data-value");
	var isasc = getQueryString(iname);
	var isa = "desc";
	if(isasc!=null && isasc!="" && isasc=="desc"){
		isa = "asc";
	}
	switch(iname){
		case "popularity":
			$("#searchbar").append('<input type="hidden" value="'+isa+'" name="popularity">');
			break;
		case "productType":
			/**
			 * 进入时先remove过滤条件，再判断是否添加相应过滤参数
			 * 过滤条件只删除自己类型的 hidden input
			 */
			if(dval == 'freeShipping'){
				$("#searchbar").find("input[name='productType'][value='freeShipping']").remove();
			}else if(dval == 'new'){
				$("#searchbar").find("input[name='productType'][value='new']").remove();
			}
			$("#searchbar").append('<input type="hidden" value="'+dval+'" name="'+iname+'">');
			break;
		case "discount":
			$("#searchbar").append('<input type="hidden" value="'+isa+'" name="discount">');
			break;
		case "price":
			$("#searchbar").append('<input type="hidden" value="'+isa+'" name="price">');
			break;
		case "review":
			$("#searchbar").append('<input type="hidden" value="'+isa+'" name="review">');
			break;
		case "sale":
			$("#searchbar").append('<input type="hidden" value="'+isa+'" name="sale">');
			break;
	}
	
	$("#searchbar").submit();
});
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}
function resetFilter(){
	$(".pricerange,.productType").remove();
	$("#searchbar").submit();
}
function applyFilter(){
	$(".pricerange,.productType").remove();
	var minPrice = parseFloat($('#minPrice').val());
	var maxPrice = parseFloat($('#maxPrice').val());
	var isprice = true;
	if(minPrice=="" || isNaN(minPrice)){
		minPrice = " ";
	}
	if(maxPrice=="" || isNaN(maxPrice)){
		maxPrice = " ";
	}
	if(minPrice>maxPrice&&maxPrice!=' '){
		isprice = false;
	}
	if(minPrice==' '&&maxPrice==' '){
		isprice = false;
	}
	if(isprice){
		var pricerange = minPrice + ":" + maxPrice;
		$("#searchbar").append('<input type="hidden" name="pricerange" value="' + pricerange + '" />');
	}
	//过滤按钮
	$(".filterTab").each(function(i,e){
		var jdom = $(this);
		var isfilter = jdom.find(".slider_buttonOpen").length;
		if(isfilter>0){
			var iname = jdom.attr("data-name");
			var dval = jdom.attr("data-value");
			$("#searchbar").append('<input type="hidden" value="'+dval+'" name="'+iname+'">');
		}
	});
	$("#searchbar").submit();
}
