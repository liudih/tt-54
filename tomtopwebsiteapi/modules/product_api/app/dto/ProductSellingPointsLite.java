package dto;

import java.io.Serializable;

public class ProductSellingPointsLite implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer ilanguageid;

    private String ccontent;

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}

}
