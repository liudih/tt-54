package entity.loyalty;

import java.io.Serializable;

public class IntegralBehavior  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1407832764586070973L;
	private Integer iid;
	private String cname;
	private Integer iintegral;
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Integer getIintegral() {
		return iintegral;
	}
	public void setIintegral(Integer iintegral) {
		this.iintegral = iintegral;
	}
	
	

}
