$(document).ready(function() {	
	$('form').unbind();
	var rules = {
        ipageid:{
            required:true
        },
        iruleid:{
            required:true
        },
        ctype:{
        	 required:true
        },        
        ienable:{
            required:true
        },
        cname:{
        	required:true
        },
        isort:{
        	required:true,
        	digits:true
        }
    }	
	jQuery.validator.addMethod("boolean", function(value, element) {   
	    var tel = /^(false|true|1|0)$/;
	    return this.optional(element) || (tel.test(value));
	}, "Please enter a boolean type value!");
	var disableSaveBtn = function(status){
		if(status){
			$("#add_save").attr('disabled',"disabled");
		}
		else{
			$("#add_save").removeAttr('disabled');
		}
	}
	var iruleid = null;
	$('#ipageid').change(function(){
		$('#iruleid').html('<option></option>');
		$.getJSON(jsRoute.controllers.manager.luckdraw.PagePrizeAction.getRuleListByPageid().url,{pageid:$(this).val()},function(data){
			if(data && data.length > 0){
				for(var i=0;i<data.length;i++){
					var opt = '';
					if(iruleid && iruleid==data[i].iid){
						opt += '<option value="'+data[i].iid+'" selected="selected" extraparam=\''+JSON.stringify(data[i].cextraparams)+'\'>'+data[i].crulename+'</option>';
					}
					else{
						opt += '<option value="'+data[i].iid+'"  extraparam=\''+JSON.stringify(data[i].cextraparams)+'\'>'+data[i].crulename+'</option>';
					}
				}
				$('#iruleid').append(opt);
			}
			$('#iruleid').change();
		});
	});
	$('#iruleid').change(function(){
		$('#ruleparam').html('');
		var extraparam = $.parseJSON($('#iruleid option[value="'+$(this).val()+'"]').attr('extraparam')||'');
		if(extraparam && extraparam.length > 0){
			setParam(extraparam,'extra');
		}
	});
	$('select[name="ctype"]').change(function(){
		$('#prizeparam').html('');
		var param = $.parseJSON($(this).find($('option[value="'+$(this).val()+'"]')).attr('param'));
		if(param && param.length > 0){
			setParam(param);
		}
	});
	function setParam(param,type){
		type= type || '';
		param.sort(function(a,b){
			a.priority - b.priority;
		});
		var paramhtml='';
		for(var i=0;i<param.length;i++){
			var value = '';
			var paramvalue = $('input[name='+(type ? 'cextraparam' :'ctypeparam')+']').val()
			if(paramvalue){
				var paramvalue = $.parseJSON(paramvalue);
				if(paramvalue){
					for(var name in paramvalue){
						if(name == param[i].name){
							value = paramvalue[name];
							break;
						}							
					}
				}
			}
			var validateclass = "required";
			var isdate = false;
			var isboolean = false;
			var iscouponrule = false;
			if(param[i].type == 'long' || param[i].type == 'int' || param[i].type == 'short' || param[i].type == 'java.lang.Short' || param[i].type == 'java.lang.Integer' || param[i].type == 'java.lang.Long'){
				validateclass+=' digits';
			}
			else if(param[i].type == 'float' || param[i].type == 'double' || param[i].type == 'java.lang.Float' || param[i].type == 'java.lang.Double'){
				validateclass+=' number';
			}
			else if(param[i].type == 'java.util.Date' || param[i].type == 'java.sql.Date'){
				validateclass+=' date';
				isdate=true;
			}
			else if(param[i].type == 'boolean' || param[i].type == 'java.lang.Boolean'){
				validateclass+=' boolean';
				isboolean = true;
			}
			else if(param[i].type == 'couponrule'){
				iscouponrule = true;
			}
			
			if(isboolean){
				paramhtml ='<div>'+param[i].desc+'</br><select '+type+'paramname="'+param[i].name+'" name="_'+param[i].name+'"  class="'+validateclass+'"><option value="0" '+(value && value ==0 ? 'selected="selected"':'')+'>No</option><option value="1"  '+(value && value ==1 ? 'selected="selected"':'')+'>Yes</option></select></div>'
			}
			else if(!type && iscouponrule){
				paramhtml = '<div>'+param[i].desc+'</br><select id="couponselect" '+type+'paramname="'+param[i].name+'" name="_'+param[i].name+'"  class="'+validateclass+'"></select></div>';
			}
			else{
				paramhtml ='<div>'+param[i].desc+'</br><input '+type+'paramname="'+param[i].name+'" name="_'+param[i].name+'" value="'+value+'" class="'+validateclass+'"></div>'
			}
			
			if(type){
				$('#ruleparam').append(paramhtml);
			}
			else{
				$('#prizeparam').append(paramhtml);
				if(iscouponrule){
					$.getJSON(jsRoute.controllers.manager.luckdraw.PagePrizeAction.getCouponRules().url,function(data){
						if(data && data.length > 0){
							for(var x=0;x<data.length;x++){
								$('#couponselect').append('<option value="'+data[x].iid+'" '+(value && value ==data[x].iid ? 'selected="selected"':'')+'>'+data[x].cname+'</option>');
							}
						}
					});
				}
			}			
			if(isdate){
				$('input['+type+'paramname="'+param[i].name+'"]').datetimepicker({
					minView: "month",
					maxView: "year",
					format : "yyyy-mm-dd",
					autoclose : true
				});
			}
		}
	}
	var submithandler = function(form){
		disableSaveBtn(true);
		$.get(jsRoute.controllers.manager.luckdraw.PagePrizeAction.validate().url,{ipageid:form.ipageid.value},function(data){
			if(data && data > 0 && !confirm('Page has been started, and if you want to add and modify rules ?')){
				disableSaveBtn(false);
			}
			else{
				$('input[name="ctypeparam"]').val('');
				$('input[name="cextraparam"]').val('');
				var typeparam={};
				var extraparam={};
				$('#prizeparam input').each(function(){
					typeparam[$(this).attr('paramname')] = 	$(this).val();	
				});
				$('#prizeparam select').each(function(){
					typeparam[$(this).attr('paramname')] = 	$(this).val();	
				});
				
				$('#ruleparam input').each(function(){
					extraparam[$(this).attr('extraparamname')] = $(this).val();	
				});
				$('#ruleparam select').each(function(){
					extraparam[$(this).attr('extraparamname')] = $(this).val();	
				});
				if(!isNull(typeparam)){
					$('input[name="ctypeparam"]').val(JSON.stringify(typeparam));
				}
				if(!isNull(extraparam)){
					$('input[name="cextraparam"]').val(JSON.stringify(extraparam));
				}
				form.submit();
				
				function isNull(obj){
					var isnull = true;
					for(var o in obj){
						isnull = false;
						break;
					}
					return isnull;
				}
			}
		},'text');
		return false;
	}
	var jqvalidate = $('#addform').validate({
		rules:rules,
		submitHandler:submithandler
	});		
	$("#add_btn").click(function(){
		iruleid = null;
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.luckdraw.PagePrizeAction.add().url,formtype:'add'});
		jqvalidate.resetForm();
		$('#iruleid').html('<option></option>');
		$('#myModalLabel').text('添加奖品');
		$('#ruleparam').html('');
		$('#prizeparam').html('');
	});	
	$(".edit").click(function() {
		iruleid=null;
		jqvalidate.resetForm();
		$('#myModalLabel').text('修改奖品');
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.luckdraw.PagePrizeAction.save().url,formtype:'edite'});
		var href = $(this).attr("url");
		$.get(href, function(data) {
			if(data){
				iruleid=data.page.iruleid;
				$('#addform').form('load',data.page);
				$('select[name=ctype]').change();
				$('#ipageid').change();
			}
		}, "json");
	});
});
