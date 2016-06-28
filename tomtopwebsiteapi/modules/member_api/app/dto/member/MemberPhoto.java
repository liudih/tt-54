package dto.member;

import java.io.Serializable;
import java.sql.Date;

public class MemberPhoto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cemail;

	private String ccontenttype;

	private byte[] bfile;

	private String cmd5;

	private Date dcreatedate;
	
	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCcontenttype() {
		return ccontenttype;
	}

	public void setCcontenttype(String ccontenttype) {
		this.ccontenttype = ccontenttype;
	}

	public byte[] getBfile() {
		return bfile;
	}

	public void setBfile(byte[] bfile) {
		this.bfile = bfile;
	}

	public String getCmd5() {
		return cmd5;
	}

	public void setCmd5(String cmd5) {
		this.cmd5 = cmd5;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	
}
