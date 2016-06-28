$(function (){
	var rotateTimeOut = function (){
		$('#rotate').rotate({
			angle:0,
			animateTo:2160,
			duration:8000,
			callback:function (){
				alert('网络超时，请检查您的网络设置！');
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
				$('#text').css({"display":"block"})
				$('.alert').html(txt);
				bRotate = !bRotate;
			}
		})
	};
	$(".close").click(function(){
		$("#text").hide(500);
	})
	$('.pointer').click(function (){
		if(bRotate)return;
		var z=10;
		var item = rnd(0,z);
		for(var i=0; i<z; i++){
			if(i==item){
			rotateFn(i, 360/z/2+360/z*i, 'ORDER REFUND 100% ');
			break;
			}
		var bgH=$(document).height();		
		$(".bg").css("height",bgH);			
		}
		$(function(){
		var topW=$(window).height()/2-100;
		var leftW=$(window).width()/2-100;
		$(".wrap").css({"top":topW,"left":leftW});
	})
		console.log(item);
	});
});
function rnd(n, m){
	return Math.floor(Math.random()*(m-n+1)+n)
}