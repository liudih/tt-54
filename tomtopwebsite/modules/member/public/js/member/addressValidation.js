var html = '<label id="{{ id }}-error" class="edit_error" for="{{ id }}">{{ msg }}</label>';

var options={
		submitHandler : function(form) {
			submitform.call(this,form)
		},
		ignore : "",
		focusInvalid : false,
		errorClass : "edit_error",
		errorElement : "label",

		rules : {
			cfirstname : {
				required : true,
				maxlength : 20
			},
			clastname: {
				required : true
			},
			cstreetaddress : {
				required : true
			},
			icountry : {
				required : true
			},
			cprovince : {
				required : true
			},
			ccity : {
				required : true
			},
			cpostalcode : {
				required : true,
			},
			ctelephone : {
				required : true,
				maxlength : 20
			}
		},

		messages : {
			cfirstname : {
				required : "First Name is required.",
				maxlength : "No more than {0} characters."
			},
			clastname : {
				required : "Last Name is required.",
			},
			cstreetaddress : {
				required : "Street is required.",
			},
			icountry : {
				required : "Country is required.",
			},
			cprovince : {
				required : "State/Province/Region is required.",
			},
			ccity : {
				required : "City is required.",
			},
			cpostalCode : {
				required : "Zip/Postal code is required.",
			},
			ctelephone : {
				required : "Phone number is required.",
				maxlength : "No more than {0} characters."
			}
		},

		onkeyup : function(element, event) {
			if (event.which === 9 && this.elementValue(element) === '') {
				return;
			} else if (element.name in this.submitted
					|| element === this.lastActive) {
				this.element(element);
			}
		},

		highlight : function(error, element) {
			$(element).addClass("edit_INerror");
		},

		errorPlacement : function(error, element) {
			$(element).addClass("edit_INerror");
			if($(element).attr("name") == "cfirstname"){
				if($(element).next().next().attr("for") != undefined ){
					$(element).nextAll(".edit_error").remove();
					$(element).next().after("<label id='both-name-error' class='edit_error' for='both-name'>First Name and Last Name are required.</label>");
				} else {
					$(element).next().after(error);
				}
			} else if($(element).attr("name") == "clastname"){
				if($(element).next().attr("for") != undefined){
					$(element).nextAll(".edit_error").remove();
					$(element).after("<label id='both-name-error' class='edit_error' for='both-name'>First Name and Last Name are required.</label>");
				} else{
					$(element).after(error);
				}
			} else {
				$(element).after(error);
			}
		},

		unhighlight : function(element, error, valid) {
			$(element).removeClass("edit_INerror");
			if($(element).attr("name") == "cfirstname" && $(element).next().next().attr("for") == "both-name") {
				$(element).nextAll('.edit_error').remove();
				$(element).next().after("<label id='clastname-error' class='edit_error' for='clastname'>Last Name is required.</label>");
			} else 	if($(element).attr("name") == "clastname" && $(element).next().attr("for") == "both-name") {
				$(element).nextAll(".edit_error").remove();
				$(element).after("<label id='cfirstname-error' class='edit_error' for='cfirstname'>First Name is required.</label>");
			} else {
				$(element).nextAll('.edit_error').remove();
			}
		},

		success : function() {
			// 所有校验通过会调用
		},
	};

var _fn=$.fn   //解决闭包匿名函数访问不到方法的问题，先将其保存，然后再用$.fn继承保存的这个对象

function shippingAddressSubmitSuccess($form, data) {
	if (data) {
		$.fn.extend(_fn)
		var $newCloumn=$(data.addressHtml);
		$newform=$newCloumn.find('.address-form');
		$newform.validate(options);
		
		var $newOrderCloumn=$(data.orderAddressHtml);
		if($newOrderCloumn != null || $newOrderCloumn != undefined) {
			$newOrderform=$newOrderCloumn.find('.address-form');
			$newOrderform.validate(options);
		}
		var addressType = data.addressType;
		var isdefault = data.isdefault;
		
		//添加地址成功
		if(data.errorCode == 0 && data.actionType=="add"){
			var addId = data.addedId;
			if(addressType == "shipping"){
				$("#new_address_tr").before($newCloumn); //添加收货地址
				newShippingColumnStyle(addId,isdefault);
				closePopTable();
				resetTableForm();
			} else if(addressType == "billing"){
				$("#new_order_address_tr").before($newOrderCloumn); //添加账单地址
				newBillingColumnStyle(addId,isdefault);
				closePopTable();
				resetTableForm();
			} else {
				$("#new_address_tr").before($newCloumn); //添加收货地址
				$("#new_order_address_tr").before($newOrderCloumn);//添加账单地址
				var orderId = data.orderAddressId;
				newShippingColumnStyle(addId,isdefault);
				newBillingColumnStyle(orderId,isdefault,addressType);
				closePopTable();
				resetTableForm();
			}
		}else if (data.errorCode == 0 && data.actionType=="modify") {
			var modifyId = data.modifyId;
			var node = $("#edit_tr" + modifyId).next();
			node.prev().remove();    
			node.before($newCloumn);
			if(addressType == "shipping"){
				newShippingColumnStyle(modifyId,isdefault);			
			} else if(addressType == "billing") {
				newBillingColumnStyle(modifyId,isdefault);
			}
			closePopTable();
		}else if (data.errorCode == 1) {
			var required = data.required;
			for ( var i in required) {
				var input = $form.find('input[name=' + i + ']');
				var msg = html.replace('{{ id }}', i).replace('{{ msg }}',required[i]);
				$(input).after($(msg));
			}
		} else if(data.errorCode == 2){
			//TODO 服务层保存出错页面
			alert("save error");
		} 
	}
}

