package entity.mobile.auth;

import java.util.List;

import services.member.login.IUserInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUser implements IUserInfo{
	String kind;
	String etag;
	String gender;
	Boolean verified;
	List<UserEmail> emails;
	String objectType;
	String id;
	String displayName;
	String nickname;
	UserName name;
	String url;
	UserImage image;
	Boolean isPlusUser;
	String language;
	AgeRange ageRange;
	Integer circledByCount;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public List<UserEmail> getEmails() {
		return emails;
	}

	public void setEmails(List<UserEmail> emails) {
		this.emails = emails;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public UserName getName() {
		return name;
	}

	public void setName(UserName name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UserImage getImage() {
		return image;
	}

	public void setImage(UserImage image) {
		this.image = image;
	}

	public Boolean getIsPlusUser() {
		return isPlusUser;
	}

	public void setIsPlusUser(Boolean isPlusUser) {
		this.isPlusUser = isPlusUser;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public AgeRange getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(AgeRange ageRange) {
		this.ageRange = ageRange;
	}

	public Integer getCircledByCount() {
		return circledByCount;
	}

	public void setCircledByCount(Integer circledByCount) {
		this.circledByCount = circledByCount;
	}

	@Override
	public String getEmail() {
		return this.getEmails().get(0).getValue();
	}

}
