function initTree() {
	$(".chooseDefault").parents("li").show();
	$(".chooseDefault").parents("li").siblings().show();
	
	//$(".chooseDefault").parent().parentsUntil("li .root").show('fast');
	$('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');
    $('.tree li.parent_li > span').on('click', function (e) {
    	$("#categeroytree").find('span').attr('style', '');
    	if (!$(this).hasClass('choose')) {
    	  	$(this).attr('style', 'background-color:#eee;border:1px solid #999;padding:0 8px 5px');
    	}
        var children = $(this).parent('li.parent_li').find(' > ul > li');
        if (children.is(":visible")) {
            children.hide('fast');
            $(this).attr('title', 'Expand this branch').find(' > span > i').addClass('icon-plus-sign').removeClass('icon-minus-sign');
            if (!$(this).hasClass('choose')) {
           		$(this).attr('style', 'background-color:none;border:none');
            }
        } else {
            children.show('fast');
            $(this).attr('title', 'Collapse this branch').find(' > span > i').addClass('icon-minus-sign').removeClass('icon-plus-sign');   
        }
        e.stopPropagation();
    });
    $(".chooseDefault").click(function(){
    	if ($(this).hasClass('uncancle')) {
    		return false;
    	} else {
    		$(this).parent().removeClass('choose');
    	}
    });
}
$(document).ready(function(){
	initTree();
});