package dto.interaction;

import java.io.Serializable;
import java.util.Date;

import services.base.utils.DateFormatUtils;

public class InteractionComment implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String csku;

	private String cmemberemail;

	private String ccomment;

	private Integer iprice;

	private Integer iquality;

	private Integer ishipping;

	private Integer iusefulness;

	private Double foverallrating;

	private Date dcreatedate;

	private Date dauditdate;

	private Integer istate;

	private String ccountry;

	private String cplatform;

	private Integer iwebsiteid;

	private Integer iorderid;

	private Integer count;

	private String ctitle;

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
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku == null ? null : csku.trim();
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public String getCcomment() {
		return ccomment;
	}

	public void setCcomment(String ccomment) {
		this.ccomment = ccomment == null ? null : ccomment.trim();
	}

	public Integer getIprice() {
		return iprice;
	}

	public void setIprice(Integer iprice) {
		this.iprice = iprice;
	}

	public Integer getIquality() {
		return iquality;
	}

	public void setIquality(Integer iquality) {
		this.iquality = iquality;
	}

	public Integer getIshipping() {
		return ishipping;
	}

	public Integer getIusefulness() {
		return iusefulness;
	}

	public void setIusefulness(Integer iusefulness) {
		this.iusefulness = iusefulness;
	}

	public Double getFoverallrating() {
		return foverallrating;
	}

	public void setIshipping(Integer ishipping) {
		this.ishipping = ishipping;
	}

	public void setFoverallrating(Double foverallrating) {
		this.foverallrating = foverallrating;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public String getDcommentDate() {
		return DateFormatUtils.getDateTimeMDYHMS(dcreatedate);
	}

	public String getDcommentDate2() {
		return DateFormatUtils.getStrFromYYYYMMDDHHMMSS(dcreatedate);
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDauditdate() {
		return dauditdate;
	}

	public void setDauditdate(Date dauditdate) {
		this.dauditdate = dauditdate;
	}

	public String getDauditdate2() {
		return DateFormatUtils.getDateTimeYYYYMMDD(dauditdate);
	}

	public Integer getIstate() {
		return istate;
	}

	public void setIstate(Integer istate) {
		this.istate = istate;
	}

	public Integer getIpriceStarWidth() {
		return iprice != null ? iprice * 20 : 0;
	}

	public Integer getIqualityStarWidth() {
		return iquality != null ? iquality * 20 : 0;
	}

	public Integer getIshippingStarWidth() {

		return ishipping != null ? ishipping * 20 : 0;
	}

	public Integer getIusefulnessStarWidth() {
		return iusefulness != null ? iusefulness * 20 : 0;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public String getCplatform() {
		return cplatform;
	}

	public void setCplatform(String cplatform) {
		this.cplatform = cplatform;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIorderid() {
		return iorderid;
	}

	public void setIorderid(Integer iorderid) {
		this.iorderid = iorderid;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

}