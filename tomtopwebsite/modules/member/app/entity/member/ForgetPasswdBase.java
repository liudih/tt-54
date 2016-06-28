package entity.member;

import java.sql.Timestamp;

public class ForgetPasswdBase {

	private String cid;// UUID
	private Integer iwebsiteid;
	private Timestamp dcreatedate;
	private String cmemberemail;
	private boolean buse;// 是否使用
	private String crandomcode;

	public String getCrandomcode() {
		return crandomcode;
	}

	public void setCrandomcode(String crandomcode) {
		this.crandomcode = crandomcode;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public boolean isBuse() {
		return buse;
	}

	public void setBuse(boolean buse) {
		this.buse = buse;
	}

	public Timestamp getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Timestamp dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
