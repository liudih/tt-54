package dto.product;

import java.io.Serializable;

public class ProductActivityRelationDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer iactivityid;
	private String cspu;
	private String csku;
	private String clistingid;
	private float fprice;
	private Integer iqty;

	public Integer getIactivityid() {
		return iactivityid;
	}

	public void setIactivityid(Integer iactivityid) {
		this.iactivityid = iactivityid;
	}

	public String getCspu() {
		return cspu;
	}

	public void setCspu(String cspu) {
		this.cspu = cspu;
	}

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

	public float getFprice() {
		return fprice;
	}

	public void setFprice(float fprice) {
		this.fprice = fprice;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

}
