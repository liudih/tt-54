package dto.member;

import java.io.Serializable;
import java.util.Date;



public class MemberEmailVerify implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private Integer iid;
	
	private String cemail;
	
	private String cmark;
	
	private Integer idaynumber;
	
	private Boolean bisending;
	
	private String cactivationcode;
	
	private Date dvaliddate;
	
	private Date dsenddate;
	
	private Date dcreatedate;
	
	private Integer iresendcount;
	
	public Integer getIresendcount() {
		return iresendcount;
	}

	public void setIresendcount(Integer iresendcount) {
		this.iresendcount = iresendcount;
	}

	public String getCmark() {
		return cmark;
	}

	public void setCmark(String cmark) {
		this.cmark = cmark;
	}
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

	

	public Integer getIdaynumber() {
		return idaynumber;
	}

	public void setIdaynumber(Integer idaynumber) {
		this.idaynumber = idaynumber;
	}

	public Boolean getBisending() {
		return bisending;
	}

	public void setBisending(Boolean bisending) {
		this.bisending = bisending;
	}

	public String getCactivationcode() {
		return cactivationcode;
	}

	public void setCactivationcode(String cactivationcode) {
		this.cactivationcode = cactivationcode;
	}

	public Date getDvaliddate() {
		return dvaliddate;
	}

	public void setDvaliddate(Date dvaliddate) {
		this.dvaliddate = dvaliddate;
	}

	public Date getDsenddate() {
		return dsenddate;
	}

	public void setDsenddate(Date dsenddate) {
		this.dsenddate = dsenddate;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	
}
