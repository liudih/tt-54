package com.tomtop.website.migration.product;

import java.util.List;

public class SellingPoint {
	Integer _id;
	String _class;
	String sku;
	String language;
	List<String> languageSellPointSet;
	String creator;
	String createdDate;
	Integer productId;
	Integer autoEnToLanguageSalePoint;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getLanguageSellPointSet() {
		return languageSellPointSet;
	}

	public void setLanguageSellPointSet(List<String> languageSellPointSet) {
		this.languageSellPointSet = languageSellPointSet;
	}

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getAutoEnToLanguageSalePoint() {
		return autoEnToLanguageSalePoint;
	}

	public void setAutoEnToLanguageSalePoint(Integer autoEnToLanguageSalePoint) {
		this.autoEnToLanguageSalePoint = autoEnToLanguageSalePoint;
	}

}
//
// {
// "_id" : 1,
// "_class" : "com.tomtop.application.orm.product.SkuToSellPoint",
// "productId" : 476183,
// "sku" : "K1305",
// "language" : "EN",
// "languageSellPointSet" : [
// "The timer operates on in put voltages from 5 to 60 Volts DC.",
// "The meter has a positive and a negative lead for easy installation.",
// "6 digit display, from 1/10 hour to 99999.9.",
// "Widely used for tracking time on machinery such as boats, cars, trucks, tractors or any other device where time is used to determine oil change intervals, service requirements or where there is a need to record hours operated.",
// "It is designed to withstand vibration and shaking up to 8g."
// ],
// "creator" : 179,
// "createdDate" : "2014-07-14 15:44:40"
// }
