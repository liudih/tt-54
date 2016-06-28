$(function(){
	var simpleProductUrl="/api/product/push";					// 1.鍗曞搧鍒婄櫥
	var mutilProductUrl="/api/mutilproduct/push";				//2.澶氬睘鎬у垔鐧�
	var deleteSalepriceUrl="/api/product/saleprice/69fd2577-ed6d-47be-bd25-77ed6dc7be49";	//9銆� 鍒犻櫎褰撳墠浜у搧浠锋牸
	var modifyPriceUrl="/api/product/price";
	var modifyDescriberUrl="/api/product/translate/update";
	var sellPointUrl="/api/product/sellingpoints";
	var modifyImgUrl="/api/product/images";
	var categoryUrl="/api/product/category/push";
	var storageUrl="/api/product/storage";
	var iqtyUrl="/api/product/qty";
	var stateUrl="/api/product/status";
	var salePriceUrl="/api/product/saleprice";
	var mutilAttributeUrl="/api/mutilproduct/attribute";
	var attributeUrl="/api/product/attribute";
	var pushStorageUrl="/base/pushstorage";
	var pushSeriesUrl="/api/category/pushSeries";
	var deleteProductImages="/api/product/deleteimage/1/6cb9164b-fdf5-4aea-b916-4bfdf5aaea76"
	var changeOrderStateUrl="/checkout/changeOrderStatusToDispatched";
	var shippingDataUrl="/api/shippingmethod/push";
	var thiredDataUrl="/api/product/thirdplatformdata/push";
	var addImgUrl="/api/product/images";
	var mutiLangeUrl="/api/product/translate/update";
	var addMutiLangeUrl="/api/product/translate";
	
	$("#addData").bind("click",function(){
		/*testProduct(shippingDataUrl,shippingData);
		var durl="/api/product/attribute/103776c3-64e6-4a5c-b776-c364e66a5c49/type/1";
		testProduct(durl,"d");*/
		/*for(var i=0;i<5000;i++){
			testProduct(modifyPriceUrl,updatePrice);
		}*/
		testProduct(modifyPriceUrl,updatePrice);
		/*testProduct(addImgUrl,addImgBack);
	testProduct(sellPointUrl,sellPoint);
		testProduct(mutilProductUrl,product111);
		testProduct(mutilAttributeUrl,mutilAttribute);//Get璇锋眰
		testProduct(modifyPriceUrl,modifyPrice);//Get璇锋眰
*/	});
	//testProduct(simpleProductUrl,simpleProduct);//post璇锋眰
})
function testProduct(url,params){
	if($.trim(params)==""){
		$.ajax({
			type: "GET",
			url: url,
			dataType:'text',
			contentType: 'application/json;charset=UTF-8',
			success: function(msg){
				//alert( "Data Saved: " + msg );
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error1");
			}
		});

	}else if($.trim(params)=="d"){
		$.ajax({
			type: "delete",
			url: url,
			//data:JSON.stringify({"msgStr":"123"}),
			//data:{"msgStr":JSON.stringify(msgStr)},
			dataType:'text',
			contentType: 'application/json;charset=UTF-8',
			success: function(msg){
				//alert( "Data Saved: " + msg );
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("delete error");
			}
		});
	}else{
		
		$.ajax({
			type: "POST",
			url: url,
			//data:JSON.stringify({"msgStr":"123"}),
			//data:{"msgStr":JSON.stringify(msgStr)},
			data: JSON.stringify(params),
			dataType:'text',
			contentType: 'application/json;charset=UTF-8',
			success: function(msg){
				alert( "Data Saved: " + msg );
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("error2");
			}
		});
	}
}