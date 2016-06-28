package forms.member.address;

import java.io.Serializable;

import play.data.validation.Constraints.Required;

public class ShippingAddressForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cmemberemail;

	private Boolean bdefault;

	private Boolean copytoBillingAddress;

	private String addressType;

	@Required
	private String cfirstname;

	private String clastname;

	@Required
	private String cstreetaddress;

	@Required
	private String ccity;

	@Required
	private Integer icountry;

	@Required
	private String cprovince;

	@Required
	private String cpostalcode;

	@Required
	private String ctelephone;

	public Integer getIid() {
		return iid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public Boolean getBdefault() {
		return bdefault;
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public String getClastname() {
		return clastname;
	}

	public String getCstreetaddress() {
		return cstreetaddress;
	}

	public String getCcity() {
		return ccity;
	}

	public Integer getIcountry() {
		return icountry;
	}

	public String getCprovince() {
		return cprovince;
	}

	public String getCpostalcode() {
		return cpostalcode;
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public void setBdefault(Boolean bdefault) {
		this.bdefault = bdefault;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname;
	}

	public void setCstreetaddress(String cstreetaddress) {
		this.cstreetaddress = cstreetaddress;
	}

	public void setCcity(String ccity) {
		this.ccity = ccity;
	}

	public void setIcountry(Integer icountry) {
		this.icountry = icountry;
	}

	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}

	public void setCpostalcode(String cpostalcode) {
		this.cpostalcode = cpostalcode;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone;
	}

	public Boolean getCopytoBillingAddress() {
		return copytoBillingAddress;
	}

	public void setCopytoBillingAddress(Boolean copytoBillingAddress) {
		this.copytoBillingAddress = copytoBillingAddress;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

}
