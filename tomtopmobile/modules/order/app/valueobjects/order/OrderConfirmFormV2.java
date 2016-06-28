package valueobjects.order;

import play.data.validation.Constraints;
public class OrderConfirmFormV2 {

	@Constraints.Required
	String orderNum;

	@Constraints.Required
	String firstName;
	String lastName;

	@Constraints.Required
	String address1;
	String address2;

	@Constraints.Required
	String province;

	@Constraints.Required
	String city;

	@Constraints.Required
	String zipCode;

	@Constraints.Required
	String telephone;

	@Constraints.Required
	String PayerID;

	@Constraints.Required
	String token;

	Integer shippingMethodIdValue;

	@Constraints.Required
	String countryCode;
	String leaveMessage;
	String country;
	String countrysn;
	
	@Constraints.Required
	private Integer shipMethodId;
	@Constraints.Required
	private String shipMethodCode;

	public String getLeaveMessage() {
		return this.leaveMessage;
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getShippingMethodIdValue() {
		return this.shippingMethodIdValue;
	}

	public void setShippingMethodIdValue(Integer shippingMethodIdValue) {
		this.shippingMethodIdValue = shippingMethodIdValue;
	}

	public String getPayerID() {
		return this.PayerID;
	}

	public void setPayerID(String payerID) {
		this.PayerID = payerID;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountrysn() {
		return this.countrysn;
	}

	public void setCountrysn(String countrysn) {
		this.countrysn = countrysn;
	}

	public Integer getShipMethodId() {
		return shipMethodId;
	}

	public void setShipMethodId(Integer shipMethodId) {
		this.shipMethodId = shipMethodId;
	}

	public String getShipMethodCode() {
		return shipMethodCode;
	}

	public void setShipMethodCode(String shipMethodCode) {
		this.shipMethodCode = shipMethodCode;
	}
}