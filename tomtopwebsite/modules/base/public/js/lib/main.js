$(document).ready(function(){
	
	//单选
	$(".radioList").children("li").click(function(){
		$(this).addClass("radioAci");
		$(this).siblings().removeClass("radioAci");
	})
	
	
	$(".closeP").click(function(){
		$(this).parents(".edit_box").hide();
		$(".blackBK").hide();	
	})
	
	$(".bannedClick").unbind("click");
	//a链接点击无虚线
	$("a").bind("focus",function() {
    	if(this.blur) {this.blur()};
	});
	
	//input text
	
	$("input[type='password'],input[type='text']").focus(function(){
		inputTXT = $(this).attr("placeholder");
		//alert(inputTXT)
        $(this).attr("placeholder","");
		if($(this).val()!=inputTXT){
			$(this).css({"color":"#333"})
		}
    }).blur(function(){
		if($(this).val()==""){
			$(this).css({"color":"#ccc"})
		}
        $(this).attr("placeholder",inputTXT);
    });
	/////选择邮寄方式///////
	
	$(".selectShippingMethod_click").click(function(){
		$(this).siblings(".selectShippingMethod_none").toggle();
		if($(this).parent().hasClass("selectShippingMethod_none")){
			$(this).parent().toggle();
		}
	})
	
	//选择颜色和尺寸
	
	$(".productSpecialColor_pic").click(function()
	{
		$(".productSpecialColor_pic").removeClass("rightDown_SJ");
		$(this).addClass("rightDown_SJ");
	})
	$(".productSpecialSize").click(function()
	{
		$(this).siblings().removeClass("rightDown_SJ");
		$(this).addClass("rightDown_SJ");
	})
	$(".chooseColor_li").find("li").click(function()
	{
		$(".chooseColor_li").find("li").removeClass("rightDown_SJ");
		$(this).addClass("rightDown_SJ");
	})
	$(".invalids").unbind("click");  
	$(".chooseBox").click(function(ev)
	{
		if($(this).hasClass("chooseDefault")==false)
		{
			
			var oEvent = ev || event;
			var disX = oEvent.clientX-$("#moveLR").offset().left+165;
			$(this).toggleClass("chooseSelected");
			if($(".chooseProduct_color").css("display")== "none"&& $(this).hasClass("chooseSelected") == true){
				$(this).parents(".browsepic").siblings().children(".chooseBox").removeClass("colorShow")
				$(this).addClass("colorShow")
				//$(this).parents("#moveLR").siblings(".chooseProduct_color").show();
				$(".chooseProduct_color").css({"left":disX})
			}else if($(".chooseProduct_color").css("display")== "block" && $(this).hasClass("chooseSelected") == true){
				$(this).parents(".browsepic").siblings().children(".chooseBox").removeClass("colorShow")
				$(this).addClass("colorShow")
				$(".chooseProduct_color").css({"left":disX})
			}else if($(this).hasClass("colorShow") == true){
				$(this).parents("#moveLR").siblings(".chooseProduct_color").hide();
			}
		}
	})
	$(".multiattribute_c,.posClick").click(function(ev)
	{
		var _this = $(this);
		var oEvent = ev || event;
		var disX = oEvent.clientX-$(".oneFree_list,.posBox").offset().left-63;
		if($(this).hasClass("posClick")){
			var disY = _this.offset().top-320;
		}else{
			var disY = _this.offset().top-405;
		}
		if($(".chooseProduct_color").css("display")== "none"){
			$(".multiattribute_c,.posClick").removeClass("colorShow");
			$(this).addClass("colorShow");
			$(".chooseProduct_color").show();
			$(".chooseProduct_color").css({"left":disX,"top":disY,"z-index":"99999999"})
		}else if($(".chooseProduct_color").css("display")== "block" && $(this).hasClass("colorShow") == false){
			$(".multiattribute_c,.posClick").removeClass("colorShow")
			$(this).addClass("colorShow")
			$(".chooseProduct_color").css({"left":disX,"top":disY,"z-index":"99999999"})
		}else if($(this).hasClass("colorShow") == true){
				$(".chooseProduct_color").hide();
				$(this).removeClass("colorShow")
			}
	})

	$(".chooseProduct_close,.Cancel").click(function()
	{
		$(this).parents(".chooseProduct_color").hide();
	})

	$("#procuctPOP").find(".customer_popBox").mouseover(function()
	{
		$(this).find(".browseLeft_click,.browseRight_click").css({"opacity":1});
	})
	$("#procuctPOP").find(".customer_popBox").mouseout(function()
	{
		$(this).find(".browseLeft_click,.browseRight_click").css({"opacity":0});
	})
	
	//买家图片展示鼠标经过区域
	
	$(".browsepic_img").mouseover(function()
	{
		$(this).children(".browsepic_hoverBOX").stop(true);
		$(this).children(".browsepic_hoverP").show();
		$(this).children(".browsepic_hoverBOX").animate(
		{
			bottom:'0px'
		});
	})
	$(".browsepic_img").mouseout(function()
	{
		$(this).children(".browsepic_hoverBOX").stop(true);
		$(this).children(".browsepic_hoverP").hide();
		$(this).children(".browsepic_hoverBOX").animate(
		{
			bottom:'-36px'
		});
	})
	$("#wishListPOP").find(".customer_popClose").click(function()
	{
		$(".rightPaly_box").css({"z-index":"1"})
	})
	$(".customerSmallmove").children("li").click(function()
	{
		var index = $(this).index();
		$(this).siblings("li").removeClass("cpActive");
		$(this).addClass("cpActive")
		$(this).parent().siblings().children(".customer_bigPic").children("li").css({"display":"none"})
		$(this).parent().siblings().children(".customer_bigPic").children("li").eq(index).css({"display":"block"});
		var nd = $(this).parent().prev().find(".nowIndex:eq(0)");
		nd.val(index);
	})

	$(".productDescription_navigation li a").click(function()
	{
		var index = $(this).parents("li").index();
		$(".productDescription_navigation li").removeClass("productDescription_active");
		$(this).parents("li").addClass("productDescription_active");
		$(".Description").css({"display":"none"});
		$(".Description").eq(index).css({"display":"block"});
		$(".indexExhibitPRO").css({"display":"none"});
		$(".indexExhibitPRO").eq(index).css({"display":"block"});
		$(".productDescription_navigation li").children(".navigMore").hide();
		$(this).siblings(".navigMore").show();
	})
	
	$(".ControlNa li").click(function()
	{
		var index = $(this).index();
		$(".ControlNa li").removeClass("controAc");
		$(this).addClass("controAc");
		$(".Control").css({"display":"none"});
		$(".Control").eq(index).css({"display":"block"});
	})
	
	$(".postPhotosA").click(function()
	{
		$(this).parents(".postPhotos").siblings(".blockPopup_box").fadeIn();
		mH = $(this).parents().siblings(".blockPopup_box").find(".scrollWZ").height();
		mY = $(this).parents().siblings(".blockPopup_box").find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			$(this).parents().siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			$(this).parents().siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"auto"})
		}
	})
	
	$(".xxk li").click(function()
	{
		var index = $(this).index();
		$(this).siblings().removeClass("xxkActi");
		$(this).addClass("xxkActi");
		$(this).parents().siblings(".xxkBox").css({"display":"none"});
		$(this).parents().siblings(".xxkBox").eq(index).css({"display":"block"});
	})
	
	//添加视频文字缩放
	$(".ADD_videoBox").children("em").click(function()
	{
		if($(".ADD_videoBox").children("em").hasClass("emUps") == true)
		{
			$(this).siblings(".ADD_videoHide").animate({
				height:'130px'
			})
			$(this).animate({
				top:'125px'
			},function(){$(this).removeClass("emUps");})
		}else
		{
			var H = $(this).siblings(".ADD_videoHide").children(".ADD_videoHeight").height()+30;
			$(this).siblings(".ADD_videoHide").animate({
				height:H
			})
			$(this).animate({
				top:H-5
			},function(){$(this).addClass("emUps");})
		}
	})
	
	
	//right折叠
	
	$(".right_folding").click(function()
	{
		if($(this).siblings(".productDescription_rightHide").css("display")=="none")
		{
			$(this).siblings(".productDescription_rightHide").css({"display":"block"})
			$(this).siblings(".productDescription_rightHide").animate({
				width:'205px'
			})
			$(this).children("em").removeClass("emZd")
			$(this).parent().siblings(".productDescription_left,.productDescription_language").animate({
				width:'976px'
			})
		}else{
			$(this).siblings(".productDescription_rightHide").animate({
				width:'0px'
			},function(){$(this).css({"display":"none"})});
			$(this).children("em").addClass("emZd");
			$(this).parent().siblings(".productDescription_left,.productDescription_language").animate({
				width:'1200px'
			})
		}
	})
	//video
	$(".productVideo_pic").click(function()
	{
		$(this).children(".blockPopup_box").css({"display":"block"})
	})
	//比价格弹框
