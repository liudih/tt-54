$(document).ready(function() {	
	$('form').unbind();
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
	var rules = {
        curl:{
            required:true
        },
        itype:{
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
        denableenddate:{
       	 required:true,
       	 date:true
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
		if(form.denableenddate.value < form.denablestartdate.value){
			alert("end time Can not be less than start time");
			return false;
		}
		disableSaveBtn(true);
		if(form.id == "editeForm" && form.oldcurl.value == form.curl.value){
			return true;
		}
		$.get(jsThemeSku.controllers.manager.ThemeAction.validateUrl().url,{url:form.curl.value},function(data){
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
});