function newShippingColumnStyle(addId,isdefault){
	var tableNode = $("#shipping_address_table");
	tableNode.find("input[id^='address_radio']").each(function(i,e){
		this.checked = false;
	});
	$("#address_radio" + addId)[0].checked = true;
	Listeners.fireListenner('addresschang',addId);  //调用监听事件，监听单选按钮选择的是哪个地址
	Listeners.addListener('addresschang',function(addId){
   		$('input[name=hidden_radio_value]').val(addId);
   	});	
	changeClassStyle(tableNode,addId,isdefault);
}

function newBillingColumnStyle(addId,isdefault,addressType){
	var tableNode = $("#billing_address_table");
	tableNode.find("input[name=order_address_radio]").each(function(i,e){
		this.checked = false;
	});
	$("#address_radio" + addId)[0].checked = true;
	changeClassStyle(tableNode,addId,isdefault);
	if(addressType == "both"){
		changeCheckboxStyle(tableNode,addId);
		changeTableTrStyle(tableNode,addId);
		changeDeleteStyle(tableNode,addId);
	}
}

function changeClassStyle(tableNode,addId,isdefault){
	if(isdefault == true){
		changeDefaultStyle(tableNode,addId);
		changeDeleteStyle(tableNode,addId);
		changeCheckboxStyle(tableNode,addId);
		changeTableTrStyle(tableNode,addId);
	} 
}

function closePopTable(){
	//成功添加后关闭隐藏编辑框
	$(".edit_box").hide();
	$(".blackBK").css({"display":"none"});
}

function resetTableForm(){
	//清空表单
	$('#add_new_address_form')[0].reset();
	var h3 = $('#add_new_address_form').find("h3[name=flag_current_country]");
	h3.removeClass();
	h3.find("span[name=current_coutry]").text("please select");
}

function changeDefaultStyle(tableNode,id){
	tableNode.find("a[name=set_default_address]").attr("cannotclick","false"); 					//恢复其他的设置默认为可点
	$("#default"+ id).children("a[name=set_default_address]").attr("cannotclick","true");	 
}

function changeDeleteStyle(tableNode,id){
	tableNode.find("td[id^='td_delete']").removeClass().addClass("chooseDelete");
	$("#td_delete"+ id).removeClass().addClass("chooseDef");
}

function changeTableTrStyle(tableNode,id){
	tableNode.find("tr[id^='edit_tr']").removeClass().addClass("chooseOther");
	$("#edit_tr" + id).removeClass().addClass("chooseDefault").addClass("C_default").addClass("chooseThis");
}

function changeCheckboxStyle(tableNode,id){
	tableNode.find("input[name=bdefault]").each(function(i,e){
		this.checked=false;
	});
	$("#checkbox_default" + id)[0].checked=true;
}

