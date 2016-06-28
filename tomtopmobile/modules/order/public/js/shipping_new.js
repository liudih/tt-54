$(function(){
	var aaid = $("#theAddresssId").val();
	if(aaid!=null){
		refreshShipMethod(aaid);
	}
});

//新版本的物流方式获取
function refreshShipMethod(addressId){
	//var country = $('#ship_address_ul .sel input[name=countryCode]').val();
	var subtotal = Number.parseFloat($('#cart_subtotal').html());
	var discount = Number.parseFloat($('#discount_promo').html());
	var discount2 = Number.parseFloat($('#discount_coupon').html());
	var discount3 = Number.parseFloat($('#discount_point').html());
	var total = subtotal + discount + discount2 + discount3;
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
			'addressId': addressId
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
					html += '<ul onclick="bindClickForShippingMethod(this)" id="'+data[i].id+'" code="'+(data[i].code||'')+'" value="'+data[i].price+'" class="selectShippingMethod lbBox chooseOneBox">'
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
	var discount = Number.parseFloat($('#discount_promo').html());
	var discount2 = Number.parseFloat($('#discount_coupon').html());
	var discount3 = Number.parseFloat($('#discount_point').html());
	var shipprice = Number.parseFloat(freight);
	var total = subtotal + discount + discount2 + discount3 + shipprice;
	
	if ($("#cart_currency").val() == 'JPY') {
		$('#shipCost').html(Math.round(shipprice));
		$('#cartGrandTotal').html(Math.round(total));
	} else {
		$('#shipCost').html(shipprice.toFixed(2));
		$('#cartGrandTotal').html(total.toFixed(2));
	}
}
