var promo_apply_url = checkout_loyalty.controllers.cart.LoyaltyController.applyPromo().url;
var coupon_apply_url = checkout_loyalty.controllers.cart.LoyaltyController.applyCoupon().url;
var coupon_get_rul = checkout_loyalty.controllers.cart.LoyaltyController.getUsableCoupon().url;
var perfer_current_rul = checkout_loyalty.controllers.cart.LoyaltyController.getAllCurrentPrefer().url;
var undo_currentprefer_rul = checkout_loyalty.controllers.cart.LoyaltyController.undoCurrentPrefer().url;
var point_get_rul = checkout_loyalty.controllers.cart.LoyaltyController.getMyUsablePoint().url;
var undo_currentpoint_url = checkout_loyalty.controllers.cart.LoyaltyController.undoCurrentPoint().url;


$(function(){
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
	
	function applyCoupon(code) {
		$.ajax({
			type : 'GET',
			data : {
				"code" : code
			},
			url : coupon_apply_url,
			error : function() {
			},
			success : function(data) {
				if (data.result == 'success') {
					window.location.reload();
				} else {
					ttmErrorAlert('Invalid coupon code',3000);
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
						$("#checkout_coupon_div").show();
						var listdata=data.data;
						$("#coupon_input_show").hide();
			        	var listHtml='';  
			            for(var i=0 ; i < listdata.length ; i++){ 
			            	listHtml +='<div class="publicePop_conMP chooseOneBox"><label class="chooseOneLabel input_control"><input type="radio" '+"code="+listdata[i].code+'><div class="radio lineBlock"><span> </span></div><div class="lineBlock couponPaperBox lbBox"><table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td class="curveBorder">'+(listdata[i].value<1?listdata[i].value*100+"%":(listdata[i].unit=="JPY"?parseInt(listdata[i].value):(listdata[i].value).toFixed(2)))+" "
			            		+listdata[i].unit+'</td><td class="couponPaperTxt"><p>'+(listdata[i].value<1?listdata[i].value*100+"%":(listdata[i].unit=="JPY"?parseInt(listdata[i].value):(listdata[i].value).toFixed(2)))+" "+listdata[i].unit+" "+'orders of '+listdata[i].spendLimitCurrency+listdata[i].spendLimitValue +" "+'or more</p><p>Valid date:'+listdata[i].startDate+"~"+listdata[i].endDate+'</p></td></tr></table></div></label></div>';
			            }  
			            $(listHtml).prependTo('#checkout_coupon_insert');
			        	$("#checkout_coupon_insert .chooseOneBox").click(function(){
			        		 	$(this).find(".radio").addClass("aciCss");
			        		 	$(this).siblings().find(".radio").removeClass("aciCss");
			        		 	var s_code=$(this).find("input[type=radio]").attr("code");
			        	    	$("#checkout_coupon_code").val(s_code);
			        	    });
			        	$("#checkout_coupon_apply").click(function(){
			        		var coupon = $("#checkout_coupon_code").val();
			        		if(!coupon){
			        			return;
			        		}
			           	 	applyCoupon(coupon);
			           });
			        }
     	        }
     	  })
	}
	
	function getUsablePoint(){
		$("#checkout_point_div").show();
		 $.ajax({ 
    		  type: 'GET',  
    	        url: point_get_rul,
    	        error: function () {
    	        },  
    	        success:function(data){
					if(data.result=='success'){
						var avaliable=data.data;
						var total=data.total;
						$("#checkout_totalpoint").text(total);
						$("#checkout_usablepoint").text(avaliable);
			           };
    	        }
    	  })
	}
	
	function undoPrefer(){
		$.ajax({ 
  		  type: 'POST',  
  	        url: undo_currentprefer_rul,
  	        error: function () {
  	        },  
  	        success:function(data){
					if(data.result=='success'){
						window.location.reload();
			           };
  	        }
  	  });
	}
	
	function undoPoint(){
		$.ajax({ 
  		  type: 'POST',  
  	        url: undo_currentpoint_url,
  	        error: function () {
  	        },  
  	        success:function(data){
					if(data.result=='success'){
						window.location.reload();
			           };
  	        }
  	  })
	}

	function applyPromo(code) {
		$.ajax({
			type : 'GET',
			data : {
				'code':code
			},
			url : promo_apply_url,
			error : function() {
			},
			success : function(data) {
				if (data.result == 'success') {
					window.location.reload();
				}else{
					ttmErrorAlert('Invalid promo code',3000);
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
	     	   		    ttmErrorAlert('Invalid points',3000);
	     	   		}
     	        }
     	  })
	}

    function getCurrentPrefer(){
		 $.ajax({ 
			 	url: perfer_current_rul,
     		    type: 'GET',  
     		    dataType: 'json',
     	        async: false,
     	        success:function(data){
     	        	if(data.result == 'success'){
     	        		var listdata = data.data;
     	        		 for(var i=0 ; i < listdata.length ; i++){  
     	        			 var cell = listdata[i];
     	        			 var currencypro=$("#discount_promo").text();
     	        			 var discont = cell.value;
     	        			//检查是否有小数点
 	 		     	  		if(currencypro.indexOf('.') == -1){
 	 		     	  			discont = Math.round(discont);
 	 		     	  			discont = parseInt(discont);
 	 		     	  			$("#discount_" + cell.preferType).text(discont);
 	 		     	  		}else{
 		 		     	  		discont = parseFloat(discont);
 	 		     	  			//保留两位小数
 		 		     	  		discont = toDecimal2(discont);
 		 		     	  		$("#discount_" + cell.preferType).text(discont);
 	 		     	  		}
     	        			 if(listdata[i].preferType == 'coupon'){
     	        				 $("#checkout_coupon_div").show();
     	        				$("#checkout_promo_div").hide();
     		     	        	$("#checkout_coupon_canel").show();
     		     	        	$("#checkout_coupon_apply").hide();
     		     	        	$("#checkout_coupon_code").text(listdata[i].code);
     		     	        	$("#checkout_coupon_code").parent().unbind();
     	        			 }
     	        			 else if(listdata[i].preferType == 'promo'){
     	        				$("#checkout_coupon_div").hide();
     	        				$("#checkout_promo_apply").hide();
     		     	        	$("#checkout_promo_cancel").css("display","inline-block");
     		     	        	$("#checkout_promo_input").val(listdata[i].code).attr("disabled","disabled");
     	        			 }
     	        			 else if(listdata[i].preferType == 'point'){
      	        				$("#checkout_point_apply").hide();
      		     	        	$("#checkout_point_cancel").css("display","inline-block");
      		     	        	$("#checkout_point_input").val(listdata[i].code).attr("disabled","disabled");
      	        			 }
 			            }  
     	        		 if($('#isLogin').val()=='true'){
     	        			 getUsablePoint();
     	        		 }
     	        		 if(!$("#checkout_promo_input").attr('disabled')&&!$("#checkout_coupon_input").attr('disabled')){
     	        			getUsableCoupon();
     	        		 }
     	        		getStorageTotal();
     	        		
     	        	}else{
     	        		 if($('#isLogin').val()=='true'){
	     	        		getUsableCoupon();
	     	        		getUsablePoint();
     	        		 }
     	        	}
     	        }
     	  })
	}
    
    $("#checkout_promo_apply").click(function(){
    	var isLogin = $("#isLogin").val();
    	if(isLogin=="false"){
    		window.location.href = '/member/login';
    		return;
    	}
    	var promo=$("#checkout_promo_input").val().trim();
    	if(!promo){
    		return;
    	}
    	applyPromo(promo);
    });
    
    $("#checkout_promo_cancel").click(function(){
    	undoPrefer();
    })
    
    
    $("#checkout_coupon_canel").click(function(){
    	undoPrefer();
    });
    
    $("#checkout_point_apply").click(function(){
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

//获取一个仓库的价格
function getStorageTotal(){
	var pnode;
	//判断cart页面
	if($("#orderitemlist").length==0){
		var chooseStorage = $("#dataContent .orgRadio.aciCss");
		if(chooseStorage!=null){
			pnode = chooseStorage.closest(".chooseOneBox").next();
		}
	}else{//订单确认页
		pnode = $("#orderitemlist"); 
	}
	var price = 0.0;
	pnode.find(".theprice").each(function(i,e){
		var thep = $(this).val();
		thep = thep.replace(/[,]/g,"");
		price += parseFloat(thep);
	});
	var discount_promo = parseFloat($("#discount_promo").html());
	var discount_coupon = parseFloat($("#discount_coupon").html());
	var discount_point = parseFloat($("#discount_point").html());
	var grandprice = (price + discount_promo + discount_coupon + discount_point);
	if($("#shipCost").length>0){
		var shipCost = parseFloat($("#shipCost").html());
		grandprice += shipCost;
	}
	
	var currency = $("#currencycode").val();
	var isJPY = 'JPY' == currency;
	price = isJPY ? Math.floor(price) : price.toFixed(2);
	grandprice = isJPY ? Math.floor(grandprice) : grandprice.toFixed(2);
	
	$("#cart_subtotal").html(price);
	$("#cartGrandTotal").html(grandprice);
}
