package dto.mobile;

public class SuggestionInfo {

	/**
	 * 搜索词
	 */
	private String keywds;

	/**
	 * 商品分类ID
	 */
	private Integer cid;

	/**
	 * 搜索结果数量
	 */
	private Integer qty;

	public String getKeywds() {
		return keywds;
	}

	public void setKeywds(String keywds) {
		if (null == keywds) {
			keywds = "";
		}
		this.keywds = keywds;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		if (null == cid) {
			cid = 0;
		}
		this.cid = cid;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		if (null == qty) {
			qty = 0;
		}
		this.qty = qty;
	}
}
