
var coupon_apply_url = cartRoutes.controllers.cart.LoyaltyController.applyCoupon().url;
var coupon_get_rul = cartRoutes.controllers.cart.LoyaltyController.getUsableCoupon().url;
var perfer_current_rul = cartRoutes.controllers.cart.LoyaltyController.getAllCurrentPrefer().url;
var undo_currentprefer_rul = cartRoutes.controllers.cart.LoyaltyController.undoCurrentPrefer().url;

$(function(){
	function toDecimal2(x) {    
        var f = parseFloat(x);    
        f = Math.round(f*100)/100;    
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
	
	function drawCouponPrefer(data){
		var preferhtml="<dd id='dd_cart_loyalty'>Coupon:<span>" +$("#cart_subtotal_symbol").text() + " " + data + "</span></dd>";
     	$("#dd_cart_loyalty").remove();
     	$("#cart_loyalty").before(preferhtml);
	}
	
	function undoDrawCouponPrefer(){
     	$("#undo_coupon").submit();
	}
	
	function applyCoupon(code){
		 $.ajax({ 
     		    type: 'GET',  
     	        data :{"code":code},
     	        url: coupon_apply_url,
     	        error: function () {
     	        },  
     	        success:function(data){
     	        	if(data.result == 'success'){
	     	        	$("#coupon_button_apply").css("display","none");
	     	        	$("#coupon_button_cancel").css("display","inline-block");
	     	        	$("#coupon_code_select").attr("disabled","disabled");
	     	        	var current_total = $("#cart_total").text() || '';
	     	        	current_total = current_total.replace(/,/g,'');
	     	        	var preferValue = data.data.value || 0;
	     	        	var grandTotal;
	     	        	//检查是否有小数点
		     	   	if(current_total.indexOf('.') == -1){
		     	   			current_total = parseInt(current_total);
		     	   			preferValue = parseInt(preferValue);
		     	   			grandTotal = current_total + preferValue;
		     	   		}else{
		     	   			current_total = parseFloat(current_total);
		     	   			preferValue = toDecimal2(preferValue);
		     	   			grandTotal = (current_total - 0) + (preferValue - 0);
		     	   			//保留两位小数
		     	   			grandTotal = toDecimal2(grandTotal);
		     	   		}
	     	        	
	     	        	$("#cart_total").text(grandTotal);
	     	        	drawCouponPrefer(preferValue);
	     	        	// 将promo隐藏
	     	        	$("#div_promo").css("display","none");
     	        	}
     	        }
     	  })
	}
	
	function getUsableCoupon(){
		 $.ajax({ 
     		    type: 'GET',  
     	        url: coupon_get_rul,
     	        error: function () {
     	        },  
     	        success:function(data){
					if(data.result=='success'){
						var listdata=data.data;
						$("#coupon_input_show").hide();
			        	var listHtml='';  
			            for(var i=0 ; i < listdata.length ; i++){  
			                listHtml += "<option value = " + listdata[i].code +">"+
			                listdata[i].code +" "+(listdata[i].value<1?listdata[i].value*100+"%":(listdata[i].unit=='JPY'?parseInt(listdata[i].value):(listdata[i].value).toFixed(2)))+" "+listdata[i].unit+
			                "</option>";
			            }  
			            var dataHead='<select id="coupon_code_select">';
			            dataHead+=listHtml;
			            $("#coupon_button_apply").css("display","inline-block");
			        	$('#coupon_insert').html(dataHead);
			        	$("#coupon_button_apply").click(function(){
			        		var islogin = $("#islogin").val();
			        		if(islogin=="false"){
			        			$(".blockPopup_box").show();
			        			return;
			        		}
			           	 applyCoupon($('#coupon_code_select option:selected').val());
			           });
			        }
     	        }
     	  })
	}
	
	function drawPromoPrefer(data){
		var preferhtml="<dd id='dd_cart_loyalty'>Promo:<span>" +$("#cart_subtotal_symbol").text()+" "+data+"</span></dd>";
     	$("#dd_cart_loyalty").remove();
     	$("#cart_loyalty").before(preferhtml);
	}
	
	function drawPointsPrefer(data){
		var preferhtml="<dd id='dd_cart_points'>Points:<span>" +$("#cart_subtotal_symbol").text()+" "+data+"</span></dd>";
     	$("#dd_cart_points").remove();
     	$("#cart_loyalty").before(preferhtml);
	}
	
	function undoDrawPromoPrefer(){
		$('#promo_undo').submit();
	}
	
	function undoPoint(){
   	 $('#undo_loyalty_point').submit();
	}
	
	function applyPromo(){
		var promo_apply_url = cartRoutes.controllers.cart.LoyaltyController.applyPromo().url;
		 $.ajax({ 
     		  type: 'GET',  
     	        data : $('#loyalty_promo').serialize(),
     	        url: promo_apply_url,
     	        error: function () {
     	        },  
     	        success:function(data){
     	        	if(data.result=='success'){
	     	        	$("#promo_button_cancel").show();
	     	        	$("#promo_button_apply").css("display","none");
	     	        	$("#promo_code_input").attr("disabled","disabled");
	     	        	var current_total = $("#cart_total").text() || '';
	     	        	current_total = current_total.replace(/,/g,'');
	     	        	var preferValue=data.data.value || 0;
	     	        	var grandTotal;
     	        	//检查是否有小数点
	     	   		if(current_total.indexOf('.') == -1){
	     	   			current_total = parseInt(current_total);
	     	   			preferValue = parseInt(preferValue);
	     	   			grandTotal = current_total + preferValue;
	     	   		}else{
	     	   			current_total = parseFloat(current_total);
	     	   			preferValue = toDecimal2(preferValue);
	     	   			grandTotal = (current_total - 0) + (preferValue - 0);
	     	   			//保留两位小数
	     	   			grandTotal = toDecimal2(grandTotal);
	     	   		}
	     	        	$("#cart_total").text(grandTotal);
	     	        	drawPromoPrefer(preferValue);
	     	        	$("#div_coupon").css("display","none");
     	        	}else{
     	        		$("#loyalty_promo").next(".p_error").show();
     	        	}
     	        }
     	  })
	}
	
	function applyPoint(){
		var costpoint = $('#point_code_input').val();
		var point_apply_url = cartRoutes.controllers.cart.LoyaltyController.applyPoints(costpoint).url;
		 $.ajax({ 
     		    type: 'POST',  
     	        url: point_apply_url,
     	        error: function () {
     	        },  
     	        success:function(data){
     	        	if(data.result=='success'){
	     	        	$("#point_button_cancel").show();
	     	        	$("#point_button_apply").css("display","none");
	     	        	$("#point_code_input").attr("disabled","disabled");
	     	        	var current_total = $("#cart_total").text() || '';
	     	        	current_total = current_total.replace(/,/g,'');
	     	        	var preferValue=data.data.value || 0;
	     	        	
	     	        	var grandTotal;
	     	        	//检查是否有小数点
	     	   		if(current_total.indexOf('.') == -1){
	     	   			current_total = parseInt(current_total);
	     	   			preferValue = parseInt(preferValue);
	     	   			grandTotal = current_total + preferValue;
	     	   		}else{
	     	   			current_total = parseFloat(current_total);
	     	   			preferValue = toDecimal2(preferValue);
	     	   			grandTotal = (current_total - 0) + (preferValue - 0);
	     	   			//保留两位小数
	     	   			grandTotal = toDecimal2(grandTotal);
	     	   		}
	     	        	$("#cart_total").text(grandTotal);
	     	        	drawPointsPrefer(preferValue);
     	        	}else{
     	        		$("#div_point").find(".p_error").show();
     	        	}
     	        }
     	  })
	}
	
	function getUsablePoint(){
		 $.ajax({ 
   		  type: 'GET',  
   	        url: point_get_rul,
   	        error: function () {
   	        },  
   	        success:function(data){
					if(data.result=='success'){
						var point=data.data;
						$("#cart_usablepoint").text(point);
			           };
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
     	        		 for(var i=0 ; i < listdata.length ; i++){  
     	        			var preferValue = listdata[i].value || 0;
     	        			var current_total = $("#cart_total").text() || '';
     	     	        	current_total = current_total.replace(/,/g,'');
     	        			var grandTotal;
    	     	        	//检查是否有小数点
    		     	   	if(current_total.indexOf('.') == -1){
    		     	   			current_total = parseInt(current_total);
    		     	   			preferValue = parseInt(preferValue);
    		     	   			grandTotal = (current_total - 0) + (preferValue - 0);
    		     	   	}else{
    		     	   			current_total = parseFloat(current_total);
    		     	   			preferValue = toDecimal2(preferValue);
    		     	   			grandTotal = (current_total - 0) + (preferValue - 0);
    		     	   			//保留两位小数
    		     	   			grandTotal = toDecimal2(grandTotal);
    		     	   	}
 		     	        	$("#cart_total").text(grandTotal);
     	        		if(listdata[i].preferType == 'coupon'){
     	        				$("#div_coupon").show();
     	        				$("#coupon_button_apply").hide();
     		     	        	$("#coupon_button_cancel").css("display","inline-block");
     		     	        	$("#coupon_code_select").attr("disabled","disabled");
     		     	        	$("#coupon_input_show").hide();
     		     	        	drawCouponPrefer(preferValue);
     		     	        	var couponshow="<input id='currentcoupon' type='text'>"+
    			                "</input>";
     		     	        	$('#coupon_insert').append(couponshow);
     		     	        	$("#currentcoupon").val('code:'+listdata[i].code)
     		     	        	$('#currentcoupon').attr("disabled","disabled");
     		     	        	$("#coupon_button_apply").css("display","none");
     		     	        	$("#coupon_button_cancel").css("display","inline-block");
     		     	        	$("#div_promo").css("display","none");
     	        			 }
     	        		else if(listdata[i].preferType == 'promo'){
     	        				$("#promo_button_apply").css("display","none");
     		     	        	$("#promo_button_cancel").css("display","inline-block");
     		     	        	$("#promo_code_input").val(listdata[i].code).attr("disabled","disabled");
     		     	        	drawPromoPrefer(preferValue);
     		     	        	$("#div_coupon").css("display","none");
     	        			 }
     	        		else if(listdata[i].preferType == 'point'){
      	        				$("#point_button_apply").css("display","none");
      		     	        	$("#point_button_cancel").css("display","inline-block");
      		     	        	$("#point_code_input").val(listdata[i].code).attr("disabled","disabled");
      		     	        	drawPointsPrefer(preferValue);
      	        			 }
 			            }  
     	        		getUsablePoint();
     	        		if($('#div_coupon').is(':visible')&&$('#coupon_button_apply').is(':visible')){
     	        			getUsableCoupon();
     	        		 }
     	        	   }else{
     	        		   getUsablePoint();
     	        		   getUsableCoupon();
     	        	   }
     	        }
     	  })
	}
    
    function showCouponButton(){
    	$("#div_coupon").css("display","block");
    }
    function hiddenCouponButton(){
    	$("#div_coupon").css("display","none");
    }
    function validGetCoupon(succeedCallback,failedCallback){
    	 $.ajax({ 
    		    type: 'GET',  
    	        url: coupon_get_rul,
    	        error: function () {
    	        },  
    	        success:function(data){
    	        	   var listdata=data.data;
					if(data.result=='success'&&listdata.length > 0){
						succeedCallback();
			        }else{
			        	failedCallback();
			        }
    	        }
    	  })
    }
    
    $("#promo_button_apply").click(function(){
	    	$("#loyalty_promo").next(".p_error").css("display","none");
	    	applyPromo();
    });
    
    $("#promo_button_cancel").click(function(){
    	undoDrawPromoPrefer();
    })
    
    $("#coupon_button_cancel").click(function(){
    		undoDrawCouponPrefer();
    });
    
    $("#point_button_apply").click(function(){
    		var islogin = $("#islogin").val();
		if(islogin=="false"){
			$(".blockPopup_box").show();
			return;
		}
    		var cost = $('#point_code_input').val();
    	if(!(cost&&cost>0)){
    		$('#div_point').find('.p_error').css("display","block");
    		return;
    	}
	    	$('#div_point').find('.p_error').css("display","none");
	    	applyPoint();
    });
    
    $("#point_button_cancel").click(function(){
    		undoPoint();
    });
    
    $("#coupon_button_apply").click(function(){
		var islogin = $("#islogin").val();
		if(islogin=="false"){
			$(".blockPopup_box").show();
			return;
		}
    });
	getCurrentPrefer();
	
});