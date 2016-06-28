package forms.member.address;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class AddressForm implements Serializable {
	private static final long serialVersionUID = 1L;

	Integer id;

	Integer iaddressid;

	Boolean bdefault;

	@Required
	String firstName;

	@Required
	String lastName;

	String company;

	String telephone;

	String fax;

	@Required
	String StreetAddress;

	@Required
	String city;

	@Required
	String state;

	@Required
	String PostalCode;

	@Required
	Integer country;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public Integer getIaddressid() {
		return iaddressid;
	}

	public void setIaddressid(Integer iaddressid) {
		this.iaddressid = iaddressid;
	}

	public Boolean getBdefault() {
		return bdefault;
	}

	public void setBdefault(Boolean bdefault) {
		this.bdefault = bdefault;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getStreetAddress() {
		return StreetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		StreetAddress = streetAddress;
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

	public String getPostalCode() {
		return PostalCode;
	}

	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
	}

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return "AddressForm [firstName=" + firstName + ", lastName=" + lastName
				+ ", company=" + company + ", telephone=" + telephone
				+ ", fax=" + fax + ", StreetAddress=" + StreetAddress
				+ ", city=" + city + ", state=" + state + ", PostalCode="
				+ PostalCode + ", country=" + country + "]";
	}

}
