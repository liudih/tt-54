$(function(){
	getSwitchs();//获取开关状态
	$("#merchantProduct").click(function(){
		pushProductMerchants();
	});
	$("#getRefreshTokenCode").click(function(){
		$("#refreshTokenCodeDiv").hide();
		$("#updateRefreshTokenDiv").show();
	});
	$("#updateRefreshToken").click(function(){
		$("#refreshTokenCodeDiv").hide();
		updateRefreshToken();
		
	});
	$("#getRefreshTokenUrl").click(function(){
		getRefreshTokenUrl();
	});
	$("#pullProducts").click(function(){
		pullProducts();
	});
	$("#pushProducts").click(function(){
		pushProducts();
	});
	$("#autoMerchantProduct").click(function(){
		autoMerchantProduct();
	});
	$("#pullSwitch").click(function(){
		var pullSwitch=$("#pullSwitch").attr("value");
		var value=pullSwitch=="0"?"1":"0";
		checkSwitchChange("1",value);
	});
	$("#pushSwitch").click(function(){
		var pullSwitch=$("#pushSwitch").attr("value");
		var value=pullSwitch=="0"?"1":"0";
		checkSwitchChange("2",value);
	});
	
});
function getSwitchs(){
	$.ajax({
		type: "GET",
		//dataType:"xml",
		url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.getSwitchs().url,
		success:function(data){
			if($.trim(data)!=""){
				var temp=data.split("_");
				if(temp[0]=="0"){
					$("#pullSwitch").html("Turn On Pull Switch");
					$("#pullSwitch").attr("value","0");
				}else{
					$("#pullSwitch").html("Shut Down Pull Switch");
					$("#pullSwitch").attr("value","1");
				}
				if(temp[1]=="0"){
					$("#pushSwitch").html("Turn On Push Switch");
					$("#pushSwitch").attr("value","0");
				}else{
					$("#pushSwitch").html("Shut Down Push Switch");
					$("#pushSwitch").attr("value","1");
				}
			}else{
				alert("get switch fail"); 
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("error"); 
		}
	});
}
function checkSwitchChange( type, value){
		if($.trim(type)=="" || $.trim(value)==""){
			alert("opt switch fail");
			return ;
		}
		$.ajax({
			type: "GET",
			url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.checkSwitchManage(type,value).url,
			success:function(data){
				if("success"==data){
					if(type=="1"){
						if(value=="0"){
							$("#pullSwitch").html("Turn On Pull Switch");
							$("#pullSwitch").attr("value","0");
						}else{
							$("#pullSwitch").html("Shut Down Pull Switch");
							$("#pullSwitch").attr("value","1");
						}
					}else if(type=="2"){
						if(value=="0"){
							$("#pushSwitch").html("Turn On  Push Switch");
							$("#pushSwitch").attr("value","0");
						}else{
							$("#pushSwitch").html("Shut Down Push Switch");
							$("#pushSwitch").attr("value","1");
						}
					}
				}else{
					alert("fail"); 
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error"); 
			}
		});
}

function pushProductMerchants(){
	
	$.ajax({
		type: "GET",
		//dataType:"xml",
		url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.pushProductMerchants().url,
		success:function(data){
			alert(data); 
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("error"); 
		}
	});
}
function getRefreshTokenUrl(){
	$.ajax({
		type: "GET",
		//dataType:"xml",
		url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.getCodeForRefreshToken().url,
		success:function(data){
			if($.trim(data)!=""){
				
				$("#refreshTokenCode").show();
				$("#updateRefreshToken").show();
				$("#refreshTokenCodeDiv").show();
				$("#refreshTokenUrlDisplay").html(data);
				$("#refreshTokenUrl").attr("href",data);
				$("#refreshTokenUrl").html(data);
			}else{
				alert("get RefreshTokenUrl error");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("get RefreshTokenUrl error");
		}
	});
}
function updateRefreshToken(){
	var code=$("#refreshTokenCode").val();
	if($.trim(code)==""){
		alert("please enter refreshToken Code");
		return;
	}
	$.ajax({
		type: "GET",
		url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.addRefreshToken(code).url,
		success:function(data){
			alert(data); 
			location.reload();//刷新页面
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("update RefreshToken error"); 
		}
	});
}
function pushProducts(){
	$.ajax({
		type: "GET",
		//dataType:"xml",
		url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.updateRecord().url,
		success:function(data){
			alert(data); 
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			
		}
	});
}
function pullProducts(){
		$.ajax({
			type: "GET",
			//dataType:"xml",
			url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.addRecord().url,
			success:function(data){
				alert(data); 
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				
			}
		});
}

function autoMerchantProduct(){
	$.ajax({
		type: "GET",
		//dataType:"xml",
		url:js_GoogleFeeds.controllers.manager.google.category.GoogleFeeds.autoPushProductMerchants().url,
		success:function(data){
			alert(data); 
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("error"); 
		}
	});
}

$("#addF").click(function(){
	var country = $("#country").val();
	var clang = $("#clang").val();
	var currency = $("#currency").val();
	var languageid = $("#languageid").val();
	if(country==''){
		alert('请输入国家！');
		return false;
	}else if(clang==''){
		alert('请输入语言！');
		return false;
	}else if(currency==''){
		alert('请输入货币！');
		return false;
	}else if(languageid==''){
		alert('请选择语言！');
		return false;
	}else{
		return true;
	}
});
