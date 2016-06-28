require(['./common'], function (common) {
	$("#j-country-code-list").find("a").click(function(){
		var code = $(this).find("strong.i-country-code").text();
		$("input[name=qiwiCountry]").val(code);
	});
});