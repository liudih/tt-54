$(function(){
	getStorageTotal();
})

function getsubtotal(){
	var price = 0;
	$("#dataContent .theprice").each(function(i,e){
		var thep = $(this).val();
		thep = thep.replace(/[,]/g,"");
		price += parseFloat(thep);
	});
	var currency = $("#cart_currency").val();
	var isJPY = 'JPY' == currency;
	price = isJPY ? Math.floor(price) : price.toFixed(2);
	return price;
}

function placeOrderSubmit() {
	
	var shipMethodCode = $("#shipMethodCode").val();
	var shipMethodId = $("#shipMethodId").val();
	if(shipMethodCode=="" || shipMethodCode==null || shipMethodId=="" || shipMethodId==null){
		ttmErrorAlert('shipping method must select',3000);
		return 
	}
	var sd = $('#ttm-shipping-addressId').val();
	if(!sd){
		ttmErrorAlert('shipping address must select',3000);
		return ;
	}
	$('#paypal-form').submit();
}