function Currency() {
}

Currency.prototype = {
		// 得到所有货币
		init : function(currencyurl) {
			$.get(currencyurl, function(data) {
				var option = "";
				$.each(data, function(i,item){
					option = option + "<li name='currency_li'><a data-currency='"+ item.ccode +"' href='javascript:;' rel='nofollow'><em>"+ item.ccode +"</em><span>"+ item.csymbol +"</span></a></li>";
				});
				
				$(".notranslate").each(function() {
					$(this).append(option);
				});
				
				$(document).on("click","li[name=currency_li]",function(){
					//topbar中currency的切换
					var currencySymobol = $(this).find("span").text();
					var currencyCode = $(this).find("em").text();
					var h3 = $(this).parentsUntil(".switcher-currency-c", "ul.notranslate").prev("h3");
					h3.children("span").text(currencySymobol + " " + currencyCode);
					h3.children("input").val(currencyCode);
					
					//产品页面中的货币切换
					var pCurrentCurrency = $(this).parent(".notranslate").prev("#p_current_currency").text();
					var switchCurrencyJson =JSON.stringify();
					var switchCurrencyJson ={currencyCode:currencyCode};
					
					if(pCurrentCurrency != null && pCurrentCurrency != ""){
						$("#p_current_currency").text(currencyCode);
						$.ajax({
							url : js_set_regional.controllers.base.Regional.switchCurrency().url,
							type : "POST",
							data : JSON.stringify(switchCurrencyJson),
							contentType : "application/json",
							success : function(data) {
								window.location.reload(true);
							}
						});
					}
				});
			});
		}
}