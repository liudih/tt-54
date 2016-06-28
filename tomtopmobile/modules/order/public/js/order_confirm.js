$(function() {
	var shipToCountryCode = $("#theCountryCode").val();
	if(shipToCountryCode == null || shipToCountryCode==""){
		ttAlert('shipping unavailable');
	}else{
		refreshShipMethod(shipToCountryCode);
	}
	$('#shipmethodContent ul:eq(0)').click();
});

//修改地址事件
$('#shipAddressEdit').click(function() {
	$('#pop_address').find('input[name]').each(function() {
		var name = $(this).attr('name');
		if ('firstName' == name) {
			var fn = $('#shipToFirstName').text() || '';
			$(this).val(fn);
		} else if ('lastName' == name) {
			var fn = $('#shipToLastName').text() || '';
			$(this).val(fn);
		} else if ('address1' == name) {
			var fn = $('#shipToStreet1').text() || '';
			$(this).val(fn);
		} else if ('city' == name) {
			var fn = $('#shipToCity').text() || '';
			$(this).val(fn);
		} else if ('province' == name) {
			var fn = $('#shipToState').text() || '';
			$(this).val(fn);
		} else if ('zipCode' == name) {
			var fn = $('#shipToZipCode').text() || '';
			$(this).val(fn);
		} else if ('telephone' == name) {
			var fn = $('#shipToTel').text() || '';
			$(this).val(fn);
		} else if ('countryCode' == name) {
			var fn = $('#shipToCountry').text() || '';
			$(this).val(fn);
		}
		$("#pop_address").show();
	});
});
$('#ok_btn').click(function(){
	var isSubmit = true;
	$('#pop_address').find('input[name]').each(function(){
		var v =  $(this).val().trim();
		if(!v){
			$(this).addClass('input_error');
			isSubmit = false;
		 }else{
			 $(this).removeClass('input_error'); 
		 }
	});
	if(isSubmit){
		fillAddress();
		$(".popBoxNone").hide();
		$(".publice_show").fadeOut();
	}

});
function fillAddress() {
	var selectli = $("#ship_address_ul li:eq(0)");
	var orderForm = {};
	selectli.find("span[name]").each(function(i,e){
		var node = $(this);
		var name = node.attr("name");
		var val = node.html();
		orderForm[name] = val;
	});
	
	var address = {};
	$('#pop_address').find('input[name]').each(function() {
		var name = $(this).attr('name');
		var value = $(this).val();
		address[name] = value || '';
	});
	var ac = $("#confirmOrderForm input[name='countrysn']:eq(0)").val();
	//邮寄方式
	if(orderForm.countryCode != ac){
		$('#shipMethod').empty();
		refreshShipMethod(ac);
	} 
	
	orderForm = address;
	$('#shipToFirstName').text(address.firstName);
	$("#confirmOrderForm input[name='firstName']").val(address.firstName);
	$('#shipToLastName').text(address.lastName);
	$("#confirmOrderForm input[name='lastName']").val(address.lastName);
	$('#shipToStreet1').text(address.address1);
	$("#confirmOrderForm input[name='address1']").val(address.address1);
	$('#shipToCity').text(address.city);
	$("#confirmOrderForm input[name='city']").val(address.city);
	$('#shipToState').text(address.province);
	$("#confirmOrderForm input[name='province']").val(address.province);
	$('#shipToZipCode').text(address.zipCode);
	$("#confirmOrderForm input[name='zipCode']").val(address.zipCode);
	var c = $("#confirmOrderForm input[name='country']:eq(0)").val();
	$('#shipToCountry').text(c);
	$('#shipToTel').text(address.telephone);
	$("#confirmOrderForm input[name='telephone']").val(address.telephone);
}

function vaildSubmit(){
	var firstName = $('#shipToFirstName').text();
	var lastName = $('#shipToLastName').text();
	var street = $('#shipToStreet1').text();
	var city = $('#shipToCity').text();
	var state = $('#shipToState').text();
	var zipCode = $('#shipToZipCode').text();
	var country = $('#shipToCountry').text();
	var tel = $('#shipToTel').text();
	var boo = true;
	if(firstName == null || $.trim(firstName) == ''){
		boo = false;
	}
	if(lastName == null || $.trim(lastName) == ''){
		boo = false;
	}
	if(street == null || $.trim(street) == ''){
		boo = false;
	}
	if(city == null || $.trim(city) == ''){
		boo = false;
	}
	if(state == null || $.trim(state) == ''){
		boo = false;
	}
	if(zipCode == null || $.trim(zipCode) == ''){
		boo = false;
	}
	if(country == null || $.trim(country) == ''){
		boo = false;
	}
	if(tel == null || $.trim(tel) == ''){
		boo = false;
	}
	//判断物流方式不为空
	var shipMethodCode = $("#shipMethodCode").val();
	var shipMethodId = $("#shipMethodId").val();
	if(shipMethodCode=="" || shipMethodCode==null || shipMethodId=="" || shipMethodId==null){
		ttmErrorAlert('shipping method must select',3000);
		return false;
	}
	
	if(boo == false){
		$('#shipAddressEdit').trigger('click');
		$('#ok_btn').trigger('click');
	}else{
		var msg = $('#msg').val();
		//alert(msg);
		if(msg != null && $.trim(msg) != ''){
			$('#leaveMessage').val(msg);
		}
		$('#confirmOrderForm').submit();
	}
}

