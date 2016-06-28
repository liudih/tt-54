//////下拉框公用
$(function(){
	$("body").click(function(e){
		$(".btn-group").removeClass("open");
		e.stopPropagation(); 
	})
	$(".dropdown-toggle").click(function(e){
		if($(this).parent(".btn-group").hasClass("open")){
			$(this).parent(".btn-group").removeClass("open");
		}else{
			$(".btn-group").removeClass("open");
			$(this).parent(".btn-group").addClass("open");
		}
		e.stopPropagation(); 
	})
})


//html5标签兼容ie
function html5($){
	 if (! 
	 /*@cc_on!@*/
	 0) return;
	 var e = "abbr, article, aside, audio, canvas, datalist, details, dialog, eventsource, figure, footer, header, hgroup, mark, menu, meter, nav, output, progress, section, time, video".split(', ');
	 var i= e.length;
	 while (i--){
	     document.createElement(e[i])
	 } 
}
html5(jQuery);

////主导航js

$(function(){
	$(".subSecondA").hover(function(){
		var index = $(this).index();
		var thisSib = $(this).parents(".subSecond").siblings(".subThird");
		$(this).siblings().removeClass("subSecondAci");
		$(this).addClass("subSecondAci");
		thisSib.removeClass("thirdBlock");
		thisSib.eq(index).addClass("thirdBlock");
	})
})

////banner
$(document).ready(function()
{
	var interval=0;
	var now=0;
	var ready=true;
	var banners=$(".imgBanner");
	var bannerBox=$(".bannerwrap");
	var leftC =$(".bannerLeft_click");
	var rightC =$(".bannerRight_click");
	function playBanner()
	{
		clearInterval(interval);
		interval=setInterval(function()
		{
			//$("img.lazy").lazyload({event:'sporty'})
			if(banners.length <= 1) clearInterval(interval);
			next= now+1 >= banners.length ? 0 : now+1;
			var currObj=banners.eq(now);
			var nextObj=banners.eq(next);
			currObj.stop(true).fadeOut(1000);
			nextObj.stop(true).fadeIn(1000);
			
			$(".bannerPoint b").removeClass("point-on");
			$(".bannerPoint b").eq(next).addClass("point-on");
			now=next;
		},5000)
	};
	playBanner();
	leftC.hover(function(){
		$(this).addClass("leftArrAci");
	},function(){
		$(this).removeClass("leftArrAci");
	})
	rightC.hover(function(){
		$(this).addClass("rightArrAci");
	},function(){
		$(this).removeClass("rightArrAci");
	})
	$(".bannerPoint b").bind("click",function()
	{
		var index=$(this).prevAll("b").length;
		if(index == now) return;
		clearInterval(interval);
		
		var currObj=banners.eq(now);
		var nextObj=banners.eq(index);
		currObj.stop(true).fadeOut(1000);
		nextObj.stop(true).fadeIn(1000);
		
		$(".bannerPoint b").removeClass("point-on");
		$(this).addClass("point-on");
		now=index;
		playBanner();
	})
	bannerBox.mouseover(function(){
		leftC.fadeIn();
		rightC.fadeIn();
		clearInterval(interval);
	})
	bannerBox.mouseleave(function(){
		leftC.fadeOut();
		rightC.fadeOut();
		playBanner();
	})
	leftC.bind("click",function(){
		var index=$(".point-on").index();
		prev= index-1 < -1 ? banners.length : index-1;
		var currObj=banners.eq(now);
		var nextObj=banners.eq(prev);
		if(!ready)return;
		ready=false;
		currObj.stop(true).fadeOut(1000);
		nextObj.stop(true).fadeIn(1000,function(){ready=true;});
		$(".bannerPoint b").removeClass("point-on");
		$(".bannerPoint b").eq(prev).addClass("point-on");
		now=prev;
		playBanner();
	})
	rightC.bind("click",function(){
		var index=$(".point-on").index();
		next= now+1 >= banners.length ? 0 : now+1;
		var currObj=banners.eq(now);
		var nextObj=banners.eq(next);
		if(!ready)return;
		ready=false;
		currObj.stop(true).fadeOut(1000);
		nextObj.stop(true).fadeIn(1000,function(){ready=true;});
		$(".bannerPoint b").removeClass("point-on");
		$(".bannerPoint b").eq(next).addClass("point-on");
		now=next;
		playBanner();
	})
})

