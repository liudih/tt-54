package valueobjects.member;

public class DropshipBase {

	private String cemail;

	private String levelName;

	private Double discount;

	private Integer maxDropshipNumberLimit;

	public DropshipBase(String cemail, String levelName, Double discount,
			Integer maxDropshipNumberLimit) {
		super();
		this.cemail = cemail;
		this.levelName = levelName;
		this.discount = discount;
		this.maxDropshipNumberLimit = maxDropshipNumberLimit;
	}

	public Double getDiscount() {
		return discount * 100;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public DropshipBase() {
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Integer getMaxDropshipNumberLimit() {
		return maxDropshipNumberLimit;
	}

	public void setMaxDropshipNumberLimit(Integer maxDropshipNumberLimit) {
		this.maxDropshipNumberLimit = maxDropshipNumberLimit;
	}

}
