$(function(){
	$(".btn.blue").unbind().bind("click",
		function showDialog(){
			var id=$(this).parent().attr("data-target");
			$("#"+id).show();
	});
	
})