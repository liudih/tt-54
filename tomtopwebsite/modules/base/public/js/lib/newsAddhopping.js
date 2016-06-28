
/**
 * @author lijun
 */
function toDecimal2(x) {    
		var f = parseFloat(x);
        f = Math.round(f * 100) / 100;    
        var s = f.toString();    
        var rs = s.indexOf('.');    
        if (rs < 0) {    
            rs = s.length;    
            s += '.';    
        }    
        while (s.length <= rs + 2) {    
            s += '0';    
        }    
        return s;    
    }

function errorTip(tip){
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
		//ele.push('<input type="button" class="pop_input_close" value="CLOSE">');
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

function validShipAddressForm(succeedCallback,failedCallback){
	var valid = true;
	var datas = {};
	$('#addShipAddressForm').find('input[name]').each(function(){
		var value = $(this).val();
		var name = $(this).attr('name');
		if(name !='iid' && !value){
			valid = false;
//			var length = $(this).next().length;
//			if(length == 0){
//				$(this).after('<label style=""><p style="color:red;">must</p></label>');
//			}
			if('countryCode' == name){
				$(this).parent().siblings('label').show();
			}else{
				$(this).next('label').show();
			}
		}else{
			$(this).next('label').hide();
		}
		datas[name] = value;
	});
	if(valid){
		var bdefault = $('.newshopping_address_default').hasClass('sel');
		if(bdefault){
			datas['bdefault'] = bdefault;
		}
		datas.cstreetaddress = datas.address1 + (datas.address2 || '');
		var actionUrl = $('#addShipAddressForm').attr('action');
		$.ajax({url: actionUrl, 
			type: 'POST', 
			data: datas, 
			dataType: 'json', 
			timeout: 1000 * 20, 
			error: function(){}, 
			success: function(result){
				if(!result.succeed){
					if($.isFunction(failedCallback)){
						failedCallback();
					}
				}else{
					if($.isFunction(succeedCallback)){
						succeedCallback(result.id);
					}
				}
			} 
			});
	}
}

//function getShipMethod(){
//	$('#ns_loading_box').show();
//	$('#shipMethod').html("");
//	var code = $('#ship_address_ul li.sel').find('input[name=countryCode]').val();
//	var storageid = $('input[name=storageid]').val();
//	$.ajax({
//		url: '/checkout/get-sm', 
//		type: 'GET', 
//		data:{
//			'shipToCountryCode' : code,
//			"storageid":storageid
//		},
//		dataType: 'json',
//		timeout: 60000,
//		complete: function(){
//			$('#ns_loading_box').hide();
//		}, 
//		success: function(data){
//			if(data.result!="success"){
//				errorTip('Sorry, we are temporarily unable to ship to your country.');
//				return;
//			}
//			$('#shipMethod').html(data.html);
//			//$('#confirmOrderForm').find('input[name=shippingMethodIdValue]').val('');
//			//是否有邮寄方式
//			if($("#shipMethod .MethodAs").length==0){
//				$("#placeOrderBtn").css("background-color","#dedede")
//					.attr("onclick","");
//			}else{
//				$("#placeOrderBtn").css("background-color","#ff6600")
//				.attr("onclick","placeOrderBtn()");
//			}
//			
//			$('input[type=radio][name=shippingMethodIdValue]').change(function() {
//				calculateTotal();
//			});
//			calculateTotal();
//		} 
//	}); 
//}


function calculateTotal(){
//	var shipMethod = $('input[name=shippingMethodIdValue]:checked');
//	var price = shipMethod.attr('price');
	
	var price = $("#shipping_method .sel").find(".shipprice").html();
	if(price){
		var grandTotal;
		var subtotal = $('#grandTotal').attr('total');
		subtotal = subtotal.replace(/,/g,'');
		price = price.replace(/,/g,'');
		//检查是否有小数点
		if(subtotal.indexOf('.') == -1){
			subtotal = parseInt(subtotal);
			price = parseInt(price);
			grandTotal = subtotal + price;
			$('#shipCost').text(price);
		}else{
			subtotal = parseFloat(subtotal);
			price = parseFloat(price);
			grandTotal = subtotal + price;
			//保留两位小数
			grandTotal = Math.round(grandTotal * 100) / 100;
			
			$('#shipCost').text(toDecimal2(price));
		}
		//如果总金额小于0.3，只能选择paypal支付方式
		$.ajax({
			url: "/base/tousdprice",
			type: "get",
			dataType: "json",
			async: false,
			data:{
				"price": grandTotal
			},
			success: function(data){
				var usdprice = new Number(data.price);
				usdprice = usdprice.toFixed(2); 
				if(usdprice<=0.3){
					$(".payment_method_tab li:gt(0)").hide();
					$(".payment_method_tab li:eq(0)").click();
				}else{
					$(".payment_method_tab li:gt(0)").show();
				}
			}
		});
		$('#grandTotal').empty();
		$('#grandTotal').text(grandTotal);
	}
}

function tapShipDefault(node){
	if(!node){
//		getShipMethod();
		refreshShipMethod();
		return;
	}
	var id =$(node).attr('iid');
	$('#shipAddressId').val(id);
	$(node).addClass("sel");
	$(node).find(".address_default_edit").show();
	var dEle = $(node).find(".a_address_default");
	var d = dEle.attr('d');
	if(!d){
		dEle.show();
	}
	$(node).siblings(':not(.add_item)').each(function(){
		$(this).removeClass("sel");
		$(this).find('.address_default_edit').hide();
	});
	var addr = $(node).find("p[name='countryName']:eq(0)").html();
	if(addr==""){
		$('#shipping_method').empty();
		errorTip('Please select a country!');
		return;
	}
//	getShipMethod();
	refreshShipMethod();
}

function addressDefault(node){
	$(node).hide();
	var id = $(node).parents('li').attr("iid");
	var data = {"id": id};
	var _this = $(node);
	$.ajax({
		url : js_ShippingAddressRoutes.controllers.member.Address.setDefaultShippingaddress().url,
		type : "POST",
		data : JSON.stringify(data),
		contentType : "application/json",
		success : function(data) {
			if(data.errorCode == 0){
				$('.a_address_default').each(function(){
					$(this).attr('d','');
				});
				_this.attr('d',true);
			} else if(data.errorCode == 1){
				_this.show();
				errorTip('set default failed');
			} 
		}
	});
};
//点击绑定邮寄地址-->>>> 账单地址
function billSameShip(node){
	var thenode= $(node);
	var pdiv = thenode.closest("div");
	var _this = thenode.find("span");
	var theul = pdiv.find("ul:eq(0)");
    if(_this.hasClass('sel')){
       _this.removeClass("sel");
       theul.find('li').each(function(){
	       	if($(this).attr('default')){
	       		$(this).show();
	       	}else{
	       		$(this).hide();
	       	}
       });
    }else{
       _this.addClass("sel");
       var shipLi = $('#ship_address_ul').find("li.sel");
       var id = shipLi.attr('iid');
       var ele = shipLi.clone(true);
       $(ele).find('.address_default_edit').show();
       $(ele).find('.a_edit').show();
       $(ele).find('.a_address_default').show();
       theul.find('li').hide();
       
       //保存或者更新 billaddress
		var data = {"shipaddressid": id};
		$.ajax({
			url : js_ShippingAddressRoutes.controllers.member.Address.addOrUpdateBillAddress().url,
			type : "POST",
			data : JSON.stringify(data),
			contentType : "application/json",
			success : function(data) {
				if(data.iid){
					var existed = theul.find('li[iid=' + data.iid + ']');
					if(existed.length > 0){
						existed.remove();
					}
					$(ele).attr('iid',data.iid);
					$(ele).find('[name=iid]').text(data.iid);
					theul.append(ele);
				} else{
					errorTip('error');
				} 
			}
		});
    }
}

	
	$('.newshopping_address_default span').click(function(){
		var p = $(this).parent();
		if(p.hasClass('sel')){
			p.removeClass('sel');
		}else{
			p.addClass('sel');
		}
		
	});
	
	$('.newshopping_address_default span').trigger('click');
	
	$('#addShipAddressForm').find('input[name]').blur(function() {
		var value = $(this).val();
		if (value) {
			$(this).next('label').hide();
		}else{
			$(this).next('label').show();
		}
	});
	
	$('#add-address-btn').click(function(){
		validShipAddressForm(function(){
			var nextStepUrl =  $('#addShipAddressForm').attr('nextStepUrl');
			window.location.href = nextStepUrl;
		},function(){
			errorTip('add ship address failed');
		});
	});
	
	$("#ship_to_new_address").click(function() {
		$("#pop_address").find('input[name]').each(function(){
			$(this).val('');
		});
		countryPlugin.clear();
		$("#pop_address").show();
		$('#addShipAddressForm').attr('ship', 'true');
	});
	
	$("#cancel_btn").click(function() {
		$("#pop_address").hide();
	});
	
	function shipAddressEdit(node){
		var datas = {};
		var tnode = $(node);
		tnode.parents('li:eq(0)').find('[name]').each(function(){
			var name = $(this).attr('name');
			var value = $(this).text();
			datas[name] = value;
		});
		$('#addShipAddressForm').removeAttr('ship');
		
		var ulname = tnode.closest('ul').attr("name");
		if("bill_address_oceanpayment" == ulname){
			$('#addShipAddressForm').attr('ship', 'false');
		}else{
			$('#addShipAddressForm').attr('ship', 'true');
		}
		
		$('#addShipAddressForm').find('input[name]').each(function(){
			var name = $(this).attr('name');
			var value = datas[name] || '';
			$(this).val(value);
		});
		$("#pop_address").show();
		
		var countryName = datas.countryName;
		$('.country_list li').each(function(){
			var text = $(this).text();
			if(countryName == text){
				$(this).trigger('click');
				$("#province_list_id").hide();
				return false;
			}
		});
	}
	
	
	
	$('#step2_ok_btn').click(function(){
		var ship = $('#addShipAddressForm').attr('ship') || 'true';
		if('true' == ship){
			$('#shipping_method').empty();
		}
		
		validShipAddressForm(function(id){
			var datas = [];
			$('#addShipAddressForm').find('input[name]').each(function(){
				var value = $(this).val();
				var name = $(this).attr('name');
				datas[name] = htmlencode(value);
			});
			
			var addressHtml = '';
			addressHtml += '<li iid="'+id+'" class="sel" >'+
			'<input type="hidden" name="countryCode" value="'+datas.countryCode+'" />'+
            '<span name="iid" style="display:none;">'+id+'</span>'+
            '<h5><span name="cfirstname">'+datas.cfirstname+'</span>&nbsp;'+
            	'<span name="clastname">'+datas.clastname+'</span>'+
            '</h5>'+
            '<p><span name="address1">'+datas.address1+'</span></p>'+
            '<p><span name="ccity">'+datas.ccity+'</span>'+
            '<span name="cprovince">'+datas.cprovince+'</span>'+
            '<span name="cpostalcode">'+datas.cpostalcode+'</span>'+  
            '</p>'+
            '<p><span name="countryName">'+datas.countryName+'</span></p>'+
            '<p><span name="ctelephone">'+datas.ctelephone+'</span></p>'+
				
			'<div class="address_default_edit" style="display:none;">'+
			'<a onclick="shipAddressEdit(this)" name="shipAddressEdit" class="a_edit" style="display:inline-block;">Edit</a>'+
			'<a class="a_address_default" onclick="addressDefault(this)">Default</a>'+
			'</div></li>';
        
			if(datas.iid){
				var li = $('li[iid=' + id + ']');
				li.before(addressHtml);
				li.remove();
			}else{
				$('#ship_to_new_address').before(addressHtml);
			}
			$("#pop_address").hide();
			var ship = $('#addShipAddressForm').attr('ship') || 'true';
			tapShipDefault($("#ship_address_ul li.sel")[0]);
			if('true' === ship){
				$('li[iid=' + id + ']').trigger('click');
				$('#ship_address_ul li[iid='+id+']').attr("onclick","tapShipDefault(this)");
			}else{
				$('li[iid=' + id + ']').find('.address_default_edit').show();
			}
			function htmlencode(s){  
				return $("<div/>").text(s != undefined && s != null ? s:'').html();
			}  
		},function(){
			errorTip('add ship address failed');
		});
	});
	
	/**Payment Method**/
    $(".payment_method_tab li").click(function(){
        var a_index=$(this).index();
        $(this).siblings().removeClass("sel");
        $(this).addClass("sel");
        $(".payment_method_con>li").hide();
        $(".payment_method_con>li").eq(a_index).show();
        $(".payment_method_con>li").eq(a_index).find('input[type=radio]').trigger('click');
    });
    $(".span_qiwi_account").click(function(){
        $(this).parents(".method_qiwi_account").find(".qiwi_account_list").toggle();
    });

    $(".boleto_input h5").click(function(){
        $(this).parents(".boleto_input ul").show();
    });
    
    $(".boleto_input li").click(function(){
        var text = $(this).text();
        var value = $(this).attr('v');
        $('input[name=pay_typeCode]').val(value);
        
        $(".boleto_input i[tag=v]").text(text);
        $(this).parent().hide();
    });
    
    $('.qiwi_account_list li').click(function(){
    	var text = $(this).find('strong').text();
    	var cls = $(this).find("i:eq(0)").attr("class");
    	$('.qiwi_account_input strong').text(text);
    	$('.qiwi_account_list').hide();
    	$('input[name=qiwiCountry]').val(text);
    	$(".span_qiwi_account i").attr("class", cls);
    });
    
    function placeOrderBtn(){
    	var paymentId = $('input[type=radio][name=paymentId]:checked').val();
    	if('oceanpayment_qiwi' == paymentId){
    		var qiwiAccount = $('input[name=qiwiAccount]').val();
    		if(!qiwiAccount){
    			errorTip('введите пожалуйста Номер телефона');
    			$('input[name=qiwiAccount]').focus();
    			return;
    		}
    	}
       
        //判断邮寄地址属性不能为空
        var shipaddr = $("#ship_address_ul li.sel");
        if(shipaddr!=null){
        	var addressVail = true;
        	var key = "";
        	shipaddr.find("span[name]").each(function(i,e){
        		key = $(this).attr("name");
        		var val = $(this).html();
        		if(val==""){
        			addressVail = false;
        			return false;
        		}
        	});
        	if(!addressVail){
        		errorTip('The '+key.substring(1)+' in the address can not be empty');
        		shipaddr.find("[name=shipAddressEdit]:eq(0)").click();
        		return;
        	}
        }
        
        //选中的tab 是需要账单地址的选项
        var checkli = $('.payment_method_tab >.sel');
        var billIndex = $(".payment_method_tab .needBill").index(checkli);
        if(billIndex!=-1){
        	//var billAddressCountry = $(".address_country div:eq(0)").html(); //账单地址国家
        	var billContent = $("[name=bill_address_oceanpayment]:eq("+billIndex+")").find("li:visible").eq(0);
        	//判断邮寄地址属性不能为空
            if(billContent!=null){
            	var addressVail = true;
            	var key = "";
            	billContent.find("span[name]").each(function(i,e){
            		key = $(this).attr("name");
            		var val = $(this).html();
            		if(val==""){
            			addressVail = false;
            			return false;
            		}
            	});
            	if(!addressVail){
            		errorTip('The '+key+' in the bill address can not be empty');
            		billContent.find("[name=shipAddressEdit]:eq(0)").click();
            		return;
            	}
            }
            //判断bill Address
        	var billAddressId = billContent.attr("iid");
            if(billAddressId){
            	$("#billAddressId").val(billAddressId);
            }
            //判断信用卡是否存在bill地址
            var isship = billContent.attr("isship");	//地址类型 ，1：地址 2：bill地址
            if(billContent==null || (isship!=null && isship=="1") || billContent.length==0){
            	errorTip('Billing address can not be null!');
            	return;
            }
        }
        
//        //邮寄方式为空判断
//        var shipMethod = $('input[name=shippingMethodIdValue]:checked').val();
//        if(!shipMethod){
//        	errorTip('please select shipping method');
//        	return;
//        }
        
        var shipMethod = $('#shipping_method .sel').attr("code");
        var shipMethodId = $('#shipping_method .sel').attr("id");
        if(!shipMethod){
        	errorTip('please select shipping method');
        	return;
        }else{
        	$("#shipMethodCode").val(shipMethod);
        	$("#shipMethodId").val(shipMethodId);
        }
        
        $('#ns_loading_box2').show();
    	$('#placeOrderForm').submit();
    };
    
$(function(){
	$("#ship_address_ul li.sel").trigger('click');
	 //默认地址 国家为空判断
	try{
		var adr = $("#ship_address_ul li[class='sel']");
		var addr = adr.find("p[name='countryName']:eq(0)").html();
		if(addr==""){
			errorTip('Please select a country!');
	    	return;
		}
	}catch(e){}
});



//新版本的物流方式获取
function refreshShipMethod(){
	var country = $('#ship_address_ul .sel input[name=countryCode]').val();
	var subtotal = Number.parseFloat($('#subtotal').html());
	var discount = Number.parseFloat($('#discount_total').html());
	var total = subtotal + discount;
	var storageid = $('input[name=storageid]').val()||1;
	
	//如果有订单号
	var orderNum = $("#orderNum").val()||"";
	$('#ns_loading_box').show();
	$.ajax({
		url: '/checkout/ship-method', 
		type: 'GET', 
		data: {
			'country':country,
			'totalPrice':total,
			'storageid':storageid,
			'orderNumber': orderNum
		}, 
		dataType: 'json', 
		cache: false,
		//timeout: 30000, 
		success: function(result){
			var ele = [];
			for(var i = 0 ; i < result.length; i++){
				var cell = result[i];
				ele.push('<tr')
				if(i == 0){
					ele.push(' class="sel"');
				}
				ele.push(' code="');
				ele.push(cell.code);
				ele.push('" id="');
				ele.push(cell.id);
				ele.push('">');
	            ele.push('<td><span><i></i></span>');
	            ele.push(cell.title);
	            ele.push('</td>');
	            ele.push('<td>');
	            ele.push(cell.description);
	            ele.push('</td>');
				ele.push('<td class="shipprice">');
				ele.push(cell.price);
				ele.push('</td>');
				ele.push('</tr>');
			}
			//$('#shipping_method').empty();
			$('#shipping_method').html(ele.join(''));
			calculateTotal();
			
			//bind shipping method select event
			$('#shipping_method tr').click(function(){
				var d = $('#shipping_method tr.sel').removeClass('sel');
				$(this).addClass('sel');
				calculateTotal();
			});
		},
		complete: function(){
			$('#ns_loading_box').hide();
		}
	});
}