/*	$(".reportError_ul").children("li").click(function()
	{
		//$(this).children(".blockPopup_box").css({"display":"block"})
		$(this).children(".blockPopup_box").fadeIn();
		mH = $(this).children(".blockPopup_box").find(".scrollWZ").height();
		mY = $(this).children(".blockPopup_box").find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			$(this).children(".blockPopup_box").find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			$(this).children(".blockPopup_box").find(".scrollBox").css({"overflow-y":"auto"})
		}
	})*/
	
	$(".reportError_ul .popname").click(function()
	{
		var par = $(this).parent();
		par.children(".blockPopup_box").fadeIn();
		mH = par.children(".blockPopup_box").find(".scrollWZ").height();
		mY = par.children(".blockPopup_box").find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			par.children(".blockPopup_box").find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			par.children(".blockPopup_box").find(".scrollBox").css({"overflow-y":"auto"})
		}
		changeCaptcha(this);
	})
	
	$(".free_txt").click(function()
	{
		$(this).children("em").toggleClass("addRight");
	})
	//问题弹出
	$(".popClick,.browsepic_img").click(function(){
		$(this).siblings(".blockPopup_box").fadeIn();
		mH = $(this).siblings(".blockPopup_box").find(".scrollWZ").height();
		mY = $(this).siblings(".blockPopup_box").find(".scrollY").height();
		$(".scrollWZ").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			$(this).siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			$(this).siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"auto"})
		}
	})
	//rightPaly_addToCart
	$(".rightPaly_addToCart,.buttCart").click(function()
	{
		$(this).siblings(".addTo_cartHide").css({"display":"block"})
	})
	$(".addTo_close").click(function()
	{
		$(this).parents(".addTo_cartHide").css({"display":"none"})
	})
	
	//reviewsBackTxt
	$(".reviewsBackTxt").children("img").click(function()
	{
		$(this).siblings(".blockPopup_box").fadeIn();
	})
	//writeReview
	$(".write_A_review").click(function(){
		$(this).siblings(".blockPopup_box").fadeIn();
	})

	$(".e_error").focus(function()
	{
		$(".e_errorIN").css({"display":"none"})
		$(".e_Address").removeClass("e_error");
	})
	
	$(".bulkrateActi").click(function()
	{
		$(this).removeClass("bulkrateActi")	;
	})
	
	//cart
	$(".bulkrate").mouseenter(function(){
		if($(this).hasClass("bulkrateActi") == false&&$(this).hasClass("bulkClick")==false)
		{
			$(this).children(".bulk_hover").show();
		}
	})
	$(".bulkrate").mouseleave(function(){
			$(this).children(".bulk_hover").hide();
	})
	
	$(".bulkClick").click(function(e)
	{
		$(this).siblings(".bulk_hover").css({"display":"none"});
		if($(this).parents(".bulkrate").hasClass("bulkClick")==true){
			$(this).siblings(".bulk_Pop").css({"display":"none"});
			$(this).parents(".bulkrate").removeClass("bulkClick");
		}else{
			$(".bulkrate").removeClass("bulkClick");
			$(".bulk_Pop").css({"display":"none"});
			$(this).siblings(".bulk_Pop").css({"display":"block"});
			$(this).parents(".bulkrate").addClass("bulkClick");
		}
	})
	$(".cart_applied").find("input").click(function()
	{
		$(this).parents(".cart_applied").css({"display":"none"})
		$(this).parents(".bulkrate").removeClass("bulkClick");
		$(this).parents(".bulkrate").children(".bulk_Pop").css({"display":"none"});
		$(this).parents(".cart_applied").siblings(".cart_apply").css({"display":"block"})
	})
	
	$(".agreeTT").click(function(){
		$(".ContinueSpay").toggleClass("ContinueNo");
		$(".checkouts").toggle();
	})
	
	//cartDelete
	$(".heart").click(function(){
		$(this).children(".deletePop").toggle();
	})

	/*$(".pro_options").children(".heart").children("em").click(function()
	{
		$(this).toggleClass("redHeart");
	})*/
	
	$(".cartLogin").children(".blue").click(function(){
		$(this).siblings(".blockPopup_box").show();
	})
	
	$(".newAdds").children(".bulkrate").click(function()
	{
		$(this).toggleClass("bulkrateActis")
		$(this).children(".bulk_hover").hide();
	})

	
	
	$(".bil_adds").children("a").click(function()
	{
		$(this).parents(".bil_adds").siblings(".edit_box").css({"display":"block"})
		$(this).parents("ol").css({"z-index":"100"})
		$(".blackBK").css({"display":"block"})
	})
	$(".closeHS").click(function()
	{
		$(this).parents(".edit_box").hide();
		$(".blackBK").css({"display":"none"})
		$(this).parents("ol").css({"z-index":"1"})
	})
	
	
	//
	$(".editProvince").children("p").click(function()
	{
		$(this).siblings("ul").toggle();
	})
	$(".editProvince").children("ul").click(function()
	{
		$(this).hide();
	})
	
	//
	$(".Tracking").click(function()
	{
		$(this).children(".checkBoxs").toggleClass("afters")
		$(this).parents("td").siblings("td").children(".clickAddship").toggle();
	})
	//
	$(".insurance").mouseover(function()
	{
		$(this).children("span").show();
	})
	$(".insurance").mouseout(function()
	{
		$(this).children("span").hide();
	})
	//
	$(".listTogeTitle").click(function()
	{
		$(this).parents(".cartListToge").toggleClass("border0");
		$(this).siblings("ol").slideToggle(function(){$(".aymentMethodHid").animate({height:$(".aymentMethodShow").height()})});
		$(this).toggleClass("titleAC");
		//alert($(".cartListUL").height())
		
	})
	$(".rightAll").children("span").click(function(){
		$(".rightThis").children("span").addClass("afters");
		$(this).toggleClass("aftersAll")
		$(this).parents(".choWishlist").siblings(".choWishlist").find(".rightAll").children("span").toggleClass("aftersAll")
		if($(this).hasClass("aftersAll")==false){
			$(".rightThis").children("span").toggleClass("afters");
		}
		//$(".filterGray").find(".rightThis").children("span").removeClass("afters");
	})
	$(".thisAci").click(function(){
		$(this).parents("li").toggleClass("thisParAci");
	})
	
	$(".rightAlls").children("span").click(function(){
		$(this).parents(".rightBox").find(".rightThis").children("span").addClass("afters");
		$(this).toggleClass("aftersAll")
		$(this).parents(".choWishlist").siblings(".choWishlist").find(".rightAll").children("span").toggleClass("aftersAll")
		if($(this).hasClass("aftersAll")==false){
			$(this).parents(".rightBox").find(".rightThis").children("span").toggleClass("afters");
		}
	})
	$(".chooseOther").hover(function(){
		$(this).children(".chooseDef").children("a").toggleClass("block");
	})
	
	//账单地址选择
	$(".billAll").children("span").click(function(){
		var pars = $(this).parents(".addChAll").siblings(".addressUL").children("li");
		$(this).toggleClass("aftersAll")
		pars.each(function()
		{
			if($(this).children(".defADD").hasClass("defActi")==false){
				$(this).children(".allThis").children("span").addClass("afters");
				if($(".billAll").children("span").hasClass("aftersAll")==false){
					$(this).children(".allThis").children("span").toggleClass("afters");
				}
			}
		})
	})
	$(".allThis").click(function()
	{
		if($(this).siblings(".defADD").hasClass("defActi")==false){
			$(this).children("span").toggleClass("afters")
		}
	})
	
	$("#j-country-code-trigger").click(function()
	{
		$("#j-country-code-list").toggle();
	})
	
	$(".paymentMethod").children("li").click(function()
	{
		var _this = $(this).index();
		var listB = $(".panmentsList").children(".paymentTxtBox");
		$(".paymentMethod").children("li").removeClass("payment_Active")
		$(this).addClass("payment_Active");
		listB.eq(_this).siblings().hide();
		listB.eq(_this).show();
		//原来的点击事件
		$(this).children("ol").show();
		$(this).siblings().children("ol").hide();
	})
	
	
	$(".leaveM_Box").children("label").children("input").click(function()
	{
		$(this).parents("label").siblings(".leaveMT").toggle();
	})
	//删掉 购买页面leavemessage
	$(".cart_bottom").children("label").children("input").click(function(){
		$(this).parents("label").siblings(".leaveMT").toggle();
	})
	
	//newArrivals_NA
	
	$(".newArrivals_NA").children("li").mouseover(function()
	{
		var index = $(this).index();
		$(this).siblings("li").removeClass("newNA_Active");
		$(this).addClass("newNA_Active");
		$(".newArrivals").children("ul").hide();
		$(".newArrivals").children("ul").eq(index).show();
	})
	//
	$(".sideBanner li").mouseover(function()
	{
		$(this).find("p").stop(true)
		$(this).find("p").animate({bottom:0})
	})
	$(".sideBanner li").mouseout(function()
	{
		$(this).find("p").stop(true)
		$(this).find("p").animate({bottom:-20})
	})
	
	$(".attributeBOX,.levelKeywords").find("span").click(function()
	{
		$(this).toggleClass("spanActive");
	})
	$(".newArivals_sp").children("span").click(function()
	{
		$(this).addClass("spanActive");
		$(this).siblings().removeClass("spanActive");
	})
	$(".HottestItems").find("span").click(function()
	{
		$(this).addClass("spanActive");
		$(this).siblings().removeClass("spanActive");
	})
	

	
	
	var paymentLileng = $(".paymentMethodPro").find(".cartListUL")
	if(paymentLileng.length>0){
		$(".searchUD").show();
	}

	$(".arrayLI").children("a").click(function()
	{
		$("input[name='st']:eq(0)").val($(this).index());
		$(this).addClass("arrayAc");
		$(this).siblings().removeClass("arrayAc");
		$(".arrangeList").hide();
	})
	$(".arrayLess").click(function()
	{
		//var index = $(".sortActive").parent().index();
		$(".arrangeClick").addClass("arrangeLess")
		$(".arrangeClick").removeClass("arrangeMore")
		$(".arrangeClick").eq(0).show();
	})
	$(".arrayMore").click(function()
	{
		//var index = $(".sortActive").parent().index();
		$(".arrangeClick").addClass("arrangeMore")
		$(".arrangeClick").removeClass("arrangeLess")
		$(".arrangeClick").eq(0).show();
	})
	$(".arrayLine").click(function(){
		$(".arrangeClick").hide();
		$(".arrangeList").show();
	})
	
	//排序价格price点击
	$(".sortA").click(function(){
		$(this).siblings().removeClass("sortA_PriceUp");
		$(this).siblings().removeClass("sortA_PriceDo");
		$(this).siblings().children("a").removeClass("sortActive");
		$(this).children("a").addClass("sortActive");
		if($(this).hasClass("sortA_PriceUp")==false){
			$(this).addClass("sortA_PriceUp");
		}else if($(this).hasClass("sortA_PriceUp")==true && $(this).hasClass("sortA_PriceDo")==false){
			$(this).addClass("sortA_PriceDo");
		}else if($(this).hasClass("sortA_PriceUp")==true && $(this).hasClass("sortA_PriceDo")==true){
			$(this).removeClass("sortA_PriceDo");
		}
	})

	//todayDeals
	$(".dealsRight p").click(function(){
		$(this).hide();
		$(this).siblings().show();
		$(".todays_daily").toggle();
		$(".tommorrow_daily").toggle();
		$(".DealsInf").toggle();
	})
	
	//
	$(".email_Banner li").click(function()
	{
		$(this).children("span").toggleClass("selectedAC");	
	})

	$(".newAddress").click(function()
	{
		$(this).parents(".addChAll").siblings(".newAddressBox").fadeToggle();
		$(".blackBK").toggle();
	})
	
	//
	
	$(".vipTdHover").mouseover(function()
	{
		var index = $(this).index()-1;
		$(this).addClass("bkBlue");
		$(this).siblings().eq(0).addClass("bkBlue");
		$(this).parents("tr").siblings().children(".VIPTh").eq(index).addClass("bkBlue");
	})
	$(".vipTdHover").mouseout(function()
	{
		var index = $(this).index()-1;
		$(this).removeClass("bkBlue");
		$(this).siblings().eq(0).removeClass("bkBlue");
		$(this).parents("tr").siblings().children(".VIPTh").eq(index).removeClass("bkBlue");
	})
	$(".writePic li").click(function()
	{
		$(this).parents(".writePic").siblings(".blockPopup_box").fadeIn();
		//指定图片显示
		var ind = $(this).index();
		var nextli = $(this).parent().next().find(".customer_bigPic li");
		nextli.hide();
		nextli.eq(ind).show();
		var nd = $(this).parent().next().find(".nowIndex:eq(0)");
		nd.val(ind);

		mH = $(this).parents(".writePic").siblings(".blockPopup_box").find(".customer_popBox").height();
		mY = $(this).parents(".writePic").siblings(".blockPopup_box").find(".customer_popPicBox").height()
		//alert(mH)
		$(".customer_popBox").css({"margin-top":-mH/2})
		if(mY>mH)
		{
			$(this).parents(".writePic").siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"scroll"})
		}
		else{
			$(this).parents(".writePic").siblings(".blockPopup_box").find(".scrollBox").css({"overflow-y":"auto"})
		}
	})
	$(".accInfs ol").mouseover(function()
	{
		$(".parents").addClass("parentsStyle")
		$(".accInfs").css({"border-color":"#219ced"})
	})
	$(".accInfs ol").mouseout(function()
	{
		$(".parents").removeClass("parentsStyle")
		$(".accInfs").css({"border-color":"#e8e8e8"})
	})
	
	$(".totalPrice_box").mouseenter(function(){
		$(this).children(".product_VIP").stop(true);
		$(this).children(".product_VIP").fadeIn();
	})
	$(".totalPrice_box").mouseleave(function(){
		$(this).children(".product_VIP").stop(true);
		$(this).children(".product_VIP").fadeOut();
	})
	
	//产品颜色过多
	for(var i=0;i<=$(".colorOverf").length;i++){
		if($(".colorOverf").eq(i).height()>=100){
			$(".colorOverf").eq(i).css({"height":"100px","overflow-y":"scroll"})
			$(".colorOverf").eq(i).children("li").css({"margin-top":"0"})
		}
	}
	//个人中心展开评论
	$(".ticketsReply").click(function(){
		$(".questionCon_Hide").stop(true);
		$(".questionCon_Hide").slideToggle();
		if($(this).html()=="Reply"){
			$(this).html("Hide");
		}else if($(this).html()=="Hide"){
			$(this).html("Reply");
		}
	})
	//个人中心 签到
	$(".dailyPoints_tt").click(function(){
		$(this).addClass("dailyPoints_gray");
		$(this).siblings("p").fadeOut();
		$(".score_tt").animate({
			top:"0px",
			opacity:"0"
		});
		$(".dailyPoints_gray").unbind("click");
	})
	$(".dailyPoints_tt").hover(function(){
		if($(this).hasClass("dailyPoints_gray")==false){
			$(this).siblings(".dailyPoints_tt_C").fadeIn();
			$(this).siblings(".dailyPoints_tt_NC").fadeOut();
		}else{
			$(this).siblings(".dailyPoints_tt_NC").fadeIn();
			$(this).siblings(".dailyPoints_tt_C").fadeOut();
		}
	},function(){
		$(this).siblings("p").fadeOut();
	})
})

