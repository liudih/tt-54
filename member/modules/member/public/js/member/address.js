	//关闭并清空添加地址表单
	function closeAndResetAddressForm(){
		$(".edit_box").hide();
		$(".blackBK").css({"display":"none"});
		//$("form[name=add_new_address_form]")[0].reset();
		$("form[name=add_new_address_form]").each(function(){
			this.reset();
		});
		$("form[name=add_new_address_form]").find(".edit_error").remove();
		$("h3[name=flag_current_country]").removeClass();
		$("ul[name=province_list]").children().remove();
		$(".hidden_icountry_value").val("");
		$("h3[name=flag_current_country]").find("span[name=current_coutry]").text("please select");
		$("table[name=add_new_address]").find(".edit_INerror").removeClass("edit_INerror");
		$(".newAddressBox").css({"display":"none"});
	}

	//模糊查询国家
	$(document).on("keyup","input[name=search]",function(){
		var v=$(this).val().toLowerCase();
		var regstr = "^" + v + ".*";
		var reg = new RegExp(regstr,"i");
		
		var parent =  $(this).parent();
		var countryList = parent.siblings('.country_list');
		
		
		$("li",countryList).each(function(i){
			var text = $('span',this).text();
			if(reg.test(text)){
				$(this).show();
			}else{
				$(this).hide();
			}
		});
	});
	
	//光标定位到province位置时候的效果
	$(document).on("mouseover",".current_province",function(e){
		var provinceSize = $(this).nextAll("ul[name=province_list]").children("li").size();
		if( provinceSize != 0 && provinceSize != ""){
			$(this).siblings(".province_list").css({"display":"block"})
		}
	});
	
	$(document).on("mouseout",".country_all",function(){
		$(this).css("display","none");
	});
	$(document).on("mouseover",".country_all",function(){
		$(this).css("display","block");
	});
	
	$(document).on("mouseout",".province_list",function(){
		$(this).css("display","none");
	});
	
	$(document).on("mouseover",".province_list",function(e){
		var provinceSize = $("ul[name=province_list]").children().size();
		if( provinceSize != 0 && provinceSize != ""){
			$(this).css({"display":"none"});
		}
		$(this).css("display","block");
	});	
		
	//修改地址中的取消按钮
