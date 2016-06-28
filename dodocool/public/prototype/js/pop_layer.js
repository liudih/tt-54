/**
 * Created by Administrator on 2015/7/15.
 */
$(function(){
    //关闭弹出框
    $(".btn_alert_close").click(function(){
        $(this).parents(".pop_layer").hide();
    });
    //选择地区栏
    $(".sel_country h3").click( function(){
        $(this).parent().find(".allcountry").toggle();
    });
    //选择货币栏
    $(".sel_currency h3").click( function(){
        $(this).parent().find(".allcurrency").toggle();
    });
    //选择语言栏
    $(".sel_lang h3").click( function(){
        $(this).parent().find(".all_lang").toggle();
    });
    //
    $(".allcountry_list li").click(function(){
        var flagClass=$(this).attr("class");
        var flagName=$(this).find("span").html();
        $(this).parents(".alert_sel_box").find("h3").removeClass().addClass(flagClass).find("span").html(flagName);
        $(this).parents(".allcountry").hide();
    });



});



