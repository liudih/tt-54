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
