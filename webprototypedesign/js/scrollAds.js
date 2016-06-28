$(function(){
		fix = $(".spCartRightPay_Fix").offset().top;
		fixH = $(".spCartRightPay_Fix").height();
		orderH = $(".spCartLeftPay_Box").height();
		windowH = document.body.clientHeight;
})

window.onscroll = function()
{	
	var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
	if(scrollTop > fix && windowH>fixH)
	{
		$(document).ready(function()
		{
			$(".spCartRightPay_Fix").css({"position":"fixed","top":"0px","z-index":"9"})
		})
	}else if(scrollTop > fix+fixH-windowH && windowH<=fixH &&fixH<orderH){
		$(document).ready(function()
		{
			$(".spCartRightPay_Fix").css({"position":"fixed","bottom":"0px","z-index":"9"})
		})
	}else if(fixH>=orderH){
		$(document).ready(function()
		{
			$(".spCartRightPay_Fix").css({"position":"relative","z-index":"9"})
		})
	}
	else
	{
		$(document).ready(function()
		{
		$(".spCartRightPay_Fix").css({"position":"relative","z-index":"9"})
		})
	}
}


