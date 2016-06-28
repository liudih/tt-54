package entity.member;

public class MemberAddress {

	private Integer iid;

	private String cmemberemail;

	private Integer iaddressid;

	private Boolean bdefault;

	private String cfirstname;

	private String cmiddlename;

	private String clastname;

	private String ccompany;

	private String cstreetaddress;

	private String ccity;

	private Integer icountry;

	private String cprovince;

	private String cpostalcode;

	private String ctelephone;

	private String cfax;

	private String cvatnumber;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
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

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname == null ? null : cfirstname.trim();
	}

	public String getCmiddlename() {
		return cmiddlename;
	}

	public void setCmiddlename(String cmiddlename) {
		this.cmiddlename = cmiddlename == null ? null : cmiddlename.trim();
	}

	public String getClastname() {
		return clastname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname == null ? null : clastname.trim();
	}

	public String getCcompany() {
		return ccompany;
	}

	public void setCcompany(String ccompany) {
		this.ccompany = ccompany == null ? null : ccompany.trim();
	}

	public String getCstreetaddress() {
		return cstreetaddress;
	}

	public void setCstreetaddress(String cstreetaddress) {
		this.cstreetaddress = cstreetaddress == null ? null : cstreetaddress
				.trim();
	}

	public String getCcity() {
		return ccity;
	}

	public void setCcity(String ccity) {
		this.ccity = ccity == null ? null : ccity.trim();
	}

	public Integer getIcountry() {
		return icountry;
	}

	public void setIcountry(Integer icountry) {
		this.icountry = icountry;
	}

	public String getCprovince() {
		return cprovince;
	}

	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}

	public String getCpostalcode() {
		return cpostalcode;
	}

	public void setCpostalcode(String cpostalcode) {
		this.cpostalcode = cpostalcode == null ? null : cpostalcode.trim();
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone == null ? null : ctelephone.trim();
	}

	public String getCfax() {
		return cfax;
	}

	public void setCfax(String cfax) {
		this.cfax = cfax == null ? null : cfax.trim();
	}

	public String getCvatnumber() {
		return cvatnumber;
	}

	public void setCvatnumber(String cvatnumber) {
		this.cvatnumber = cvatnumber == null ? null : cvatnumber.trim();
	}
}