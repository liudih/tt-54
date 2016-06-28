 var dropship_options={
			ignore : "",
			focusInvalid : false,
			errorClass : "edit_error",
			errorElement : "label",
			rules : {
				cfullname : {
					required : true
				},
				ctelephone : {
					required : true
				},
				cshipurl : {
					required : true,
					url : true
				},
				ccountrysn : {
					required : true
				}
			},
			messages : {
				cfullname : {
					required : "full name is required."
				},
				ctelephone : {
					required : "phone number is required."
				},
				cshipurl : {
					required : "ship url is required.",
					url : "Please enter a valid URL."
				},
				ccountrysn : {
					required : "country name is required."
						
				}
			},
			highlight : function(error, element) {
				$(element).addClass("edit_INerror");
			
			},

			errorPlacement : function(error, element) {
				$(element).addClass("edit_INerror");
				$(element).after(error);
			},
			unhighlight : function(element, error, valid) {
				$(element).removeClass("edit_INerror");
				$(element).next('.edit_error').remove();
			},
			success : function() {
				$("#dropship_submit").attr('disabled', false);
			}
		};
 $('#dropshipform').validate(dropship_options);
