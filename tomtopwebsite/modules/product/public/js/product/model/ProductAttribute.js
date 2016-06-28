/*
	product deatil page function
*/
define(['jquery','./Detail'],function ($,Detail) {

    function ProductAttribute() {
    }
   
    var detailobj = new Detail();
    ProductAttribute.prototype = {
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
        	if (selectedatribute.length < 1) {
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
        markChooseAtribute : function(clistingid, data, mutilatributeMap, selectedatribute, nowclistingid){
        	var ownerff = this;
        	var multiatributeTotal = 0;
			var multiatributeSelectedTotal = 0;
			var chooseproduct = "";
        	$("#attributeShow").find("ul").each(function() {
				var ulid = $(this).attr("id");
				multiatributeTotal ++ ;
				$("#" + ulid).find("li").each(function(){
					var atribute = $(this);
					var key = atribute.attr("key");
	        		var keyvalue = atribute.attr("value");
	        		$.each(mutilatributeMap, function(m, mutilatributeitem){
	    				var breakflag = false;
				        $.map(mutilatributeitem, function(mvalue, mkey){
				        	if (mkey == key && mvalue == keyvalue) {
				        		atribute.removeClass("invalids");
				        		atribute.attr("id",m);
				        		if (clistingid != null && clistingid != "" && m == clistingid) {
				        			atribute.addClass("rightDown_SJ");
//				        			$("#submit_multiatribute_" + nowclistingid).attr("sumbitvalue",m);
				        			chooseproduct = m;
				        			breakflag = true;
				        			multiatributeSelectedTotal ++ ;
				        			return false;
				        		} else if (clistingid == null || clistingid == "") {
				        			$.map(selectedatribute, function(cvalue, ckey){
		        			    	    if(ckey == key && cvalue == keyvalue) {
		        			    	    	atribute.addClass("rightDown_SJ");
//		    			        			$("#submit_multiatribute_" + nowclistingid).attr("sumbitvalue",m);
		        			    	    	chooseproduct = m;
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
        		pophtml("Error","fail!");
//        		$("#submit_multiatribute_" + nowclistingid).css("background-color","#bbbbbb");
			} else {
				console.info(chooseproduct + "--------" + clistingid);
				if (chooseproduct != nowclistingid) {
					var map = ownerff.getProductData(chooseproduct, data);
					console.info(map);
					var url = attributeRoutes.controllers.product.Product.view(map.url).url;
//					alert(url)
					window.location.href = url;
				}
				
//				alert(url);
//				$("#submit_multiatribute_" + nowclistingid).css("background-color","#fc9d21");
			}
        },
        
        //初始化选择框
        initChoosePanel : function(nowclistingid, data) {
        	var ownerff = this; 
        	//清空当前画板
        	$("#attributeShow").empty();
        	//获得所有可选择的属性
		    var map = ownerff.getMutilatribute(data);
     	    //获得当前被选择的属性
		    $.each(map, function(i, item) {
	            var $topDiv = $("<div class=''></div>");
	            var $parentDiv = $("<div class=''>" + i +":</div>" );
		    	
	            var $parent = $("<ul id='ul" + i + "'></ul>");		// 获取<ul>节点。<li>的父节点
	        	var keyatribute = " key = '" + i +"'";
        		$.map(item, function(value, key){
		    	    var cvalue = value;
        			var selectClass = "class='invalids'";
        			var selectable = " ";
        			var id = "";
		            if(cvalue != null && cvalue['url'] != null){
		            	$parentDiv.attr("class","productSpecialColor_txt");
		            	$topDiv.attr("class","productSpecialColor_box");
        			 	var $li_1 = $("<li class='productSpecialColor_pic invalids'"+ selectClass + keyatribute + selectable+ " value='" + key + "'><span></span><img height='35px' width='33px' src='" + cvalue['url'] +"' title='" + key + "' /></li>");    //  创建一个<li>元素
        			} else {
        				$parentDiv.attr("class","productSpecialSize_txt");
		            	$topDiv.attr("class","productSpecialSize_box");
        				selectClass = "class='productSpecialSize invalids'";
		            	var $li_1 = $("<li class='productSpecialSize_box invalids'"+ selectClass + keyatribute + selectable + id + " value='" + key + "'><span></span>" + key + "</li>");    //  创建一个<li>元素
		            }
		            $parent.append($li_1);	    // 添加到<ul>节点中，使之能在网页中显示
		    	});
        		$topDiv.append($parentDiv);
        		$topDiv.append($parent);
        		$topDiv.append("<div class='clear'></div>");
	       
	            $("#attributeShow").append($topDiv);
	            $("#ul" + i).find("li").click(function()
        		{
	            	if ($(this).attr("class").indexOf("invalids") < 0 ) {
	            		if ($(this).attr("class").indexOf("rightDown_SJ")>=0) {
		            		$(this).removeClass("rightDown_SJ");
		            		var selectedatribute = {};
		            		$("#attributeShow").find("li").each(function(index) {
		            			if ($(this).attr("class").indexOf("rightDown_SJ")>=0) {
		            				var key = $(this).attr("key");
				            		var keyvalue = $(this).attr("value");
				            		selectedatribute[key] = keyvalue;
		            			}
		            		 });
		            		console.info(selectedatribute);
		            		ownerff.writeMultiAtributePanel(null, nowclistingid, data, selectedatribute);
		            	} else {
		            		$("#ul" + i + nowclistingid).find("li").removeClass("rightDown_SJ")
	            			$(this).addClass("rightDown_SJ");
		            		var clistingidvalue = $(this).attr("id");
		            		ownerff.writeMultiAtributePanel(clistingidvalue, nowclistingid, data, null);
		            	}
	            	}
        		});
	        });
			$("#attributeShow").css('display','block');  
        },
        
        //绘制多属性面板
        writeMultiAtributePanel: function(clistingid, nowclistingid, data, selectedatribute) {
        	var ownerff = this; 
        	//初始化面板
//        	$("#multiatribute_" + nowclistingid).unbind(".plugin");
        	ownerff.initChoosePanel(nowclistingid, data);
			var model = ownerff.changeModel(data);
			//获得当前被选择的属性
			var mutilatributeMap;
			if (clistingid != null && clistingid != "") {
				mutilatributeMap = ownerff.getModel(clistingid,model);
			} else {
				mutilatributeMap = ownerff.getModelbyAtribute(selectedatribute,model);
			}
			//标识可选的属性
			ownerff.markChooseAtribute(clistingid, data, mutilatributeMap, selectedatribute, nowclistingid);
        },
        
        selectModel: function(clistingid) {
        	var ownerff = this; 
        	var url = attributeRoutes.controllers.product.Product.getMultiatributeProduct(clistingid, clistingid).url;
        	$.ajax({
				url: url,
				type: "get",
				dataType: "json",
				success : function(data) {
					if (null != data) {
						console.info(data);
						ownerff.writeMultiAtributePanel(clistingid, clistingid, data, null);
					} else {
						pophtml("Error","NULL");
					}
				}
			});
        }
		
    };

    return ProductAttribute;
});
