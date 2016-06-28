package entity.mobile.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Source: https://developer.paypal.com/webapps/developer/docs/api/
 * 
 * @author kmtong
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaypalUser {
	String userId; // string Identifier for the end-user at the issuer.
	String name; // string End-User’s full name in displayable form including
					// all name parts, possibly including titles and suffixes,
					// ordered according to the end-user’s locale and
					// preferences.
	String givenName; // string Given name(s) or first name(s) of the end-user.
	String familyName; // string Surname(s) or last name(s) of the end-user.
	String email; // string End-user’s preferred email address.
	boolean verified; // boolean True if the End-User’s e-mail address has been
						// verified; otherwise false.
	String gender; // string End-user’s gender.
	String birthdate; // string End-user’s birthday, represented as an
						// YYYY-MM-DD format. They year MAY be 0000, indicating
						// it is omited. To represent only the year, YYYY format
						// would be used.
	String zoneinfo; // string Time zone database representing the End-User’s
						// time zone.
	String locale; // string End-user’s locale.
	String phoneNumber; // string End-user’s preferred telephone number.
	String address; // address End-user’s preferred address.
	boolean verifiedAccount; // boolean Verified account status.
	String accountType; // string Account type, either personal or business.
	String ageRange; // string Account holder age range.
	String payerId; // string Account payer identifier.

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getZoneinfo() {
		return zoneinfo;
	}

	public void setZoneinfo(String zoneinfo) {
		this.zoneinfo = zoneinfo;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isVerifiedAccount() {
		return verifiedAccount;
	}

	public void setVerifiedAccount(boolean verifiedAccount) {
		this.verifiedAccount = verifiedAccount;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

}