//	$(document).on("click","#address_cancel_button",function(){
//		$(this).parents(".edit_box").hide();
//		$(".blackBK").css({"display":"none"});
//		$(".newAddressBox").css({"display":"none"});
//		$(".bkBlack").remove();
//	});
	
	$(function(){
		$('[name=address_cancel_button]').click(function(){
			var box = $(this).parents(".edit_box");
			box.hide();
			$(".blackBK").css({"display":"none"});
			$(".newAddressBox").css({"display":"none"});
			$(".bkBlack").remove();
		});
	});
	//添加地址表单中的取消按钮
	$(document).on("click","input[name=add_cancel_button]",function(){
		closeAndResetAddressForm();
		$(".edit_box").css("display","none");
		
	});
	
	//地址添加弹框中的关闭按钮
	$(document).on("click","a[name=closeAddress_new]",function(){
		closeAndResetAddressForm();
	});

	//选择国家
	$(document).on("click","li[name=country_li]",function(){
		var id = $(this).parent().attr("tag");
		var countryCode = $(this).attr("countryCode");
		var countryText = $(this).find("span[name=country]").text();
		var countryValue = $(this).find("span[name=country]").attr("value");
		
		$(this).parents('.select_country').find('h3 span').text(countryText);
		var hiddenCountry = $(this).parents('.Countries_box').siblings('input[name=icountry]');
		hiddenCountry.val(countryValue);
		hiddenCountry.next(".edit_error").remove(); //当输入国家的时候，就把校验的错误消息删除
		var flagCountry = $(this).parents(".select_country").find('h3');
		flagCountry.removeClass();
		flagCountry.addClass("flag_" + countryCode);
		$(this).parents('form').find('.current_province').val('');
		//$("[id=input_current_province" + id + "]").val(""); //清空身份输入框中的值
		$(".current_province").siblings(".province_list").css({"display":"none"});
	
		
		
		 //根据选中国家，联动省
		 // 根据国家联动省的功能先注释
		 var countryValueJson = {countryValue:countryValue};
		 $.ajax({
				url : js_ShippingAddressRoutes.controllers.member.Address.getAllProvinceByCountryId().url, 
				type : "POST",
				data : JSON.stringify(countryValueJson),
				contentType : "application/json",
				success : function(provinceJson) {
					if(null != provinceJson){
						var provinceOption="";
						var provinceDate = provinceJson.provinces;
						$("li[name=province_li]").each(function(){
							 $(this).remove();
						});
						$.each(provinceDate, function(j,pitem){
							provinceOption = provinceOption + "<li name='province_li'><span name='province' value='"+ pitem.iid  +"'>"+ pitem.cname +"</span></li>";
						});
						$("ul[name=province_list]").each(function(){
							 $(this).append(provinceOption); 
						});
						$(document).on("click","li[name=province_li]",function(){
							var provinceText = $(this).find("span[name=province]").text();
							var cprovince = $(this).parent().siblings('input[name=cprovince]');
							cprovince.val(provinceText);
							cprovince.next(".edit_error").remove();
							cprovince.removeClass("edit_INerror");
							
							$(this).parent(".province_list").css({"display":"none"});
						});
					}
			 }
		 }); 
		 
	});
	
		
	//会员中心单独删除地址
	$(document).on("click","input[name=delete_m_address]",function(){
		var id = $(this).attr("tag");
		var data = {"id": id};
		$.ajax({
			url :  js_ShippingAddressRoutes.controllers.member.Address.deleteMemberAddressById().url,
			type : "POST",
			data : $.toJSON(data),
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode == 0){
					window.location.reload(); 
				} else if(data.errorCode == 1){
					//TODO  未定义错误返回界面
					pophtml("Error","required error");
				} else if(data.errorCode == 2){
					//TODO	未定义错误返回界面
					pophtml("Error","service error");
				}
			}
		});
	});
	
	//批量删除
	$(document).on("click","#remove_select_addresses",function(e){
		var ids = [];
		$("#addressUL_id").find(".afters").each(function(){
			var id = $(this).attr("value");
			ids.push(id); 
		});
		$.ajax({
			url :  js_ShippingAddressRoutes.controllers.member.Address.deleteMemberAddressBatchByIds().url,
			type : "POST",
			data : $.toJSON(ids),
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode == 0){
					window.location.reload(); 
				} else if(data.errorCode == 1){
					//TODO  未定义错误返回界面
					pophtml("Error","required error");
				} else if(data.errorCode == 2){
					//TODO	未定义错误返回界面
					pophtml("Error","service error");
				}
			}
		});
	});
		
	$(document).on("click",".defADD",function(){
		var id = $(this).attr("tag");
		
		if($(this).hasClass("defActi")){
			return false;
		}
		var data = {"id": id};
		$.ajax({
			url :  js_ShippingAddressRoutes.controllers.member.Address.setDefaultShippingaddress().url,
			type : "POST",
			data : $.toJSON(data),
			contentType : "application/json",
			success : function(data) {
				if(data.errorCode == 0){
					$(".defADD").removeClass("defActi");
					$("#default_" + id).addClass("defActi");
					$(".removeADD").removeClass("bannedClick");
					$("#default_" + id).nextAll(".removeADD").addClass("bannedClick");
					
					$("#addressUL_id").find("input[name=bdefault]").each(function(i,e){
						this.checked=false;
					});
					$("#checkbox_default" + id).attr("checked",true);
				} else if(data.errorCode == 1){
					//TODO  未定义错误返回界面
					pophtml("Error","required error");
				} else if(data.errorCode == 2){
					//TODO	未定义错误返回界面
					pophtml("Error","service error");
					
				}
			}
		});
		
		if($(this).siblings(".allThis").children("span").hasClass("afters")==false){
			$(this).addClass("defActi");
			$(this).parents("li").siblings("li").children(".defADD").removeClass("defActi");
		}
	});	
	
	//清除国家默认列表
	$(".newAddress,.deitADD").on("click",function(){
		deleteDefaultCountry();
	});
	function deleteDefaultCountry(){
		$(".address-form").find(".country_item").remove();
	}
	  
	