$(function(){
	var inN = $(".infW").length+4;
	var inW = 940/inN;
	$(".infW").css({"width":inW})
	$(".accInfs ol").css({"width":inW-1})
	$(".accInfs").css({"width":inW*4-1})
})

$(function(){
	var pu_popCon=$(".pu_popCon").height();
	$(".pu_popAuto").css({"height":pu_popCon})
	//
	$(".self_pickupClick").click(function(){
		$(".self_pickupNone").toggle();
	})
})
$(window).resize(function()
{
	$(".pu_popAuto").css({"height":"auto"})
	var pu_popCon=$(".pu_popCon").height();
	$(".pu_popAuto").css({"height":pu_popCon})
	
	$(".blockPopup_box").each(function()
	{
		if($(this).css("display")=="block"){
			var mH = $(this).find(".scrollWZ").height()+30;
			$(this).find(".scrollWZ").css({"margin-top":-mH/2})
		}
	})
		var W = $(window).width();
		var ws = $("#procuctPOP").children(".customer_popBox").width();
		$("#procuctPOP").find(".scrollBox").css({"width":ws-238});
		var scW = $("#procuctPOP").find(".scrollBox").width();
		var scH = $("#procuctPOP").find(".scrollBox").height();
		if(scW/scH>=1){
			$("#bigBacks").find("img").css({"height":"100%","width":"auto"})
		}else{
			$("#bigBacks").find("img").css({"width":"100%","height":"auto"})
		}
		var _thisH = $("#wrap").find("img").height();
		if(scH>_thisH)
		{
			$("#wrap").find("img").css({"top":"50%","margin-top":-_thisH/2})
		}else{
			$("#wrap").find("img").css({"top":"0px","margin-top":"auto"})
		}
		if(W >=1900)
		{
			$("#procuctPOP").children(".customer_popBox").css({"margin-left":-500})
		}else{
			$("#procuctPOP").children(".customer_popBox").css({"margin-left":-ws/2})
		}
	//判断右边图片多少 高度
	
		var smallL = $("#smallClickUrl").children("li")
		if($("#smallClickUrl").height()<=smallL.length/3*(smallL.eq(0).height()+17)){
			$("#smallClickUrl").css({"overflow-y":"scroll","width":"215px"})
		}else{
			$("#smallClickUrl").css({"overflow-y":"hidden","width":"205px"})
		}
})

