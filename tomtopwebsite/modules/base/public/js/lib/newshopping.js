/**
 * Created by Administrator on 15-9-23.
 */
var ns_left;
var ns_top;
var ns_position;
var ns_h2;
$(function(){
    //地址弹出层选中默认
//    $(".newshopping_address_default").click(function(){
//        $(this).toggleClass("sel");
//    });
//    $(".address_country").click(function(){
//        $(this).parents(".newshopping_address_country").find(".country_all").toggle();
//    });
    $(".address_submit .cancel").click(function(){
        $(this).parents(".blockPopup_box").hide();
    });
    $(".ns_address_list .add_item").click(function(){
        $("#pop_address").show();
    });

    $(".have_code p").click(function(){
        $(this).parents(".have_code").find(".have_code_input").toggle();
        if($(this).find("span").text()=="+"){
            $(this).find("span").text("-");
        }else{
            $(this).find("span").text("+");
        }
    });
    /**购物悬浮层**/

    try{
        ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
        ns_top=$(".newshopping_box_right").offset().top;
        ns_position=$(".shopping_bottom").offset().top;
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



    //var ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
    //var ns_top=$(".newshopping_box_right").offset().top;
    //var ns_position=$(".shopping_bottom").offset().top;
    //var ns_h2=ns_position-$(".newshopping_box_right").height()-70;
    //$(window).scroll(ns_top, function(event) {
    //    if($(this).scrollTop()>event.data&&$(this).scrollTop()<ns_h2){
    //        $(".newshopping_box_right").css({"position":"fixed","z-index":"99","left":ns_left,"top":"40px","margin-top":"0px"})
    //    }else{
    //        $(".newshopping_box_right").css({"position":"inherit","margin-top":"70px"});
    //    };
    //});
    /**购物悬浮层over**/
    /**Payment Method**/
//    $(".payment_method_tab li").click(function(){
//        var a_index=$(this).index();
//        $(this).siblings().removeClass("sel");
//        $(this).addClass("sel");
//        $(this).parents(".ns_payment_method ").find(".payment_method_con>li").hide();
//        $(this).parents(".ns_payment_method ").find(".payment_method_con>li").eq(a_index).show();
//    });
//    $(".span_qiwi_account").click(function(){
//        $(this).parents(".method_qiwi_account").find(".qiwi_account_list").toggle();
//    });

//    $(".boleto_input h5").click(function(){
//        $(this).parents(".boleto_input").find("ul").toggle();
//    });
//    $(".billing_address").click(function(){
//        var _this=$(this).find("span");
//        if(_this.attr("class")=="sel"){
//            _this.removeClass("sel");
//            _this.parents(".method_con_visa").find(".ns_address_list").hide();
//            _this.parents(".method_con_visa").find(".ship_address").show();
//        }else{
//            _this.addClass("sel");
//            _this.parents(".method_con_visa").find(".ship_address").hide();
//            _this.parents(".method_con_visa").find(".ns_address_list").show();
//        }
//    });
    $(".method_table tbody tr").click(function(){
        $(this).siblings().removeClass("sel");
        $(this).addClass("sel");
    });

    $(".have_code_select .current_num").click(function(){
        $(this).parents(".have_code_select").find(".current_list").toggle();
    });
    //$(".have_code_select .current_list li").click(function(){
    //    $(this).parents(".have_code_select").find(".current_list").hide();
    //});程序已加
    //支付方式显示按钮
    $(".btn_more_payment").click(function(){
        $(this).parents(".ns_payment_method ").find(".payment_method_tab").toggleClass("sel");
        $(this).parents(".ns_payment_method ").find(".payment_method_con").toggleClass("sel");
        $(this).toggleClass("sel");
    });

});
