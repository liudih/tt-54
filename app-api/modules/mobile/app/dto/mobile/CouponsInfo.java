package dto.mobile;

import utils.ValidataUtils;

public class CouponsInfo {

	private String code;// 购物卷码
	private Integer flag;// 标识
	private String descr;// 描述与说明 = name
	private Double dis;// 折扣比例或金额 =par
	private Double minAmt;// 最低消费金额
	private long vdate;// 过期时间

	public String getCode() {
		return ValidataUtils.validataStr(code);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getFlag() {
		return ValidataUtils.validataInt(flag);
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getDescr() {
		return ValidataUtils.validataStr(descr);
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Double getDis() {
		return ValidataUtils.validataDouble(dis);
	}

	public void setDis(Double dis) {
		this.dis = dis;
	}

	public Double getMinamt() {
		return ValidataUtils.validataDouble(minAmt);
	}

	public void setMinAmt(Double minAmt) {
		this.minAmt = minAmt;
	}

	public long getVdate() {
		return vdate;
	}

	public void setVdate(long vdate) {
		this.vdate = vdate;
	}

}
