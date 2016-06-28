define([ 'jquery', '../model/WholeSaleProduct' ], function($, WholeSaleProduct) {

	var myObj = new WholeSaleProduct();

	$("#addWholeSale").click(function() {
		myObj.addWholeSaleProduct();
	});
	
	$(".chooseProduct").click(function() {
		$(this).children("span").toggleClass("choosed");
		myObj.chooseProduct();
	});
	
	$(".qty_add").click(function() {
		myObj.addQty($(this));
	});
	
	$(".qty_reduction").click(function() {
		myObj.reductionQty($(this));
	});
	
	$(".deleteYes").click(function() {
		myObj.deleteProduct($(this));
	});
	
	$(".number").keyup(function() {
		myObj.checkNum(this);
	});
	
	$(".chooseAll").click(function() {
		myObj.chooseAll();
	});
	
	$(".deleteAllYes").click(function() {
		myObj.deleteAllProduct();
	});
	
	$(".cancel").click(function() {
		$('.pu_pop').hide();
	});
	
	// 失去焦点的数量判断
	$(".quantity-text").blur(function() {
		myObj.changeQty(this);
	});

	// 按下Enter键
	$(".quantity-text").keydown(function(event) {
		if (event.keyCode == 13) {
			myObj.changeQty(this);
		}
	});
});