$(document).ready(function(){
	
	$("#tracking-form").validate({
		 rules : {
			  cemail : {
					required : true,
					email:true
				},
				cordernumber : {
					required : true
				}
			},
			messages : {
				cemail : {
					required : "Please enter your email.",
					email : "Please check your email format."
				},
				cordernumber : {
					required : "Please enter your order number."
				}
			},			
			highlight : function( element,error) {				
				$(element).addClass("edit_INerror");
				var $eleform=$(element).parents('.errLeft');
				$eleform.removeClass('rights');
				$eleform.addClass('error');
			},
			errorPlacement: function(error, element) {
				$(element).parent().next().show();
				$("#tracking_submit").attr('disabled', true);
				 var errorMsg=$(error).html();
		         $(element).parent().next().html(errorMsg);
		     },
		     unhighlight : function(element, error, valid) {
				$(element).removeClass("edit_INerror");
				var $eleform=$(element).parents('.errLeft');
				$eleform.removeClass('error');
				$eleform.addClass('rights');
			 },
			success : function() {
				$(".edit_error").hide();
				$("#tracking_submit").attr('disabled', false);
			},
			submitHandler:function(form){
				validateAndsubmit.call(this,form);
			}
	});

	$("#grand_total").text($(".total_fragment_total").text());
	$(".Continue,.cart_bottom").hide();
});

function validateAndsubmit(form){
	var url = order_enquiry.controllers.order.OrderEnquiry.orderTrackingCheck().url;
	var redUrl = order_enquiry.controllers.order.OrderEnquiry.orderTrackingQuery().url;
	var jsonData = $(form).serialize();
	$.post(url, jsonData, function(data) {		
		if(data['errorMessages'] == 1) {
			$("#email_message").show();
			$("#email_message").html("You enter the mailbox error。");
		}else if(data['errorMessages'] == 2) {
			$("#orderId_message").show();
			$("#orderId_message").html("The order number you entered is incorrect.");
		}else{
			var trackForm = document.getElementById("tracking-form");
			trackForm.action = redUrl;
			trackForm.submit();
		}
	}, "json");

}

