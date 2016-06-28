package events.mobile;

public class ViewProductEvent {
	private String gid;

	private String sku;

	private int sitid;

	private String uuid;

	private Integer memberid;

	private Integer platformid;

	public ViewProductEvent(String gid, String sku, int sitid, String uuid,
			Integer memberid, Integer platformid) {
		super();
		this.gid = gid;
		this.sku = sku;
		this.sitid = sitid;
		this.uuid = uuid;
		this.memberid = memberid;
		this.platformid = platformid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public int getSitid() {
		return sitid;
	}

	public void setSitid(int sitid) {
		this.sitid = sitid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getMemberid() {
		return memberid;
	}

	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}

	public Integer getPlatformid() {
		return platformid;
	}

	public void setPlatformid(Integer platformid) {
		this.platformid = platformid;
	}

}
