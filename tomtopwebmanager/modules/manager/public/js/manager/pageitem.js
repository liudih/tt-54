$(document).ready(function() {	
	$('form').unbind();
	var rules = {
        cvalue:{
            required:true
        },
        ipageid:{
            required:true            
        },
        cimgurl:{
        	required:true,
        	url:true
        },
        cimgtargeturl:{
        	required:true,
        	url:true
        },
        ipriority:{
        	required:true,
        	digits:true
        },
        'langs[0].cname':{
        	required:true
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
		disableSaveBtn(true);
		$.get(jsRoute.controllers.manager.PageItemAction.validate().url,{pageid:form.ipageid.value},function(data){
			if(data && data.page > 0){
				alert("Page has been enabled can not be modified, the first choice of other pages。");
				disableSaveBtn(false);
			}
			else{
				form.submit();
			}
		},'json');
		return false;
	}
	$('#addform').validate({
		rules:rules,
		submitHandler:submithandler
	});		
	$("#add_btn").click(function(){
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.PageItemAction.add().url,formtype:'add'});
		$('.langid').each(function(){
			$(this).val($(this).attr('langid'));
		});
	});	
	$(".edit").click(function() {
		$('#addform').form('clear').attr({'action':jsRoute.controllers.manager.PageItemAction.save().url,formtype:'edite'});
		$('.langid').each(function(){
			$(this).val($(this).attr('langid'));
		});
		var href = $(this).attr("url");
		$.get(href, function(data) {
			if(data){
				$('#addform').form('load',data.page);
				for(lang in data.langs){
					$('input[idlangid='+lang+']').val(data.langs[lang].iid);
					$('input[tilangid='+lang+']').val(data.langs[lang].cname);
				}
			}
		}, "json");
	});
});
