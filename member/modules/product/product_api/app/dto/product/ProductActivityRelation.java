package dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductActivityRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cparentspu;

	private String csubspu;

	private Integer iwebsiteid;

	private Date dfromdate;

	private Date dtodate;

	private String cparentlistingid;

	private String csublistingid;

	private Date dcreatedate;
	
	private boolean bvisible;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Date getDfromdate() {
		return dfromdate;
	}

	public void setDfromdate(Date dfromdate) {
		this.dfromdate = dfromdate;
	}

	public Date getDtodate() {
		return dtodate;
	}

	public void setDtodate(Date dtodate) {
		this.dtodate = dtodate;
	}

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

	public String getCparentlistingid() {
		return cparentlistingid;
	}

	public void setCparentlistingid(String cparentlistingid) {
		this.cparentlistingid = cparentlistingid;
	}

	public String getCsublistingid() {
		return csublistingid;
	}

	public void setCsublistingid(String csublistingid) {
		this.csublistingid = csublistingid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public boolean isBvisible() {
		return bvisible;
	}

	public void setBvisible(boolean bvisible) {
		this.bvisible = bvisible;
	}

}
