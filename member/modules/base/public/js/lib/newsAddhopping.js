
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

function getShipMethod(){
	$('#ns_loading_box').show();
	$('#shipMethod').empty();
	var code = $('#ship_address_ul li.sel').find('input[name=countryCode]').val();
	var storageid = $('input[name=storageid]').val();
	$.ajax({url: '/checkout/get-sm', 
		type: 'GET', 
		data:{'shipToCountryCode' : code,"storageid":storageid}, 
		dataType: 'json',
		timeout: 1000 * 20, 
		error: function(){
			$('#ns_loading_box').hide();
		}, 
		success: function(data){
			$('#ns_loading_box').hide();
			if(data.result!="success"){
				errorTip('Sorry, we are temporarily unable to ship to your country.');
				return;
			}
			$('#shipMethod').append(data.html);
			//$('#confirmOrderForm').find('input[name=shippingMethodIdValue]').val('');
			
			$('input[type=radio][name=shippingMethodIdValue]').change(function() {
				calculateTotal();
			});
			calculateTotal();
		} 
		}); 
}


function calculateTotal(){
	var shipMethod = $('input[name=shippingMethodIdValue]:checked');
	var price = shipMethod.attr('price');
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
		
		$('#grandTotal').empty();
		$('#grandTotal').text(grandTotal);
	}
}

function bindEvents(){
	var li = $("#ship_address_ul li:not(.add_item)");
	li.unbind('click');
	li.click(function(){
		var id =$(this).attr('iid');
		$('#shipAddressId').val(id);
		$(this).addClass("sel");
		$(this).find(".address_default_edit").show();
		var dEle = $(this).find(".a_address_default");
		var d = dEle.attr('d');
		if(!d){
			dEle.show();
		}
		$(this).siblings(':not(.add_item)').each(function(){
			$(this).removeClass("sel");
			$(this).find('.address_default_edit').hide();
		});
		var addr = $(this).find("p[name='countryName']:eq(0)").html();
		if(addr==""){
			$('#shipMethod').empty();
			errorTip('Please select a country!');
			return;
		}
		getShipMethod();
	});
	
	$('.a_address_default').click(function(event){
		event.stopPropagation();
		$(this).hide();
		var id = $(this).parents('li').attr("iid");
		var data = {"id": id};
		var _this = $(this);
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
		
	});
	
}

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
			$('#shipMethod').empty();
		}
		
		validShipAddressForm(function(id){
			var datas = [];
			$('#addShipAddressForm').find('input[name]').each(function(){
				var value = $(this).val();
				var name = $(this).attr('name');
				datas[name] = htmlencode(value);
			});
			
			var ele = [];
			ele.push('<li iid="' + id + '" class="sel">');
			ele.push('<input name="countryCode" style="display:none;" value="');
			ele.push(datas.countryCode);
			ele.push('">');
			
			ele.push('<span name="iid" style="display:none;">');
			ele.push(id);
			ele.push('</span>');
			ele.push('<h5>');
			ele.push('<span name="cfirstname">');
			ele.push(datas.cfirstname);
			ele.push('</span>&nbsp;');
			ele.push('<span name="clastname">');
			ele.push(datas.clastname);
			ele.push('</span>');
			
			ele.push(' </h5>');
			ele.push('<p>');
			ele.push('<span name="address1">');
			ele.push(datas.address1);
			ele.push('</span>');
			
//			ele.push('<span name="address2">');
//			ele.push((datas.address2 || ''));
//			ele.push('</span>');
			
			ele.push('</p>');
			ele.push('<p>');
			ele.push('<span name="ccity">');
			ele.push(datas.ccity );
			ele.push('</span>');
			
			ele.push('<span name="cprovince">');
			ele.push(datas.cprovince );
			ele.push('</span>');
			
			ele.push(' <span name="cpostalcode">');
			ele.push(datas.cpostalcode );
			ele.push('</span>');
			
			ele.push('</p>');
			ele.push('<p name="countryName">');
			ele.push(datas.countryName );
			ele.push('</span>');
			
			ele.push('<p name="ctelephone">');
			ele.push(datas.ctelephone );
			ele.push('</span>');
			
			ele.push('<div class="address_default_edit" style="display:none;">');
			ele.push('<a onclick="shipAddressEdit(this)" name="shipAddressEdit" class="a_edit" style="display:inline-block;">Edit</a>');
			ele.push('<a class="a_address_default">Default</a>');
			ele.push('</div>');
         	
			ele.push('</li>');
        
			if(datas.iid){
				var li = $('li[iid=' + id + ']');
				li.before(ele.join(''));
				li.remove();
			}else{
				$('#ship_to_new_address').before(ele.join(''));
			}
			
			
			
			$("#pop_address").hide();
			
			var ship = $('#addShipAddressForm').attr('ship') || 'true';
			
			bindEvents();
			if('true' === ship){
				$('li[iid=' + id + ']').trigger('click');
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
    
    $('#placeOrderBtn').click(function(){
    	var paymentId = $('input[type=radio][name=paymentId]:checked').val();
    	if('oceanpayment_qiwi' == paymentId){
    		var qiwiAccount = $('input[name=qiwiAccount]').val();
    		if(!qiwiAccount){
    			errorTip('введите пожалуйста Номер телефона');
    			$('input[name=qiwiAccount]').focus();
    			return;
    		}
    	}
        
        var shipMethod = $('input[name=shippingMethodIdValue]:checked').val();
        if(!shipMethod){
        	errorTip('please select shipping method');
        	return;
        }
        //如果是信用卡的话 
        var isCedit = $(".payment_method_tab li:eq(1)").hasClass("sel");
        //如果是信用卡的话 
        var isJBC = $(".payment_method_tab li:eq(2)").hasClass("sel");
        var billIndex = isJBC ? 1 : 0;	//第几个bill框
        if(isCedit || isJBC){
        	//var billAddressCountry = $(".address_country div:eq(0)").html(); //账单地址国家
            var bAddress = $("[name=bill_address_oceanpayment]:eq("+billIndex+")").find("li:visible").find("p[name='countryName']:eq(0)").html();
            if(bAddress==null || bAddress==""){
            	errorTip('Please select a country in billing address!');
            	return;
            }
            //判断bill Address
        	var billAddressId = $("[name=bill_address_oceanpayment]:eq("+billIndex+")").find("li.sel").attr("iid");
            if(billAddressId){
            	$("#billAddressId").val(billAddressId);
            }
            //判断信用卡是否存在bill地址
            var billships = $("[name=bill_address_oceanpayment]:eq("+billIndex+")").find("li:visible");
            var isship = billships.attr("isship");	//地址类型 ，1：地址 2：bill地址
            if(billships==null || (isship!=null && isship=="1") || billships.length==0){
            	errorTip('Billing address can not be null!');
            	return;
            }
        }
    	$('#placeOrderForm').submit();
    });
    
$(function(){
    //getShipMethod();    
	bindEvents();
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
