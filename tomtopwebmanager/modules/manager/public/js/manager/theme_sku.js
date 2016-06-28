$(document).ready(function() {	
	$('form').unbind();
	var rules = {
        ithemeid:{
            required:true
        },
        igroupid:{
        	 required:true
        },
        csku:{
            required:true
        },
        isort:{
        	required:true,
        	digits:true
        }
    }	
	var disableSaveBtn = function(status){
		if(status){
			$("#add_theme_save").attr('disabled',"disabled");
			$("#edit_theme_save").attr('disabled',"disabled");
		}
		else{
			$("#add_theme_save").removeAttr('disabled');
			$("#edit_theme_save").removeAttr('disabled');
		}
	}
	var submithandler = function(form){
		disableSaveBtn(true);
		$.getJSON(jsThemeSku.controllers.manager.ThemeSkuAction.validate().url,{sku:form.csku.value,websiteid:form.iwebsiteid.value,themeid:form.ithemeid.value},function(data){
			if(data && data.sku > 0 && data.enable <= 0){
				form.submit();
			}
			else{
				var msg='';
				if(data.sku <= 0){
					msg += "SKU does not exist or is not available for sale. "
				}
				if(data.enable > 0){
					msg += "The theme has been enabled, you can't add SKU."
				}
				alert(msg);
				disableSaveBtn(false);
			}
		});
		return false;
	}
	$('#addform').validate({
		rules:rules,
		submitHandler:submithandler
	});	
	$('#editeForm').validate({
		rules:rules,
		submitHandler:submithandler
	});	
	$("a.edit").click(function() {
		var id = $(this).attr("data-target");
		var href = $(this).attr("href");
		var body = $(id).find("div.modal-body");
		body.html("");
		$.get(href, function(html) {
			body.html(html);
		}, "html");
	});
	bindEvent();
});
function bindEvent(){
	$(".themeurl").change(function(){
		$(".groupname").html('');
		var tager = this;
		var ithemeid = $(tager).val();
		if(!ithemeid){
			return;
		}
		else{
			$.ajax({
				type:jsThemeSku.controllers.manager.ThemeSkuAction.getThemeGroup().type,
				url:jsThemeSku.controllers.manager.ThemeSkuAction.getThemeGroup().url,
				data:{ithemeid:ithemeid},
				dataType:'json',
				error:function(){
				},
				success:function(data){
					if(data && data.length > 0){
						$(tager).siblings("input[name=ithemeid]").val(data[0].ithemeid);
						var groupselects = '';
						for(var i=0;i<data.length;i++){
							groupselects += '<option value="'+data[i].iid+'">'+data[i].cname+'</option>'			
						}	
						$(".groupname").html(groupselects);
					}
				}
			});
		}		
	});
}
