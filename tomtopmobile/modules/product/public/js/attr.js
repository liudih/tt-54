var globaldata;
//加载多属性
function popDiv(node){
	var n = $(node);
	n.parent().find(".popBoxNone").show();
	var lis = n.next().find(".listingid:eq(0)").val();
	var url = attrurl;
	//getStorage(n.next().find(".storage_content:eq(0)"),lis);//显示仓库
	$.ajax({
		url: url,
		type: "get",
		data:{
			"clistingid": lis
		},
		dataType: "json",
		error:function(err){
		},
		success : function(data) {
			if(data.result=="success"){
				globaldata = data.data;
				var box = n.next().find(".attr_content:eq(0)");
				paintPanel(box,lis,data.data);
				n.next().find("h5").show();
			}else{
				n.next().find("h5").hide();
			}
		}
	});
};
//重画面板
function paintPanel(box,listing,data){
	var html = "";
	//当前属性
	var selectedLis = changeModel(data)[listing];
	var modelData = changeModel(data);
	
	$.each(getMutilatribute(data), function(i, item) {
		html += '<p class="product_page_csTxt lineBlock">'+i+':</p>';
		if(i=="color"){
			html += '<ul class="product_page_colorPic lineBlock">';
		}else{
			html += '<ul  class="product_page_sziePic lineBlock">';
		}
		var arr = filterAttr(i,selectedLis,modelData);
		var index = 0;
		$.each(item, function(key, value){
			if(value != null && value['url'] != null){
				if(arr[key]==null){
					html += '<li class="lineBlock invalids">';
				}else{
					html += '<li class="lineBlock aciClick '+(arr[key]==listing?"aciCss":"")+'" listing="'+arr[key]+'" onclick="attrclick(this);" >';
				}
				html += '<img src="'+packImgUrl(value['url'],120,120)+'" /></li>';
			}else{
				if(arr[key]==null){
					html += '<li class="lineBlock invalids">';
				}else{
					html += '<li class="lineBlock aciClick '+(arr[key]==listing?"aciCss":"")+'" listing="'+arr[key]+'" onclick="attrclick(this);" >';
				}
				html += key+'</li>';
			}
			index++;
		});
		html += "</ul>";
	});
	box.html(html);
}

//组装图片路径
function packImgUrl(url,w,h){
	if(url.indexOf("http://")!=-1){
		return url;
	}else{
		return imgurl + "/product/xy/"+w+"/"+h+"/"+url;
	}
}
//组装多属性集合
function changeModel(data) {
	var map = {};
	$.each(data, function(i, item){
		$.each(item, function(j, jitem){
			var clistingid = jitem.clistingid;
			var modle = {};
			if (clistingid in map) {
				modle = map[clistingid];
			}
			modle[jitem.ckey] = jitem.cvalue;
			map[clistingid] = modle;
		});
	});
	return map;
}
function getMutilatribute(data) {
	var map = {};
	$.each(data, function(i, item){
		var mapatribute = {};
		$.each(item, function(j, jitem){
			var cvalue = jitem.cvalue;
			var valuemap = {};
			if(jitem.bshowimg){
				valuemap['url'] = jitem.imgUrl;
			} else {
				valuemap['cvalue'] = jitem.cvalue;
            }
			mapatribute[cvalue] = valuemap;
		});
		map[i] = mapatribute;
	});
	return map;
}