//星星评分选择

$(function(){
	$(".product_Reviews").children("em").mouseover(function(){
		var index = $(this).index()+1;
		$(this).parents(".product_Reviews").addClass("startH"+index);
	})
	$(".product_Reviews").children("em").mouseleave(function(){
		var index = $(this).index()+1;
		$(this).parents(".product_Reviews").removeClass("startH"+index);
	})
	$(".product_Reviews").children("em").click(function()
	{
		var index = $(this).index()+1;
		if ($(this).parents(".product_Reviews").attr("class").indexOf('start')>0){
			  var length = $(this).parents(".product_Reviews").attr("class").indexOf('start');
			  var start = $(this).parents(".product_Reviews").attr("class").substr(length,'start'.length+1);
			  $(this).parents(".product_Reviews").removeClass(start);
		}
		$(this).parents(".product_Reviews").addClass("start"+index);
	})
})

//tickets的十个星星
$(function(){
	$(".ticket_Reviews").children("em").mouseover(function(){
		var index = $(this).index()+1;
		$(this).parents(".ticket_Reviews").addClass("startH"+index);
	})
	$(".ticket_Reviews").children("em").mouseleave(function(){
		var index = $(this).index()+1;
		$(this).parents(".ticket_Reviews").removeClass("startH"+index);
	})
	$(".ticket_Reviews").children("em").click(function()
	{
		var index = $(this).index()+1;
	  	var start = 'start' + $(this).parents(".ticket_Reviews").data('start');
	    $(this).parents(".ticket_Reviews").removeClass(start);
		$(this).parents(".ticket_Reviews").data('start',index);
		$(this).parents(".ticket_Reviews").addClass("start"+index);
	})
})

//帮助中心折叠

$(function()
{
	$(".QA_Ud a").click(function()
	{
		$(this).siblings(".QA_txt").stop(true);
		$(this).siblings(".QA_txt").slideToggle();
		$(this).children("em").toggleClass("H_emAc");
	})
})


//写评论弹框
$(function()
{
	/*if($(".addPic_Box li").length==1){
		  $(".addPic_Box li").append("<input type='file' class='addPicInput'>");
	  }*/
/*	$(".addPicInput").click(function()
	{
		$(this).parents("li").after("<li><input type='file' class='addPicInput'></li>")
		$(this).remove();
	})
	$(".addDele").click(function()
	{
		$(this).parents("li").remove();
	})*/
	$(".editor").click(function()
	{
		if($(this).hasClass("editor_er")==false)
		{
			var tops = 36+$(this).parents("tr").height();
			$(this).parents("tr").addClass("trPosi");
			$(this).parents("tr").siblings(".werThTT").addClass("thPosi");
			$(".trPosi").after("<div class='bkBlack'></div>");
			$(".writeEdit").css({"top":tops,"display":"block"})
			$(this).addClass("editor_er");
		}
	})
	$(".closePP").click(function()
	{
		$(".bkBlack").remove();
		$(".writeEdit").fadeOut();
		$(".editor_er").parents("tr").removeClass("trPosi");
		$(".editor_er").parents("tr").siblings(".werThTT").removeClass("thPosi");
		$(".editor_er").removeClass("editor_er")
	})
})

$(function()
{
	
	var liH = $(".relatedSearches").height();
	var cliH = $(".searchUD").siblings(".attributeHid").children("ul").height();
	$(".blackXXK li,.hsXXK_C li").click(function()
	{
		var index = $(this).index();
		if($(this).hasClass("Recycle")==false){
			$(this).addClass("xxkActi");
			$(this).siblings().removeClass("xxkActi");
			$(this).parents().siblings(".xxkBOX").hide();
			$(this).parents().siblings(".xxkBOX").eq(index).fadeIn();
			xxks();
		}
		if($(".attL").height()>$(".attL").parents("ol").height()+16){
			$(".attL").parents("ol").siblings(".attM").show();
		}
		$(".attL").hide();
		$(".attL").siblings("ol").animate({height:37});
	})
	function xxks(){
		$(".attributeHid").each(function()
		{
			if($(this).find("li").length>=6){
				$(this).height(liH+37*4+24);
				$(this).css({"min-height":"235px"})
			}else if($(this).find("li").length>=4&&$(this).find("li").length<6){
				$(this).height(liH+37*4+24);
			}else
			{
				$(this).height(37*$(this).find("li").length+4);
				$(".searchUD").hide();
			}
			$(this).find(".attML_BOX").each(function(){
				if($(this).height()>$(this).parents("ol").height()+16){
					$(this).parents("ol").siblings(".attM").show();
				}
			});
		})
	}
	xxks();
	$(".searchUD").click(function()
	{
		var payH = $(this).parents(".paymentMethodPro").find(".aymentMethodShow").height();
		var cliH = $(this).siblings(".attributeHid").children("ul").height();
		if($(this).find("em").hasClass("upBAK")==false){
			$(this).siblings(".attributeHid").animate({height:cliH})
			$(this).parents(".paymentMethodPro").find(".aymentMethodHid").animate({height : payH})//支付页面的
		}else{
			$(this).siblings(".attributeHid").animate({height:liH+37*4+24})
			$(this).parents(".paymentMethodPro").find(".aymentMethodHid").animate({height : 97})
		}
		$(this).find("em").toggleClass("upBAK");
		$(this).find(".attMs").toggle();
		$(this).find(".attLs").toggle();
	})
	$(".attM").click(function(){
		var cliH = $(this).siblings("ol").children(".attML_BOX").height();
		var boxH = $(this).parents(".attributeHid").height();
		$(this).hide();
		$(this).siblings("ol").animate({height:cliH+5});
		$(this).siblings(".attL").show();
		$(this).parents(".attributeHid").animate({height:boxH+cliH-28})
	})
	$(".attL").click(function(){
		var cliH = $(this).siblings("ol").children(".attML_BOX").height();
		var boxH = $(this).parents(".attributeHid").height();
		$(this).hide();
		$(this).siblings("ol").animate({height:37});
		$(this).siblings(".attM").show();
		$(this).parents(".attributeHid").animate({height:boxH-cliH+32})
	})
})
$(function()
{
	$(".clicks").click(function()
	{
		$(this).siblings(".clickPop").fadeIn();
		$(this).after("<div class='bkBlack'></div>")
	})
	$(".closePop").click(function()
	{
		$(this).parents(".clickPop").slideUp();
		$(".bkBlack").fadeOut(function(){$(".bkBlack").remove()});
	})
	
	/*$(".affIframeClick").click(function()
	{
		$(this).siblings(".account_x").fadeIn();
		$(this).parents().siblings(".account_x").fadeIn();
	})*/
})

//心动画
/*$(function()
{
	$(".product_hart").click(function(ev)
	{
		var oEvent = ev || event;
		var heartX = $('#rightHeart').offset().left;
		var heartH =  document.body.clientHeight-155;
		var disX = oEvent.clientX-11;
		var disY = oEvent.clientY-11;
		var q = (disY-heartH)/(disX-heartX);
		var m = (heartX-disX)/4;
		var m2 = (heartX-disX)/15;
		$("body").append("<div id='heartMove'></div>");
		$("#heartMove").css({"left":disX,"top":disY,"position":"fixed"}).stop().animate({
				left:heartX,
				top:heartH
		},400,function(){
			$("#heartMove").animate({top:q*(heartX-m-disX)+disY,left:heartX-m},200,function(){
				$("#heartMove").animate({left:heartX,top:heartH},200,function(){
					$("#heartMove").animate({top:q*(heartX-m2-disX)+disY,left:heartX-m2},80,function(){
						$("#heartMove").animate({left:heartX,top:heartH},80,function(){
							$("#heartMove").animate({opacity:0},80,function(){
								$("#heartMove").remove();
									$(".heartBack").after("<img class='heartAmp' src='images/redheart.png'>")
									$(".heartAmp").animate({
											position:'absolute',
											width:'101px',
											height:'87px',
											left:'-20px',
											top:'-20px',
											opacity:0
										},300,function(){
											$(".heartAmp").remove();
										})
								})
							})
						})
					})
			})
		})
	})
})*/

