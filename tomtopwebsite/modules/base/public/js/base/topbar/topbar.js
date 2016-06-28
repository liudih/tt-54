$("#regional_settings_save").click(function(e) {
	//获取设置
	var currencyCode = $("#current_currency").val();
	var countryIsoCode = $("#current_country").val();
	var countryName = $("#current_country").attr("countryName");
	var languageId = $("#current_language").val();
	var languageName = $("#current_language").attr("shortName");
	
	var dataObject = dataObject || {};
	dataObject['currencyCode'] = currencyCode;		
	dataObject['countryIsoCode'] = countryIsoCode;
	dataObject['languageId'] = languageId;
	dataObject['languageName'] = languageName;
	
	var json=JSON.stringify(dataObject);
	
	$.ajax({
		url : js_set_regional.controllers.base.Regional.regionalSettings().url,
		type : "POST",
		data : json,
		contentType : "application/json",
		success : function(data) {
			$("#default_regional").text("Ship to" + countryName+", "+ currencyCode);
			$("#current_country_flage").removeClass();
			$("#current_country_flage").addClass("flag_" + countryIsoCode);
			window.location.reload(true);
		}
	});
})