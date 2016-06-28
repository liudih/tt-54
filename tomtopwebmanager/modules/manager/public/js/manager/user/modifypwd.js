$(function(){
	
	function oldPwdValid(){
		var flag = true;
		var oldpassword=$("#oldpassword").val();
		$.ajax({
			async:false,
			type: "POST",
			url:js_ModifyPwdRoutes.controllers.manager.AdminUser.checkOldPwd(oldpassword).url,
			success:function(msg){
				if(msg.errorCode==0){
					$("#oldPwdTip").html("<font color='green' size='7'><i class='icon-ok'></i></font>");
					return flag;
				} 
				else{                                 
					$("#oldPwdTip").html("<font color='red' size='7'><i class='icon-remove'></i></font>");
					flag = false; 
				}
			},
			error: function(){
				flag = false;
			},
		});

		return flag;
	}
	
	function validPasswordOne(){
		var num=$("#newPwdOne").val().length;
		if(num < 6){
			$("#newPwdOneTip").html("<font color='red' size='2'>密码过短，请重新输入</font>");           
			return false;
		}
		else if(num > 18){
			$("#newPwdOneTip").html("<font color='red' size='2'>密码过长，请重新输入</font>");   
			return false;
		}
		else{
			$("#newPwdOneTip").html("<font color='green' size='7'><i class='icon-ok'></i></font>");   
			return true;
		}
	}
	
	function validPasswordTwo(){
		var tmp=$("#newPwdOne").val();
		var num=$("#newPwdTwo").val().length;
		if($("#newPwdTwo").val() != tmp){
			$("#newPwdTwoTip").html("<font color='red' size='2'>两次密码输入不一致</font>");   
			return false;
		}else{
			if(num >= 6 && num <= 18){
				$("#newPwdTwoTip").html("<font color='green' size='7'><i class='icon-ok'></i></font>");   
				return true;
			}else{
				$("#newPwdTwoTip").html("<font color='red' size='2'>重复密码输入错误</font>");   
				return false;
			}
		}
	}
	
	$("#oldpassword").blur(function(){
		$("#modify-btn-tip").empty();
		oldPwdValid();
	});
	$("#newPwdOne").blur(function(){
		$("#modify-btn-tip").empty();
		validPasswordOne();
	}) ;
	$("#newPwdTwo").blur(function(){
		$("#modify-btn-tip").empty();
		validPasswordTwo();
	});
	
	$("#modify-password-btn").click(function(){
		var oldPwd=$("#oldpassword").val();
		var newPwdOne=$("#newPwdOne").val();
		var newPwdTwo=$("#newPwdTwo").val();
		   
		if(oldPwdValid()&& validPasswordOne() && validPasswordTwo()){
			
			$.ajax({
				type: "POST",
				url:js_ModifyPwdRoutes.controllers.manager.AdminUser.modifyPwd(oldPwd,newPwdOne,newPwdTwo).url,
				success:function(data){   
					var msg = data.msg;
					if(data.errorCode==0){
						$("#modify-btn-tip").show().html("<font color='green' size='7'><i class='icon-ok'>"+msg+"</i></font>");
						$("#oldpassword").val("");
						$("#newPwdOne").val("");
						$("#newPwdTwo").val("");
						$("#oldPwdTip").empty();
						$("#newPwdOneTip").empty();
						$("#newPwdTwoTip").empty();
						$("#modify-btn-tip").delay(10000).hide(0);        
					}else{
						$("#modify-btn-tip").show().html("<font color='red' size='3'>"+msg+"</font>");
					}                                     
				}
			});
			
		}else{
			$("#modify-btn-tip").show().html("<font color='red' size='3'>请按照提示修改后再提交!</font>");
		}
	});                  
})