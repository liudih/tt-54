package dto.interaction;

import java.io.Serializable;

public class ListingCount implements Serializable  {
	private static final long serialVersionUID = 1L;
	String clistingid;
	Integer count;
	
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}
