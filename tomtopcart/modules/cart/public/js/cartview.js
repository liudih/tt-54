$(function(){
	$('.nw_btn_paypal').click(function(){
		var url = $(this).attr('url');
		skippay(url,false);
	});
	$('.nw_btn_place').click(function(){
		var url = $(this).attr('url');
		skippay(url,true);
	});
	
	//save for later显示
	var cartsize = $("#cartsize").val();
	var latesize = $("#latesize").val();
	if(cartsize!="" && parseInt(cartsize)==0 && latesize!="" && parseInt(latesize)>0){
		$(".warehouse_later_list .myshop_wares").show();
	}
	
	//设置默认仓库
	var thestorageid = $("#thestorageid").val();
	var ishave = false;
	if(thestorageid!=null && thestorageid!="" && !isNaN(thestorageid)){
		var t0 = parseInt(thestorageid);
		$("#cartlist_ul .warehouse_sel").each(function(i,e){
			var t = parseInt($(this).attr("storageid"));
			if(t==t0){
				$(this).addClass("sel");
				setStorageid(t);
				ishave = true;
				return false;
			}
		});
	}
	if(ishave==false){
		var sl = $("#cartlist_ul .storage_products>.sel");
		if(sl.length==0){
			$("#cartlist_ul li:eq(0)").find(".warehouse_sel").addClass("sel");
			var storageid=$("#cartlist_ul li:eq(0)").find(".warehouse_sel").attr("storageid");
			setStorageid(storageid);
			//reloadPage(storageid);
		}
	}
	
	// 设置drop shipping order
	//所在仓库没有drop就去掉勾
	var isExistDrop = false;
	$("#cartlist_ul .storage_products>.sel").parent().find(".csku").each(function(i, e) {
		if ($(this).html() == "X01") {
			isExistDrop = true;
			return false;
		}
	});
	if(!isExistDrop){
		$(".nw_drop_order").removeClass("sel");
	}else{
		$(".nw_drop_order").addClass("sel");
	}
	
	//设置所选仓库的金额
	var chooseStorage = $("#cartlist_ul .storage_products>.sel").parent();
	if(chooseStorage!=null){
		getStorageTotal(chooseStorage);
	}
})

//选择仓库
$("#cartlist_ul .warehouse_sel").click(function(){
	var pnode= $(this).parent();
	//是否存在drop，否则就去掉勾
	var isExistDrop = false;
	pnode.find(".csku").each(function(i, e) {
		if ($(this).html() == "X01") {
			isExistDrop = true;
			return false;
		}
	});
	if(!isExistDrop){
		$(".nw_drop_order").removeClass("sel");
	}else{
		$(".nw_drop_order").addClass("sel");
	}
	getStorageTotal(pnode);
	var storageid=$(this).filter(".warehouse_sel").attr("storageid");
	if(storageid){
		setStorageid(storageid);
		removeLoyaltyCookie();
		reloadPageUsingSId(storageid);
	}
});
//获取一个仓库的价格
function getStorageTotal(pnode){
	var price = 0;
	pnode.find(".theprice").each(function(i,e){
		var thep = $(this).val();
		thep = thep.replace(/[,]/g,"");
		price += parseFloat(thep);
	});
	var currency = $("#currencycode").val();
	var isJPY = 'JPY' == currency;
	price = isJPY ? Math.floor(price) : price.toFixed(2);
	$("#cart_subtotal").html(price);
	$("#cart_total").html(price);
}

//刷新页面
function reloadPage(){
	var stid = $("#cartlist_ul .storage_products>.sel").attr("storageid");
	window.location.href = "/cart?storageid="+stid;
}
function reloadPageUsingSId(id){
	window.location.href = "/cart?storageid="+id;
}

function additem(cid){
	var list = [];
	var map = {};
	map['clistingid'] = cid;
	map['qty'] = 1;
	map['storageid'] = 1;
	list[0] = map;
	var dd = $.toJSON(list);
	var url = "/cart/savecartitem";
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
			}else if(data.result=="sold-out"){
				pophtml("Error","Sold out!");
			}else {
				pophtml("Error","add error!");
			}
		}
	});
}

