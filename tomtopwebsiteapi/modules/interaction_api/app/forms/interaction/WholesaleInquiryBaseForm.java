package forms.interaction;

import java.util.Date;

/**
 * 客户批发记录实体表
 * @author guozy
 *
 */
public class WholesaleInquiryBaseForm{

	private static final long serialVersionUID = 1L;
    private Integer iid;

    private String clistingid;

    private String csku;

    private String cname;

    private String cphone;

    private String cemail;

    private Double ftargetprice;

    private Integer iquantity;

    private String ccountrystate;

    private String ccompany;

    private String cinquiry;

    private Date startDate;
    
    private Date endDate;
    
    private Date dcreateDate;
	
    public Date getDcreateDate() {
		return dcreateDate;
	}

	public void setDcreateDate(Date dcreateDate) {
		this.dcreateDate = dcreateDate;
	}

	private Integer pageSize;
    
    private Integer pageNum;

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

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCphone() {
		return cphone;
	}

	public void setCphone(String cphone) {
		this.cphone = cphone;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Double getFtargetprice() {
		return ftargetprice;
	}

	public void setFtargetprice(Double ftargetprice) {
		this.ftargetprice = ftargetprice;
	}

	public Integer getIquantity() {
		return iquantity;
	}

	public void setIquantity(Integer iquantity) {
		this.iquantity = iquantity;
	}

	public String getCcountrystate() {
		return ccountrystate;
	}

	public void setCcountrystate(String ccountrystate) {
		this.ccountrystate = ccountrystate;
	}

	public String getCcompany() {
		return ccompany;
	}

	public void setCcompany(String ccompany) {
		this.ccompany = ccompany;
	}

	public String getCinquiry() {
		return cinquiry;
	}

	public void setCinquiry(String cinquiry) {
		this.cinquiry = cinquiry;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getPageSize() {
		return pageSize != null ? pageSize : 20;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum != null ? pageNum : 1;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
}
