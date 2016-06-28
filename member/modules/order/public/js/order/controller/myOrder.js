define([ 'jquery', '../model/MyOrder' ], function($, MyOrder) {

	var myOrderObj = new MyOrder();

	myOrderObj.init();

	$("a[tag=pageSize]").click(function() {
		myOrderObj.changePageSize(this);
		myOrderObj.submit();
	});

	$("li[tag=status_tab]").click(function() {
		myOrderObj.changeTab(this);
		myOrderObj.submit();
	});

	$("li[tag=is_show]").click(function() {
		myOrderObj.changeIsShow(this);
		myOrderObj.submit();
	});

	$("select[name=status_value]").change(function() {
		myOrderObj.changeStatus($(this).val());
	});

	$("#search_orders_submit").click(function() {
		$("input[name=pageNum]").val(1);
		myOrderObj.submit();
	});

	$("#searchOrders").keydown(function(e) {
		var keyNum = e.which || e.keyCode;
		if (keyNum == 13) {
			myOrderObj.submit();
		}
	});

	$("#search_type").change(function() {
		var name = $("#search_type").val();
		$("#search_order_text").attr("name", name);
	});
});