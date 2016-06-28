package forms.mobile;

import java.io.Serializable;

import utils.ValidataUtils;

public class UserInfoForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nickname;// 昵称
	private String fname;// 名字前
	private String lname;// 名字后
	private Integer gender;// 性别
	private String birth;// 生日
	private String country;// 国家
	private String about;// 简介

	public String getNickname() {
		return ValidataUtils.validataStr(nickname);
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFname() {
		return ValidataUtils.validataStr(fname);
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return ValidataUtils.validataStr(lname);
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public Integer getGender() {
		return ValidataUtils.validataInt(gender);
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBirth() {
		return ValidataUtils.validataStr(birth);
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCountry() {
		return ValidataUtils.validataStr(country);
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAbout() {
		return ValidataUtils.validataStr(about);
	}

	public void setAbout(String about) {
		this.about = about;
	}

}
