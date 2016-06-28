$(document).ready(function() {	
	$('form').unbind();
	var rules = {
        ipageid:{
            required:true
        },
        crule:{
            required:true
        },
        ienable:{
            required:true
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
	var currntParam = null;
	$('select[name="crule"]').change(function(){
		$('#ruleparam').html('');
		currntParam = null;
		var param = $.parseJSON($(this).find($('option[value="'+$(this).val()+'"]')).attr('param'));
		if(param && param.length > 0){
			currntParam = param;
			param.sort(function(a,b){
				a.priority - b.priority;
			});
			var paramhtml='';
			for(var i=0;i<param.length;i++){
				var value = '';
				if($('input[name=cruleparam]').val()){
					var paramvalue = $.parseJSON($('input[name=cruleparam]').val());
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
				if(isboolean){
					paramhtml ='<div>'+param[i].desc+'</br><select paramname="'+param[i].name+'" name="_'+param[i].name+'"  class="'+validateclass+'"><option value="0" '+(value && value ==0 ? 'selected="selected"':'')+'>No</option><option value="1"  '+(value && value ==1 ? 'selected="selected"':'')+'>Yes</option></select></div>'
				}
				else{
					paramhtml ='<div>'+param[i].desc+'</br><input paramname="'+param[i].name+'" name="_'+param[i].name+'" value="'+value+'" class="'+validateclass+'"></div>'
				}
				$('#ruleparam').append(paramhtml);
				if(isdate){
					$('input[paramname="'+param[i].name+'"]').datetimepicker({
						minView: "month",
						maxView: "year",
						format : "yyyy-mm-dd",
						autoclose : true
					});
				}
			}
		}
	});
	var submithandler = function(form){
		disableSaveBtn(true);
		if(form.oldpage.value == form.ipageid.value){
			setrule();
			return true;
		}
		$.get(jsRoute.controllers.manager.luckdraw.PageRuleAction.validate().url,{ipageid:form.ipageid.value},function(data){
			if(data){
				alert(data);
				disableSaveBtn(false);
			}
			else{
				setrule();
				form.submit();
			}
		},'text');
		return false;		
		function setrule(){
			$('input[name="cruleparam"]').val('');
			if(currntParam && currntParam.length > 0){
				var param={};
				$('#ruleparam input').each(function(){
					param[$(this).attr('paramname')] = 	$(this).val();	
				});
				$('#ruleparam select').each(function(){
					param[$(this).attr('paramname')] = 	$(this).val();	
				});
				$('input[name="cruleparam"]').val(JSON.stringify(param));
			}
		}
	}
	$('#addform').validate({
		rules:rules,
		submitHandler:submithandler
	});		
	$("#add_btn").click(function(){
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.luckdraw.PageRuleAction.add().url,formtype:'add'});
		$('#myModalLabel').text('添加规则');
		$('#ruleparam').html('');
		currntParam = null;
	});	
	$(".edit").click(function() {
		$('#myModalLabel').text('修改规则');
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.luckdraw.PageRuleAction.save().url,formtype:'edite'});
		var href = $(this).attr("url");
		$.get(href, function(data) {
			if(data){
				data.page.oldpage=data.page.ipageid;
				$('#addform').form('load',data.page);
				$('select[name=crule]').change();
			}
		}, "json");
	});
});
