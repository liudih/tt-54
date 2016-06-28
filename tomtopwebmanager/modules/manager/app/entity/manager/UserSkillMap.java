package entity.manager;

import java.util.Date;

public class UserSkillMap {
	private Integer iid;
	private Integer iuserid;
	private String cskilltype;
	private Integer iskillid;
	private Date dcreateDate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIuserid() {
		return iuserid;
	}

	public void setIuserid(Integer iuserid) {
		this.iuserid = iuserid;
	}

	public String getCskilltype() {
		return cskilltype;
	}

	public void setCskilltype(String cskilltype) {
		this.cskilltype = cskilltype;
	}

	public Integer getIskillid() {
		return iskillid;
	}

	public void setIskillid(Integer iskillid) {
		this.iskillid = iskillid;
	}

	public Date getDcreateDate() {
		return dcreateDate;
	}

	public void setDcreateDate(Date dcreateDate) {
		this.dcreateDate = dcreateDate;
	}

}
