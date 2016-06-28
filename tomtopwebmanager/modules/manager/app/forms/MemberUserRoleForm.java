package forms;

public class MemberUserRoleForm {

	private int iid;
	private String cemail;
	private Integer siteId;
	private Integer memberRole;
	private String searchEmail;

	public int getIid() {
		return iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(Integer memberRole) {
		this.memberRole = memberRole;
	}

	public String getSearchEmail() {
		return searchEmail;
	}

	public void setSearchEmail(String searchEmail) {
		this.searchEmail = searchEmail;
	}

}
