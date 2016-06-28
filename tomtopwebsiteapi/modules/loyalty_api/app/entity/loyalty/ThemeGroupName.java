package entity.loyalty;

public class ThemeGroupName {
	private Integer iid;

	private Integer igroupid;

	private String cname;

	private Integer ilanguageid;


	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIgroupid() {
		return igroupid;
	}

	public void setIgroupid(Integer igroupid) {
		this.igroupid = igroupid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

    public Integer getIlanguageid() {
        return ilanguageid;
    }

    public void setIlanguageid(Integer ilanguageid) {
        this.ilanguageid = ilanguageid;
    }

	@Override
	public String toString() {
		return "ThemeGroupName [iid=" + iid + ", igroupid=" + igroupid
				+ ", cname=" + cname + ", ilanguageid=" + ilanguageid + "]";
	}


}