$("[tag='submit_button']").click(function() {
	var dom = $(this);
	var name = dom.parents("li:eq(0)").attr("name");
	var paymentid = dom.parents("li:eq(0)").find("input[type='radio']:eq(0)").val();
	$("#paymentId").val(paymentid);
	if(paymentid=="paypal"){
		var redirect = '<p class="loading">'
					+ '<i>Redirecting</i>'
					+ '<img src="/base/assets/images/payment_wait.gif">'
					+ '</p>';
		//防止多次提交
		$(this).parent().append(redirect);
		$(this).remove();
		var orderid = $("#corderId").val();
		location.href = "/paypal/ec?ordernum="+orderid;
	}else{
		$("#payment_form").submit();
	}
});
