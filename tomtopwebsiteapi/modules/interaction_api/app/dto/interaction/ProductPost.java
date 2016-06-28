package dto.interaction;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProductPost implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String csku;

	private String ctitle;

	private String cquestion;

	private String canswer;

	private String cmemberemail;

	private Date dcreatedate;

	private String crecoveryuser;

	private Date drecoverydate;

	private Integer istate;

	private Integer itypeid;

	private Boolean breply;

	private Integer iwebsiteid;

	private Integer ilanguageid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCquestion() {
		return cquestion != null ? cquestion.trim() : null;
	}

	public void setCquestion(String cquestion) {
		this.cquestion = cquestion;
	}

	public String getCanswer() {
		return canswer != null ? canswer.trim() : null;
	}

	public void setCanswer(String canswer) {
		this.canswer = canswer;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCrecoveryuser() {
		return crecoveryuser;
	}

	public void setCrecoveryuser(String crecoveryuser) {
		this.crecoveryuser = crecoveryuser;
	}

	public Date getDrecoverydate() {
		return drecoverydate;
	}

	public void setDrecoverydate(Date drecoverydate) {
		this.drecoverydate = drecoverydate;
	}

	public Integer getIstate() {
		return istate;
	}

	public void setIstate(Integer istate) {
		this.istate = istate;
	}

	public Integer getItypeid() {
		return itypeid;
	}

	public void setItypeid(Integer itypeid) {
		this.itypeid = itypeid;
	}

	public Boolean getBreply() {
		return breply;
	}

	public void setBreply(Boolean breply) {
		this.breply = breply;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCreateDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"MMM dd ',' yyyy 'at' hh:mm a", Locale.US);
		return sdf.format(this.dcreatedate);
	}

	public Long getCreateTime() {
		return this.dcreatedate.getTime();
	}

}