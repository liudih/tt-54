var promo_apply_url = checkout_loyalty.controllers.cart.LoyaltyController.applyPromo().url;
var coupon_apply_url = checkout_loyalty.controllers.cart.LoyaltyController.applyCoupon().url;
var coupon_get_rul = checkout_loyalty.controllers.cart.LoyaltyController.getUsableCoupon().url;
var perfer_current_rul = checkout_loyalty.controllers.cart.LoyaltyController.getAllCurrentPrefer().url;
var undo_currentprefer_rul = checkout_loyalty.controllers.cart.LoyaltyController.undoCurrentPrefer().url;
var point_get_url = checkout_loyalty.controllers.cart.LoyaltyController.getMyUsablePoint().url;

function toDecimal2(x) {    
	var f = parseFloat(x);
    f = Math.round(f * 100) / 100;    
    var s = f.toString();    
    var rs = s.indexOf('.');    
    if (rs < 0) {    
        rs = s.length;    
        s += '.';    
    }    
    while (s.length <= rs + 2) {    
        s += '0';    
    }    
    return s;    
}

$(function(){
	
	function applyCoupon(code){
		 $.ajax({ 
     		  type: 'GET',  
     	        data :{"code":code},
     	        url: coupon_apply_url,
     	        error: function () {
     	        },  
     	        success:function(data){
     	        	if(data.result=='success'){
     	        		window.location.reload();
		     	   		}else {
							$("#checkout_coupon_div .error_p").text(data.data.errorMessage).show();
						}
     	        	}
     	  })
	}
	
	function getUsablePoint(){
		var islogin = $("#islogin").val();
		if(islogin=="false"){
			return;
		}
		 $.ajax({ 
  		  type: 'GET',  
  	        url: point_get_url,
  	        error: function () {
  	        },  
  	        success:function(data){
					if(data.result=='success'){
						$("#checkout_point_div").show();
						var point=data.data;
						$("#cart_usablepoint").text(point);
			           };
  	        }
  	  })
	}
	
	function getUsableCoupon(){
		var islogin = $("#islogin").val();
		if(islogin=="false"){
			return;
		}
		 $.ajax({ 
     		  type: 'GET',  
     	        url: coupon_get_rul,
     	        error: function () {
     	           
     	        },  
     	        success:function(data){
					if(data.result=='success'){
						$("#checkout_coupon_div").show();
						var listdata=data.data;
						$("#coupon_input_show").hide();
			        	var listHtml='';  
			            for(var i=0 ; i < listdata.length ; i++){  
			                listHtml += "<li " +"code="+listdata[i].code+">"+
			                (listdata[i].value<1?listdata[i].value*100+"%":(listdata[i].unit=='JPY'?parseInt(listdata[i].value):(listdata[i].value).toFixed(2)))+" "+listdata[i].unit+
			                "</li>";
			            }  
			           
			            $("#checkout_coupon_apply").css("display","inline-block");
			        	$('#checkout_coupon_insert').html(listHtml);
			        	$("#checkout_coupon_insert li").click(function(){
			        		 	$(this).parents(".have_code_select").find(".current_list").hide();
			        	    	$("#checkout_coupon_code").text($(this).attr("code"));
			        	    });
			        	$("#checkout_coupon_apply").click(function(){
			        		var coupon = $("#checkout_coupon_code").text();
			        		if(!coupon){
			        			return;
			        		}
			           	 	applyCoupon(coupon);
			           });
			        }
     	        }
     	  })
	}
	
	function undoPrefer(){
		$('#checkout_undo_prefer').submit();
	}
	
	function undoPoint(){
   	 $('#checkout_undo_point').submit();
	}
	

	function applyPromo() {
		$.ajax({
			type : 'GET',
			data : {code:$('#checkout_promo_input').val()},
			url : promo_apply_url,
			error : function() {
			},
			success : function(data) {
				if (data.result == 'success') {
					window.location.reload();
				}else{
					$("#checkout_promo_div .error_p").text(data.data.errorMessage).show();
				}
			}
		})
	}
	
	function applyPoint(costpoint){
		var point_apply_url = checkout_loyalty.controllers.cart.LoyaltyController.applyPoints(costpoint).url;
		 $.ajax({ 
     		  type: 'POST',  
     	        url: point_apply_url,
     	        error: function () {
     	        },  
     	        success:function(data){
     	        	if(data.result=='success'){
     	        		window.location.reload();
	     	   		}else{
	     	   			$("#checkout_point_div .error_p").text(data.data.errorMessage).show();
	     	   		}
     	        }
     	  })
	}

    function getCurrentPrefer(){
		 $.ajax({ 
     		  type: 'GET',  
     	        url: perfer_current_rul,
     	        error: function () {
     	        },  
     	        success:function(data){
     	        	if(data.result=='success'){
     	        		var listdata=data.data;
     	        		var discounttotal = 0;
     	        		 for(var i=0 ; i < listdata.length ; i++){ 
     	        			var cell = listdata[i];
     	        			
 		     	        	var total = $("#cart_total").text();
 		     	        	var discont = cell.value;
 		     	        	total = total.replace(/,/g,'');
	 		     	  		//检查是否有小数点
	 		     	  		if(total.indexOf('.') == -1){
	 		     	  			total = parseInt(total);
	 		     	  			discont = parseInt(Math.round(discont));
	 		     	  			discounttotal += discont;
	 		     	  			total = total + discont;
	 		     	  			$("#cart_total").text(total);
	 		     	  			$("#discount_total").text(discounttotal);
	 		     	  		}else{
		 		     	  		total = parseFloat(total);
		 		     	  		discont = parseFloat(discont);
		 		     	  		discounttotal += discont;
		 		     	  		total = total + discont;
	 		     	  			//保留两位小数
		 		     	  		total = Math.round(total * 100) / 100;
		 		     	  		$("#discount_total").text(toDecimal2(discounttotal));
	 		     	  			$('#cart_total').text(toDecimal2(total));
	 		     	  		}
 	        				if(cell.preferType == 'coupon'){
     	        				$("#checkout_promo_div").hide();
     	        				$("#checkout_coupon_div").show();
     		     	        	$("#checkout_coupon_canel").show();
     		     	        	$("#checkout_coupon_apply").hide();
     		     	        	$("#checkout_coupon_code").text(cell.code);
     		     	        	$("#checkout_coupon_code").unbind();
     	        			 }
     	        			 else if(cell.preferType == 'promo'){
     	        				$("#checkout_coupon_div").hide();
     	        				$("#checkout_promo_apply").hide();
     		     	        	$("#checkout_promo_cancel").css("display","inline-block");
     		     	        	$("#checkout_promo_input").val(cell.code).attr("disabled","disabled");
     	        			 }
     	        			 else if(cell.preferType == 'point'){
     	        				$("#checkout_point_div").show();
      	        				$("#checkout_point_apply").hide();
      		     	        	$("#checkout_point_cancel").css("display","inline-block");
      		     	        	$("#checkout_point_input").val(cell.code).attr("disabled","disabled");
      		     	        	$("#checkout_point_div .points_num").hide();
      	        			 }
 			            }  
     	        		 if($('#checkout_point_div').is(':hidden')){
     	        			 getUsablePoint();
     	        		 }
     	        		
     	        		 if(!$("#checkout_promo_input").attr('disabled')&&$('#checkout_coupon_code').text()=='Coupon Code'){
     	        			getUsableCoupon();
     	        		 }
     	        	}else{
     	        		$("#checkout_promo_input").attr("disabled",false);
     	        		$("#checkout_point_input").attr("disabled",false);
     	        		getUsablePoint();
     	        		getUsableCoupon();
     	        	}
     	        }
     	  })
	}
    
    $("#checkout_promo_apply").click(function(){
    	var promo=$("#checkout_promo_input").val().trim();
    	if(!promo){
    		return;
    	}
    	applyPromo();
    });
    
    $("#checkout_promo_cancel").click(function(){
    	undoPrefer();
    })
    
    
    $("#checkout_coupon_canel").click(function(){
    	undoPrefer();
    });
    
    $("#checkout_point_apply").click(function(){
    	var islogin = $("#islogin").val();
		if(islogin=="false"){
			$(".blockPopup_box").show();
			return;
		}
    	var cost = $('#checkout_point_input').val();
    	if(!(cost&&cost>0)){
    		return;
    	}
    	applyPoint(cost);
    });
    
    $("#checkout_point_cancel").click(function(){
    	undoPoint();
    });
    
	getCurrentPrefer();
});
