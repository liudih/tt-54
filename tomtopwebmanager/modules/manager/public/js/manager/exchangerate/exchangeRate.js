define([ 'jquery' ], function($) {
	$('a.editExchangeRate').click(function() {
		var url = $(this).attr('href');
		$.get(url, function(data) {
			var e = $('#edit-exchangeRate-modal').find('div.modal-body');
			e.html(data);
		}, 'html');
	});
});

$(document).ready(function(){			
			
	 $("#add-CurrencyRate-manager").validate({
		 rules : {
			  ccode : {
				  required : true
			  },
			 fexchangerate : {
				 required : true,
				 Wname: true
			  },
			  csymbol : {
				  required : true
			  }
			},
			messages : {
			    ccode : {
					required : "Plaese enter ccode."
			    },
				fexchangerate : {
					required : "Plaese enter fexchangerate."
				 },
				 csymbol : {
						required : "Plaese enter csymbol."
				 }		
			},
			errorPlacement: function(error, element) {
				$("#addcurrencyrateId").attr('disabled', true);
		        var errorMsg=$(error).html();
		        $(element).next().html(errorMsg);
		     },
			success : function() {
				$("#addcurrencyrateId").attr('disabled', false);
			},		
			submitHandler:function(form){
				var form = $("#add-CurrencyRate-manager");
				var url = form.attr("action");
				var $this = this;
				$.post(url, form.serialize(), function(data) {		
					if(data['dataMessages'] == 1) {
						alert("Add Currency Sucess！");
						 var url = ExchangeRateInit.controllers.manager.ExchangeRate.index().url;
						 window.location = url;
					} else if (data['dataMessages'] == 2) {
						alert("Add Currency Failed！");
					}else if (data['dataMessages'] == 3) {
						alert("Can't add the same Code.");
					}else {
						return false;
					}
				}, "json");
			}
	    });
	 
	 
		// 数字格式验证  
	 jQuery.validator.addMethod("Wname", function(value, element) {  
	     var chrnum =/(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))/;  
	     return this.optional(element) || (chrnum.test(value));  
	 }, "Can only enter numbers."); 
		
});
