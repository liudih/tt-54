$(function(){
		fix = $(".spCartRightPay_Fix").offset().top;
		fixH = $(".spCartRightPay_Fix").height();
		orderH = $(".spCartLeftPay_Box").height();
		windowH = $(window).height();
		bodyH = $(document.body).height();
})

window.onscroll = function()
{	
	var userAgent = navigator.userAgent.toLowerCase(),uaMatch;
	var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
	//document.title=scrollTop;
	if(scrollTop > fix && windowH>fixH)
	{
		$(document).ready(function()
		{
			 if(fix+orderH<scrollTop+windowH-(windowH-fixH-80)){
				$(document).ready(function()
				{
					$(".spCartRightPay_Fix").css({"position":"relative","top":bodyH-fixH-fix-623,"z-index":"99"})
				})
			 }else{
				$(".spCartRightPay_Fix").css({"position":"fixed","top":"0px","z-index":"99"})	 
			 }
		})
	}else if(scrollTop > fix+fixH-windowH && windowH<=fixH &&fixH<orderH&&scrollTop<bodyH-windowH-590){
		$(document).ready(function()
		{
			$(".spCartRightPay_Fix").css({"position":"fixed","bottom":"0px","top":"auto","z-index":"99"})
		})
	}else if(fixH>=orderH){
		$(document).ready(function()
		{
			$(".spCartRightPay_Fix").css({"position":"relative","z-index":"99"})
		})
	}else if(fix+orderH<scrollTop+windowH+100 && windowH<=fixH &&fixH<orderH){
		$(document).ready(function()
		{
			if(userAgent.match(/chrome\/([\d.]+)/)){
				$(".spCartRightPay_Fix").css({"position":"relative","top":bodyH-fixH-fix-608,"z-index":"99"})
			}else{
				$(".spCartRightPay_Fix").css({"position":"relative","top":bodyH-fixH-fix-623,"z-index":"99"})
			}
		})
	}
	else
	{
		$(document).ready(function()
		{
		$(".spCartRightPay_Fix").css({"position":"relative","z-index":"99"})
		})
	}
}