function delitem(node){
	confirmHtml("Confirm","Remove from your cart?",function(){
		var pnode = $(node).closest(".itemline");
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
$(".input_num").blur(function() {
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

// 按下Enter键
$(".input_num").keydown(function(event) {
	if (event.keyCode == 13) {
		var n = this.value;
		if (isNaN(n) || n < 1 || n > 999) {
			n = 1;
		}
		this.value = n;
		updateQtyCommon(this,n);
	}
});

//更新数量
function updateQtyCommon(node, num){
	var pnode = $(node).closest(".itemline");
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
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
				resetNum(node);
			} else {
				pophtml("Error","update error!");
				resetNum(node);
			}
		}
	});
}

//还原数量
function resetNum(node){
	var pnode = $(node).closest(".itemline");
	var num = pnode.find(".iqty:eq(0)").val();
	pnode.find(".input_num:eq(0)").val(num);
}

//--------------save for later---------------

function addLaterCart(node){	
	var pnode = $(node).closest(".itemline");
	var sid = pnode.find(".storageid:eq(0)").val();
	var num = pnode.find(".iqty:eq(0)").val();
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		map['qty'] = num;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	var url = cartRoutes.controllers.cart.CartController.saveLaterCartItem().url;
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

function moveToCart(node){	
	var pnode = $(node).closest(".itemline");
	var sid = pnode.find(".storageid:eq(0)").val();
	var num = pnode.find(".iqty:eq(0)").val();
	var list = [];
	pnode.find(".clistingid").each(function(i,e){
		var map = {};
		map['clistingid'] = $(this).val();
		map['storageid'] = sid;
		map['qty'] = num;
		list[list.length] = map;
	});
	var dd = $.toJSON(list);
	var url = cartRoutes.controllers.cart.CartController.updateCartLaterItem().url;
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		success: function(data){
			if(data.result=="success"){
				reloadPage();
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
			}else if(data.result=="sold-out"){
				pophtml("Error","Sold out!");
			}else {
				pophtml("Error","add error!");
			}
		}
	});
}
function delLateritem(node){
	confirmHtml("Confirm","Are you sure you wish to delete the selected items?",function(){
		var pnode = $(node).closest(".itemline");
		var sid = pnode.find(".storageid:eq(0)").val();
		var num = pnode.find(".iqty:eq(0)").val();
		var list = [];
		pnode.find(".clistingid").each(function(i,e){
			var map = {};
			map['clistingid'] = $(this).val();
			map['storageid'] = sid;
			map['qty'] = num;
			list[list.length] = map;
		});
		var dd = $.toJSON(list);
		var url = cartRoutes.controllers.cart.CartController.delCartLaterItem().url;
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
	});
}

function addcollect(lis,action){
	var islogin = $("#islogin").val();
	if(islogin=="false"){
		$(".blockPopup_box").show();
		return;
	}
	$.get("/cart/collect", {
		"lid" : lis,
		"action" : action
	}, function(data) {
		if (data.result == "nologin") {
			$(".blockPopup_box").show();
			return;
		}
		if (data.result == "success") {
			pophtml("Success","Success!");
		} else {
			pophtml("Error", data.result);
		}
	}, "json");
}

function skippay(url,needLoggen){
	//判断选择仓库
	var storageid = $("#cartlist_ul .sel").attr("storageid");
	if(storageid==null || storageid==undefined || storageid==""){
		pophtml("Error","Please choose the warehouse!");
		return;
	}
	url += "?storageid="+storageid
	var paramlogin = 0;
	if(needLoggen){
		paramlogin = 1;
		var islogin = $("#islogin").val();
		if(islogin=="false"){
			$(".blockPopup_box").show();
			return;
		}
	}
	//判断0元订单
	var grandtotal = parseFloat($("#cart_total").html()); 
	if(grandtotal<=0){
		pophtml("Error","Total can not be zero!");
		return;
	}
	//判断产品是否是在售状态和库存
	$.ajax({
		url: "/cart/checkstatus",
		type: "GET",
		dataType: "json",
		data:{"islogin": paramlogin},
		async:false,
		success: function(data){
			if(data.result=="success"){
				$("#ns_loading_box").show();
				window.location.href = url;
			}else if(data.result=="no-enough"){
				pophtml("Error","Out of stock!");
				reloadPage();
			}else if(data.result=="sold-out"){
				pophtml("Error","Sold out!");
				reloadPage();
			}else if(data.result=="no-login"){
				$(".blockPopup_box").show();
			}else {
				pophtml("Error","add error!");
				reloadPage();
			}
		}
	});
}

