package entity.tracking;

public class Affiliate {

	String caid;
	int iwebsiteid;
	/**
	 * <ul>
	 * <li>0 ＝ 渠道
	 * <li>1 ＝ 会员个体 （如会员申请的 AID）
	 * <li>2 ＝ 营销人员（内部员工）
	 * </ul>
	 */
	int itype;

	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
	}

	public int getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(int iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public int getItype() {
		return itype;
	}

	public void setItype(int itype) {
		this.itype = itype;
	}

}
