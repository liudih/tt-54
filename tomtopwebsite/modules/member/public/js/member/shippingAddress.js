//选择收货地址
$(document).on("click","input[id^='address_radio']",function(){
	var id = $(this).val();
	var name = $(this).attr("name");
	if(name == "address_radio") {
		Listeners.fireListenner('addresschang',$(this).val());  //调用监听事件，监听单选按钮选择的是哪个地址
		Listeners.addListener('addresschang',function(id){
	   		$('input[name=hidden_radio_value]').val(id);
	   	});	
	}
	$("tr[id^='edit_tr']").removeClass("chooseThis").addClass("chooseOther");
	$("#edit_tr" + id).addClass("chooseThis");
});

//删除收货地址
$(document).on("click","a[name=delete_address]",function(){
	var id = $(this).attr("tag");
	var data = {"id": id};
	$.ajax({
		url : js_ShippingAddressRoutes.controllers.member.Address.deleteMemberAddressById().url,
		type : "POST",
		data : JSON.stringify(data),
		contentType : "application/json",
		success : function(data) {
			if(data.errorCode == 0){
				$("#edit_tr" + id).remove();
			} else if(data.errorCode == 1){
				//TODO  未定义错误返回界面
				alert("required error");
			} else if(data.errorCode == 2){
				//TODO	未定义错误返回界面
				alert("service error");
			}
		}
	});
});	

//设置默认收货地址
$(document).on("click","a[name=set_default_address]",function(){
	if($(this).attr("cannotclick") == "true") {
		return false;
	} else {
		var id = $(this).attr("tag");
		var val = $(this).attr("val");
		var memberemail = $(this).attr("email");
		var data = {"id": id};
		$.ajax({
			url : js_ShippingAddressRoutes.controllers.member.Address.setDefaultShippingaddress().url,
			type : "POST",
			data : JSON.stringify(data),
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode == 0){
					if(val == "shipping"){
						var tableNode = $("#shipping_address_table");
						tableNode.find("input[id^='address_radio']").each(function(i,e){
							this.checked = false;
						});
						$("#address_radio" + id)[0].checked = true;
						Listeners.fireListenner('addresschang',id);  //调用监听事件，监听单选按钮选择的是哪个地址
						Listeners.addListener('addresschang',function(id){
					   		$('input[name=hidden_radio_value]').val(id);
					   	});	
					} else {
						var tableNode = $("#billing_address_table");
						tableNode.find("input[name=order_address_radio]").each(function(i,e){
							this.checked = false;
						});
						$("#address_radio" + id)[0].checked = true;
					}
					
					tableNode.find("tr[id^='edit_tr']").removeClass().addClass("chooseOther");
					$("#edit_tr" + id).removeClass().addClass("chooseDefault").addClass("C_default").addClass("chooseThis");
					
					
					tableNode.find("td[id^='td_delete']").removeClass().addClass("chooseDelete");
					$("#td_delete"+ id).removeClass().addClass("chooseDef");
				
					tableNode.find("input[name=bdefault]").each(function(i,e){
						this.checked=false;
					});
					
					$("#checkbox_default" + id)[0].checked=true;
					tableNode.find("a[name=set_default_address]").attr("cannotclick","false"); 					//恢复其他的设置默认为可点
					$("#default"+ id).children("a[name=set_default_address]").attr("cannotclick","true");	 

				} else if(data.errorCode == 1){
					//TODO  未定义错误返回界面
					alert("required error");
				} 
			}
		});
	}
});