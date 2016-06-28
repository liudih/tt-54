window.onscroll = function()
{	
	var my_hide = $(".TT_top");
	var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
	if(scrollTop >= 300){
		my_hide.fadeIn();
	}else{
		my_hide.stop(true);
		my_hide.fadeOut();
	}
}