//所有图片左右点击
$(function()
{
	var now = 0;
	$(".customer_leftClick,.browseLeft_click").click(function()
	{
		var nd = $(this).parent().find(".nowIndex:eq(0)");
		if(nd!=null && typeof(nd)!="undefined"){
			now = nd.val();
		}
		var picLI = $(this).siblings(".customer_bigPic").children("li");
		var ff = picLI.eq(now).find("iframe:eq(0)");
		if(ff.length>0){
			ff.attr("src",ff.attr("src"));
		}
		var picSmall = $(this).parents(".customer_popPicBox").siblings(".customerSmallmove").children("li");
		var picLength = picLI.length;
		now--;
		if(now<0){
			now = picLength-1;
		}
		picLI.hide();
		picLI.eq(now).fadeIn();
		picSmall.removeClass("cpActive");
		picSmall.eq(now).addClass("cpActive");
		if(nd!=null && typeof(nd)!="undefined"){
			nd.val(now);
		}
	})
	$(".customer_rightClick,.browseRight_click").click(function()
	{
		var nd = $(this).parent().find(".nowIndex:eq(0)");
		if(nd!=null && typeof(nd)!="undefined"){
			now = nd.val();
		}
		var picLI = $(this).siblings(".customer_bigPic").children("li");
		var ff = picLI.eq(now).find("iframe:eq(0)");
		if(ff.length>0){
			ff.attr("src",ff.attr("src"));
		}
		var picSmall = $(this).parents(".customer_popPicBox").siblings(".customerSmallmove").children("li");
		var picLength = picLI.length;
		now++;
		if(now>picLength-1){
			now = 0;
		}
		picLI.hide();
		picLI.eq(now).fadeIn();
		picSmall.removeClass("cpActive");
		picSmall.eq(now).addClass("cpActive");
		if(nd!=null && typeof(nd)!="undefined"){
			nd.val(now);
		}
	})
})

//产品页面头部左右点击
$(function()
{
	now = 0;
	var picLi = $(".browseLeft_clicks").parents(".scrollBox").siblings(".customerSmallmove").children("li");
	var bigPic = $(".browseLeft_clicks").siblings(".customer_popPicBox").find("img");
	$(".browseLeft_clicks").click(function()
	{
		now--;
		if(now<0){
			now = picLi.length-1;
		}      
		var bigA = $(this).siblings(".customer_popPicBox").find("a");
		var bigUrl = picLi.eq(now).children("a").attr("href");
		bigPic.attr("src",bigUrl);
		bigA.attr("href",bigUrl);
		picLi.removeClass("cpActive");
		picLi.eq(now).addClass("cpActive");
	})
	$(".browseRight_clicks").click(function()
	{
		now++;
		if(now>picLi.length-1){
			now = 0;
		}      
		var bigA = $(this).siblings(".customer_popPicBox").find("a");
		var bigUrl = picLi.eq(now).children("a").attr("href");
		bigPic.attr("src",bigUrl);
		bigA.attr("href",bigUrl);
		picLi.removeClass("cpActive");
		picLi.eq(now).addClass("cpActive");
	})
	$("#smallClickUrl").find("img").bind("click",function()
	{
		now=$(this).parents("li").index();
	})
	$(".productSmallmove").find("img").bind("click",function()
	{
		//now=$(this).parents("li").index();
		//alert("s")
	})
	
	//点击弹出放大
	$(".hoverBig").children("li").click(function(a){
		//var num = $("#upDownbox").find(".cpActive");
		var numImg = $("#upDownbox").find(".productSmallmove");
		var _liIndex = numImg.children(".cpActive").index();
		
		var pImg = $("#smallClickUrl").children("li");
		pImg.removeClass("cpActive");
		pImg.eq(_liIndex).addClass("cpActive");
		var imgSrc = $("#smallClickUrl").find(".cpActive").children("a").attr('href');
		$("#zoom2").attr("src",imgSrc);
		$("#zoom2").attr("href",imgSrc);
		$("#zoom2").children("img").attr("src",imgSrc);
		
		$("#procuctPOP").fadeIn();
		var W = $(window).width();
		var ws = $("#procuctPOP").children(".customer_popBox").width();
		$("#procuctPOP").find(".scrollBox").css({"width":ws-238});
		var scW = $("#procuctPOP").find(".scrollBox").width();
		var scH = $("#procuctPOP").find(".scrollBox").height();
		if(scW/scH>=1){
			$("#bigBacks").find("img").css({"height":"100%","width":"auto"})
		}else{
			$("#bigBacks").find("img").css({"width":"100%","height":"auto"})
		}
		var _thisH = $("#wrap").find("img").height();
		if(scH>_thisH)
		{
			$("#wrap").find("img").css({"top":"50%","margin-top":-_thisH/2})
		}else{
			$("#wrap").find("img").css({"top":"0px","margin-top":"auto"})
		}
		if(W >=1900)
		{
			$("#procuctPOP").children(".customer_popBox").css({"margin-left":-500})
		}else{
			$("#procuctPOP").children(".customer_popBox").css({"margin-left":-ws/2})
		}
	//判断右边图片多少 高度
	
	var smallL = $("#smallClickUrl").children("li")
	if($("#smallClickUrl").height()<=smallL.length/3*(smallL.eq(0).height()+17)){
		$("#smallClickUrl").css({"overflow-y":"scroll","width":"215px"})
	}else{
		$("#smallClickUrl").css({"overflow-y":"hidden","width":"205px"})
	}
	})
})

//选择类目
$(function(){
	$(".categories").click(function(){
		$(this).siblings().removeClass("cateAci");
		$(this).toggleClass("cateAci");
		$(this).find(".rightThis_s").children("span").toggleClass("afters")
		$(this).siblings().find(".rightThis_s").children("span").removeClass("afters")
	})
	$(".categories").click(function()
	{
		if($(".firstWhole").children().hasClass("cateAci")==true){
			$(".secondWhole").show();
		}else{
			$(".secondWhole").hide();
		}
		if($(".firstWhole").children().hasClass("cateAci")==true&&$(".secondWhole").children().hasClass("cateAci")==true){
			$(".thirdWhole").show();
		}else{
			$(".thirdWhole").hide();
			$(".fourthWhole").hide();
		}
		if($(".firstWhole").children().hasClass("cateAci")==true&&$(".secondWhole").children().hasClass("cateAci")==true&&$(".thirdWhole").children().hasClass("cateAci")==true){
			$(".fourthWhole").show();
		}else{
			$(".fourthWhole").hide();
		}
	})
})
//message A链接点击后样式
$(function(){
	$(".aBold").click(function(){
		$(this).parents("tr").addClass("aNormal");
	})
})
//电话号码选择
$(function(){
	var phonClick = $("#j-country-code-list").find("li");
	var phonNum = $("#j-country-code-trigger").find(".i-country-code");
	var classVal=$("#j-country-code-trigger").find("i");
	phonClick.click(function()
	{
		var classV=$(this).find("i").attr("class");
		phonNum.html($(this).find(".i-country-code").html())
		classVal.removeClass();
		classVal.addClass(classV);
		$(this).siblings().removeClass("fn-hide");
		$(this).addClass("fn-hide");
		$(this).parents("#j-country-code-list").fadeOut();
	})
})

///////////替换函数
$(function(){
	var valClick = $(".vals").find("li");
	var valNum = $(".val").find(".i-country-code");
	$(".val").click(function(){
		$(this).siblings(".vals").toggle();
	})
	valClick.click(function()
	{
		valNum.html($(this).find(".i-country-code").html())
		$(this).parents(".vals").fadeOut();
	})
})


