package entity.email;

import java.io.Serializable;
import java.util.Date;

public class EmailLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cfromemail;
	private String ctoemail;
	private String ctitle;
	private String ccontent;
	private Date dsenddate;
	private Date dcreatedate;
	// tomtop判断的结果
	private Boolean bsendstatus;
	// 第三方平台返回的结果
	private String cthirdresult;
	// 失败原因
	private String cfailreason;

	public String getCfromemail() {
		return cfromemail;
	}

	public void setCfromemail(String cfromemail) {
		this.cfromemail = cfromemail;
	}

	public String getCtoemail() {
		return ctoemail;
	}

	public void setCtoemail(String ctoemail) {
		this.ctoemail = ctoemail;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
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

	public Boolean getBsendstatus() {
		return bsendstatus;
	}

	public void setBsendstatus(Boolean bsendstatus) {
		this.bsendstatus = bsendstatus;
	}

	public String getCthirdresult() {
		return cthirdresult;
	}

	public void setCthirdresult(String cthirdresult) {
		this.cthirdresult = cthirdresult;
	}

	public String getCfailreason() {
		return cfailreason;
	}

	public void setCfailreason(String cfailreason) {
		this.cfailreason = cfailreason;
	}

}
