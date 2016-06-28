package forms.mobile.member;

import java.util.HashMap;

import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author lijun
 *
 */
public class AddressForm {

	Integer id;

	// 是否为默认收货地址
	Boolean isDefault;

	@Required
	String street;

	@Required
	Integer countryId;

	@Required
	String state;

	@Required
	String city;

	@Required
	String postalCode;

	@Required
	String phoneNumber;

	@Required
	Integer type;
	
	@Required
	String firstName;
	
	@Required
	String lastName;

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	@Override
	public String toString() {
		ObjectMapper om = new ObjectMapper();
		HashMap map = om.convertValue(this, HashMap.class);
		return map.toString();
	}

}
