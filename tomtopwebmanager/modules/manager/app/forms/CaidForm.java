package forms;

import play.data.validation.Constraints.Required;



public class CaidForm {

	@Required
	String caid;
	
	Integer website;

	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
	}
	
	public Integer getWebsite(){
		return website;
	}
	
	public void setWebsite(Integer website){
		this.website = website;
	}
}
