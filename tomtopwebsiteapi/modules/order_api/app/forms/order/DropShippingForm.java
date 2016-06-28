package forms.order;

import play.data.validation.Constraints.Required;

public class DropShippingForm {
	@Required
	private String sku;
	@Required
	private String qty;
	@Required
	private String countrysn;
	@Required
	private String state;
	@Required
	private String phone;
	@Required
	private String city;
	@Required
	private String name;
	@Required
	private String address;
	@Required
	private String zipCode;

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

	public String getCountrysn() {
		return countrysn;
	}

	public void setCountrysn(String countrysn) {
		this.countrysn = countrysn;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
