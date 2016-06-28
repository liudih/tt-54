window.onload = window.onresize = function () {
    resizeIframe();
}
var resizeIframe=function(){
    var bodyw=document.body.clientWidth;
    for(var ilength=0;ilength<=document.getElementsByTagName("iframe").length;ilength++){

        document.getElementsByTagName("iframe")[ilength].height = bodyw*9/16;//设定高度

    }
}