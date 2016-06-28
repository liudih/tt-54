package dto.mobile;

public class CategoryInfo {

	// 父类别ID
	private Integer ipcid;
	// 名称
	private String cname;
	// 类别ID
	private Integer icid;
	// 级别
	private Integer level;

	public CategoryInfo() {
		super();
	}

	public CategoryInfo(Integer ipcid, String cname, Integer icid, Integer level) {
		super();
		if (ipcid == null) {
			ipcid = 0;
		}
		this.ipcid = ipcid;
		if (cname == null) {
			cname = "";
		}
		this.cname = cname;
		if (icid == null) {
			icid = 0;
		}
		this.icid = icid;
		if (level == null) {
			level = 0;
		}
		this.level = level;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		if (cname == null) {
			this.cname = "";
		}
		this.cname = cname;
	}

	public Integer getIpcid() {

		return ipcid;
	}

	public void setIpcid(Integer ipcid) {
		if (ipcid == null) {
			ipcid = 0;
		}
		this.ipcid = ipcid;
	}

	public Integer getIcid() {
		return icid;
	}

	public void setIcid(Integer icid) {
		if (icid == null) {
			icid = 0;
		}
		this.icid = icid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		if (level == null) {
			level = 0;
		}
		this.level = level;
	}

}
