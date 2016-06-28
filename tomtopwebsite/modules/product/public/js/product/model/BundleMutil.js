/*
	product deatil page function
*/
define(['jquery','./Detail'],function ($,Detail) {

    function ModelBundleMutil() {
    }
   
    var detailobj = new Detail();
    ModelBundleMutil.prototype = {
        getMutilatribute: function(data) {
        	var map = {};
        	$.each(data, function(i, item){
        		var mapatribute = {};
        		$.each(item, function(j, jitem){
        			var cvalue = jitem.cvalue;
        			var valuemap = {};
        			if(jitem.bshowimg){
						valuemap['url'] = jitem.imgUrl;
        			} else {
						valuemap['cvalue'] = jitem.cvalue;
		            }
					mapatribute[cvalue] = valuemap;
        		});
        		map[i] = mapatribute;
        	});
        	
        	return map;
        },
        
        changeModel: function(data) {
        	var map = {};
        	$.each(data, function(i, item){
        		$.each(item, function(j, jitem){
        			var clistingid = jitem.clistingid;
        			var modle = {};
        			if (clistingid in map) {
        				modle = map[clistingid];
        			}
        			modle[jitem.ckey] = jitem.cvalue;
        			map[clistingid] = modle;
        		});
        	});
        	
        	return map;
        },
        
        getModel: function(clistingid,data) {
        	var ownerff = this; 
        	var mutilatribute = data[clistingid];
        	var mutilatributeMap = {};
        	$.each(data, function(i, item){
        		var flag = ownerff.compareProductAtribute(mutilatribute,item,1);
        		if (flag) {
        			mutilatributeMap[i] = item;
        		}
        	});
        	return mutilatributeMap;
        },
        
        getModelbyAtribute: function(selectedatribute,data) {
        	var ownerff = this; 
        	var mutilatributeMap = {};
        	if (jQuery.isEmptyObject(selectedatribute)) {
        		mutilatributeMap = data;
        	} else {
        		$.each(data, function(i, item){
            		var flag = ownerff.compareProductAtribute(selectedatribute,item,0);
            		if (flag) {
            			mutilatributeMap[i] = item;
            		}
            	});
        	}
        	
        	return mutilatributeMap;
        },
        
        getProductData : function(clistingid, data) {
        	var map = {};
        	$.each(data, function(i, item){
        		$.each(item, function(j, jitem){
        			var listingid = jitem.clistingid;
        			if (listingid == clistingid) {
        				map = jitem;
        			}
        		});
        	});
        	
        	return map;
        },
        
        compareProductAtribute : function(productdata, comparedata, misstotal) {
        	var matchtotal = 0;
        	var total = 0;
        	$.map(productdata, function(key, value){
        		total ++ ;
        		$.each(comparedata, function(cvalue, ckey){
		    	    if(ckey == key && cvalue == value) {
		    	    	matchtotal++ ;
		    	    }
	        	});
		    });
        	if ((matchtotal + misstotal) < total) {
        		return false;
        	} else {
        		return true;
        	}
        },
        
        //标识可选的属性
        markChooseAtribute : function(clistingid, backclistingid, mutilatributeMap, selectedatribute){
        	var multiatributeTotal = 0;
			var multiatributeSelectedTotal = 0;
        	$("#multiatribute_").find("ul").each(function() {
				multiatributeTotal ++ ;
				var $this = $(this);
				$($this).find("li").each(function(){
					var atribute = $(this);
					var key = atribute.attr("key");
	        		var keyvalue = atribute.attr("value");
	        		$.each(mutilatributeMap, function(m, mutilatributeitem){
	    				var breakflag = false;
				        $.map(mutilatributeitem, function(mvalue, mkey){
				        	if (mkey == key && mvalue == keyvalue) {
				        		atribute.removeClass("invalids");
				        		atribute.attr("selectedid",m);
				        		if (clistingid != null && clistingid != "" && m == clistingid) {
				        			atribute.addClass("rightDown_SJ");
				        			$("#submit_multiatribute_").data('listingid',m);
				        			breakflag = true;
				        			multiatributeSelectedTotal ++ ;
				        			return false;
				        		} else if (clistingid == null || clistingid == "") {
				        			$.map(selectedatribute, function(cvalue, ckey){
		        			    	    if(ckey == key && cvalue == keyvalue) {
		        			    	    	atribute.addClass("rightDown_SJ");
		    			        			$("#submit_multiatribute_").data('listingid',m);
		    			        			breakflag = true;
		    			        			multiatributeSelectedTotal ++ ;
		    			        			return false;
		        			    	    }
				                	});
				        		}
				        	} 
				        });
	    				if (breakflag) {
	    					return false;
	    				}
		        	});
				});
    		});
        	if (multiatributeSelectedTotal != multiatributeTotal) {
        		$("#submit_multiatribute_").addClass("grayINP");
			} else {
				$("#submit_multiatribute_").removeClass("grayINP");
			}
        },
        
        //初始化选择框
        initChoosePanel : function(backclistingid, data) {
        	var ownerff = this; 
        	//清空当前画板
        	$("#multiatribute_").empty();
        	//获得所有可选择的属性
		    var map = ownerff.getMutilatribute(data);
     	    //获得当前被选择的属性
		    $.each(map, function(i, item) {
	        	var $parent = $("<ul></ul>");		// 获取<ul>节点。<li>的父节点
	        	$("#multiatribute_").append(i);
	        	var keyatribute = " key = '" + i +"'";
        		$.map(item, function(value, key){
		    	    var cvalue = value;
        			var selectClass = "class='invalids'";
        			var selectable = " ";
        			var id = "";
		            if(cvalue != null && cvalue['url'] != null){
		            	var imgg = cvalue['url'];
		            	if(imgg.indexOf("http://")==-1 && imgg.indexOf("https://")==-1){
		            		imgg = "/imgxy/120/120/" + imgg;
		            	}
        			 	var $li_1 = $("<li "+ selectClass + keyatribute + selectable + id + " value='" + key + "'><span></span><img height='35px' width='33px' src='" + imgg +"' title='" + key + "' /></li>");    //  创建一个<li>元素
        			} else {
        				$parent.attr("class","productSpecialSize_box");
        				selectClass = "class='productSpecialSize invalids'";
		            	var $li_1 = $("<li "+ selectClass + keyatribute + selectable + id + " value='" + key + "'><span></span>" + key + "</li>");    //  创建一个<li>元素
		            }
		            $parent.append($li_1);	    // 添加到<ul>节点中，使之能在网页中显示
		    	});
	            $("#multiatribute_").append($parent);
	            $("#multiatribute_").append("<div class='clear'></div>");
	            $("#multiatribute_").find("li").unbind('click');
	            $("#multiatribute_").find("li").click(function()
        		{
	            	if (!$(this).hasClass("invalids")) {
	            		if ($(this).hasClass("rightDown_SJ")) {
		            		$(this).removeClass("rightDown_SJ");
		            		var selectedatribute = {};
		            		$("#multiatribute_").find("li").each(function(index) {
		            			if ($(this).hasClass("rightDown_SJ")) {
		            				var key = $(this).attr("key");
				            		var keyvalue = $(this).attr("value");
				            		selectedatribute[key] = keyvalue;
		            			}
		            		 });
		            		ownerff.writeMultiAtributePanel(null, backclistingid, data, selectedatribute);
		            	} else {
		            		$(this).removeClass("rightDown_SJ");
	            			$(this).addClass("rightDown_SJ");
		            		var clistingidvalue = $(this).attr("selectedid");
		            		ownerff.writeMultiAtributePanel(clistingidvalue, backclistingid, data, null);
		            	}
	            	}
        		});
	        });
			$("#multiatri_").css('display','block');  
        },
        
        //绘制多属性面板
        writeMultiAtributePanel: function(clistingid, backclistingid, data, selectedatribute) {
        	var ownerff = this; 
        	//初始化面板
        	$("#multiatribute_").unbind(".plugin");
        	ownerff.initChoosePanel(backclistingid, data);
			var model = ownerff.changeModel(data);
			//获得当前被选择的属性
			var mutilatributeMap;
			if (clistingid != null && clistingid != "") {
				mutilatributeMap = ownerff.getModel(clistingid,model);
			} else {
				mutilatributeMap = ownerff.getModelbyAtribute(selectedatribute,model);
			}
			//标识可选的属性
			ownerff.markChooseAtribute(clistingid, backclistingid, mutilatributeMap, selectedatribute);
        },
        
        selectModel: function(clistingid, backclistingid, mainclistingid) {
        	var ownerff = this; 
        	var url = bundleJavaScriptRoutes.controllers.product.Product.getMultiatributeProduct(clistingid, mainclistingid, 'productbase').url;
        	$.ajax({
				url: url,
				type: "get",
				dataType: "json",
				error:function(err){
					$(".chooseProduct_color").hide();
				},
				success : function(data) {
					if (null != data && !jQuery.isEmptyObject(data)) {
						$(".chooseProduct_color").show();
						$("#submit_multiatribute_").unbind("click");
						ownerff.writeMultiAtributePanel(clistingid, backclistingid, data, null);
						//绑定事件
						$("#cancel_multiatribute_").click(function(){
							$("#submit_multiatribute_").parents(".chooseProduct_color").hide();
						});
						$("#submit_multiatribute_").click(function(){
							var multiatributeTotal = 0;
							var multiatributeSelectedTotal = 0;
							$("#multiatribute_").find("ul").each(function(index) {
								multiatributeTotal ++ ;
								var $this = $(this);
								$($this).find("li").each(function(){
									if ($(this).hasClass("rightDown_SJ")) {
										multiatributeSelectedTotal ++ ;
			            			}
								});
		            		 });
							if (multiatributeSelectedTotal != multiatributeTotal) {
								return;
							}
							var productid = $(this).data('listingid');
							var productData = ownerff.getProductData(productid, data);
							var title = productData.title;
							var url = "/" + productData.url+'.html';
							var $a = $("<a href='"+url+"' title='" + title +"'>" + title +"</a>");
							var imgurl = productData.imgUrl;
							var price = productData.price; 
							var symbol = productData.symbol;
							if (price != null) {
								$("#"+backclistingid).parent().attr('oriprice', price);
								$("#blackPrice_"+backclistingid).html(symbol + " " + price);
							}
							$("#img_href_" + backclistingid).attr("href", url);
							$("#img_href_" + backclistingid+" img").attr("src", imgurl);
							$("#title_" + backclistingid).empty();
							$("#title_" + backclistingid).append($a);
							$("#" + backclistingid).attr("value", productid);
							$("#submit_multiatribute_").parents(".chooseProduct_color").hide();
							detailobj.bundelSaleCalculatePrice();
						});
					} else {
						$(".chooseProduct_color").hide();
					}
				}
			});
        },
        
        getMutilModel: function(clistingid, backclistingid, mainclistingid) {
        	var ownerff = this; 
        	var url = cartRoutes.controllers.product.Product.getMultiatributeProduct(clistingid, mainclistingid, 'buyonegetone').url;
        	$.ajax({
				url: url,
				type: "get",
				dataType: "json",
				error:function(err){
					$(".chooseProduct_color").hide();
				},
				success : function(data) {
					if (null != data && !jQuery.isEmptyObject(data)) {
						$(".chooseProduct_color").show();
						$("#submit_multiatribute_").unbind("click");
						ownerff.writeMultiAtributePanel(clistingid, backclistingid, data, null);
						//绑定事件
						$("#cancel_multiatribute_").click(function(){
							$("#submit_multiatribute_").parents(".chooseProduct_color").hide();
						});
						$("#submit_multiatribute_").click(function(){
							var multiatributeTotal = 0;
							var multiatributeSelectedTotal = 0;
							$("#multiatribute_").find("ul").each(function(index) {
								multiatributeTotal ++ ;
								var $this = $(this);
								$($this).find("li").each(function(){
									if ($(this).hasClass("rightDown_SJ")) {
										multiatributeSelectedTotal ++ ;
			            			}
								});
		            		 });
							if (multiatributeSelectedTotal != multiatributeTotal) {
								return;
							}
							var productid = $(this).data('listingid');
							var classname = '.add' + backclistingid;
							var map = ownerff.changeModel(data);
							var productData = ownerff.getProductData(productid, data);
							var title = productData.title;
							var url = "/" + productData.url+'.html';
							var $a = $("<a target='_blank' href='"+url+"' title='" + title +"'>" + title +"</a>");
							var imgurl = productData.imgUrl;
				         	if(imgurl.indexOf("http://")==-1 && imgurl.indexOf("https://")==-1){
		            			imgurl = "/imgxy/265/265/" + imgurl;
		            		}
							var symbol = productData.symbol;
							$("#img_href_" + backclistingid).attr("href", url);
							$("#img_href_" + backclistingid+" img").attr("src", imgurl);
							$("#title_" + backclistingid).empty();
							$("#title_" + backclistingid).append($a);
							$("#" + backclistingid).data("id", productid);
							$(classname).attr("value", productid);
							var attribute = map[productid];
							$('#attributes' + backclistingid).empty();
							$.map(attribute, function(value, key){
					    	    var cvalue = value;
					    	    var key = key;
					    	    var $_p = "<p class='lineBlock'>" + key + ":<span>" + cvalue +"</span></p>"
					            $('#attributes' + backclistingid).append($_p);
					    	});
							$("#submit_multiatribute_").parents(".chooseProduct_color").hide();
						});
					} else {
						$(".chooseProduct_color").hide();
					}
				}
			});
        }
		
    };

    return ModelBundleMutil;
});
