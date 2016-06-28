$(function(){
	//左边类目选择
	$(".dirTitleList").click(function(){
		$(this).toggleClass("dirAci");
		$(this).children("i").toggleClass("icon-OrLtArr");
		$(this).next(".dirList").slideToggle();
	})
	$(".dirSelectList").click(function(){
		$(this).toggleClass("selectOrange")
		$(this).children(".multi-select").toggleClass("multiAci")
	})
	$(".dirTitle").click(function(){
		$(this).siblings(".dirToggle,.dirToggleFs").slideToggle();
		$(this).children(".icon-minus").toggleClass("icon-plus");
	})
	
	//banner下面的导航 选择显示方式
	$(".icon-showBlock").click(function(){
		$(this).addClass("active");
		$(".icon-showList").removeClass("active");
		//产品显示方式切换
		$(".categoryProductList").addClass("categoryProductBlock");
	})
	$(".icon-showList").click(function(){
		$(this).addClass("active");
		$(".icon-showBlock").removeClass("active");
		//产品显示方式切换
		$(".categoryProductList").removeClass("categoryProductBlock");
	})
	//likes
	$(".likes").click(function(){
		$(this).children(".icon-hearts").addClass("icon-heartR")
	})
	
})
$(document).ready(function(){
	$(".dirToggle").each(function(){
		var scrollBoxH = $(this).height();
		if(scrollBoxH>130){
			$(this).css({"height":"130px"})
			$(this).jscroll({
				W:"16px",
				BgUrl:"url(icon/jsScroll.jpg)",
				Bg:"right 0 repeat-y",
				Btn:{btn:false},
				Fn:function(){}
			});
		}
		
	})

});