//liveChat 对话框
$(function(){
	function getContentSize(){
		var winH = $(window).height();
		$(".liveChat_dialogBox").css({"height":winH-100})
		var writH = $(".liveChat_dialogBox").height();
		$(".liveChat_dialogShow").css({"height":writH-33})
		$(".liveChat_serviceLeft").css({"height":winH-67})
	}        
	window.onload = getContentSize;
    window.onresize = getContentSize;
	$(".chooseQ_writeTxt").keydown(function(){
		var writeH = $(".chooseQ_writeTxt").height();
		if(writeH>30){
			$(".chooseQ_writeTxt").css({"overflow-y":"scroll"})
		}else{
			$(".chooseQ_writeTxt").css({"overflow-y":"auto"})
		}
	})
	$(".liveChat_set").click(function(){
		$(this).children(".liveChat_setTxt").toggle();
		$(".liveChat_service").hide();
	})
	$(".liveChat_transferKF").click(function(){
		$(this).siblings(".liveChat_service").toggle();
		$(".liveChat_setTxt").hide();
	})
	$(".liveChat_setTxt p").click(function(e){
		$(this).addClass("liveChat_enter");
		$(this).siblings().removeClass("liveChat_enter");
		$(this).parent().hide(); 
		e.stopPropagation();
	})
	$(".liveCh_B").click(function(e){
		$(".liveChatPop").hide();
		$(this).siblings(".liveChatPop").toggle();
		e.stopPropagation();
	})
	$(".liveNo").click(function(){
		$(this).parents(".liveChatPop").hide();
	})
	$(".liveYes").click(function(){
		$(this).parents(".liveChat_service").hide();
		$(this).parents(".liveChatPop").hide();
	})
	$(".liveChat_serviceLeft ul li").click(function(){
		var xxkB = $(".liveChat_serviceRight").find(".liveChat_customer_con");
		var _this = $(this).index();
		$(this).addClass("liveLeft_aci");
		$(this).siblings().removeClass("liveLeft_aci");
		xxkB.hide();
		xxkB.eq(_this).show();
	})
	$(".liveChat_close").click(function(){
		$(this).children(".liveChat_questionnaire").show();;
	})
	$(".notSatisfiedWith").click(function(){
		$(".notSatisfiedWith_Box").show();
		$(".notSatisfied_Box").hide();
	})
	$(".notSatisfied").click(function(){
		$(".notSatisfied_Box").show();
		$(".notSatisfiedWith_Box").hide();
	})
	$(".chooseQ_Exit").click(function(){
		$(this).siblings(".liveChat_questionnaire").show();
	})
	
	$(".chooseQ_select").click(function(){
		$(this).children("ul").toggle();
		$(".chooseQ_select").css({"z-index":"1"})
		$(this).css({"z-index":"2"})
	})
	$(".chooseQ_select").find("li").click(function(){
		var liveP = $(this).parents(".chooseQ_select").find("p");
		liveP.html($(this).html())
	})
})
/*
$(function(){
	function consMove(){
		var w = $(".consumption").width();
		var wz = w - $(".currentWZ").width()/2;
		$(".consMove").animate({width:w},1000,function(){
			$(".currentWZ").css({"display":"block","left":wz})
			var i= 1;
		    setInterval(function() {
				if (i > 36) {
					i = 1;
				}
				var postion = i + "px -560px";
				$(".consMove").css('background-position',postion);
				i++;}, 200);
		})
	}
	var amount = 19999;
	$(".currentWZ").append("$"+amount)
	$('.progressPoint li').each(function(){
	   var from=$(this).data('from');
	   var to=$(this).data('to');
	   if(amount>=from&&amount<=to){
		   if($(this).index()==0){
				$(".consumption").animate({
					width:amount/to*146
			  	},function(){consMove()})
			}else{
			   $(".consumption").animate({
					width:146*($(this).index())+(amount-from)/(to-from)*146
			 	 },function(){consMove()})
			}
	   }else if(amount>=100000)
	   {
			$(".consumption").animate({
				width:730
			},function(){consMove()})
		}
	});
})*/


$(function(){try{myMove("moveLR","browsemove_box","browsepic");}catch(e){};})
$(function(){try{myMove("productDIVpic_box","productmove_box","productmovepic");}catch(e){};})
$(function(){try{myMove("customerDIVpic_box","customermove_box","customermovepic");}catch(e){};})
$(function(){try{myMove("videoDIVpic_box","customermove_box","customermovepic");}catch(e){};})
$(function(){try{myMove("browseDIVpic_box","productmove_box","productmovepic");}catch(e){};})
$(function(){try{myMove("clearHideBox","clearMoveBox","clearPic");}catch(e){};})
$(function(){try{myMove("clearHideBox1","clearMoveBox","clearPic");}catch(e){};})
$(function(){try{myMove("clearHideBox2","clearMoveBox","clearPic");}catch(e){};})
$(function(){try{myMove("direcories_box","SuperDeals_movebox","SuperDeals_movepic");}catch(e){};})
$(function(){try{myMove("videoSwitch","videoMove","videoList");}catch(e){};})
$(function(){try{upDownMove("upDownbox","productSmallmove","productSmallImg");}catch(e){};})	
/*
$(function(){try{myMove("accH_box","accMovebox","accMovePic");}catch(e){};})
$(function(){try{myMove("accH_box1","accMovebox","accMovePic");}catch(e){};})
*/

//superDeals

$(function(){
	try
	{
		var bannerBoxs = document.getElementById("superD_box");
		var leftClick = getByClass(bannerBoxs,"browseLeft_click")[0];
		var rightClick = getByClass(bannerBoxs,"browseRight_click")[0];
		var bannerBox = getByClass(bannerBoxs,"superDMove_box")[0];
		var bannerPic = getByClass(bannerBoxs,"superDCon_box");
		var bannerlist = document.getElementById("indexBanner_list").getElementsByTagName("li");
		var now = 0;
		bannerBox.style.width=(bannerPic[0].offsetWidth)*bannerPic.length+2+"px";
		//alert(bannerPic.length)
		for(var i=0;i<bannerlist.length;i++)
		{
			bannerlist[i].index=i;
			bannerlist[i].onclick=function()
			{
				now=this.index;
				tab()
			}
		}
		rightClick.onclick=function()
		{
			now++;
			if(now>=bannerlist.length)
			{
				now=0
			}
			tab()
		}
		leftClick.onclick=function()
		{
			now--;
			if(now<0)
			{
				now=bannerlist.length-1
			}
			tab()
		}
		function tab()
		{
			for(i=0;i<bannerlist.length;i++)
			{
				bannerlist[i].className="";
			}
			bannerlist[now].className="listActive"
			startMove(bannerBox,{left:-1198*now});
		}
		function next()
		{
			now++;
			if(now==bannerlist.length)
			{
				now=0;
			}
			tab();
		}
		var timer = setInterval(next , 5000)
		bannerBoxs.onmouseover  = function()
		{
			startMove(leftClick,{opacity:100})
			startMove(rightClick,{opacity:100})
			clearInterval(timer);
		}
		bannerBoxs.onmouseout  = function()
		{
			startMove(leftClick,{opacity:0})
			startMove(rightClick,{opacity:0})
			timer = setInterval(next,5000)
		}
		
	}catch(e){};
})




//index
$(function(){
	try
	{
		var oDiv = document.getElementById("SuperDeals_box");
		var leftClick = getByClass(oDiv,"browseLeft_click")[0];
		var rightClick = getByClass(oDiv,"browseRight_click")[0];

		$(rightClick).click(function(){
			var page = parseInt($("#superdeals_right_click").attr("value"));
			var index = parseInt($("#superdeals_right_click").attr("super_deals_page_index"));
			if(index >= 0 && index <= 3) {
				index = index + 1 ;
			}
			$("#superdeals_right_click").attr({super_deals_page_index : index});
			if(page < 3) {
				var data = {currentPage : page}
				$.ajax({
					url : js_super_deals.controllers.home.Home.getNextSuperDeals().url, 
					type : "POST",
					async: false,
					data : $.toJSON(data),
					contentType : "application/json",
					success : function(data) {
						$("#superdeals_right_click").attr({value : page+1});
						$("#super_deals_ul_li_clear").before(data);
					}
				});
			}
			var divBox = getByClass(oDiv,"SuperDeals_movebox")[0];
			var SmallBox = getByClass(oDiv,"SuperDeals_movepic");
			divBox.style.width = (SmallBox[0].offsetWidth+8)*SmallBox.length+"px";
			
			if(index < 4){
				if(divBox.offsetWidth-divBox.offsetLeft>oDiv.offsetWidth*2)
				{
					startMove(divBox,{left:divBox.offsetLeft-oDiv.offsetWidth})
				}
				else
				{
					startMove(divBox,{left:divBox.offsetLeft-(divBox.offsetWidth+divBox.offsetLeft-oDiv.offsetWidth)})
				}
			}
		})
		leftClick.onclick = function()
		{
			var index = parseInt($("#superdeals_right_click").attr("super_deals_page_index"));
			if(index > 0) {
				index = index -1 ;
			}
			$("#superdeals_right_click").attr({super_deals_page_index : index});
			var divBox = getByClass(oDiv,"SuperDeals_movebox")[0];
			var SmallBox = getByClass(oDiv,"SuperDeals_movepic");
			divBox.style.width = (SmallBox[0].offsetWidth+8)*SmallBox.length+"px";
			if(divBox.offsetLeft == 0)
			{
				startMove(divBox,{left:0})
			}
			else if(-divBox.offsetLeft >= oDiv.offsetWidth)
			{
				startMove(divBox,{left:divBox.offsetLeft+oDiv.offsetWidth})
			}
			else
			{
				startMove(divBox,{left:divBox.offsetLeft-divBox.offsetLeft})
			}
		}
		leftClick.onmouseover = rightClick.onmouseover = oDiv.onmouseover = function()
		{
			startMove(leftClick,{opacity:100})
			startMove(rightClick,{opacity:100})
		}
		leftClick.onmouseout = rightClick.onmouseout = oDiv.onmouseout = function()
		{
			startMove(leftClick,{opacity:0})
			startMove(rightClick,{opacity:0})
		}
	}catch(e){};
})

