package dto.order;

public class OrderReport implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int siteId;

	private String type;

	private String startDate;

	private String endDate;

	private String startDateByM;

	private String startDateByR;

	private String endDateByR;
	
	private String cvhost;

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDateByM() {
		return startDateByM;
	}

	public void setStartDateByM(String startDateByM) {
		this.startDateByM = startDateByM;
	}

	public String getStartDateByR() {
		return startDateByR;
	}

	public void setStartDateByR(String startDateByR) {
		this.startDateByR = startDateByR;
	}

	public String getEndDateByR() {
		return endDateByR;
	}

	public void setEndDateByR(String endDateByR) {
		this.endDateByR = endDateByR;
	}

	public String getCvhost() {
		return cvhost;
	}

	public void setCvhost(String cvhost) {
		this.cvhost = cvhost;
	}

}
