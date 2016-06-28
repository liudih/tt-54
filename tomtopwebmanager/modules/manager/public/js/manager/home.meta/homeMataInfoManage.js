
$(document).on("click","#remove_seo_id",function(){
	$('.delete_seo-manager').unbind();
	$('.delete_seo-manager').submit(function(){
		var form = $(this);
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if(data['dataMessages']==3){
				alert("Remove sucess！");
				 var url = seoList.controllers.manager.Seo.getSeoList().url;
				 window.location = url;
			}else if(data['dataMessages']==4){
				alert("Reomove failed！");
				return false;
			}else {
				return false;
			}
		});
	}); 

});
	
$(document).ready(function(){			
	 $("#seo-manager").validate({
		 rules : {
			 ctitle : {
				 required : true
			  },
			  ckeywords : {
				  required : true
			  },
			  cdescription : {
				  required : true
			  }
			},
			messages : {
				 ctitle : {
					required : "Plaese enter title."
				 },
				ckeywords : {
					required : "Please enter keywords."
				},
				cdescription : {
					required : "Please enter notes."
				}				
			},
			errorPlacement: function(error, element) {
				$("#seoId").attr('disabled', true);
		        var errorMsg=$(error).html();
		        $(element).next().html(errorMsg);
		     },
			success : function() {
				$("#seoId").attr('disabled', false);
			},		
			submitHandler:function(form){
				var form = $("#seo-manager");
				var url = form.attr("action");
				var $this = this;
				$.post(url, form.serialize(), function(data) {		
					if(data['dataMessages'] == 1) {
						alert("Add sucess！");
						 var url = seoList.controllers.manager.Seo.getSeoList().url;
						 window.location = url;
					} else if (data['dataMessages'] == 2) {
						alert("Add failed！");
					}else {
						return false;
					}
				}, "json");
			}
	    });
});