function upDownMove(myId,moveBox,smallBox)
{
	var oDiv = document.getElementById(myId);
	var divBox = getByClass(oDiv,moveBox)[0];
	//var SmallBox = getByClass(oDiv,smallBox);
	var SmallBox = divBox.getElementsByTagName("li");
	var SmallBoxmove = getByClass(oDiv,"productSmallPic")[0];
	var upClick = getByClass(oDiv,"productSmallPic_up")[0];
	var downClick = getByClass(oDiv,"productSmallPic_down")[0];
	
	divBox.style.height = (SmallBox[0].offsetHeight+12)*SmallBox.length+"px";
	if(SmallBox.length<=5)
	{
		downClick.style.display = "none";
	}
	downClick.onclick = function()
	{
			
		if(divBox.offsetHeight+divBox.offsetTop>SmallBoxmove.offsetHeight*2)
		{
			startMove(divBox,{top:divBox.offsetTop-SmallBoxmove.offsetHeight})
		}
		else
		{
			startMove(divBox,{top:divBox.offsetTop-(divBox.offsetHeight+divBox.offsetTop-SmallBoxmove.offsetHeight)})
		}
		
	}
	upClick.onclick = function()
	{
		if(divBox.offsetTop == 0)
		{
			startMove(divBox,{top:0})
		}
		else if(-divBox.offsetTop >= oDiv.offsetHeight)
		{
			startMove(divBox,{top:divBox.offsetTop+SmallBoxmove.offsetHeight})
		}
		else
		{
			startMove(divBox,{top:divBox.offsetTop-divBox.offsetTop})
		}
	}
}

function myMove(myId,moveBox,smallBox)
{
	var oDiv = document.getElementById(myId);
	var divBox = getByClass(oDiv,moveBox)[0];
	var SmallBox = getByClass(oDiv,smallBox);
	var leftClick = getByClass(oDiv,"browseLeft_click")[0];
	var rightClick = getByClass(oDiv,"browseRight_click")[0];
	var insertNode = getByClass(oDiv,"clear")[0];
	var ready = true;	
	
	var maxPage = parseInt($(rightClick).attr("max_page"));		    //总共的页数
	var url = $(rightClick).attr("next_page_ajax_url");			    //点击右翻页要提交到的地址，从页面中传过来
	var perPage = parseInt($(rightClick).attr("per_page"));			//每页显示的个数
	
	if((SmallBox.length+1)*SmallBox[0].offsetWidth<oDiv.offsetWidth){
			rightClick.style.display = "none";
	}

	$(rightClick).click(function()
	{
		var page = parseInt($(rightClick).attr("total_page"));		//已经查询数据的页码总数，从 0 开始
		var index = parseInt($(rightClick).attr("index"));			//表示当前页，页面从第0页开始

		if(url != null && url != ""){
			if((index == page) && (page < maxPage-1)) {
				var data = {currentPage : page, liClass : smallBox, perPage : perPage}		//传入需要延迟加载页的前一页和liClass，此时的page就是当前页
				$.ajax({
					url : url, 
					type : "POST",
					async: false,
					data : JSON.stringify(data),
					contentType : "application/json",
					success : function(data) {
						$(rightClick).attr({total_page : page+1});
						$(insertNode).before(data);
					}
				});
			} 
		}
		$(".chooseProduct_color").hide();
		var SmallBox = getByClass(oDiv,smallBox);
		if(SmallBox != ""){
			divBox.style.width = (SmallBox[0].offsetWidth+8)*SmallBox.length+"px";
			if(!ready)return;
			ready = false;	
			if(index < maxPage) {
				$(rightClick).attr({index:index+1});
				index = parseInt($(rightClick).attr("index"));
			}
			if(index == maxPage-1 ){
				rightClick.style.display = "none";
			}
			if(divBox.offsetWidth+divBox.offsetLeft<=oDiv.offsetWidth)
			{
				startMove(divBox,{left:oDiv.offsetWidth-divBox.offsetWidth},function(){ready=true})
			}
			else if(divBox.offsetWidth-divBox.offsetLeft>oDiv.offsetWidth*2)
			{
				startMove(divBox,{left:divBox.offsetLeft-oDiv.offsetWidth},function(){ready=true})
			}else
			{
				startMove(divBox,{left:divBox.offsetLeft-(divBox.offsetWidth+divBox.offsetLeft-oDiv.offsetWidth)},function(){ready=true})
			}
/*			
			if((SmallBox.length+1)*SmallBox.offsetWidth()<oDiv.offsetWidth)
			{
				rightClick.style.display = "none";
			}*/
			
			//重新加载倒计时方法
			leftClick.style.display = "block";
			$(".retroclockbox_xs").each(function(i,e){
				if(!$(this).hasClass("xdsoft")){
					timedownFun($(this).attr("id"), true,"xs",true);
				}
			});
		} else{
			rightClick.style.display = "none";
		}
	})
	
	leftClick.onclick = function()
	{
		if(!ready)return;
		ready = false;
		$(".chooseProduct_color").hide();
		var index = parseInt($(rightClick).attr("index"));			//表示当前页，页面从第0页开始
		$(rightClick).attr({index:index-1});						//点击左键 index 减 1
		if(divBox.offsetLeft == 0)
		{
			startMove(divBox,{left:0},function(){ready=true})
		}
		else if(-divBox.offsetLeft >= oDiv.offsetWidth)
		{
			startMove(divBox,{left:divBox.offsetLeft+oDiv.offsetWidth},function(){ready=true})
		}
		else
		{
			startMove(divBox,{left:divBox.offsetLeft-divBox.offsetLeft},function(){ready=true})
		}
		rightClick.style.display = "block";
		if(-divBox.offsetLeft<=oDiv.offsetWidth)
		{
			leftClick.style.display = "none";
		}
	}
	if(divBox.style.left==0)
	{
		leftClick.style.display = "none";
	}
	leftClick.onmouseover = rightClick.onmouseover = oDiv.onmouseover = function()
	{
		startMove(leftClick,{opacity:100})
		startMove(rightClick,{opacity:100})
	}
	leftClick.onmouseout = rightClick.onmouseout = oDiv.onmouseout = function()
	{
		startMove(leftClick,{opacity:0})
		startMove(rightClick,{opacity:0})
	}
}



//倒计时

(function($){
jQuery.fn.flipCountDown = jQuery.fn.flipcountdown = function( _options ){
	var default_options = {
			showHour	:true,
			showMinute	:true,
			showSecond	:true,
			am			:false,

			tzoneOffset	:0,
			speedFlip	:60,
			period		:1000,
			tick		:function(){
							return new Date();
						},
			autoUpdate	:true,
			size		:'md'
		},
		
		sizes = {
			lg:77,
			md:52,
			sm:35,
			xs:24
		},
		
		createFlipCountDown = function( $box ){
			var $flipcountdown 	= $('<div class="xdsoft_flipcountdown"></div>'),
				$clearex 		= $('<div class="xdsoft_clearex"></div>'),	 
				
				options = $.extend({},default_options),
				
				timer = 0,
				
				_animateRange = function( box,a,b ){
					_animateOne( box,a,(a>b&&!(a==9&&b==0))?-1:1,!(a==9&&b==0)?Math.abs(a-b):1 );
				},
				
				_animateOne = function( box,a,arrow,range ){
					if( range<1 )
						return;
	
					_setMargin(box,-(a*6*sizes[options.size]+1),1,arrow,function(){
						_animateOne(box,a+arrow,arrow,range-1);
					},range);
				},
				
				_setMargin = function( box, marginTop, rec, arrow,callback,range){
					if( marginTop<=-sizes[options.size]*60 )
						marginTop = -1;
					box.css('background-position','0px '+marginTop+'px' );
					if( rec<=6 ){
						setTimeout(function(){
							_setMargin(box, marginTop-arrow*sizes[options.size], ++rec, arrow, callback,range);	
						},parseInt(options.speedFlip/range));
					}else
						callback();
				},
				
				blocks = [],
				
				_typeCompare	= 	function ( a,b ){
					return 	a&&b&&(
								(a==b)||
								(/^[0-9]+$/.test(a+''+b))||
								(/^[:.\s]+$/.test(a+''+b))
							);
				},
				
				_generate = function( chars ){
					if( !(chars instanceof Array) || !chars.length )
						return false;
					for( var i = 0, n = chars.length;i<n;i++ ){
						if( !blocks[i] ){
							blocks[i] = $('<div class="xdsoft_digit"></div>');
							$clearex.before(blocks[i]);
						}
						if( blocks[i].data('value')!=chars[i] ){
							if( !_typeCompare(blocks[i].data('value'),chars[i]) ){
								blocks[i]
									.removeClass('xdsoft_separator')
									.removeClass('xdsoft_dot');
								switch( chars[i] ){
									case ':':blocks[i].addClass('xdsoft_separator');break;
									case '.':blocks[i].addClass('xdsoft_dot');break; 
									case ' ':blocks[i].addClass('xdsoft_space');break; 
								}
							}
							if( !isNaN(chars[i]) ){
								var old = parseInt(blocks[i].data('value')), 
									ii = parseInt(blocks[i].data('i')),
									crnt = parseInt(chars[i]);
								if( isNaN(old)||i!=ii ){
									old = (crnt-1)<0?9:crnt-1;
								}
								_animateRange(blocks[i],old,crnt);
							}
							blocks[i].data('value',chars[i]);
							blocks[i].data('i',i);
						}
					}
					if( blocks.length>chars.length ){
						for(;i<blocks.length;i++ ){
							blocks[i][0].parentNode.removeChild(blocks[i][0]);
							delete blocks[i];
						}
						blocks.splice(chars.length);
					}
					
				},
				
				counter = 0,
				
				_calcMoment = function(){
					var value = '1',chars = [];
					if(options.tick)
						value = (options.tick instanceof Function)?options.tick.call($box,counter):options.tick;
					
					if( typeof value!=='undefined' ){
						switch( value.constructor ){
							case Date:
								var h = (value.getHours()+options.tzoneOffset)%(options.am?12:24);
		
								if( options.showHour ){
									chars.push(parseInt(h/10));
									chars.push(h%10);
								}
										
								if( options.showHour && (options.showMinute || options.showSecond) )
									chars.push(':');
								
								if( options.showMinute ){
									chars.push(parseInt(value.getMinutes()/10));
									chars.push(value.getMinutes() % 10);
								}
								
								if( options.showMinute && options.showSecond )
									chars.push(':');
								
								if( options.showSecond ){
									chars.push(parseInt(value.getSeconds()/10));
									chars.push(value.getSeconds() % 10);
								}
							break;
							case String:
								chars = value.replace(/[^0-9\:\.\s]/g,'').split('');
							break;
							case Number:
								chars = value.toString().split('');
							break;
						}
						_generate(chars);
					}
				};
				
			$flipcountdown
				.append($clearex)
				.on('xdinit.xdsoft',function(){
					clearInterval(timer);
					if( options.autoUpdate )
						timer = setInterval( _calcMoment,options.period );
					_calcMoment();
				});
				
			$box.data('setOptions',function( _options ){
				options = $.extend({},options,_options);
				if( !sizes[options.size] )
					options.size = 'lg';
				$flipcountdown
					.addClass('xdsoft_size_'+options.size)
					.trigger('xdinit.xdsoft');
			});
			$box.append($flipcountdown);
		};	
	return this.each(function(){
		var $box = $(this);
		if( !$box.data('setOptions') ){
			$box.addClass('xdsoft')
			createFlipCountDown($box);
		}
		$box.data('setOptions')&&
			$.isFunction($box.data('setOptions'))&&
				$box.data('setOptions')(_options);
	});
}
})(jQuery);

