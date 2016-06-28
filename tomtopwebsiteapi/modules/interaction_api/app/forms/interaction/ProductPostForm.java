package forms.interaction;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

public class ProductPostForm {

	@Required
	private String clistingid;
	@Required
	@MaxLength(value = 150)
	private String ctitle;
	@Required
	@MaxLength(value = 250)
	private String cquestion;
	private String canswer;
	@Required
	@Email
	private String cmemberemail;
	@Required
	private Integer itypeid;

	public Integer getItypeid() {
		return itypeid;
	}

	public void setItypeid(Integer itypeid) {
		this.itypeid = itypeid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCquestion() {
		return cquestion;
	}

	public void setCquestion(String cquestion) {
		this.cquestion = cquestion;
	}

	public String getCanswer() {
		return canswer;
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

}
