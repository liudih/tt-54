package dto.product;

import java.io.Serializable;

public class SimpleProductBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String csku;
	String clistingid;
	public String getCsku() {
		return csku;
	}
	public void setCsku(String csku) {
		this.csku = csku;
	}
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
}