function downtimer(intDiff,node){
	window.setInterval(function(){
		var day=0,
		hour=0,
		minute=0,
		second=0;
		if(intDiff > 0){
			day = Math.floor(intDiff / (60 * 60 * 24));
			hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
			minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
			second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
		}
		if (hour <= 9) hour = '0' + hour;
		if (minute <= 9) minute = '0' + minute;
		if (second <= 9) second = '0' + second;
		node.find('.priceDiscountTimer-day:eq(0)').text(day);			
		node.find('.priceDiscountTimer-hour:eq(0)').text(hour);
		node.find('.priceDiscountTimer-minute:eq(0)').text(minute);
		node.find('.priceDiscountTimer-second:eq(0)').text(second);
		intDiff--;
	}, 1000);
}



//公用倒计时

function timedownFun(nodeid, isreload,showsize,showday){
	var timenode = $("#"+nodeid);
	var intDiff = parseInt(timenode.attr("data"))/1000;
	var day=0,
	hour=0,
	minute=0,
	second=0;
	var formattime = "";
	timenode.flipcountdown({
		tzoneOffset:0,
		tick:function(){
			if(intDiff<=0){
				if(isreload){
					location.reload();
				}
				return "00:00:00";
			}
			if(intDiff > 0){
				day = Math.floor(intDiff / (60 * 60 * 24));
				hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
				minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
				second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
			}
			if (day <= 9) day = '0' + day;
			if (hour <= 9) hour = '0' + hour;
			if (minute <= 9) minute = '0' + minute;
			if (second <= 9) second = '0' + second;
			
			intDiff--;
			formattime = hour+":"+minute+":"+second;
			
			if(showday && day <= 12){
				formattime = day+":"+formattime;
			}
			return formattime;
		},
		size:showsize
	});
}


 var Listeners={
		stong:[],
		addListener:function(event,handler){
			var e= {};
			e['event']=event
			e['handler']=handler;
			this.stong.push(e);
		},
		fireListenner:function(event,addressid){
			for(var i in this.stong){
				var e=this.stong[i];
				if(event==e['event']){
					e['handler'].call(this,addressid);
				}
			}
		}
};

$(function(){
	$(document).on("click",".addsClick",function(){
		$(this).siblings(".edit_box").css({"display":"block"}) 
		$(".blackBK").css({"display":"block"})
		$(this).siblings(".edit_box").find(".newAddressBox").css({"display":"block"})
	})
	
	//hart
/*	$(".wishList").click(function(){
		$(this).addClass("redWish");
		$(this).children(".wishR").show();
		$(this).children(".wishN").hide();
		$("#wishListPOP").css({"display":"block"})
	})*/
	
	$(document).on("click",".chooseDelete > a",function(){
		$(this).siblings(".deletePop").toggle();
	})
	
	$(document).on("click",".deletePop > input",function(e){
		$(this).parents(".deletePop").hide();
		e.stopPropagation();
	})
	$(document).on("click",".deitADD",function()
	{
		$(this).siblings(".newAddressBox").after("<div class='bkBlack'></div>");
		$(this).siblings(".newAddressBox").addClass("addPop");
		$(this).siblings(".newAddressBox").fadeIn()
	})
	$(document).on("click",".closeAddress",function()
	{
		if($(this).parents(".newAddressBox").hasClass("addPop"))
		{
			$(this).parents(".newAddressBox").fadeOut(function(){
				$(this).parents(".newAddressBox").fadeOut();
           		
        	});
			$(".bkBlack").fadeOut(function(){$(".bkBlack").remove();$(this).parents(".newAddressBox").removeClass("addPop");});
		}else{
			$(this).parents(".newAddressBox").fadeOut();
			$(".blackBK").fadeOut();
		}
	})
	$(document).on("click",".rightThis",function(){ 
		$(this).children("span").toggleClass("afters");
		if($(this).children("span").hasClass('afters')){
			$(this).children(".joinCheck").attr("checked", true);
			$(this).children(".joinCheck").prop("checked",true);
		}else{
			$(this).children(".joinCheck").attr("checked", false);
		}
	})
	$(document).on("click",".delete",function(){
		if($(this).hasClass("bannedClick")==false)
		{
			$(".cartListUL,.listOl_bd").css({"z-index":"1"})
			$(this).parents(".cartListUL,.listOl_bd,.cartListLI").css({"z-index":"2"})
			$(this).children(".deletePop").toggle();
		}else{
			$(this).unbind("click");
		}
	})
	
	$(document).on("click",".customer_popClose,.cancelClose",function(e)
	//$(".customer_popClose,.cancelClose").click(function(e)
	{
		$(this).parents(".blockPopup_box").fadeOut();
		$(this).parents(".account_Y").fadeOut();
		$(this).parents(".account_x").fadeOut();
		e.stopPropagation();
	})
})
function changeCaptcha(node){
	var e = $(node).parent().find(".captcha:eq(0)")[0];
	var url = e.src;
	if(url.indexOf("?")>0){
		url = url.substring(0,url.indexOf("?"));
	}
	e.src = url+'?r='+Math.random();
}

//公共弹出框
function pophtml(title, text){
	var errorBox = $('#errorBox');
	if(errorBox.length == 0){
		var html = [];
		html[html.length] = '<div style="display: block" class="pu_pop popNone_s" id="errorBox" >';
		html[html.length] = '<div class="ns_pop_box"><div class="btn_pop_close" id="popClose" ></div>';
		html[html.length] = '<div class="pop_title"><h3>'+title+'</h3></div>';
		html[html.length] = '<div class="pop_con"><p id="errorBoxTxt">'+text+'</p></div>';
		html[html.length] = '<div class="pop_input_box">';
		html[html.length] = '<input type="button" class="pop_input_confirm" value="OK" id="errorBoxOkBtn"/></div></div>';
		html[html.length] = '<div class="blockPopup_black"></div></div>';
		$('body').append(html.join(''));
		$('#errorBoxOkBtn,#popClose').click(function() {
			$('#errorBox').remove();
		});
	}
	$('#errorBox').show();
	$('#errorBoxTxt').text((text || ''));
	
}
//控制子导航margin-left deal
$(function()
{
	var dealsW = $(".dealsNav").outerWidth(true);
	$(".dealsNav").css({"margin-left":-dealsW/2+70})
})
