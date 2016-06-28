package forms.member.register;

import java.io.Serializable;
import java.util.Date;

import play.data.validation.Constraints.Required;

public class RegisterUpdateForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String cfirstname;
	String clastname;
	String cemail;
	String caccount;
	String cpassword;
	String cnewpassword;
	String ccnewpassword;
	Integer igender;
	String ccountry;
	String caboutme;
	String tokencode;
	String day;
	String month;
	String year;
	Date dbirth;
	String countryName;
	
	boolean bactivated;
	
	public boolean isBactivated() {
		return bactivated;
	}
	public void setBactivated(boolean bactivated) {
		this.bactivated = bactivated;
	}
	public String getCfirstname() {
		return cfirstname;
	}
	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}
	public String getClastname() {
		return clastname;
	}
	public void setClastname(String clastname) {
		this.clastname = clastname;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getCaccount() {
		return caccount;
	}
	public void setCaccount(String caccount) {
		this.caccount = caccount;
	}
	public String getCpassword() {
		return cpassword;
	}
	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}
	public String getCnewpassword() {
		return cnewpassword;
	}
	public void setCnewpassword(String cnewpassword) {
		this.cnewpassword = cnewpassword;
	}
	public String getCcnewpassword() {
		return ccnewpassword;
	}
	public void setCcnewpassword(String ccnewpassword) {
		this.ccnewpassword = ccnewpassword;
	}
	public Integer getIgender() {
		return igender;
	}
	public void setIgender(Integer igender) {
		this.igender = igender;
	}
	public String getCaboutme() {
		return caboutme;
	}
	public void setCaboutme(String caboutme) {
		this.caboutme = caboutme;
	}
	public String getCcountry() {
		return ccountry;
	}
	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}
	public String getTokencode() {
		return tokencode;
	}
	public void setTokencode(String tokencode) {
		this.tokencode = tokencode;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Date getDbirth() {
		return dbirth;
	}
	public void setDbirth(Date dbirth) {
		this.dbirth = dbirth;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

}
