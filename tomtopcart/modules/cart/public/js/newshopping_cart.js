/**
 * Created by Administrator on 2015/9/29.
 */
var ns_left;
var ns_top;
var ns_position;
var ns_h2;
$(function(){


    try{
         ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
         ns_top=$(".newshopping_box_right").offset().top;
         ns_position=$(".warehouse_later_list").offset().top+$(".warehouse_later_list").height();
         ns_h2=ns_position-$(".newshopping_box_right").height()-70;

        $(window).scroll(ns_top, function(event) {

            if($(this).scrollTop()>event.data&&$(this).scrollTop()<ns_h2){
                $(".newshopping_box_right").css({"position":"fixed","z-index":"99","left":ns_left,"top":"40px","margin-top":"0px"})
            }else{
                $(".newshopping_box_right").css({"position":"inherit","margin-top":"70px"});
            }
        });
    }catch(e){

    }
    $(window).resize(function(){
        $(".newshopping_box_right").css({"position":"inherit","margin-top":"70px"});
         ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
        if(!document.body.scrollTop==0||!document.documentElement.scrollTop==0){
            $(".newshopping_box_right").css({"position":"fixed","z-index":"99","left":ns_left,"top":"40px","margin-top":"0px"})
        }
    });

    $(".ship_address_pop .address_country").click(function(){
        $(this).parents().find(".country_all").toggle();
    });
    $(".method_table tbody tr").click(function(){
        $(this).siblings().removeClass("sel");
        $(this).addClass("sel");
    });
    $(".ship_pop_Continue").click(function(){
        $("#ship_to_pop").hide();
    });
    $(".newshopping_box_right .ship_to p span").click(function(){
        $("#ship_to_pop").show();
    });
    $(".warehouse_list .warehouse_sel").click(function(){
        $(this).parents("li").siblings().find(".warehouse_sel").removeClass("sel");
        $(this).addClass("sel");
    });

    $(".warehouse_later_list .warehouse_sel").click(function(){
        $(this).find(".warehouse_sel_btn").toggleClass("sel");
        $(this).parents("li").find(".myshop_wares").slideToggle(400);

        setTimeout(function(){
            ns_position=$(".warehouse_later_list").offset().top+$(".warehouse_later_list").height();
            ns_h2=ns_position-$(".newshopping_box_right").height()-70;
        },410);
    });

    $(".choose_product_box>span").click(function(){
        $(this).parents(".choose_product_box").children(".choose_product").toggle();
    });

    $(".choose_boxs .span_close").click(function(){
        $(".choose_product").hide();
    });
    $(".choose_product .cancel").click(function(){
        $(".choose_product").hide();
    });

    $(".color_list li,.size_list li").click(function(){
        $(this).siblings().removeClass("sel");
        $(this).addClass("sel");
    });
    $(".color_list .canno_sel,.size_list .canno_sel").unbind("click");

    $("#promo_code_apply").click(function(){
        $(this).hide();
        $("#promo_code_cancel").show();
        $("#promo_code_text").attr("disabled","disabled");
    });

    $("#promo_code_cancel").click(function(){
        $(this).hide();
        $("#promo_code_apply").show();
        $("#promo_code_text").removeAttr("disabled");
    });

    $("#coupon_code_apply").click(function(){
        $(this).hide();
        $("#coupon_code_cancel").show();
        $("#coupon_code_select").attr("disabled","disabled");
    });

    $("#coupon_code_cancel").click(function(){
        $(this).hide();
        $("#coupon_code_apply").show();
        $("#coupon_code_select").removeAttr("disabled");
    });

//优惠劵
    $(".have_code p").click(function(){
        $(this).parents(".have_code").find(".have_code_input").toggle();
        if($(this).find("span").text()=="+"){
            $(this).find("span").text("-");
        }else{
            $(this).find("span").text("+");
        }
    });
    $(".have_code_select .current_num").click(function(){
        $(this).parents(".have_code_select").find(".current_list").toggle();
    });
    //$(".have_code_select .current_list li").click(function(){
    //    $(this).parents(".have_code_select").find(".current_list").hide();
    //});程序已加

});
