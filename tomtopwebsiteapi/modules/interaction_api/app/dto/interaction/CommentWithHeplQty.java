package dto.interaction;

public class CommentWithHeplQty extends InteractionComment {
	private static final long serialVersionUID = 1L;
	private Integer helpfulqty = 0;

	private Integer nothelpfulqty = 0;

	private String ccode;

	public Integer getHelpfulqty() {
		return helpfulqty;
	}

	public void setHelpfulqty(Integer helpfulqty) {
		this.helpfulqty = helpfulqty;
	}

	public Integer getNothelpfulqty() {
		return nothelpfulqty;
	}

	public void setNothelpfulqty(Integer nothelpfulqty) {
		this.nothelpfulqty = nothelpfulqty;
	}

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
}
