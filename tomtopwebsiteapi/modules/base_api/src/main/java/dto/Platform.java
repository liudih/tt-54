package dto;

import java.io.Serializable;

public class Platform implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private Integer id;

	final private String code;

	public Platform(Integer id, String code) {
		this.id = id;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

}
