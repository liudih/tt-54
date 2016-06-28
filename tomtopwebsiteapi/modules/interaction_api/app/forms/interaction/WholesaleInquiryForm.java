package forms.interaction;

import play.data.validation.Constraints.Required;

public class WholesaleInquiryForm {

    private String clistingid;

    private String csku;
    
    @Required
    private String cname;
    @Required
    private String cphone;
    @Required
    private String cemail;
    @Required
    private Double ftargetprice;
    @Required
    private String ccountrystate;
    @Required
    private String ccompany;
    @Required
    private String cinquiry;
    @Required
    private Integer iquantity;
    
    public String validate() {
        if (cinquiry!=null && cinquiry.length()>2000) {
            return "Inquiry is too long";
        }
        return null;
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
	public Integer getIquantity() {
		return iquantity;
	}
	public void setIquantity(Integer iquantity) {
		this.iquantity = iquantity;
	}
    
    

}
