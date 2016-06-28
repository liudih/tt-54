$(function(){
	$(".aciClick").click(function(){//公用的添加当前样式
		$(this).addClass("aciCss");
		$(this).siblings().removeClass("aciCss");
	})
	$(".popBoxClick").click(function(){//sub弹出框
		$(this).siblings(".popBoxNone").toggle();
		$(this).toggleClass("subCate_titleSj");
		$(this).addClass("clickNone")
	})
	
	////////////////////////无用///////////////////////
	$(".popBoxClick1").click(function(){
		$(this).siblings(".popBoxNone1").toggle();
	})
	
	$(".blackPop").click(function(){
		$(".popBoxNone1").hide();
	})
	/////////////////////////////////////////////
	
	$(document).on("click",'.popBoxFilter',function(){
		$(this).siblings(".popBoxNone").show();
	})
	
	$(document).on("click",".blackPop,.iconClose,.li_clickPop li",function(){
		$(".popBoxNone").hide();
		$(".popBoxClick").removeClass("subCate_titleSj");
	})
	
	/////////////////////////////////////多选产品按钮
	$(".chooseClick").click(function(){
		var disabled =$(this).siblings("input").attr("disabled");
		if(disabled!="disabled"){
			$(this).toggleClass("aciCss");
		}
        //e.stopPropagation();
	})
	////////////////////////////////////////单选产品按钮
	$(".chooseOneClick").click(function(){
		$(this).parents(".chooseOneBox").siblings().find(".chooseOneClick").removeClass("aciCss");
		$(this).addClass("aciCss");
	})
	$(".chooseOneLabel").click(function(){
		var disabled =$(this).find("input").attr("disabled");
		if(disabled!="disabled"){
			$(this).parents(".chooseOneBox").siblings().find(".radio").removeClass("aciCss");
			$(this).find(".radio").addClass("aciCss");
		}
	})
	$(".chooseOneLabels").click(function(){
		var disabled =$(this).find("input").attr("disabled");
		if(disabled!="disabled"){
			$(this).parents(".chooseOneBox").siblings().find(".radio").removeClass("aciCss_s");
			$(this).find(".radio").addClass("aciCss_s");
		}
	})
	/////////////////////////////////////////多选按钮
	$(".chooseMoreClick").click(function(){
		var tgClass = $(this).parents(".chooseMoreBox").find(".checkbox");
		if($(this).hasClass("aciCss")==true){
			tgClass.removeClass("aciCss")
		}else{
			tgClass.addClass("aciCss")
		}
	})
	////////////////已读邮件////////////////////////
	$(".readClick").click(function(){
		$(this).removeClass("blue");
	})
	$(".markedReadClick").click(function(){
		var aci = $(".aciCss").parents(".span15").siblings("a")
		//alert(aci)
		aci.removeClass("blue");
		$(".checkbox").removeClass("aciCss");
	})
	////////////////////////////////////////编辑个人中心地址
	$(".addressClick").click(function(){
		var sibs = $(this).siblings(".addressConCss");
		$(this).css({"border-width":"1px","border-color":"#ccc","border-style":"solid"})
		$(this).children().css({"display":"inline-block"});
		sibs.find(".address_infCon").siblings().css({"display":"none"});
		sibs.css({"border":"0"});
	})
	$(".defaultAddress_click").click(function(){
		var sibs = $(this).parents(".addressConCss");
		sibs.siblings(".addressConCss").removeClass("defaultAddress_css");
		sibs.addClass("defaultAddress_css");
	})
})
//电话号码选择
$(function(){
	///////////////////////////国家选择电话号码
	$("#j-country-code-trigger").click(function()
	{
		$("#j-country-code-list").toggle();
	})
	var phonClick = $("#j-country-code-list").find("li");
	var phonNum = $("#j-country-code-trigger").find(".i-country-code");
	phonClick.click(function()
	{
		phonNum.html($(this).find(".i-country-code").html())
		$(this).siblings().removeClass("fn-hide");
		$(this).addClass("fn-hide");
		$(this).parents("#j-country-code-list").fadeOut();
	})
})
////////////////////////////////////////////////写评论添加图片

function addPicH() {
	$(".addPic_Box").children("li").each(function(){
		var addPic_H = $(this).width();
		$(this).css({"height":addPic_H})
	})
}

////////////////////////////////////////////////清除input值
$(function() {
    $(".sClean").hover(function(){
		values = $(this).siblings(".txtInput");
		values.val("")
		values.focus();
    })
    $(".sClean").click(function(){
		values.val("")
    })
})
////////////////////////////////////////////////
$(function(){
//email提示
	$(".emailPromptInp").focus(function(){
		$(this).siblings(".emailPromptNone").css({"display":"block"})
	})
	$(".emailPromptInp").blur(function(){
		$(this).siblings(".emailPromptNone").css({"display":"none"})
	})
////////////////搜索提示      可以删除////////////////////	
	// $(".searchInp").focus(function(){
		// $(this).parents(".hotSearches_position").find(".hotSearches").css({"display":"block"})
	// })
	// $("body").click(function(){
		// $(".hotSearches_position").find(".hotSearches").css({"display":"none"});
	// })
	// $(".searchInp").click(function(){
		// return false;
	// })
	
	
	// $(".searchInp").blur(function(){
		// $(this).parents(".hotSearches_position").find(".hotSearches").css({"display":"none"})
	// })
///////////////////////////////////////////
	
//国家选择
	$(".placeholder_countryClick").focus(function(){
		$(".select_countryNone").show();
	})
	// $(".placeholder_countryClick").blur(function(){
		// $(".select_countryNone").hide();
	// })
//滑块上下滑动
	$(".slideToggle_click").click(function(){
		$(this).children().toggleClass("checkboxAfter");
		$(".slideToggle_box").slideToggle()
	})
})

