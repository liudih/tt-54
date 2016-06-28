function Language() {
}

Language.prototype = {
	// 得到所有货币
	init : function(languageurl) {
		$.get(languageurl, function(data) {
			var option = "";
			$.each(data, function(i,item){
				option = option + "<li name=language_li value='"+ item.id + "'><span>"+ item.name + "</span></li>";
			});
			
			$("ul[name=language_ul]").each(function() {
				$(this).append(option);
			});
			
			$(document).on("click","li[name=language_li]",function(){
				var languageName = $(this).find("span").text();
				var languageId = $(this).attr("value");
				var h3 = $(this).parentsUntil(".select_language", "ul[name=language_ul]").prev("h3");
				h3.children("span").text(languageName);
				h3.children("input").val(languageId);
				h3.children("input").attr({shortName:languageName});
			});
		});
	}
}