//function refreshShipMethod(shipToCountryCode){
//	//alert(shipToCountryCode);
//	$.ajax({url: '/order/refresh-ship-method', 
//		type: 'GET', 
//		data:{'orderNum' : "@orderNum",'shipToCountryCode' : shipToCountryCode}, 
//		dataType: 'html', 
//		timeout: 1000 * 20, 
//		error: function(){}, 
//		success: function(html){
//			$('#shipMethod').empty();
//			$('#shipMethod').append(html);
//			
//			$('#shipping-method').find('ul:first').trigger('click');
//		} 
//	}); 
//}

$("#country_select").find('li').click(function() {
	var codeid = $(this).attr('cid');
	var code = $(this).attr('code');
	var text = $(this).text();
	$('#search_country').val(text);
	//$('#country').val(code);
	//$('#country_select').hide();
	$('#country_label').hide();
	//alert(codeid);
	$("#confirmOrderForm input[name='countryCode']").val(codeid);
	$("#confirmOrderForm input[name='country']:eq(0)").val($(this).html());
	$("#confirmOrderForm input[name='countrysn']:eq(0)").val(code);
});
 function countryHide(){
		$(".select_countryNone").hide();
 }
$(".placeholder_countryClick").blur(function(){
	window.setTimeout("javascript:countryHide()",200);
})

function ttAlert(tip){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var ele = [];
		ele.push('<div id="errorBox" style="display: block" class="pu_pop popNone_s">');
		ele.push('<div class="ns_pop_box">');
		ele.push('<div class="btn_pop_close"></div>');
		ele.push('<div class="pop_title">');
		ele.push('<h3>Error</h3>');
		ele.push('</div>');
		ele.push('<div class="pop_con">');
		ele.push('<p id="errorBoxTxt"></p>');
		ele.push('</div>');
		ele.push('<div class="pop_input_box">');
		ele.push('<input id="errorBoxOkBtn" type="button" class="pop_input_confirm" value="OK">');
		ele.push('</div>');
		ele.push('</div>');
		ele.push('<div class="blockPopup_black"></div>');
		ele.push('</div>');
		$('body').append(ele.join(''));
		$('#errorBoxOkBtn').click(function() {
			$('#errorBox').hide();
		});
		$('#errorBox').find('.btn_pop_close').click(function() {
			$('#errorBox').hide();
		});
	}
	
	$('#errorBox').show();
	$('#errorBoxTxt').text((tip || ''));
}


/*******NNNNNNNNNNN********/

//新版本的物流方式获取
function refreshShipMethod(countryCode){
	//var country = $('#ship_address_ul .sel input[name=countryCode]').val();
	var subtotal = Number.parseFloat($('#cart_subtotal').html());
	var discount = Number.parseFloat($('#discount_coupon').html());
	var total = subtotal + discount;
	var storageid = getCookie("storageid")||1;
	
	//如果有订单号
	var orderNum = $("#orderNum").val()||"";
	$('#ns_loading_box').show();
	$("#shipmethodContent").html("");
	$("#shipMethodCode").val("");
	$("#shipMethodId").val("");
	$.ajax({
		url: '/order/ship-method', 
		type: 'GET', 
		data: {
			//'country':country,
			'totalPrice':total,
			'storageid':storageid,
			'orderNumber': orderNum,
			'countryCode':countryCode
		}, 
		dataType: 'json', 
		cache: false,
		async: false,
		//timeout: 30000, 
		success: function(data){
			var theCsymbol = $("#theCsymbol").val();
			var html = '';
			for(var i=0;i<data.length;i++){
				if(data[i].code!=null && data[i].code!=""){
					html += '<ul onclick="bindClickForShippingMethod(this)" id="'+data[i].id+'" code="'+data[i].code+'" value="'+data[i].price+'" class="selectShippingMethod lbBox chooseOneBox">'
					+'<label class="chooseOneLabel">'
					+'<li class="lineBlock selectShippingMethod_L input_control">'
					+'<input type="radio" />'
					+'<div class="radio"><span> </span></div>'
					+'</li>'
					+'<li class="lineBlock selectShippingMethod_C">'
					+'<p class="mailWay">'+data[i].title+'</p>'
					+'<p>'+data[i].description+'</p>'
					+'</li>'
					+'<li class="lineBlock selectShippingMethod_R">'+theCsymbol+' '+data[i].price+'</li>'
					+'</label></ul>';
				}
			}
			
			$("#shipmethodContent").html(html);
			if(html.length>0){
				$('#shipmethodContent ul:eq(0)').click();
			}
		},
		complete: function(){
			$('#ns_loading_box').hide();
		}
	});
}

function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}


function bindClickForShippingMethod(node){
	var jnode = $(node);
	var iid = jnode.attr('id');
	var code= jnode.attr('code');
	var freight = jnode.attr('value');
	$("#shipMethodCode").val(code);
	$("#shipMethodId").val(iid);
	
	$("#shipmethodContent div").removeClass("aciCss");
	$(node).find('div').addClass('aciCss');
	
	var subtotal = Number.parseFloat($('#cart_subtotal').html());
	var discount = Number.parseFloat($('#discount_coupon').html());
	var shipprice = Number.parseFloat(freight);
	var total = subtotal + discount + shipprice;
	
	if ($("#cart_currency").val() == 'JPY') {
		$('#shipCost').html(Math.round(shipprice));
		$('#grandTotal').html(Math.round(total));
	} else {
		$('#shipCost').html(shipprice.toFixed(2));
		$('#grandTotal').html(total.toFixed(2));
	}
}

