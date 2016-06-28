package valueobjects.member;

/**
 * 用于表示一个会员ID。
 * 
 * 由于会员ID定义之前比较含糊，故此用这个对象用于尝试收窄会员ID定义的分歧。
 * 
 * @author kmtong
 *
 */
public class MemberIdentification {

	final String email;
	final int siteID;

	public MemberIdentification(String email, int siteID) {
		super();
		this.email = email;
		this.siteID = siteID;
	}

	public String getEmail() {
		return email;
	}

	public int getSiteID() {
		return siteID;
	}

}
