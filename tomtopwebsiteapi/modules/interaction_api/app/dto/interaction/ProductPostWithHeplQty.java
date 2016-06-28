package dto.interaction;

import java.io.Serializable;

public class ProductPostWithHeplQty extends ProductPost implements Serializable  {
	private static final long serialVersionUID = 1L;

	private Integer ihelpfulqty = 0;

	private Integer inothelpfulqty = 0;

	private String ccode;

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
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
}
