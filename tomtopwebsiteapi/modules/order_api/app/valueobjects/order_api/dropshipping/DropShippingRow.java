package valueobjects.order_api.dropshipping;

import java.io.Serializable;
import java.util.Date;

public class DropShippingRow implements Serializable {
	private String dsOrderID;
	private Date dsOrderDate;
	private String sku;
	private String qty;
	private String country;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String phone;
	private String note;
	private String errorLog;
	private String countrysn;

	public String getDsOrderID() {
		return dsOrderID;
	}

	public void setDsOrderID(String dsOrderID) {
		this.dsOrderID = dsOrderID;
	}

	public Date getDsOrderDate() {
		return dsOrderDate;
	}

	public void setDsOrderDate(Date dsOrderDate) {
		this.dsOrderDate = dsOrderDate;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public String getCountrysn() {
		return countrysn;
	}

	public void setCountrysn(String countrysn) {
		this.countrysn = countrysn;
	}

}
