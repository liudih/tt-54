	function sendControls(control, dataCallback, errorCallback, doneCallback) {
			$.ajax({
				method : "POST",
				url : "/livechat/control",
				data : JSON.stringify(control),
				dataType : "json",
				contentType : "application/json",
				processData : false,
				success : dataCallback,
				error : errorCallback
			}).done(doneCallback);
		}

		function logout() {
			var ctl = {
				type : "LOGOUT"
			};
			sendControls(ctl);
		}
		
		var DispClose = true;
		window.onbeforeunload = function()
		{
			if(DispClose){
				return "";
			}
		}
		$(window).unload(function()
		{
			if(DispClose){
				logout();
			}
			DispClose = true;
		});
		window.onkeydown = function (){
			var ev = window.event || arguments[0];
			var code = ev.keyCode || ev.which;
			if (code==116) { 
				DispClose=false; 
			}
		}
		window.oncontextmenu = function(){
			return false;
		}
		
		function redirect(url){
		    DispClose = false;
			location.href = url;
		}
