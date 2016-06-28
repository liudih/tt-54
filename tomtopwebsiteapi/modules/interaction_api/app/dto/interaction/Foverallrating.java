package dto.interaction;

import java.io.Serializable;

public class Foverallrating implements Serializable {
	private static final long serialVersionUID = 1L;
	String clistingid;
	/**
	 * count:对此listing评论总数
	 */
	Integer count;
	Integer type;
	Integer num;
	Double percentage;

	public Foverallrating() {
	}

	public Foverallrating(String clistingid, Integer count, Integer type,
			Integer num, Double percentage) {
		super();
		this.clistingid = clistingid;
		this.count = count;
		this.type = type;
		this.num = num;
		this.percentage = percentage;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	/**
	 * 一颗星是1，两颗星是2
	 * 
	 * @return
	 */
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 当前星星的总数
	 * 
	 * @return
	 */
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCount() {
		return count;
	}
}