function submitform(form){
	var self=this;
	var url =$(form).attr("action");
	var value = $(form).find("input[name=address_save_submit]").val();
	var $submit=$(form).find("input[name=address_save_submit]");
	$submit.attr("disabled",true);
	var eb = $('.edit_box').length;
	$.ajax({
        url: url,
        type: "POST",
        data: $(form).serialize(),
        error: function(){
        	$submit.attr("disabled",false);
        },
        success: function(data){
        	var url= $(form).attr("action");
        	if(eb && 'modify' == data.actionType && ('shipping' == data.addressType || 'billing' == data.addressType) && 0 == data.errorCode){
        		var datas = {};
        		$('input[name]',form).each(function(){
        			var name = $(this).attr('name');
        			var val = $(this).val();
        			datas[name] = val;
        		});
        		$('textarea[name]',form).each(function(){
        			var name = $(this).attr('name');
        			var val = $(this).val();
        			datas[name] = val;
        		});
        		$('#name' + datas.iid).empty();
        		$('#name' + datas.iid).append((datas.cfirstname + '&nbsp;' + datas.clastname));
        		
        		var coutry = $('#current_coutry_span' + datas.iid).text();
        		var text = [];
        		text.push(datas.cstreetaddress);
        		text.push('&nbsp;');
        		text.push(datas.ccity);
        		text.push('&nbsp;');
        		text.push(datas.cprovince);
        		text.push('&nbsp;');
        		text.push(coutry);
        		text.push('&nbsp;');
        		text.push(datas.cpostalcode);
        		text.push('&nbsp;');
        		text.push(datas.ctelephone);
        		
        		$('#address_info_' + datas.iid).empty();
        		$('#address_info_' + datas.iid).append((text.join('')));
        		
        		$('#address_cancel_button').trigger('click');
        		$('.edit_box').hide();
        		$('.blackBK').hide();
        		
        	}else if('add' == data.actionType){
        		var datas = {};
        		$('input[name]',form).each(function(){
        			var name = $(this).attr('name');
        			var val = $(this).val();
        			datas[name] = val;
        		});
        		
        		$('textarea[name]',form).each(function(){
        			var name = $(this).attr('name');
        			var val = $(this).val();
        			datas[name] = val;
        		});
        		
        		var tr = $('#shipping_address_table').find('tr:eq(0)').clone(true,true);
        		var oldId = $('input[name=iid]',tr).val();
        		var newId = data.iid;
        		
        		$(tr).attr('id','edit_tr' + newId)
        		$('[id$=' + oldId +']',tr).each(function(){
        			var v = $(this).attr('id').replace(oldId,newId);
        			$(this).attr('id',v);
        		});
        		
        		$('input',tr).each(function(){
        			var ele = $(this);
        			$.each(this.attributes, function() {
        			    if(this.specified) {
        			    	if(this.value.indexOf(oldId) != -1){
        			    		var v = this.value.replace(oldId,newId);
        			    		ele.attr(this.name,v);
        			    	}
        			    }
        			  });
        		});
        		
        		$('#name' + newId,tr).empty();
        		$('#name' + newId,tr).append((datas.cfirstname + '&nbsp;' + datas.clastname));
        		var coutry = $('span[name=current_coutry]',form).text();
        		var text = [];
        		text.push(datas.cstreetaddress);
        		text.push('&nbsp;');
        		text.push(datas.ccity);
        		text.push('&nbsp;');
        		text.push(datas.cprovince);
        		text.push('&nbsp;');
        		text.push(coutry);
        		text.push('&nbsp;');
        		text.push(datas.cpostalcode);
        		text.push('&nbsp;');
        		text.push(datas.ctelephone);
        		
        		$('form input[name][type=text]',tr).each(function(){
        			var name = $(this).attr('name');
        			if('iid' != name){
        				$(this).val(datas[name]);
        			}
        		});
        		
        		$('form textarea',tr).each(function(){
        			var name = $(this).attr('name');
        			$(this).val(datas[name]);
        		});
        		
        		$('[tag]',tr).each(function(){
        			var tag = $(this).attr('tag');
        			if(tag.indexOf(oldId) != -1){
			    		var v = tag.replace(oldId,newId);
			    		$(this).attr('tag',v);
			    	}
        		});
        		
        		$('input[name=cprovince]').val(datas.cprovince);
        		$('a[name=delete_address]').attr('tag',newId);
        		$('#address_info_' + newId,tr).empty();
        		$('#address_info_' + newId,tr).append((text.join('')));
        		
        		$(tr).show();
        		if('shipping' == data.addressType){
        			$('#new_address_tr').before(tr);
        		}else if('billing' == data.addressType){
        			$('#new_order_address_tr').before(tr);
        		}
        		
        		 
        		$('#address-form-' + newId).find('li span[value=' + datas.icountry + ']').parent().trigger('click');
        		 
        		$('[name=add_cancel_button]',form).trigger('click');
         		
         		
        	}else if(url.indexOf("addnewshippingaddress") != -1 || url.indexOf("modifyshippingaddress") != -1) {
        		//在订单页面修改或者添加成功
        		shippingAddressSubmitSuccess.call(self,$(form),data);
        	} else {
        		//在会员中心修改或者添加成功
        		if(data.errorCode == 0){
        			window.location.reload();          //因为要实现分页，所以刷新界面
        		} else if(data.errorCode == 1) {
        			var required = data.required;
        			for ( var i in required) {
        				var input = $(form).find('input[name=' + i + ']');
        				var msg = html.replace('{{ id }}', i).replace('{{ msg }}',required[i]);
        				$(input).after($(msg));
        			}
        		} else if(data.errorCode == 2){
        			alert("save error");
        		}
        	}
        	$submit.attr("disabled",false);
        }
    });
}

//表单校验
$('.address-form').each(function(){
	$(this).validate(options);
});