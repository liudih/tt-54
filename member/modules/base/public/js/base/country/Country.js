function Country() {
}

Country.prototype = {
	// 得到所有国家
	init : function(countryurl) {
		$.get(countryurl, function(data) {
			var option = "";
			$.each(data, function(i, item) {
				option = option + "<li class='country_item flag_" + item.isoCode
						+ "'><em></em><span data='"	+ item.isoCode + "'>" + item.name + "</span></li>";
			});
			$(".country_list").each(function() {
				$(this).append(option);
			});
			$(".country_list li.country_item").click(function() {
				var countryText = $(this).find("span").text();
				var countryCode = $(this).find("span").attr("data");
				var texts = $(this).parentsUntil(".selectFlag", "div.country_all").prev(".pu_navHover");
				texts.children("#current_country_flage").removeClass();
				texts.children("#current_country_flage").addClass("flag_" + countryCode);
				texts.children(".flag_Txt").text(countryText);
				
				var dataObject = dataObject || {};
				dataObject['countryIsoCode'] = countryCode;
				
				var json=JSON.stringify(dataObject);
				
				$.ajax({
					url : js_set_regional.controllers.base.Regional.switchCountry().url,
					type : "POST",
					data : json,
					contentType : "application/json",
					success : function(data) {
					}
				});
				
			});
			 
//			$(".country_list li.country_item").click(function() {
//				var countryText = $(this).find("span").text();
//				var countryCode = $(this).find("span").attr("data");
//				var h3 = $(this).parentsUntil(".select_country", "div.country_all").prev("h3");
//				h3.removeClass();
//				h3.addClass("flag_" + countryCode);
//				h3.children("span").text(countryText);
//				h3.children("input").val(countryCode);
//				h3.children("input").attr({countryName:countryText});
//			});
		});
	},
	search : function(q) {
		var regstr = $(q).val() + ".*";
		var reg = new RegExp(regstr, "i");
		$(".country_list li.country_item").each(function(i) {
			$(this).hide();
		});
		$(".country_list li.country_item").each(function(i) {
			if ($.trim($(this).html()) != "") {
				if (reg.test($(this).children("span").html().toLowerCase())) {
					$(this).show();
				} else {
					$(this).hide();
				}
			}
		});
	},
}
