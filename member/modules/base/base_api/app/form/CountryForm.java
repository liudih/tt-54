package form;

import play.data.validation.Constraints.Required;

public class CountryForm {
	@Required
	private Integer cp;
	@Required
	private Integer iid;
	@Required
	private Integer ilanguageid;
	@Required
	private String ccurrency;
	@Required
	private Integer ishowindex;
	
	public Integer getCp() {
		return cp;
	}
	public void setCp(Integer cp) {
		this.cp = cp;
	}
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Integer getIlanguageid() {
		return ilanguageid;
	}
	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
	public String getCcurrency() {
		return ccurrency;
	}
	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}
	public Integer getIshowindex() {
		return ishowindex;
	}
	public void setIshowindex(Integer ishowindex) {
		this.ishowindex = ishowindex;
	}
	
}
