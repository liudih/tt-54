
$.metadata.setType("attr", "validate");

$("a[name^='ThemeGroupsMap']").each(function(){
	$(this).click(function () {
		var url =$(this).attr('href');
		$('#edit-ThemeGroup-modal .modal-body').html(' ');
		$.get(url, '', function(data){  
	        $('#edit-ThemeGroup-modal .modal-body').html(data);
	    })  
	});
	
});

$("#edit-ThemeGroup-modal").on("hidden", function() {
	$(this).removeData("modal");
});


$(document).on("click","#remove_ThemeGroupTemplate_id",function(){
	$('.delete_ThemeGroupTemplate-manager').unbind();
	$('.delete_ThemeGroupTemplate-manager').submit(function(){
		var form = $(this);
		var url = form.attr("action");
		var $this = this;
		$.post(url, form.serialize(), function(data) {
			if(data['dataMessages']==3){
				alert("Remove Success！");
				 var url = themeGroupTemplateList.controllers.manager.ThemeGroupTemplate.getInitThemeGroups().url;
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



$(document).ready(function(){			
	 $("#update-ThemeGroupTemplate-manage").validate({
		 rules : {
			'languages[0].cname' :{
				  required : true
			  }, 
			  ithemeid : {
					 required : true
			  },
			  isort : {
				 required : true,
				 Wname : true
			  },
			  curl : {
					 required : true,
					 Wurl : true
			  }
			},
			messages : {
				'languages[0].cname' : {
						required : "Please enter cname."
				},	
				ithemeid : {
					required : "Please choose itemeid."
				},
				 isort : {
					required : "Please enter isort."
				},	
				curl : {
					required : "Please enter curl."
				}		
			},
			errorPlacement: function(error, element) {
				$("#updateThemeGroupTemplateId").attr('disabled', true);
		        var errorMsg=$(error).html();
		        $(element).next().html(errorMsg);
		     },
			success : function() {
				$("#updateThemeGroupTemplateId").attr('disabled', false);
			},		
			submitHandler:function(form){
				$("#updateThemeGroupTemplateId").disabled = true;
				var form = $("#update-ThemeGroupTemplate-manage");
				var url = form.attr("action");
				var $this = this;
				$.post(url, form.serialize(), function(data) {
					if(data['dataMessages']==5){
						alert("Change Success！");
						 var url = themeGroupTemplateList.controllers.manager.ThemeGroupTemplate.getInitThemeGroups().url;
						 window.location = url;
					}else if(data['dataMessages']==6){
						alert("Change Failed！");
						return false;
					} else if (data['dataMessages'] == 7) {
						$("#update_theme_id").html("The theme has been enabled.");
						return false;
					}else {
						return false;
					}
				}, "json");
			}
	    });
	 
		// 字母和数字的验证  
	 jQuery.validator.addMethod("Wname", function(value, element) {  
	     var chrnum =/^-?\d+$/;  
	     return this.optional(element) || (chrnum.test(value));  
	 }, "Can only enter numbers."); 
	 
		//验证网址的格式
	 jQuery.validator.addMethod("Wurl", function(value, element) {  
	     var chrnum =/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;  
	     return this.optional(element) || (chrnum.test(value));  
	 }, "Please check the URL format."); 
});



$(document).ready(function(){			
	 $("#add-ThemeGroup-manager").validate({
		 rules : {
			'languages[0].cname' :{
				  required : true
			  },
			  ithemeid : {
				 required : true
			 },
			  isort : {
				 required : true,
				 Wname : true
			  },
			  curl : {
					 required : true,
					 Wurl : true
			  }
			},
			messages : {
				'languages[0].cname' : {
						required : "Please enter cname."
				},	
				ithemeid : {
						required : "Please choose itemeid."
				},
				 isort : {
					required : "Please enter isort."
				},	
				curl : {
					required : "Please enter curl."
				}		
			},
			errorPlacement: function(error, element) {
				$("#addthemeGrouptemplateId").attr('disabled', true);
		        var errorMsg=$(error).html();
		        $(element).next().html(errorMsg);
		     },
			success : function() {
				$("#addthemeGrouptemplateId").attr('disabled', false);
			},		
			submitHandler:function(form){
				$("#addthemeGrouptemplateId").disabled = true;
				var form = $("#add-ThemeGroup-manager");
				var url = form.attr("action");
				var $this = this;
				$.post(url, form.serialize(), function(data) {		
					if(data['dataMessages'] == 1) {
						alert("Add Success！");
						 var url = themeGroupTemplateList.controllers.manager.ThemeGroupTemplate.getInitThemeGroups().url;
						 window.location = url;
					} else if (data['dataMessages'] == 2) {
						alert("Add Failed！");
						return false;
					} else if (data['dataMessages'] == 7) {
						$("#theme_id").html("The theme has been enabled.");
						return false;
					}else {
						return false;
					}
				}, "json");
			}
	    });
	 
	 
		// 字母和数字的验证  
	 jQuery.validator.addMethod("Wname", function(value, element) {  
	     var chrnum =/^-?\d+$/;  
	     return this.optional(element) || (chrnum.test(value));  
	 }, "Can only enter numbers."); 
	 
		//验证网址的格式
	 jQuery.validator.addMethod("Wurl", function(value, element) {  
	     var chrnum =/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;  
	     return this.optional(element) || (chrnum.test(value));  
	 }, "Please check the URL format."); 
});