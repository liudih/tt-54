$(document).on("click","#remove_ThemeCssTemplate_id",function(){
	$('.delete_ThemeCssTemplate-manager').unbind();
	$('.delete_ThemeCssTemplate-manager').submit(function(){
		var form = $(this);
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if(data['dataMessages']==3){
				alert("Remove Success！");
				 var url = themeTemplateList.controllers.manager.ThemeCssTemplate.getList().url;
				 window.location = url;
			}else if(data['dataMessages']==4){
				alert("Reomove Failed！");
				return false;
			}else {
				return false;
			}
		});
	}); 

});
	
$(document).on("click","#updateThemeCssTemplateId",function(){
	$('.update-ThemeCssTemplate-manage').unbind();
	$('.update-ThemeCssTemplate-manage').submit(function(){
		$("#updateThemeCssTemplateId").disabled = true;
		var form = $(this);
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if(data['dataMessages']==5){
				alert("Change Success！");
				 var url = themeTemplateList.controllers.manager.ThemeCssTemplate.getList().url;
				 window.location = url;
			}else if(data['dataMessages']==6){
				alert("Change Failed！");
				return false;
			}else if(data['dataMessages']==7){
				alert("Cname Reiteration Error！");
				return false;
			}else {
				return false;
			}
		});
	}); 

});


$(document).ready(function(){			
	 $("#add-ThemeCss-manager").validate({
		 rules : {
			 cvalue : {
				 required : true
			  },
			  cname : {
				 required : true
			  }
			},
			messages : {
				 cvalue : {
					required : "Plaese enter cvalue."
				 },
				cname : {
					required : "Please enter cname."
				}			
			},
			errorPlacement: function(error, element) {
				$("#addthemetemplateId").attr('disabled', true);
		        var errorMsg=$(error).html();
		        $(element).next().html(errorMsg);
		     },
			success : function() {
				$("#addthemetemplateId").attr('disabled', false);
			},		
			submitHandler:function(form){
				$("#addthemetemplateId").disabled = true;
				var form = $("#add-ThemeCss-manager");
				var url = form.attr("action");
				var $this = this;
				$.post(url, form.serialize(), function(data) {		
					if(data['dataMessages'] == 1) {
						alert("Add Success！");
						 var url = themeTemplateList.controllers.manager.ThemeCssTemplate.getList().url;
						 window.location = url;
					} else if (data['dataMessages'] == 2) {
						alert("Add Failed！");
						return false;
					}else if(data['dataMessages']==7){
						$("#cname_id").next().html("Cname Reiteration Error！");
						return false;
					}else {
						return false;
					}
				}, "json");
			}
	    });
});