//运动框架

function getByClass(oParent,sClass){
	var aEle = oParent.getElementsByTagName("*");
	var aResult = [];
	for(var i=0; i<aEle.length; i++){
		if(aEle[i].className==sClass){
			aResult.push(aEle[i]);
			}
		}
		return aResult;
	}
function getStyle(obj,name)
{
	if(obj.currentStyle)
	{
		return obj.currentStyle[name];
	}else
	{
		return getComputedStyle(obj,false)[name];
    }
}
function getPos(ev)
{
	var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
	var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;
	return {x:ev.clientX+scrollLeft,y:ev.clientY+scrollTop}
}
function startMove(obj,json,fnEnd)
{
  try
	{
		clearInterval(obj.timer);
		obj.timer=setInterval(function()
		{
			var bStop = true; //假设所有的都到了
			for(var attr in json)
			{
				cur = 0;
				if(attr=="opacity")
				{
					cur=Math.round(parseFloat(getStyle(obj,attr))*100);
				}
				else
				{
					cur=parseInt(getStyle(obj,attr))
				}
				var speed = (json[attr]-cur)/6;
				speed = speed>0? Math.ceil(speed):Math.floor(speed);
				if(cur!=json[attr])
				bStop=false;
				
				if(attr=="opacity")
				{
					obj.style.filter="alpha(opacity:"+(cur+speed)+")";
					obj.style.opacity=(cur+speed)/100;
				}
				else
				{
					obj.style[attr]=cur+speed+"px"
				}			
		   }
		   if(bStop)
		   {
			   clearInterval(obj.timer);
			   if(fnEnd)fnEnd();
		   }
		},30);
	}catch(e){};
}
//运动框架结束


//锚点缓动
var ss = {
	fixAllLinks: function() {
	var allLinks = document.getElementsByTagName('a');
		for (var i=0;i<allLinks.length;i++) {
			var lnk = allLinks[i];
			if ((lnk.href && lnk.href.indexOf('#') != -1) &&
			( (lnk.pathname == location.pathname) ||
			('/'+lnk.pathname == location.pathname) ) &&
			(lnk.search == location.search)) {
			ss.addEvent(lnk,'click',ss.smoothScroll);
			}
		}
	},
	smoothScroll: function(e) {
	if (window.event) {
		target = window.event.srcElement;
	} else if (e) {
		target = e.target;
	} else return;
	if (target.nodeName.toLowerCase() != 'a') {
		target = target.parentNode;
	}
	if (target.nodeName.toLowerCase() != 'a') return;
	anchor = target.hash.substr(1);
	var allLinks = document.getElementsByTagName('a');
	var destinationLink = null;
	for (var i=0;i<allLinks.length;i++) {
		var lnk = allLinks[i];
		if (lnk.name && (lnk.name == anchor)) {
		destinationLink = lnk;
		break;
		}
	}
	if (!destinationLink) destinationLink = document.getElementById(anchor);
	if (!destinationLink) return true;
	var destx = destinationLink.offsetLeft;
	var desty = destinationLink.offsetTop;
	var thisNode = destinationLink;
	while (thisNode.offsetParent &&(thisNode.offsetParent != document.body)) {
		thisNode = thisNode.offsetParent;
		destx += thisNode.offsetLeft;
		desty += thisNode.offsetTop;
	}
	clearInterval(ss.INTERVAL);
	cypos = ss.getCurrentYPos();
	ss_stepsize = parseInt((desty-cypos)/ss.STEPS);
	ss.INTERVAL=setInterval('ss.scrollWindow('+ss_stepsize+','+desty+',"'+anchor+'")',10);
	if (window.event) {
		window.event.cancelBubble = true;
		window.event.returnValue = false;
	}
	if (e && e.preventDefault && e.stopPropagation) {
		e.preventDefault();
		e.stopPropagation();
	}
},
scrollWindow: function(scramount,dest,anchor) {
wascypos = ss.getCurrentYPos();
isAbove = (wascypos < dest);
window.scrollTo(0,wascypos + scramount);
iscypos = ss.getCurrentYPos();
isAboveNow = (iscypos < dest);
if ((isAbove != isAboveNow) || (wascypos == iscypos)) {
window.scrollTo(0,dest);
clearInterval(ss.INTERVAL);
location.hash = anchor;
}
},
getCurrentYPos: function() {
if (document.body && document.body.scrollTop)
return document.body.scrollTop;
if (document.documentElement && document.documentElement.scrollTop)
return document.documentElement.scrollTop;
if (window.pageYOffset)
return window.pageYOffset;
return 0;
},
addEvent: function(elm, evType, fn, useCapture) {
if (elm.addEventListener){
elm.addEventListener(evType, fn, useCapture);
return true;
} else if (elm.attachEvent){
var r = elm.attachEvent("on"+evType, fn);
return r;
} else {
alert("Handler could not be removed");
}
}
}
ss.STEPS = 30;  //设置滑动速度
ss.addEvent(window,"load",ss.fixAllLinks);

