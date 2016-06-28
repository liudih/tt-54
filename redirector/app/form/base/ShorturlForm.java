package form.base;

import play.data.validation.Constraints.Required;


public class ShorturlForm {
	@Required
	String url;
	@Required
	String aid;
	@Required
	String taskid;
	Integer tasktype;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public Integer getTasktype() {
		return tasktype;
	}
	public void setTasktype(Integer tasktype) {
		this.tasktype = tasktype;
	}
}
