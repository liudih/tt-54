var cartlist = [];
var storagemap = {
    "1":"CN",
	"2":"US",
	"3":"GB",
	"4":"IT",
	"5":"DE",
	"6":"FR",
	"7":"AU",
	"9":"FR-X51",
	"10":"FR-X52",
	"11":"FR-X53",
	"12":"ES"
}
$(function(){
	showCartData();
	$("#topSearch_cart").mouseenter(function(){
	    $("#topSearch_Hidebox").show().delay(1000);
	    showCartData();
	});
	$("#topSearch_cart").mouseleave(function(){
		$("#topSearch_Hidebox").hide().delay(1000);
	});
});

function showCartData(){
	$("#topSearch_Hidebox").css("height","auto");
	$("#cartsCon").html("");
	$(".noneProduct:eq(0)").hide();
	$("#loadingimg").show();
	var url = "/cart/cartdropdata";
	$.ajax({
		url: url,
		type: "get",
		cache : false,
		dataType: "json",
		success:function(data){
			if(data.result=="success" && data.size>0){
				if(data.size>3){
					$("#topSearch_Hidebox").css("height","285px");
				}else{
					$("#topSearch_Hidebox").css("height","auto");
				}
				//$("#cartsCon").html(data.html);
				var chtml = '';
				var dd = data.data;
				var urlprefix = $(".urlprefixCls:eq(0)").val();
				
				if(dd){
					for(var p in dd){
						chtml += '<p class="miniCarWare" style="font-size:14px;color:#333;padding:10px 0 10px 16px;border-bottom: 1px solid #ccc;background-color:#fff;">Ships from '+storagemap[p]+' Warehouse<span></span></p>';
						for(var i=0;i<dd[p].length;i++){
							var oldp = parseFloat(dd[p][i].origprice);
							var newp = parseFloat(dd[p][i].nowprice);
							var dis = (oldp-newp)/oldp;
							var theurl = urlprefix+'/'+dd[p][i].url;
							chtml += '<li class="lbBox" id="drop-'+dd[p][i].listingId+'">'+
							'<a class="cartHeader_IMG lineBlock" href="'+theurl+'.html" >'+
							'<img itemprop="image" title="'+dd[p][i].title+'" src="'+packImg(dd[p][i].imageUrl)+'" width="80" height="80" /></a>'+
						    '<div class="lineBlock cartHeader_TXT">'+
							'<a class="cartTxt" href="'+theurl+'.html">'+dd[p][i].title+'</a>'+
							'<p class="cartSale">'+
							(dis!=null && dis>0 ? 
							'<span class="saleOff">'+parseInt(dis*100+'')+'% OFF</span>'+
							'<span class="seleNum">'+dd[p][i].symbol+' '+dd[p][i].origprice+'</span>'
							: '')+
							'</p>'+
							'<p>'+
							'<span class="cartPrice">'+dd[p][i].symbol+' '+dd[p][i].nowprice+'</span>'+
							'<span>x'+dd[p][i].num+'</span>'+
							'</p>'+
							'</div>'+
							'<a class="icon-small-close" onclick="deldrop(\''+dd[p][i].listingId+'\',\''+p+'\')" href="javascript:void(0);"></a>'+
							'</li>';
						}
					}
				}
				$("#cartsCon").html(chtml);
				
				$("#cartdropbtn").show();
				$("#shoppingbtn").hide();
			}else{
				$(".noneProduct:eq(0)").show();
				data['size']=0;
				$("#cartdropbtn").hide();
				$("#shoppingbtn").show();
			}
			
			var cnum = parseInt(data.size);
//			var btnval = $("#cartdropbtn").val();
//			var re = /[(][0-9]+[)]/; 
//			btnval = btnval.replace(re,"("+cnum+")");
//			$("#cartdropbtn").val(btnval);
			$("#cartdropnum").html(cnum);
			//cartlist = list;	//存全局变量
		},
		complete:function(){
			 $("#loadingimg").hide();
		}
	});
}
function deldrop(cid,sid){
	$("#topSearch_Hidebox").show();
	var url = "/cart/delcartitem";
	var list = [];
	var cids = cid.split(",");
	for(var i=0;i<cids.length;i++){
		if(cids[i]!="" && cids[i].length>1){
			var map = {};
			map['clistingid'] = cids[i];
			var ssid = isNaN(sid) ? 1 : sid;
			map['storageid'] = ssid;
			list[list.length] = map;
		}
		var dd = $.toJSON(list);
	}
	
	$.ajax({
		url : url,
		type: "POST",
		dataType: "json",
		contentType: "application/json",
		data: dd,
		async : true,
		success : function(data) {
			if (data.result == "success") {
//				$("#drop-" + cid).hide();
//				var len = $("#cartsCon >.lbBox:visible").length;
//				if(len > 0){
//					$("#cartdropbtn").show();
//					$("#shoppingbtn").hide();
//				}
//				else{
//					$(".noneProduct:eq(0)").show();
//					$("#cartdropbtn").hide();
//					$("#shoppingbtn").show();
//				}
//				$("#cartdropnum").html(len);
//				
//				if(len<3){
//					$("#topSearch_Hidebox").css("height","auto");
//				}
				showCartData();
				$("#topSearch_Hidebox").show();
			}
		},
		complete : function() {
			// $("#loadingimg").hide();
		}
	});
};

function packImg(url){
	if(url.indexOf("http://")!=-1 || url.indexOf("https://")!=-1){
		return url;
	}else{
		return "http://img.tomtop-cdn.com/product/xy/120/120/"+url;
	}
}

