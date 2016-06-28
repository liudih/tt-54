package dto.interaction;

import java.io.Serializable;
import java.util.Date;

public class ProductPostHelpQty implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer ihelpfulqty;

	private Integer inothelpfulqty;

	private Integer ipostid;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIhelpfulqty() {
		return ihelpfulqty;
	}

	public void setIhelpfulqty(Integer ihelpfulqty) {
		this.ihelpfulqty = ihelpfulqty;
	}

	public Integer getInothelpfulqty() {
		return inothelpfulqty;
	}

	public void setInothelpfulqty(Integer inothelpfulqty) {
		this.inothelpfulqty = inothelpfulqty;
	}

	public Integer getIpostid() {
		return ipostid;
	}

	public void setIpostid(Integer ipostid) {
		this.ipostid = ipostid;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
