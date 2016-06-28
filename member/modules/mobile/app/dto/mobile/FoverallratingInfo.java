package dto.mobile;

public class FoverallratingInfo {
	/**
	 * 商品ID
	 */
	String lid;
	/**
	 * count:
	 */
	Integer count;
	/**
	 * 星级
	 */
	Integer type;
	/**
	 * 当前星级数量
	 */
	Integer num;
	/**
	 * 比例
	 */
	Double ratio;

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

}