function pophtml(title, text){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var html = [];
		html[html.length] = '<div style="display: block" class="pu_pop popNone_s" id="errorBox" >';
		html[html.length] = '<div class="ns_pop_box"><div class="btn_pop_close" id="popClose" ></div>';
		html[html.length] = '<div class="pop_title"><h3>'+title+'</h3></div>';
		html[html.length] = '<div class="pop_con"><p id="errorBoxTxt">'+text+'</p></div>';
		html[html.length] = '<div class="pop_input_box">';
		html[html.length] = '<input type="button" class="pop_input_confirm" value="OK" id="errorBoxOkBtn"/></div></div>';
		html[html.length] = '<div class="blockPopup_black"></div></div>';
		$('body').append(html.join(''));
		$('#errorBoxOkBtn,#popClose').click(function() {
			$('#errorBox').remove();
		});
	}
	$('#errorBox').show();
	$('#errorBoxTxt').text((text || ''));
}

function confirmHtml(title, text, fun){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var html = [];
		html[html.length] = '<div style="display: block" class="pu_pop popNone_s" id="errorBox" >';
		html[html.length] = '<div class="ns_pop_box"><div class="btn_pop_close" id="popClose"></div>';
		html[html.length] = '<div class="pop_title"><h3>'+title+'</h3></div>';
		html[html.length] = '<div class="pop_con"><p id="errorBoxTxt">'+text+'</p></div>';
		html[html.length] = '<div class="pop_input_box">';
		html[html.length] = '<input type="button" class="pop_input_close" id="errorBoxOkBtn" value="CANCEL" />';
		html[html.length] = '<input type="button" class="pop_input_confirm" value="OK" id="confirmBtn" /></div></div>';
		html[html.length] = '<div class="blockPopup_black"></div></div>';
		$('body').append(html.join(''));
		$('#errorBoxOkBtn,#popClose').click(function() {
			$('#errorBox').remove();
		});
		$('#confirmBtn').click(function() {
			fun();
		});
	}
	$('#errorBox').show();
	$('#errorBoxTxt').text((text || ''));
}

function addDropshipping(node){
	var the = $(node);
	var isc = the.hasClass("sel");
	var dropstorageDom = $("#cartlist_ul .storage_products>.sel");
	var dropStorageid = dropstorageDom.attr("storageid");
	dropStorageid = dropStorageid==null ? 1 : dropStorageid;
	var listingid = "";
	dropstorageDom.parent().find(".csku").each(function(i, e) {
		if ($(this).html() == "X01") {
			var pnode = $(this).closest(".itemline");
			listingid = pnode.find(".clistingid:eq(0)").val();
			return false;
		}
	});
	
	if(isc && listingid!=""){
		var sid = dropStorageid;
		var list = [];
		var map = {};
		map['clistingid'] = listingid;
		map['storageid'] = sid;
		list[list.length] = map;
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
					the.removeClass("sel");
					reloadPage();
				}
			}
		});
	}else if(!isc){
		var url = "/cart/adddropshipping";
		$.ajax({
			url : url,
			type : "GET",
			dataType : "json",
			data:{
				"storageid": dropStorageid
			},
			async : true,
			success : function(data) {
				if (data.result == "success") {
					the.addClass("sel");
					reloadPage();
				}
			},
			complete : function() {
			}
		});
		
	}
}
// 设置仓库id
function setStorageid(storageid){
		var url = cartRoutes.controllers.cart.CartController.setStorageid(storageid).url;
		$.ajax({
			url: url,
			type: "POST",
			success: function(data){
				if(data.result=="success"){
					
				}
			}
		});
	
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function removeLoyaltyCookie() {
	setCookie("loyalty", "", -1); 
	setCookie("point", "", -1); 
}