//锚点缓动结束

//导航三秒隐藏

$(function(){
	var ready = true;
	var iTimeoutId = setTimeout(function(){$(".p").find("#light_menu").fadeOut()},5000);
	iTimeoutId;
	$("#left_menu").hover(function(){
		clearTimeout(iTimeoutId);
		if(!ready)return;
		ready = false;
		$("#light_menu").fadeIn(function(){ready=true});
	},function(){
		//if(!ready)return;
		//ready = false;
		$("#light_menu").fadeOut().stop(true);
		$("#light_menu").fadeOut(function(){ready=true});
	})
})

$(function(){
//面包屑导航
	$(".Bread_crumbs").find("dl").mouseover(function()
	{
		var width = $(this).children("dt").width()+2+"px";
		var widths = $(this).children("dt").width()+20+"px";
		$(this).css({"width":width});
		$(this).children("dt").addClass("Bread_crumbs_over");
		$(this).children("dd").css({"min-width":widths});
	})
	$(".Bread_crumbs").find("dl").mouseout(function()
	{
		$(this).children("dt").removeClass("Bread_crumbs_over");
	})
	
	$(".topBrd").find("dl").mouseenter(function()
	{
		var width = $(this).children("dt").width()+2+"px";
		var widths = $(this).children("dt").width()+35+"px";
		$(this).css({"width":width});
		$(this).children("dt").addClass("Bread_crumbs_over");
		$(this).children("dd").css({"min-width":widths});
		$(this).parents("li").css({"border-color":"#f3f3f3"})
		var ddW = $(".underDeals").find("dd")
		ddW.css({"margin-left":-ddW.width()/2+40})
	})
	$(".topBrd").find("dl").mouseleave(function()
	{
		$(this).children("dt").removeClass("Bread_crumbs_over");
		$(this).parents("li").css({"border-color":"#ccc"})
	})	
})

window.onscroll = function()
{	
	var my_pay = document.getElementById("productDescription_navigation");
	var my_hide = document.getElementById("topAdd_Fixed");
	var scrollTop = document.documentElement.scrollTop||document.body.scrollTop;
	var productH = $(".productDisplay_box").height()+25;
	
	if($(".peijian").length<1){
		var peijianH = 0;
	}else{
		var peijianH = $(".peijian").height();
	}
	
	if($(".tuijian").length<1){
		var tuijianH = 0;
	}else{
		var tuijianH = $(".tuijian").height();
	}
	if(scrollTop > 200)
	{
		$("#top").css({opacity:"1"})
	}else
	{
		$("#top").css({opacity:"0"})
	}
	
	//alert($(".peijian").each().length)
	if($(".peijian").length>0 && $(".tuijian").length>0)
	{
		 tuijianH = $(".tuijian").height()+15;
	}
	//document.title = scrollTop;
	if(peijianH==0&&tuijianH==0){
		productH = $(".productDisplay_box").height();
	}else{
		productH = $(".productDisplay_box").height()+25;
	}
		$("#tops").css({"top":10+productH+peijianH+tuijianH})
	if(scrollTop >= 500)
	{
		$("#topAdd_Fixed").css({"display":"block"})
		startMove(my_hide,{top:0},function()
		{
			my_hide.style.position = "fixed";
		});
	}else
	{
		startMove(my_hide,{top:-60},function()
		{
			my_hide.style.display = "none";
		});
	}
	if(scrollTop >= 200+productH+peijianH+tuijianH)
	{
		$(document).ready(function()
		{
			$("#topAdd_Fixed").css({"height":"110px"});
			$("#productDescription_navigation").addClass("productDescription_navigationFixed");
			//$(".right_folding em").css({"position":"fixed"});
		})
	}else
	{
		$(document).ready(function()
		{
			$("#topAdd_Fixed").css({"height":"60px"});
			//my_hide.style.height = 80;
			$("#productDescription_navigation").removeClass("productDescription_navigationFixed");
		})
	}
	var rightH = $(".productDescription_rightHide").height();
	if(scrollTop >= 280+productH+peijianH+tuijianH && scrollTop <= 270+productH+peijianH+tuijianH+rightH-500)
	{
		$(document).ready(function()
		{
			$(".right_folding em").css({"position":"fixed"});
		})
	}else
	{
		$(document).ready(function()
		{
			$(".right_folding em").css({"position":"absolute"});
		})
	}
	
}



//indexBanner

