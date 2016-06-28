package services.base.receivedata;

public enum HandleReceivedDataType {

	NOT_SET_HANDLE_TYPE("notSetHandleType"), // 默认的一个操作类型

	// ==============对产品进行的操作===================
	UPDATE_PRODUCT_STATUS_AND_QUANTITY_AND_LABEL("updateProductStatusQtyLabel"), // 更改产品状态，数量，和标签

	ADD_PRODUCT("addproduct"),

	ADD_MULTI_PRODUCT("addmultiproduct"),

	ADD_PRODUCT_SALE_PRICE("addproductsaleprice"),

	UPDATE_PRODUCT_FREIGHT("updateproductfreight"),

	UPDATE_PRODUCT_QTY("updateproductqty"),

	DELETE_PRODUCT_LABEL("deleteproductlabel"),

	UPDATE_PRODUCT_STORAGE("updateproductstorage"),

	UPDATE_PRODUCT_TRANSLATION("updateproducttranslation"),

	ADD_PRODUCT_LABLE("addproductlable"),

	ADD_PRODUCT_TRANSLATION("addproducttranslation"),

	ADD_PRODUCT_IMAGE("addproductimage"),

	ADD_PRODUCT_SELLING_POINTS("addproductsellingpoints"),

	UPDATE_PRODUCT_STATUS("updateproductstatus"),

	ADD_PRODUCT_URL("addproducturl"),

	UPDATE_PRODUCT_PRICE("updateproductprice"),

	ADD_CATEGORY("addcategory"),

	UPDATE_COST_PRICE("updatecostprice"),

	ADD_ATTRIBUTE("addattribute"),

	ADD_PRODUCT_MULTI_ATTRIBUTE("addproductmultiattribute"),

	DELETE_PRODUCT_MULTI_ATTRIBUTE("deleteproductmultiattribute"),

	DELETE_PRODUCT_ATTRIBUTE("deleteproductattribute"),

	ADD_PRODUCT_ATTRIBUTE("addproductattribute"),

	// ==============对订单进行的操作===================
	UPDATE_ORDER_STATUS("updateOrderStatus"),

	UPDATE_ORDER_REMARK("updateOrderRemark"),

	UPDATE_ORDER_PACK("updateOrderPack"),

	DELETE_CURRENT_PRODUCTSALEPRICE("deletecurrentproductsaleprice"),
	
	ADD_THIRDPLATFORMDATA("addthirdplatformdata"),

	DELETE_PRODUCT_SELLINGPOINT("deleteProductSellingPoints"),
	
	DELETE_PRODUCT_IMAGE("deleteproductimage"),

	ADD_PRODUCT_CATEGORY("addProductCategory");
	
	private String type;

	private HandleReceivedDataType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
