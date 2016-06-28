$(function(){
	var shippingCountry = $('#ttm-sd-country').country();
	$("input").blur(function(){
		 var v =  $(this).val();
		 if(!v){
			$(this).addClass('input_error');
		 }else{
			$(this).removeClass('input_error'); 
		 }
	});
	$('#AddSABtn').click(function(){
		var isSubmit = true;
		var datas = {};
		$('#add-shipping-address-form').find('input[name]').each(function(){
			var v =  $(this).val().trim();
			var name = $(this).attr('name');
			if('countryId' == name){
				if(!v){
					$('#ttm-sd-country').find('input[tag=text]').addClass('input_error');
					isSubmit = false;
					return;
				}
				var cname = $(this).attr('cname');
				var countryName = $('#ttm-sd-country').find('input[tag=text]').val();
				if(countryName != cname){
					countryName = countryName.toLowerCase();
					$('#ttm-sd-country').find('input[tag=value]').val('');
					$('#ttm-sd-country').find('input[tag=value]').attr('cname','');
					var len = $.allCountry.length;
					for(var i = 0 ; i < len ; i++){
						var c = $.allCountry[i];
						if(c.cname.toLowerCase() == countryName){
							$('#ttm-sd-country').find('input[tag=value]').val(c.iid);
							$('#ttm-sd-country').find('input[tag=value]').attr('cname',c.cname);
							break;
						}
					}
					v = $('#ttm-sd-country').find('input[tag=value]').val();
					if(!v){
						$('#ttm-sd-country').find('input[tag=text]').addClass('input_error');
						isSubmit = false;
						return;
					}
				}
				datas[name] = $(this).val();
			}else if(!v){
				$(this).addClass('input_error');
				isSubmit = false;
			 }else{
				 $(this).removeClass('input_error'); 
				 
				 datas[name] = $(this).val();
			 }
		});
		if(isSubmit){
			$.ajax({
				url: '/order/add-shipping-address',//@controllers.order.routes.AddressController.AddshippingAddress()
				type: 'POST', 
				data:datas, 
				dataType: 'Json', 
				timeout: 30000, 
				error: function(){
					ttmErrorAlert('add shipping address failed',3000);
				}, 
				success: function(result){
					if(!result.succeed){
						for(var attr in result){
							if("countryId" == attr){
								$('#ttm-sd-country').find('input[tag=text]').addClass('input_error');
							}else{
								$('input[name=' + attr + ']').addClass('input_error');
							}
						}
					}else{
						$('#add-shipping-address-form').hide();
						window.location.reload();
					}
				} 
			}); 
		}
	});
	
	$('#ttm-sa-select').find('ul').unbind('click').click(function(){
		var addressId = $(this).attr('addressId');
		$('#ttm-shipping-addressId').val(addressId);
		var name = $(this).find('.address_name').text();
		var txt = $(this).find('.address_txt').text();
		var phone = $(this).find('.address_phone').text();
		$('#ttm-asa').find('.address_name').text(name);
		$('#ttm-asa').find('.address_txt').text(txt);
		$('#ttm-asa').find('.address_phone').text(phone);
		
		refreshShipMethod(addressId);
		
		$(".popBoxNone").hide();
		$(".publice_show").fadeOut();
	});
	var clean = $('#ttm-sd-country').find('.sClean');
	clean.unbind('hover click');
	clean.hover(function(){
		shippingCountry.clear();
	});
});




//**********************bill_address*************************


$(function(){
	var shippingCountry = $('#ttm-bd-country').country();
	$("input").blur(function(){
		 var v =  $(this).val();
		 if(!v){
			$(this).addClass('input_error');
		 }else{
			$(this).removeClass('input_error'); 
		 }
	});
	$('#AddBABtn').click(function(){
		var isSubmit = true;
		var datas = {};
		$('#add-billing-address-form').find('input').each(function(){
			var v =  $(this).val().trim();
			 if(!v){
				$(this).addClass('input_error');
				isSubmit = false;
			 }else{
				 $(this).removeClass('input_error'); 
				 var name = $(this).attr('name');
				 datas[name] = $(this).val();
			 }
		});
		if(isSubmit){
			$.ajax({
				//@controllers.order.routes.AddressController.AddshippingAddress()
				url: '/order/add-shipping-address',
				type: 'POST', 
				data:datas, 
				dataType: 'Json', 
				timeout: 30000, 
				error: function(){
					alert('add shipping address failed');
				}, 
				success: function(result){
					if(!result.succeed){
						for(var attr in result){
							if("countryId" == attr){
								$('#add-sa-id-text').addClass('input_error');
							}else{
								$('input[name=' + attr + ']').addClass('input_error');
							}
						}
					}else{
						$('#add-billing-address-form').hide();
						window.location.reload();
					}
				} 
			}); 
		}
	});
	$('#ttm-ba-select').find('ul').click(function(){
		var name = $(this).find('.address_name').text();
		var txt = $(this).find('.address_txt').text();
		var phone = $(this).find('.address_phone').text();
		$('#ttm-aba').find('.address_name').text(name);
		$('#ttm-aba').find('.address_txt').text(txt);
		$('#ttm-aba').find('.address_phone').text(phone);
		$(".popBoxNone").hide();
	});
});

