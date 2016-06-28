package forms;

import java.util.Date;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;

public class CustomerServiceScheduleForm {
	@Required
	private Integer iuserid;
	@DateTime(pattern = "yyyy-MM-dd HH:mm")
	private Date dstartdate;
	@DateTime(pattern = "yyyy-MM-dd HH:mm")
	private Date denddate;
	@Required
	private Integer p;

	public Integer getIuserid() {
		return iuserid;
	}

	public void setIuserid(Integer iuserid) {
		this.iuserid = iuserid;
	}

	public Date getDstartdate() {
		return dstartdate;
	}

	public void setDstartdate(Date dstartdate) {
		this.dstartdate = dstartdate;
	}

	public Date getDenddate() {
		return denddate;
	}

	public void setDenddate(Date denddate) {
		this.denddate = denddate;
	}

	public Integer getP() {
		return p;
	}

	public void setP(Integer p) {
		this.p = p;
	}

	@Override
	public String toString() {
		return "CustomerServiceScheduleForm [iuserid=" + iuserid
				+ ", dstartdate=" + dstartdate + ", denddate=" + denddate
				+ ", p=" + p + "]";
	}

}
