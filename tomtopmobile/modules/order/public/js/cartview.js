$(function() {
	// 设置收藏状态
	if (typeof (membercollecturl) != "undefined") {
		$.get(membercollecturl, {}, function(data) {
			var list = data;
			$(".heartClick").each(function(i, e) {
				for (var i = 0; i < list.length; i++) {
					if ($(e).attr("data") == list[i]) {
						$(e).addClass("redHeart");
					}
				}
			});
		}, "json");
	}
	
	//设置默认仓库
	var thestorageid = $("#thestorageid").val();
	var ishave = false;
	if(thestorageid!=null && thestorageid!="" && !isNaN(thestorageid)){
		var t0 = parseInt(thestorageid);
		$("#dataContent .orgRadio").each(function(i,e){
			var t = parseInt($(this).attr("storageid"));
			if(t==t0){
				$(this).addClass("aciCss");
				setStorageid(t);
				ishave = true;
				return false;
			}
		});
	}
	
	if(ishave==false){
		var sl = $("#dataContent .aciCss");
		if(sl.length==0){
			$("#dataContent .orgRadio:eq(0)").addClass("aciCss");
			var storageid=$(".orgRadio:eq(0)").attr("storageid");
			setStorageid(storageid);
		}
	}
	
	//设置所选仓库的金额
	getStorageTotal();
});
//设置仓库id
function setStorageid(storageid){
	var url = "/cart/storageid/set?storageid="+storageid;
	$.ajax({
		url: url,
		type: "POST",
		success: function(data){
			if(data.result=="success"){
			}
		}
	});
}

//刷新页面
function reloadPage(){
	var stid = $(".chooseOneLabel .aciCss").attr("storageid");
	window.location.href = "/cart?storageid="+stid;
}
function popCart(node){
	Dialog(function(){delCart(node)},"Remove from your cart?");
}

//删除单个商品
function delCart(node) {
	var pnode = $(node).closest(".shoppingCart_listBox");
	var sid = pnode.find(".storageid:eq(0)").val();
	sid = isNaN(sid) ? 1 : sid;
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	var url = "/cart/delcartitem";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}
		}
	});
}

function updateItem(type,node){
	var num = 1;
	if(type=="sub"){
		num = parseInt($(node).next().find("input:eq(0)").val());
		if (num > 1) {
			num = num - 1;
		}else{
			return;
		}
		$(node).next().find("input:eq(0)").val(num);
	}else if(type=="add"){
		num = parseInt($(node).prev().find("input:eq(0)").val());
		if (num < 999) {
			num = num + 1;
		}else{
			return;
		}
		$(node).prev().find("input:eq(0)").val(num);
	}
	updateQtyCommon(node,num);
}

//失去焦点的数量判断
$(".itemnum").blur(function() {
	var n = this.value;
	var oldnum = $(this).closest(".itemline").find(".iqty:eq(0)").val();
	if (isNaN(n) || n < 1 || n > 999) {
		n = 1;
	}
	if(parseInt(n)==parseInt(oldnum)){
		this.value = n;
		return ;
	}
	this.value = n;
	updateQtyCommon(this,n);
});

//更新数量
function updateQtyCommon(node, num){
	var pnode = $(node).closest(".shoppingCart_listBox");
	var sid = pnode.find(".storageid:eq(0)").val();
	sid = isNaN(sid) ? 1 : sid;
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		map['qty'] = num;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	$.ajax({
		url: "/cart/updatecartitem",
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		success: function(data){
			if (data.result == "success") {
				reloadPage();
			} else if (data.result == "no-enough") {
				$(node).parent().find("input:eq(0)").val(data.oldnum);
				alert("Low stocks!");
			} else {
				alert("update error!");
			}
		}
	});
}

//计算总价
function sumAllPrice() {
	savetotal();// 计算折扣的价格
}
//计算 save total
function savetotal() {
	var saveprice = 0;
	$(".shoppingCart_listBox").each(
			function(i, e) {
				var savep = $(this).find(".product_price span:eq(0)").html();
				if (savep != null && savep != undefined) {
					saveprice += parseFloat(savep)
							* parseInt($(this).find(".itemnum:eq(0)")
									.val());
				}
			});
	if (saveprice > 0) {
		$(".subtotalCls:eq(0)").html(saveprice.toFixed(2));
		$(".GrandTotalCls:eq(0)").html(saveprice.toFixed(2));
	}else{
		$(".subtotalCls:eq(0)").html("0.00");
		$(".GrandTotalCls:eq(0)").html("0.00");
	}
}


//点击仓库按钮
function touchStorage(node, sid){
	$(node).parents(".chooseOneBox").siblings().find(".radio").removeClass("aciCss");
	$(node).find(".radio").addClass("aciCss");
	setStorageid(sid);
	getStorageTotal();
}