//heart
$(function(){
	$(".heartClick").click(function(){
		$(this).addClass("redHeart");
	})
})


function imgHeight(){
	$(".product_img").each(function(){
			var product_imgW = $(this).width();
			$(this).css({"height":product_imgW})
	})
}
//公用选项卡

$(function(){
	////////////////////////////////////////////////
	$(".TAB_Nav").find("li").click(function(){
		var _this = $(this).index();
		$(this).addClass("aciCss");
		$(this).siblings().removeClass("aciCss");
		$(this).parents(".TAB_Nav").siblings(".TAB_Con").hide();
		$(this).parents(".TAB_Nav").siblings(".TAB_Con").eq(_this).show();
		imgHeight()
	})
})
$(function(){//关闭弹窗
	$(".closePop").click(function(){
		//touchValue=1;
		$(".popBoxNone").hide();
		$(".publice_show").fadeOut();
	})
})

/////////////////////////////////////////////////
$(function(){
	addPicH();
	imgHeight();
})
$(window).resize(function(){
	imgHeight();
	addPicH();
})

function Dialog(fun){
	var params ={content:'Remove from your cart?',button1:'Cancel',button2:'Sure'};	
	params = $.extend({'position':{'zone':'center'},'overlay':true}, params);
	var markup = [
        '<div class="dialogBox">',
			'<div class="pu_popBox closePop">',
		    	'<div class="empty"> </div>',
		    	'<div class="pu_popCon">',
		        	'<div class="pu_popAuto">',
		            	'<div class="pu_popAutopadd">',
		               ' <p class="pu_popTxt">',
						     params.content,
						'</p></div></div>',
		             '<div class="pop_puButton lbBox">',
		                '<a href="javascript:;" class="Cancel closePop lineBlock">',params.button1,'</a>',
		                '<a href="javascript:;" onclick="'+fun+'" class="Cancel closePop lineBlock">',params.button2,'</a>',
		    '</div></div></div><div class="blackPop"> </div></div>'
    ].join('');
    $(markup).hide().appendTo('body').fadeIn();
    
	$(".closePop").click(function(){
		$(".dialogBox").fadeOut(function(){
			$(this).remove();
		});
	})
	$(".pu_popCon").click(function(){
		return false; 
	})
	function popHeight(){
		$(".pu_popAuto").css({"height":"auto"})
		var pu_popCon=$(".pu_popCon").height();
		$(".pu_popAuto").css({"height":pu_popCon})
	}
	popHeight();
	window.onresize = popHeight;
}

/////////////////公用的弹框////////////////////////////////
$(function(){	
	
	function successful(){
		var params ={content:'Add cart successfully!'};	
		params = $.extend({'position':{'zone':'center'},'overlay':true}, params);
		var markup = [
            '<div class="successfulPop_box">',
				'<div class="checkbox"></div>',
				'<p>',params.content,'</p></div>'
        ].join('');
        $(markup).hide().appendTo('body').fadeIn();
        setTimeout(function () { 
	        $('.successfulPop_box').fadeOut(function(){$('.successfulPop_box').remove()});
	    }, 1000);
	}
	$(".successfulPop_click").click(function(){
		successful();
	})
})

////////////////////////////////////////////

$(function(){
	//滑块按钮
	$(".slider_buttonClick").click(function(){
		$(this).toggleClass("slider_buttonOpen")
	})
})




//快捷导航左下角
$(function(){
	$(".quickNavClick").click(function(){
		touchValue=0;
		$(".TT_absLB").css({"z-index":"9999"})
		$(".quickNavBox").show(function(){
			$(".quickNavCon").addClass("opened-nav");
		});
	})
	$(".quickNavBlockPop").click(function(){
		touchValue=1;
		$(".quickNavCon").removeClass("opened-nav");
		$(".quickNavBox").fadeOut(function(){
			$(".TT_absLB").css({"z-index":"8000"})
		})
	})
})

/////////////////////////////////////////////////////////////移动端日历
$(function () {
	var currYear = (new Date()).getFullYear();	
	var opt={};
	opt.date = {preset : 'date'};
	opt.datetime = {preset : 'datetime'};
	opt.time = {preset : 'time'};
	opt.defaults = {
		theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式 
        mode: 'scroller', //日期选择模式
		//dateFormat: 'yyyy-mm-dd',
		lang: 'zh',
		showNow: true,
		//nowText: "今天",
        startYear: currYear - 60, //开始年份
        endYear: currYear + 0 //结束年份
	};
	try{
  		$("#appDate").mobiscroll($.extend(opt['date'], opt['default']));
  	}catch(e){};
});

/////////////////////////////////////////////////////////////////星星
$(function(){
	$(".writeReview_start").children("em").mouseover(function(){
		var index = $(this).index()+1;
		$(this).parents(".writeReview_start").addClass("startH"+index);
	})
	$(".writeReview_start").children("em").mouseleave(function(){
		var index = $(this).index()+1;
		$(this).parents(".writeReview_start").removeClass("startH"+index);
	})
	$(".writeReview_start").children("em").click(function()
	{
		var index = $(this).index()+1;
	  	var start = 'start' + $(this).parents(".writeReview_start").data('start');
	    $(this).parents(".writeReview_start").removeClass(start);
		$(this).parents(".writeReview_start").data('start',index);
		$(this).parents(".writeReview_start").addClass("start"+index);
	})
})
