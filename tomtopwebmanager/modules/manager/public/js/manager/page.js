$(document).ready(function() {	
	$("#itype").change(function(){
		var selectValue=$(this).attr("value");
		if(selectValue==4){
			$("#activity_id").show();
		}else{
			$("#activity_id").hide();
		}
	});
	
	$("#add_save").click(function(){
		var itype = $("#itype").val();
		if(itype == 4){
			var templateVal=$("#itemplate_id").attr("value");
			if(templateVal==0){					
				$("#itemplate_id").next().next().html("This field is required.");
				return false;
			}	
		}
	});

	$('form').unbind();
	binddate();
	function binddate(){
		$('#denablestartdate').datetimepicker({
			minView: "month",
			maxView: "year",
			format : "yyyy-mm-dd",
			autoclose : true
		});
		
		$('#denableenddate').datetimepicker({
			minView: "month",
			maxView: "year",
			format : "yyyy-mm-dd",
			autoclose : true
		});
	}
	var rules = {
        curl:{
            required:true
        },
        icssid:{
            required:true
        },
        ienable:{
            required:true
        },
        'langs[0].ctitle':{
        	required:true
        },
        iwebsiteid:{
        	required:true
        },
        denablestartdate:{
       	 required:true,
       	 date:true
        },   
        itype:{
          	 required:true
        },               
         denableenddate:{
       	 required:true,
       	 date:true
        }                    
    }	
	var disableSaveBtn = function(status){
		if(status){
			$("#add_save").attr('disabled',"disabled");
		}
		else{
			$("#add_save").removeAttr('disabled');
		}
	}
	var submithandler = function(form){
		if(form.denableenddate.value < form.denablestartdate.value){
			alert("end time Can not be less than start time");
			return false;
		}
		
			
		disableSaveBtn(true);
		if($(form).attr('formtype') == "edite" && $(form.curl).attr('oldvalue') == form.curl.value){
			return true;
		}
		$.get(jsRoute.controllers.manager.PageAction.validate().url,{url:form.curl.value},function(data){
			if(data && data > 0){
				alert("URL already exists.");
				disableSaveBtn(false);
			}
			else{
				form.submit();
			}
		},'text');
		return false;
	}
	$('#addform').validate({
		rules:rules,
		submitHandler:submithandler
	});		
	$("#add_btn").click(function(){
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.PageAction.add().url,formtype:'add'});
		$('#addform input').removeAttr('readonly');
		$('#addform select').show();
		$('#addform .select-text').remove();
		binddate();
		$('.langid').each(function(){
			$(this).val($(this).attr('langid'));
		});
	});	
	var dateToYYYYMMDD = function(date){
		var month = date.getMonth()+1;
		month = month > 9 ? month:'0'+month;
		var day = date.getDate();
		day = day > 9 ? day:'0'+day;
		return date.getFullYear()+'-'+month+'-'+day;
	}
	$(".edit").click(function() {
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.PageAction.save().url,formtype:'edite'});
		$('.langid').each(function(){
			$(this).val($(this).attr('langid'));
		});
		
		var href = $(this).attr("url");
		$.get(href, function(data) {
			if(data){
				$("#myModalLabel").html("修改数据信息页面");
				if(data.page.itype==4){
					$("#activity_id").show();
					
				}else{
					$("#activity_id").hide();
				}
				data.page.denablestartdate = dateToYYYYMMDD(new Date(data.page.denablestartdate));
				data.page.denableenddate = dateToYYYYMMDD(new Date(data.page.denableenddate));
				$('input[name=curl]').attr('oldvalue',data.page.curl);
				$('#addform').form('load',data.page);
				
				for(lang in data.langs){
					$('input[idlangid='+lang+']').val(data.langs[lang].iid);
					$('input[tilangid='+lang+']').val(data.langs[lang].ctitle);
				}
				$('#addform .select-text').remove();
				if(data.page.ienable == 1){
					$('#addform input').attr('readonly','readonly');
					$('#addform select').each(function(){
						if(this.name != 'ienable'){
							$(this).hide();
							$(this).after('<input readonly="readonly" value="'+$(this).find('option[value="'+$(this).val()+'"]').text()+'" class="m-wrap medium span6 select-text">');
						}						
					});
					$('#denablestartdate').datetimepicker('remove');
					$('#denableenddate').datetimepicker('remove');
				}
				else{
					$('#addform input').removeAttr('readonly');
					$('#addform select').show();					
				}
			}
		}, "json");
	});
});
