package dto.member;

import java.io.Serializable;
import java.sql.Date;

public class MemberByStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer iid;
	private String cemail;
	private Double famount;
	private Integer icount;
	private Date dcreatedate;

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

	public Double getFamount() {
		return famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	public Integer getIcount() {
		return icount;
	}

	public void setIcount(Integer icount) {
		this.icount = icount;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
