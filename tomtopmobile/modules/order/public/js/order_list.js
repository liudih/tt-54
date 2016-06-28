var pageArr = [1,1,1,1];
var loadingArr = [true,true,true,true];
var firstLoad = [true,true,true,true];

$(window).scroll(function(){
	var scrollTop = $(this).scrollTop();
	var scrollHeight = $(document).height();
    var windowHeight = $(this).height();
    var nowdiv = $(".publicTAB_css .aciCss:eq(0)").index();
    
	if(Math.abs(scrollHeight-scrollTop-windowHeight) < 10 && loadingArr[nowdiv]){
		pageArr[nowdiv] = pageArr[nowdiv] + 1;
		loadData(nowdiv, pageArr[nowdiv]);
	} 
})

function loadData(nowdiv, page){
	var theurl = "/order/order-list/";
	var st = nowdiv+1;
	theurl += st;
	$.ajax({
		url: theurl,
		type: "get",
		dataType: "json",
		async: false,
		data:{
			"p": page,
			"isajax": 1
		},
		success:function(data){
			if(data.html==null || data.html==""){
				loadingArr[nowdiv] = false;
			}
			if(page==1){
				firstLoad[nowdiv] = false;
			}
			$(".TAB_Con").eq(nowdiv).append(data.html);
		},
		complete:function(){
		}
	});
}

$(".publicTAB_css li").click(function(){
	var nowdiv = $(this).index();
	if(firstLoad[nowdiv] && $(".TAB_Con").eq(nowdiv).find(".orderInfBox").length==0){
		loadData(nowdiv, 1);
	}
});