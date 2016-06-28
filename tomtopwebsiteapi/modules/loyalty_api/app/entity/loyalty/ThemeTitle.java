package entity.loyalty;

public class ThemeTitle {
	private Integer iid;

	private Integer ithemeid;

	private String ctitle;

	private Integer ilanguageid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIthemeid() {
		return ithemeid;
	}

	public void setIthemeid(Integer ithemeid) {
		this.ithemeid = ithemeid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle == null ? null : ctitle.trim();
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
}