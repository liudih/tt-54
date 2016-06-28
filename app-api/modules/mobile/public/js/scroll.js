window.onscroll = function()
{	
	var my_hide = $(".TT_top");
	var my_button = $("#proPayBut");
	var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
	if(scrollTop >= 300){
		my_hide.fadeIn();
	}else{
		my_hide.stop(true);
		my_hide.fadeOut();
	}
	if(scrollTop >= 10){
		my_button.fadeIn();
	}else{
		my_button.stop(true);
		my_button.fadeOut();
	}
}