	var hiddenFlag=getCookie('app-hidden-flag');
	if(hiddenFlag=='1'){
		deleteApp($(".closeDownApp"));
	}else{
		showApp();
	}
	var browser = {
		versions : function() {
			var u = navigator.userAgent, app = navigator.appVersion;
			return {// 移动终端浏览器版本信息
				trident : u.indexOf('Trident') > -1, // IE内核
				presto : u.indexOf('Presto') > -1, // opera内核
				webKit : u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
				gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, // 火狐内核
				mobile : !!u.match(/AppleWebKit.*Mobile.*/), // 是否为移动终端
				ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
				android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或者uc浏览器
				iPhone : u.indexOf('iPhone') > -1, // 是否为iPhone或者QQHD浏览器
				iPad : u.indexOf('iPad') > -1, // 是否iPad
				webApp : u.indexOf('Safari') == -1
			// 是否web应该程序，没有头部与底部
			};
		}(),
		language : (navigator.browserLanguage || navigator.language).toLowerCase()
	}
	
	if ( browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
//		$(".downloadApp").click(function(){
//			window.location = "https://itunes.apple.com/cn/app/tomtop-global-online-shopping/id1027264288?mt=8";
//		})
		
		deleteApp($(".closeDownApp"));
        
	}else if (browser.versions.android) {
		$(".downloadApp").click(function(){
			
			/// 通过iframe的方式试图打开APP，如果能正常打开，会直接切换到APP，并自动阻止a标签的默认行为
			// 否则打开a标签的href链接
			var ifrSrc = 'tomtop://';
			if (!ifrSrc) {
				return;
			}
			var ifr = document.createElement('iframe');
			ifr.src = ifrSrc;
			ifr.style.display = 'none';
			document.body.appendChild(ifr);
			setTimeout(function() {
				document.body.removeChild(ifr);

			}, 1000);

		})
		
		$('.downloadApp').attr('href','http://www.tomtop.com/img/downloads/tomtop_app.apk'); 
		if (document.all) {
			$(".downloadApp").click();
		}
		// 其它浏览器
		else {
			var e = document.createEvent("MouseEvents");
			e.initEvent("click", true, true);
			$(".downloadApp").dispatchEvent(e);
		}
		
	}
	
	
