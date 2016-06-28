(function(w) {
    var pcm = readCookie('pcm');
    //var ua  = w.navigator.userAgent;
	var ua  = w.navigator.userAgent.toLocaleLowerCase();
    var matchedRE = /iphone|android|symbianos|windows\sphone/g;
    function readCookie(name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1, c.length)
            }
            if (c.indexOf(nameEQ) == 0) {
                return c.substring(nameEQ.length, c.length)
            }
        }
        return null;
    }
    if (matchedRE.test(ua) && pcm != '1' ) {
    	var redirectUrl = w.location.href;
    	var mredirectUrl = redirectUrl;
    	if(redirectUrl.indexOf('uat.tomtop.com')>0){
    		mredirectUrl = redirectUrl.replace('uat.tomtop.com','muat.tomtop.com');
    	}else{
    		mredirectUrl =  redirectUrl.replace(/([0-9a-zA-Z_!~*'()-]+\.tomtop.com)/g,'m.tomtop.com');
    	}
    	if(redirectUrl!=mredirectUrl){
    		w.location.href = mredirectUrl;
    	}
    }
})(window);

function setLangCookie(obj){
	var url = obj.id;
	var langid = obj.tabIndex;
	if(url.indexOf('.tomtop.com')>0){
		var lang = obj.lang;
		setCurrentCookie("PLAY_LANG",lang,langid);
		associateCurrency(lang);
	}
	document.location.href=url;
}

function associateCurrency(lang)
{
	if(lang == "en"){
		switchCurrency("USD");
	}else if(lang == "ru"){
		switchCurrency("RUB");
	}else if(lang == "jp"){
		switchCurrency("JPY");
	}else{
		switchCurrency("EUR");
	}
}

function switchCurrency(currency)
{
	document.cookie = 'TT_CURR='+currency+';domain=.tomtop.com';
}

function setCurrentCookie(name,value,langid)
{
	var Days = 1;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	var Days = 1;
	var expout = new Date();
	expout.setTime(expout.getTime() + -1*24*60*60*1000);
	document.cookie=name + "=;expires="+expout.toGMTString();
	document.cookie=name + "="+ escape(value) + ";expires="+exp.toGMTString()+";path=/; domain=.tomtop.com";
	document.cookie="TT_LANG="+langid+";expires="+exp.toGMTString()+";path=/; domain=.tomtop.com";
}
$(function(){
$(".pu_navHover").parent(".lineBlock").hover(function(){
	$(this).find(".pu_blockWarp").show();
},function(){$(this).find(".pu_blockWarp").hide();})
})