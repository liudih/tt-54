package forms.interaction;

import play.data.validation.Constraints.Required;

public class ReportErrorForm {

    private String clistingid;

    private String csku;
    @Required
    private String cerrortype;
    @Required
    private String cemail;
    @Required
    private String cinquiry;
    
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
	public String getCerrortype() {
		return cerrortype;
	}
	public void setCerrortype(String cerrortype) {
		this.cerrortype = cerrortype;
	}
	public String getCemail() {
		return cemail;
	}
	public void setCemail(String cemail) {
		this.cemail = cemail;
	}
	public String getCinquiry() {
		return cinquiry;
	}
	public void setCinquiry(String cinquiry) {
		this.cinquiry = cinquiry;
	}
    
    


    
}