$(function(){
	try
	{
		var bannerBox = document.getElementById("indexBanner_box");
		var leftClick = getByClass(bannerBox,"browseLeft_click")[0];
		var rightClick = getByClass(bannerBox,"browseRight_click")[0];
		var bannerPic = getByClass(bannerBox,"indexBanner_A")[0].getElementsByTagName("li");
		var bannerlist = getByClass(bannerBox,"indexBanner_list")[0].getElementsByTagName("li");
		var now = 0;
		var nowzIndex = 2 ;
		var ready = true;
		function tab()
		{
			for(var i=0 ; i<bannerlist.length;i++)
			{
				bannerPic[i].style.zIndex=0;
				bannerlist[i].className="";
				startMove(bannerPic[i],{opacity:0})
			}
			bannerPic[now].style.zIndex=1;
			bannerlist[now].className="listActive";
			startMove(bannerPic[now],{opacity:100},function(){ready=true});
		}
		for(var i=0;i<bannerlist.length;i++)
		{
			bannerlist[i].index=i;
			bannerlist[i].onmouseover=function()
			{
				now=this.index;
				tab()
			}
		}
		rightClick.onclick = function()
		{
			if(!ready)return;
			ready = false;	
			now ++;
			if(now == bannerPic.length)
			{
				now = 0
			}
			tab();
		}
		leftClick.onclick = function()
		{
			if(!ready)return;
			ready = false;	
			now --;
			if(now == -1)
			{
				now = bannerPic.length-1;
			}
			tab();
		}
		
		// 自动播放
		function next()
		{
			if(!ready)return;
			ready = false;	
			now ++;
			if(now == bannerPic.length)
			{
				now = 0
			}
			tab();
		}
		var timer = setInterval(next , 5000)
		bannerBox.onmouseover = function()
		{
			startMove(leftClick,{opacity:100})
			startMove(rightClick,{opacity:100})
			clearInterval(timer);
		}
		bannerBox.onmouseout = function()
		{
			startMove(leftClick,{opacity:0})
			startMove(rightClick,{opacity:0})
			timer = setInterval(next,5000)
		}
	}catch(e){};
})

$(function(){
	$("div[name=second_subDt]").children(".subDtNat:first-child").addClass("subDtBG");
	$("div[name=second_subDt]").next(".subPic").addClass("block");
	$(".subDtNat").hover(function(){
		var _index=$(this).index();
		var thisNav = $(this).parents(".submenu").find(".subPic");
		$(this).siblings().removeClass("subDtBG")
		$(this).addClass("subDtBG")
		thisNav.removeClass("block");
		thisNav.eq(_index).addClass("block");
	})
	////////////////////////////////////
	///////////////单选/////////////////
	///////////////////////////////////
	$(".chooseOne").click(function(){
		$(this).parents("li").siblings().removeClass("C_default");
		$(this).parents("li").addClass("C_default");
	})
	////////////////////////////////////
	///////////////二维码/////////////////
	///////////////////////////////////
	$(".topCountries_fifth").hover(function(){
		$(".appCode").toggle();
	})
})
$(document).ready(function(){
	
	//国家弹出
	$(".topCountries_first").click(function()
	{
		$(this).children(".blockPopup_box").css({"display":"block"})
	})
	$(".framework_close,.blockPopup_black").click(function(e)
	{
		$(this).parents(".blockPopup_box").css({"display":"none"})
        e.stopPropagation();
	})
	$(".switcher-currency-c,.select_language").click(function()
	{
		$(this).children("ul").toggle();
	})
	$(".request_A").click(function()
	{
		var par = $(".submitRequest_Box");
		par.fadeIn();
		mH = par.find(".scrollWZ").height();
		mY = par.find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			par.find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			par.find(".scrollBox").css({"overflow-y":"auto"})
		}
	})
})

$(function(){
	$(document).on("click",".select_country > h3",function(){
		$(this).siblings(".country_all").toggle();
	})
	$(document).on("click",".country_list",function(){
		$(this).parents(".country_all").hide();	
	})
})
//index right floor hide&&show
$(function(){
	if($("#indexBody").length>0){
		//$("body").css({"background-color":"#f3f3f3"})
		$(".right_floor").show();
		var f1 = $("#f1").offset().top+10;
		var f2 = $("#f2").offset().top+10;
		var f3 = $("#f3").offset().top+10;
		var f4 = $("#f4").offset().top+10;
		var f5 = $("#f5").offset().top+10;
		$(window).scroll(function(){
			var scrof = $(this).scrollTop();
			if(scrof<=f1+150&&scrof-f1>-30){
				$(".right_floor").removeClass("floorH")
				$(".f1").addClass("floorH")
			}else if(scrof<=f2+150&&scrof>f1){
				$(".right_floor").removeClass("floorH")
				$(".f2").addClass("floorH")
			}else if(scrof<=f3+150&&scrof>f2){
				$(".right_floor").removeClass("floorH")
				$(".f3").addClass("floorH")
			}else if(scrof<=f4+150&&scrof>f3){
				$(".right_floor").removeClass("floorH")
				$(".f4").addClass("floorH")
			}else if(scrof<f5+150&&scrof>f4){
				$(".right_floor").removeClass("floorH")
				$(".f5").addClass("floorH")
			}else{
				$(".right_floor").removeClass("floorH")
			}
		})
	}else{
		$(".right_floor").hide();
	}
})

// 回到顶部锚点
$(function(){
	$("#top").click(function() {
		$("html,body").animate({
			scrollTop : 0
		});
	});
})