////move 左右点击切换产品
$(function(){
	moveBox($(".moveWarp"));    //logo 切换
	moveBox($(".dealsWarp"));   // 超级打折
	moveBox($(".topSellers"));	//Top Sellers 
	moveBox($(".newArrivals")); //新品
	moveBox($(".mightLike")); //Products You Might Like
	moveBox($(".ItemsConsider")); //More Items to Consider
	moveBox($(".viewedWarp"));  //历史记录左边
	moveBox($(".alsoLike"));    //历史记录右边
	
})

// $(function(){try{moveBox($(".moveWarp"));}catch(e){};})
// $(function(){try{moveBox($(".dealsWarp"));}catch(e){};})
// $(function(){try{moveBox($(".topSellers"));}catch(e){};})
// $(function(){try{moveBox($(".newArrivals"));}catch(e){};})
// $(function(){try{moveBox($(".mightLike"));}catch(e){};})
// $(function(){try{moveBox($(".ItemsConsider"));}catch(e){};})
// $(function(){try{moveBox($(".viewedWarp"));}catch(e){};})
// $(function(){try{moveBox($(".alsoLike"));}catch(e){};})


function moveBox(warp){
	var page = 1;
	var listWarp = warp;
	var leftC = listWarp.find(".moveLeftClick");
	var rightC = listWarp.find(".moveRightClick");
	var moveBox = listWarp.find(".moveBox");             //列表容器
	var warpW = listWarp.find(".moveHidden").width(); //容器宽度
	var moveList = listWarp.find(".moveList");
	var moveListW = moveList.outerWidth(true);           //列表宽度 包括margin
	var moveBoxW = moveListW*moveList.length;            //列表的总宽度
	var position = moveBox.position().left;               //容器运动距离
	var page_number = Math.ceil(moveBoxW/warpW);           //总共多少页 向上取整
	var page_n = listWarp.siblings().find(".page");        //当前第几页
	var page_s = listWarp.siblings().find(".pages");       //总共多少页
	var dragWarp = listWarp.find(".feed-scrollbar");       //拖拽容器
	
	moveBox.css({"width":moveBoxW})
	page_s.html(page_number);
	
	if(moveBoxW<=warpW){                    //如果移动距离小于容器宽度  左右按钮隐藏
		leftC.hide();
		rightC.hide();
		dragWarp.hide();                   //拖拽隐藏
	}else{
		rightC.addClass("rightArrAci");
	}
	//moveBanner();
	rightC.bind("click",function(){
		$("img.lazy").lazyload({event:'sporty'})
		if (!moveBox.is(":animated")) {      //判断是否在运动
			if(scrol.length>0){                  //判断是否有 scroll 拉条
				if(dragW>warpW-(scrol.position().left+dragW)){
					scrol.animate({
						left:'+='+(warpW-scrol.position().left-dragW)
					})
					moveBox.animate({
						left:-(moveBoxW-position-warpW)  //移动距离 = 总宽度 -移动的距离-容器宽度
					}).find('img').trigger('sporty');
				}else{
					scrol.animate({
						left:'+='+(dragW)
					})
					moveBox.animate({
						left:'-='+(warpW)                   //否则 移动距离为 容器宽度
					}).find('img').trigger('sporty');
				}
				var pages = scrol.position().left+1; 
				var num = Math.ceil(pages/dragW);
				if(num>=page_number){                        //页码显示数字
					num=page_number;
					page_n.html(num);
				}else{
					page_n.html(num+1);
				}
				if(num>=page_number-1){
					rightC.removeClass("rightArrAci");
				}
			}else{
				if(page>=page_number-1){          //如果当前页面=总页面-1
					moveBox.animate({
						left:-(moveBoxW-position-warpW)  //移动距离 = 总宽度 -移动的距离-容器宽度
					}).find('img').trigger('sporty');
				}else{
					moveBox.animate({
						left:'-='+(warpW)                   //否则 移动距离为 容器宽度
					}).find('img').trigger('sporty');
				}
				page++;                                    //第几页计算
				if(page>=page_number){
					page=page_number;
					page_n.html(page);
				}else{
					page_n.html(page);
				}
				if(page>=page_number){
					rightC.removeClass("rightArrAci");
				}
			}
			leftC.addClass("leftArrAci");              //当前按钮
		}
	})
	leftC.bind("click",function(){
		if (!moveBox.is(":animated")) {
			if(scrol.length>0){                  //判断是否有 scroll 拉条
				if(scrol.position().left<dragW){
					scrol.animate({
						left:0
					})          
					moveBox.animate({
						left:0  
					})
				}else{
					scrol.animate({
						left:'-='+(dragW)
					})
					moveBox.animate({
						left:'+='+(warpW)                   
					})
				}
				//判断第几页
				var pages = scrol.position().left; 
				var num = Math.ceil(pages/dragW);
				if(num<=1){
					num=1;
					page_n.html(num);
				}else{
					page_n.html(num);
				}
				if(num<=1){
					leftC.removeClass("leftArrAci");
				}
			}else{
				if(page<=2){          
					moveBox.animate({
						left:0  
					})
				}else{
					moveBox.animate({
						left:'+='+(warpW)                   
					})
				}
				page--;                                 //第几页计算
				if(page<=1){
					page=1;
					page_n.html(page);
				}else{
					page_n.html(page);
				}
				if(page<=1){
					leftC.removeClass("leftArrAci");
				}
			}
			rightC.addClass("rightArrAci");
		}
	})
	
	
	///////////拖拽//////
	var scrolBox = listWarp.find(".feed-scrollbar");
	var scrol = listWarp.find(".feed-scrollbar-thumb");
	var dragW = warpW/(moveBoxW/warpW);                  //拖拽按钮宽度
	var disX = 0;
	scrol.css({"width":dragW});
	
	function mousemove(ev)
	{
		var scrolL = scrol.position().left;
		var oEvent = ev || event;
		var scrolBox_H = oEvent.clientX - disX;
		if(scrolBox_H < 0)
		{
			scrolBox_H = 0;
		}
		else if(scrolBox_H > scrolBox.width()-dragW)
		{
			scrolBox_H = scrolBox.width()-dragW;
		}
		
		var scale = scrolBox_H / (scrolBox.width()-dragW);
		
		moveBox.css({"left":-scale * (moveBoxW-warpW)});   // 产品移动
		scrol.css({"left":scrolBox_H});                    // scroll移动
		
		var pages = scrol.position().left; 
		var num = Math.ceil(pages/dragW);
		page_n.html(num+1);
		//拖拽判断 左右按钮点击当前情况
		if(pages==0){
			leftC.removeClass("leftArrAci");
		}else{
			leftC.addClass("leftArrAci");
		}
		if(pages==warpW-scrol.width()){
			rightC.removeClass("rightArrAci");
		}else{
			rightC.addClass("rightArrAci");
		}
	}
	
	function mouseup(ev)
	{
		this.onmousemove = null;
		this.onmouseup = null;
	    scrol.removeClass("block")
		if(scrol.releaseCapture)
		{
		   scrol.releaseCapture();
		}
		$("img.lazy").lazyload({event:'sporty'})
	}
	
	scrol.mousedown(function(ev)
	{
        var oEvent = ev || event;
	    disX = oEvent.clientX-scrol.position().left;
	    scrol.addClass("block")
		if(scrol.setCapture)
		{
			scrol.onmousemove= mousemove;
			scrol.onmouseup = mouseup;
			scrol.setCapture();
		}
		else
		{
			document.onmousemove= mousemove;
			document.onmouseup = mouseup
		}
	    return false;
	})
	
	
	
}

//初始化 无缝滚动
$(function(){
	$(".scrollRightWarp").myScroll({
		speed:3000, //数值越大，速度越慢
		rowHeight:82 //li的高度
	});
});
//class

$(function(){
	var classMouse = $(".categoriesClass").children("li");
	var listBloxk = $(".categoriesList");
	classMouse.mouseover(function(){
		var index = $(this).index();
		$(this).siblings().removeClass("categoriesAci");
		$(this).addClass("categoriesAci");
		listBloxk.hide();
		listBloxk.eq(index).show();
		//.find('img').trigger('sporty');
		//$("img.lazy").lazyload({event:'sporty'})
	})
})



$(function() {
	//$("img.lazy").lazyload();
  	 $("img.lazy").lazyload({effect : "fadeIn"});

});

