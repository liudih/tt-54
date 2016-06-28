$(function (){
	var rotateTimeOut = function (){
		$('#rotate').rotate({
			angle:0,
			animateTo:2160,
			duration:8000,
			callback:function (){
				alert('time-out,please check network configuration');
			}
		});
	};
	var bRotate = false;
	var rotateFn = function (awards, angles, txt){
		bRotate = !bRotate;
		$('#rotate').stopRotate();
		$('#rotate').rotate({
			angle:0,
			animateTo:angles+1800,
			duration:3000,
			callback:function (){
				$('.bg').css({"display":"block"})
				$('.wrap').css({"display":"block"})
				$('.error').css({"display":"none"})
				$('.alert').html(txt);
				bRotate = !bRotate;
			}
		})
	};
	/*$(".pointer").click(function(){
		$('.bg').css({"display":"block"})
		//$('.error').css({"display":"block"})
		$('.wrap').css({"display":"none"})
		})*/
	$(".closed").click(function(){
		$(".bg").hide();
	})
	$('.pointer').click(function (){
		if(bRotate)return;
		var z=10;
		var item = rnd(0,z);
		var lj=5;   //这里是后台出来的奖
		var data = {};
		data.activityType = $("#lotteryPageType").val();
		data.activityPageId = $("#lotteryPageID").val();
		data.startDate = $("#lotteryBeginDate").val();
		data.endDate = $("#lotteryEndDate").val();
		data.activityPageItemId = 1;
		$.ajax({
			url : "/research/hothit",
			type : "post",
			data : $.toJSON(data),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				var message = "";
				if(data.fixedStatus == "EXOURE"){
					message = "The Lucky Draw game is over, please look forward to more exciting events.";
					$(".error .win").html(message);
					$('.bg').show();
					$('.error').show();
					$('.wrap').hide();
					return;
				}
				if(data.fixedStatus == "NO_LOGIN"){
					$(".blockPopup_box").show();
					return;
				}
				if(data.fixedStatus == "NOT_ENOUGH_MONEY"){
					$('.bg').show();
					$('.error').show();
					$('.wrap').hide();
					return;
				}
				if(data.fixedStatus == "SUCC"){
					lj = data.lotteryResult.lotteryLevel;
					if(lj==5) {
						lj = 1;
					}
					else if(lj==4)
					{
						lj=3;
					}
					else if(lj==3)
					{
						lj=5;
					}
					else if(lj==1)
					{
						lj=9;
					}
					for(var i=0; i<z; i++){
						if(i==lj){
							rotateFn(i, 360/z/2+360/z*i, data.lotteryResult.desc);
							break;
						}		
					}
					console.log(item);
				} else {
					rotateFn(1, 360/z/2+360/z*1, "no prize");
				}
			}
		});
	});
	//获取中奖结果
	var getPrizeResult = function(){
		var param = {};
		param.pageId = $("#lotteryPageID").val();
		$.ajax({
			url : "/research/prizeResult",
			type : "post",
			data : $.toJSON(param),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				$(".bd ul").empty();
				for(var i=0;i<data.length;i++){
					$(".bd ul").append("<li>"+data[i].cemail+"<span>"+data[i].cname+"</span></li>");
				}
			}
		});
	}
	//定时更新中奖结果
	setInterval(getPrizeResult,20000);
});
function rnd(n, m){
	return Math.floor(Math.random()*(m-n+1)+n)
}