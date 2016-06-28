//加入购物车
function addCart(node){
	var n = $(node);
	// 按钮类型 buynow or addcart
	var type = n.attr("type");
	var listingid = n.parent().find(".listingid:eq(0)").val();
	var numnode = n.closest(".buyPopCon").find(".productnum:eq(0)");
	var storageid = n.closest(".buyPopCon").find(".storage_content li.aciCss").attr("data");
	var num = numnode.val();
	if(isNaN(num) || parseInt(num)<1){
		num = 1;
		numnode.val(1);
	}
	
	var list = [];
   	var map = {};
	map['clistingid'] = listingid;
	map['qty'] = num;
	map['storageid'] = storageid;
	list[0] = map;
	var dd = $.toJSON(list);
	
	$.ajax({
		url : "/cart/savecartitem",
		type : "post",
		data: dd,
		dataType : "json",
		contentType: "application/json",
		success : function(data) {
			var result = data.result;
			if ("success" == result) {
				ttmSucceedAlert("Add cart successfully!",1000);
				getCartSize();
				if(type == "buynow"){
					location.href = "/cart";
				}
			} else if(result == "notenough"){
				ttmSucceedAlert("Low stocks!",1000);
			}else {
				ttmSucceedAlert("Add cart fail!",1000); 					
			}
		}
	});
}

//购买数量的添加和减少
function qtyAdd(node){
	var num = $(node).next().find("input:eq(0)");
	var val = num.val();
	if(isNaN(val) || parseInt(val)<1){
		val = 1;
	}
	val = parseInt(val)-1;
	val = val<1?1:val;
	num.val(val);
};
function qtyReduction(node){
	var num = $(node).prev().find("input:eq(0)");
	var val = num.val();
	if(isNaN(val) || parseInt(val)<1){
		val = 1;
	}
	val = parseInt(val)+1;
	val = val<1?1:val;
	num.val(val);
};