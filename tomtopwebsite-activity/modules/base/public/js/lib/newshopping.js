/**
 * Created by Administrator on 15-9-23.
 */
$(function(){
    //µØÖ·µ¯³ö²ãÑ¡ÖÐÄ¬ÈÏ
    $(".newshopping_address_default").click(function(){
        $(this).toggleClass("sel");
    });
    $(".address_country").click(function(){
        $(this).parents(".newshopping_address_country").find(".country_all").toggle();
    });
    $(".address_submit .cancel").click(function(){
        $(this).parents(".blockPopup_box").hide();
    });
    $(".ns_address_list .add_item").click(function(){
        $("#pop_address").show();
    });

    $(".have_code p").click(function(){
        $(this).parents(".have_code").find(".have_code_input").toggle();
    });
    /**¹ºÎïÐü¸¡²ã**/
    var ns_left=parseInt($(".newshopping_box_right").offset().left)+"px";
    var ns_top=$(".newshopping_box_right").offset().top;
    var ns_position=$(".shopping_bottom").offset().top;
    var ns_h2=ns_position-$(".newshopping_box_right").height()-70;
    $(window).scroll(ns_top, function(event) {
        if($(this).scrollTop()>event.data&&$(this).scrollTop()<ns_h2){
            $(".newshopping_box_right").css({"position":"fixed","z-index":"99","left":ns_left,"top":"40px","margin-top":"0px"})
        }else{
            $(".newshopping_box_right").css({"position":"inherit","margin-top":"70px"});
        };
    });
    /**¹ºÎïÐü¸¡²ãover**/
    /**Payment Method**/
    $(".payment_method_tab li").click(function(){
        var a_index=$(this).index();
        $(this).siblings().removeClass("sel");
        $(this).addClass("sel");
        $(this).parents(".ns_payment_method ").find(".payment_method_con>li").hide();
        $(this).parents(".ns_payment_method ").find(".payment_method_con>li").eq(a_index).show();
    });
    $(".span_qiwi_account").click(function(){
        $(this).parents(".method_qiwi_account").find(".qiwi_account_list").toggle();
    });

    $(".boleto_input h5").click(function(){
        $(this).parents(".boleto_input").find("ul").toggle();
    });
    $(".billing_address").click(function(){
        var _this=$(this).find("span");
        if(_this.attr("class")=="sel"){
            _this.removeClass("sel");
            _this.parents(".method_con_visa").find(".ns_address_list").hide();
            _this.parents(".method_con_visa").find(".ship_address").show();
        }else{
            _this.addClass("sel");
            _this.parents(".method_con_visa").find(".ship_address").hide();
            _this.parents(".method_con_visa").find(".ns_address_list").show();
        }
    });
    $(".method_table tbody tr").click(function(){
        $(this).siblings().removeClass("sel");
        $(this).addClass("sel");
    });



});

