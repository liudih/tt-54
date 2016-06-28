package forms.topic;

import play.data.validation.Constraints.Required;

public class RequestTopicPageForm {
	@Required
	private String type;
	@Required
	private Integer languageId;
	@Required
	private Integer year;
	private Integer month;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	@Override
	public String toString() {
		return "RequestTopicPageForm [type=" + type + ", languageId="
				+ languageId + ", year=" + year + ", month=" + month + "]";
	}

}
