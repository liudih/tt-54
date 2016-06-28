/**
 * Created by Administrator on 2015/9/29.
 */

$(function(){

    var ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
    var ns_top=$(".newshopping_box_right").offset().top;
    var ns_position=$(".warehouse_later_list").offset().top;
    var ns_h2=ns_position-$(".newshopping_box_right").height()-70;

    $(window).scroll(ns_top, function(event) {
        if($(this).scrollTop()>event.data&&$(this).scrollTop()<ns_h2){
            $(".newshopping_box_right").css({"position":"fixed","z-index":"99","left":ns_left,"top":"40px","margin-top":"0px"})
        }else{
            $(".newshopping_box_right").css({"position":"inherit","margin-top":"70px"});
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

    $(".warehouse_sel_btn").click(function(){
        $(this).parents("li").find(".myshop_wares").slideToggle(400);
    })


    //�����ʾ����ѡ��size
    $(".choose_product_box>span").click(function(){
        $(this).parents(".choose_product_box").children(".choose_product").toggle();
    });

    //����رյ���ѡ��size
    $(".choose_boxs .span_close").click(function(){
        $(".choose_product").hide();
    });
    $(".choose_product .cancel").click(function(){
        $(".choose_product").hide();
    });

    //ѡ��size
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
});






