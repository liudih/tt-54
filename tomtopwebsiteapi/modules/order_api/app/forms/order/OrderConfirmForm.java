package forms.order;

import play.data.validation.Constraints.Required;

/**
 * 
 * @author lijun
 *
 */
public class OrderConfirmForm {
	@Required
	String orderNum;
	@Required
	String firstName;

	String lastName;
	@Required
	String address1;

	String address2;

	@Required
	String province;
	@Required
	String city;
	@Required
	String zipCode;
	@Required
	String telephone;
	@Required
	String PayerID;
	@Required
	String token;
	@Required
	Integer shippingMethodIdValue;
	@Required
	String countryCode;
	
	String leaveMessage;
	
	String country;	//国家
	
	String countrysn;	//国家缩写

	
	public String getLeaveMessage() {
		return leaveMessage;
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getShippingMethodIdValue() {
		return shippingMethodIdValue;
	}

	public void setShippingMethodIdValue(Integer shippingMethodIdValue) {
		this.shippingMethodIdValue = shippingMethodIdValue;
	}

	public String getPayerID() {
		return PayerID;
	}

	public void setPayerID(String payerID) {
		PayerID = payerID;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountrysn() {
		return countrysn;
	}

	public void setCountrysn(String countrysn) {
		this.countrysn = countrysn;
	}

}