//过滤每行属性
function filterAttr(nowattr,selectdata,data){
	var arr = {};
	$.each(data, function(i, item){
		var isfuhe = true;
		$.each(item, function(key, value){
			if(key!=nowattr && selectdata[key]!=value){
				isfuhe = false;
			}
		});
		if(isfuhe){
			var nt = item[nowattr];
			arr[nt] = i;
		}
	});
	//console.log(arr);
	return arr;
}
//组合为了改变产品信息的数据
function packSomeData(data){
	var val = {};
	$.each(data, function(i, item){
		$.each(item, function(j, jitem){
			var map = {};
			$.each(jitem, function(key, value){
				map[key] = value;
			});
			val[jitem['clistingid']] = map;
		});
	});
	return val;
}
//更改产品信息
function changeInfo(listing,data,box){
	var jdata = packSomeData(data);
	var val = jdata[listing];
	box.find(".buyPop_Title:eq(0)").html(val.title);
	box.find(".tempimg img:eq(0)").attr("src",packImgUrl(val.imgUrl,377,377));
	box.find(".product_price:eq(0)").html(val.symbol+" "+val.price);
	var baseprice = val.symbol+" "+val.basePrice;
	box.find(".product_priceGR:eq(0)").html(val.isDiscounted?baseprice:"");
	
	var discounthtml = "";
	if(val.isDiscounted){
		discounthtml = '<div class="discount_icon">'+(parseFloat(val.discount).toFixed(2)*100)+'</div>';
	}
	box.find(".discountcontent:eq(0)").html(val.isDiscounted?discounthtml:"");
	if(val.validToBySeconds>0){
		box.find(".product_page_Expires").show();
		var timeid = box.find(".product_page_Expires:eq(0)").attr("timeid");
		if(timeid.length>0){
			window.clearInterval(Number(timeid));
		}
		var tid = downtimer(val.validToBySeconds,box.find(".product_page_Expires .orange:eq(0)"));
		box.find(".product_page_Expires:eq(0)").attr("timeid",tid);
	}else{
		box.find(".product_page_Expires").hide();
	}
}

function attrclick(node){
	var n = $(node);
	if(n.hasClass("aciCss")){
		return ;
	}
	if(globaldata!=null){
		var outbox = n.closest(".buyPopBox");
		var box = n.closest(".attr_content");
		var lis = n.attr("listing");
		var lisbox = n.closest(".buyPopCon").find(".listingid:eq(0)");
		lisbox.val(lis);
		paintPanel(box,lis,globaldata);
		getStorage(box.prev(),lis);//显示仓库
		//改变信息
		changeInfo(lis,globaldata,outbox);
	}
};

//循环折扣倒计时
$(".product_page_Expires").each(function(i,e){
	var val = $(this).attr("data");
	var istime = $(this).attr("timeid");
	if(val!=null && val.length>0 && istime.length==0){
		$(this).show();
		var timeid = downtimer(val,$(this).find(".orange:eq(0)"));
		$(this).attr("timeid",timeid);
	}
});

function downtimer(idf,node){
	var intDiff = parseInt(idf)/1000;
	var InterValObj = window.setInterval(function(){
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
		if (day <= 9) day = '0' + day;
		if (hour <= 9) hour = '0' + hour;
		if (minute <= 9) minute = '0' + minute;
		if (second <= 9) second = '0' + second;
		var html = day + ":" + hour + ":" + minute + ":" + second;
		if(Number(day)>7){
			html = hour + ":" + minute + ":" + second;
		}
		node.html(html);
		if(intDiff==0){
			window.clearInterval(InterValObj);
		}
		intDiff--;
	}, 1000);
	return InterValObj;
}

//显示仓库
function getStorage(jnode,listingid){
	$.ajax({
		url: "/showproductstorage",
		type: "get",
		data:{
			"listingid": listingid
		},
		dataType: "json",
		success : function(data) {
			if(data.result=="success"){
				var list = data.list;
				var html = '';
				for(var i=0;i<list.length;i++){
					html += '<li class="lineBlock aciClick '+(i==0?"aciCss":"")+'" data="'+list[i].iid+'" onclick="touchStorage(this)" >'+list[i].cstoragename+'</li>';
				}
				jnode.html(html);
			}
		}
	});
}

//点击仓库
function touchStorage(node){
	var the = $(node);
	var pnode = the.parent();
	pnode.find("li").removeClass("aciCss");
	the.addClass("aciCss");
}
