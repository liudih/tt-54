$(function(){
	//去除排序按钮的选项，重新赋值
	$(".sortNA a").attr("href","javascript:void(0)");
	//去除重复的input
	$(".sortNA input[type=hidden]").remove();
	
	//默认第一次进来时高亮选中 most popularity
	var popitem = $("#searchbar").find("input[name='popularity']");
	var priceitem = $("#searchbar").find("input[name='price']");
	var discountitem = $("#searchbar").find("input[name='discount']");
	var reviewitem = $("#searchbar").find("input[name='review']");
	var saleitem = $("#searchbar").find("input[name='sale']");
	if(popitem.length ==0 &&priceitem.length ==0&& discountitem.length==0&& reviewitem.length==0&&saleitem.length ==0){
		$("#searchbar").find("a[data-name='popularity']").attr("data-value","asc").addClass("sortActive").parent().addClass("sortA_PriceDo");
	}
	
	//高亮筛选
	$(".sortNA a").each(function(i,e){
		var jdom = $(this);
		var dname = jdom.attr("data-name");
		var dval = jdom.attr("data-value");
		var isexist = $("input[name='"+dname+"'][value='"+dval+"']").length>0;
		if(dname=="productType" && (dval=="freeShipping" || dval=="new") && isexist){
			$(".rightThis[data-name='"+dname+"'][data-value='"+dval+"']").find("span:eq(0)").addClass("afters");
			return true;
		} 
		var dval2 = getQueryString(dname);
		if(dname!=null && dname!="" && dval2!=null){
			if(dname!="popularity"){
				var itemParent = jdom.parent();
				if(dval2=="asc"){
					itemParent.addClass("sortA_PriceUp");
				}else if(dval2=="desc"){
					itemParent.addClass("sortA_PriceDo");
				}
			}
			jdom.addClass("sortActive");
		}
	});
})
$(".sortNA a").click(function(){
	//$(".sortCls").remove();//删除已存在的排序属性
	
	var jdom = $(this);
	if(jdom.hasClass("arrayLess")||jdom.hasClass("arrayMore")||jdom.hasClass("arrayLine") ){
		return ;
	}
	var iname = jdom.attr("data-name");
	var dval = jdom.attr("data-value");
	
	var isasc = getQueryString(iname);
	var isa = "desc";
	if(isasc!=null && isasc!="" && isasc=="desc"){
		isa = "asc";
	}
	//默认第一次进来时 是按点击量排序，所以需要变更一下排序方式
	if(isasc ==null && iname=='popularity'){
		isa = "asc";
	}
	
	//如果是过滤类型不取消高亮
	if(iname !='productType'){
		$("#searchbar").find("input[name='price']").remove();
		$("#searchbar").find("input[name='discount']").remove();
		$("#searchbar").find("input[name='review']").remove();
		$("#searchbar").find("input[name='popularity']").remove();
		$("#searchbar").find("input[name='sale']").remove();
	}
	
	switch(iname){
		case "popularity":
			$("#searchbar").append('<input type="hidden" value="desc" name="popularity">');
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
			
			//点击过滤条件时需要特殊处理
			var newnode = $(".rightThis[data-name='"+iname+"'][data-value='"+dval+"']").find("span:eq(0)");
			if(!newnode.hasClass('afters')){
				$("#searchbar").append('<input type="hidden" value="'+dval+'" name="'+iname+'">');
			}
			
			break;
		case "discount":
			 
			$("#searchbar").append('<input type="hidden" value="'+isa+'" name="discount">');
			break;
	//	case "weight":
	//		$("#searchbar").append('<input type="hidden" value="freeShipping" name="productType">');
	//		break;
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
	$("#p").val(1);
	$("#searchbar").submit();
});
$("#pricerange-ui").click(function(event){
	event.preventDefault();
	$(".sortCls").remove();
	var minPrice = parseFloat($('#minPrice').val());
	var maxPrice = parseFloat($('#maxPrice').val());
	
	if(minPrice=="" || isNaN(minPrice)){
		minPrice = " ";
	}
	if(maxPrice=="" || isNaN(maxPrice)){
		maxPrice = " ";
	}
	if(minPrice>maxPrice&&maxPrice!=' '){
		return false;
	}
	if(minPrice==' '&&maxPrice==' '){
		return false;
	}
	var pricerange = minPrice + ":" + maxPrice;
	$("#searchbar").append('<input type="hidden" name="pricerange" value="' + pricerange + '" />');
	$("#p").val(1);
	$("#searchbar").submit();
});
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}