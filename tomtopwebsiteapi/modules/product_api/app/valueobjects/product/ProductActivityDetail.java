package valueobjects.product;

import java.util.Date;

import play.data.validation.Constraints.Required;

public class ProductActivityDetail {

	@Required
	private String cparentspu;

	@Required
	private String csubspu;

	@Required
	private String dfromdate;

	@Required
	private String dtodate;

	private String[] cskuM;

	private String[] csku;

	private String clistingid;

	@Required
	private float fprice;

	@Required
	private Integer iqty;

	public String getCparentspu() {
		return cparentspu;
	}

	public void setCparentspu(String cparentspu) {
		this.cparentspu = cparentspu;
	}

	public String getCsubspu() {
		return csubspu;
	}

	public void setCsubspu(String csubspu) {
		this.csubspu = csubspu;
	}

	public String getDfromdate() {
		return dfromdate;
	}

	public void setDfromdate(String dfromdate) {
		this.dfromdate = dfromdate;
	}

	public String getDtodate() {
		return dtodate;
	}

	public void setDtodate(String dtodate) {
		this.dtodate = dtodate;
	}

	public String[] getCskuM() {
		return cskuM;
	}

	public void setCskuM(String[] cskuM) {
		this.cskuM = cskuM;
	}

	public String[] getCsku() {
		return csku;
	}

	public void setCsku(String[] csku) {
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
