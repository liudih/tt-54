function Currency() {
}

Currency.prototype = {
		// 得到所有货币
		init : function(currencyurl) {
			$.get(currencyurl, function(data) {
				var option = "";
				$.each(data, function(i,item){
					option = option + "<a data-currency='"+ item.ccode 
					+"' href='javascript:;' rel='nofollow'><em>"
					+ item.ccode +"</em><span>"+ item.csymbol +"</span></a>";
					
				});
				
				$(".pu_notranslate").append(option);
				
				$(".pu_notranslate a").each(function(){
					$(this).click(function(){
						//topbar中currency的切换
						var currencySymobol = $(this).find("span").text();
						var currencyCode = $(this).find("em").text();
						$('#top_nav_currency_1').html(currencySymobol+"<i class='icon-arr'> </i>")
						//产品页面中的货币切换
						var pCurrentCurrency = currencyCode;
						var switchCurrencyJson =JSON.stringify();
						var switchCurrencyJson ={currencyCode:currencyCode};
						
						$.ajax({
							url : js_set_regional.controllers.base.Regional.switchCurrency().url,
							type : "POST",
							data : JSON.stringify(switchCurrencyJson),
							contentType : "application/json",
							success : function(data) {
								window.location.reload(true);
							}
						});
					});
				});
				 
			});
		}
}