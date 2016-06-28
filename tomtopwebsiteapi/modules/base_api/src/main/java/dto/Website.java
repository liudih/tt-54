package dto;

import java.io.Serializable;

public class Website implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private Integer iid;

	final private String ccode;

	public Website(Integer iid, String ccode) {
		this.iid = iid;
		this.ccode = ccode;
	}

	public Integer getIid() {
		return iid;
	}

	public String getCcode() {
		return ccode;
	}

}
