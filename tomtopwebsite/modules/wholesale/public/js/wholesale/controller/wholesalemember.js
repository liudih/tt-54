var wholesalemember_options = {
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
		},
		cskype : {
			required : true
		},
		fpurchaseamount : {
			required : true,
			number : true
		},
		icategroyIds : {
			required : true
		},
		agree : {
			required : true
		}
	},
	messages : {
		cfullname : {
			required : "Name name is required."
		},
		ctelephone : {
			required : "Phone Number is required."
		},
		cshipurl : {
			required : "Shop URL/Company's URL.etc is required.",
			url : "Please enter a valid URL."
		},
		ccountrysn : {
			required : "country name is required."
		},
		cskype : {
			required : "Skype/MSN is required."
		},
		fpurchaseamount : {
			required : "Expected Purchase Amount is required.",
			number : "Expected Purchase Amount is number."
		},
		icategroyIds : {
			required : "Wholesale Categories is required."
		},
		agree : {
			required : "Please agree this."
		}
	},
	highlight : function(error, element) {
		$(element).addClass("edit_INerror");
	},
	errorPlacement : function(error, element) {
		if (element.is(':radio') || element.is(':checkbox')) { // 如果是radio或checkbox
			$(element).addClass("edit_INerror");
			$(element).after(error);
		} else {
			$(element).addClass("edit_INerror");
			$(element).after(error);
		}
	},
	unhighlight : function(element, error, valid) {
		$(element).removeClass("edit_INerror");
		$(element).next('.edit_error').remove();
	},
	success : function() {
		$("#wholesale_submit").attr('disabled', false);
	}
};
$('#wholesaleform').validate(wholesalemember_options);