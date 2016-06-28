$(function(){
	
	//文本框失去焦点 验证邮箱
	$(".inputTxt_s").blur(function(){
		var email =$(".inputTxt_s").val();
		var flag=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/.test(email);
		if(flag==false){
	 		var emailNotRightAlert = $("input[name='livechat_validate_eamil']").val();
			alert(emailNotRightAlert);
		}
	})
	$(".chooseQ_Submit").click(function(){
		var email = $('.inputTxt_s').val();
		var leaveContent = $('#customer_leave_content').val();
		if(leaveContent == ''){
	 		var contentEmptyAlert = $("input[name='livechat_validate_content']").val();
			alert(contentEmptyAlert);
			return false;
		}
    	var msg = {
			'cemail' : email,
			'ccontent' : leaveContent
		};
    		
		$.ajax({
		    url: '/livechat/leave/save',
		    data:msg,
		    type: 'POST',
		    success: function(data) {
		    	var leaveMsgHandleAlert = $("input[name='livechat_leavemessage_success_handle']").val();
		    	var statu = confirm(leaveMsgHandleAlert);
		        if(!statu){
		        	window.close();
		        }else{
		        	$('.inputTxt_s').val('');
		    		$('#customer_leave_content').val('');
		    		return false;
		        }
		    }
		});
	})
	
});