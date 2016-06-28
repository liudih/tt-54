define(['jquery'], function($){
	
	$(".brows-img").click(function(){
				var id=$("#tag-id").val();
				var email=$("#tag-email").val();
				var clistingid=$("#clistingid").val();
				var list = [];
			   	var map = {};
			   	map['id']=id;
			   	map['clistingid'] = clistingid;
		 		map['email'] = email;
		 		list[0] = map;
				$.ajax({
					url : "/review/productphotos/others",
					type : "get",
					data:{data: $.toJSON(list)},
					dataType : "json",
					success : function(data) {
						var $fc=$("#smallimg" +id).children(":first");
						var $sfc=$("#max_image" +id).children(":first");
						$("#smallimg" +id).empty();
						$("#max_image" +id).empty();
						$("#smallimg" +id).append($fc);
						$("#max_image" +id).append($sfc);
						$.each(data, function(i,item){
		        			var image = item.cimageurl;
						    $("#smallimg" +id).append("<li  class=\"productSmallImg\" > <img src="+image+" width=\"80px;\" height=\"50px;\" />  </li>");
		        			$("#max_image" +id).append("<li  class=\"productSmallImg\" > <img src="+image+" width=\"500px;\" height=\"300px;\" />  </li>");
		        		});
						$(".customerSmallmove").children("li").click(function()
						{
							var index = $(this).index();
							$(this).siblings("li").removeClass("cpActive");
							$(this).addClass("cpActive")
							$(this).parent().siblings().children(".customer_bigPic").children("li").css({"display":"none"})
							$(this).parent().siblings().children(".customer_bigPic").children("li").eq(index).css({"display":"block"})
						});
					}
				});
			});
	
	
	
	
	
	
	

	
});