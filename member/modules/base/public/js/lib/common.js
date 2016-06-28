/**
 * @desc 通用JS
 */
//切换货币
$(function(){
	function switchCurrency(currency){
//		var usAmount = 0;
//		var rate = currencyRate[currency];
//		var label = currencyLabel[currency];
//		
//		$('.pricelab').each(function(){
//			usAmount = $(this).attr('usvalue');
//			$(this).html(label+(usAmount * rate).toFixed(2));
//		});
		//保存cookie保存cookie
		document.cookie = 'currency='+currency;
	}
	
	$(".pu_navHover").parent(".lineBlock").hover(function(){
		$(this).find(".pu_blockWarp").show();
	},function(){$(this).find(".pu_blockWarp").hide();})
})
/**
 * @desc 通用JS
 */
//切换语言
$(function(){
	//读取cookie的值 
	function switchLanguage(language){
		document.cookie = 'language='+language;
	}
	function getLanguage(language){//获取指定名称的cookie的值
	    var arrLang = document.cookie.split("; ");
	    for(var i = 0;i < arrLang.length;i ++){
	        var temps = arrLang[i].split("=");
	        if(temps[0] == "language"){
	        	switchLanguage(unescape(temps[1]));
	        }
	   }
	}
	getLanguage();
	var langC = $(".pu_langWarp").siblings(".pu_blockWarp").children("a");
	langC.bind("click",function(){
		var lang = $(this).attr("lang");
		var langTxt = $(this).text();
		switchLanguage(lang);
		$(this).parents(".pu_blockWarp").siblings(".pu_navHover").html(langTxt+"<i class='icon-arr'> </i>");
		$(this).parents(".pu_blockWarp").hide();
		window.location.reload(true);
	})
})
/**
 * @desc 通用JS
 */
//切换国家 搜索国家
function Country() {}
Country.prototype = {
	// 得到所有国家
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
$(function(){
var country = new Country();
//	$(".country_list li.country_item").click(function() {
//		var countryText = $(this).find("span").text();
//		var countryCode = $(this).find("span").attr("data");
//		var texts = $(this).parentsUntil(".selectFlag", "div.country_all").prev(".pu_navHover");
//		texts.children("#current_country_flage").removeClass();
//		texts.children("#current_country_flage").addClass("flag_" + countryCode);
//		texts.children(".flag_Txt").text(countryText);
//	});
	$("input[name=country_filter]").keyup(function() {
		country.search(this);
	});
})

 

 
/**
 * @desc 历史记录 返回相关产品
 * @desc 公用
 */

$(function(){
	 
})

 
function setLangCookie(obj){
	var url = obj.id;
	var langid = obj.tabIndex;
	if(url.indexOf('.tomtop.com')>0){
		var lang = obj.lang;
		setCurrentCookie("PLAY_LANG",lang,langid);
	}
	document.location.href=url;
}

function setCurrentCookie(name,value)
{
	var Days = 1;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	var Days = 1;
	var expout = new Date();
	expout.setTime(expout.getTime() + -1*24*60*60*1000);
	document.cookie=name + "=;expires="+expout.toGMTString();
	document.cookie=name + "="+ escape(value) + ";expires="+exp.toGMTString()+";path=/; domain=.tomtop.com";
}
 