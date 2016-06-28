package forms.mobile;

import java.io.Serializable;

import utils.ValidataUtils;

public class AddressForm implements Serializable {

	private static final long serialVersionUID = 1L;

	// id
	private Integer aid;

	// 邮箱
	private String email;

	// 是否默认
	private int isdef;

	// 名字
	private String fname;

	// 中名
	private String mname;

	// 姓
	private String lname;

	// 公司
	private String company;

	// 城市
	private String city;

	// 街道
	private String street;

	// 国家
	private int country;

	// 省份
	private String provice;

	// 邮编
	private String postal;

	// 电话
	private String tel;

	// 传真
	private String fax;

	// 税号
	private String vatno;

	// 类型 1. 收货地址 2. 账单地址
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public String getEmail() {
		return ValidataUtils.validataStr(email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getIsdef() {
		return ValidataUtils.validataInt(isdef);
	}

	public void setIsdef(int isdef) {
		this.isdef = isdef;
	}

	public String getFname() {
		return ValidataUtils.validataStr(fname);
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return ValidataUtils.validataStr(mname);
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLname() {
		return ValidataUtils.validataStr(lname);
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getCompany() {
		return ValidataUtils.validataStr(company);
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCity() {
		return ValidataUtils.validataStr(city);
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return ValidataUtils.validataStr(street);
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getCountry() {
		return ValidataUtils.validataInt(country);
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public String getProvice() {
		return ValidataUtils.validataStr(provice);
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getPostal() {
		return ValidataUtils.validataStr(postal);
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public String getTel() {
		return ValidataUtils.validataStr(tel);
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return ValidataUtils.validataStr(fax);
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getVatno() {
		return ValidataUtils.validataStr(vatno);
	}

	public void setVatno(String vatno) {
		this.vatno = vatno;
	}

}
