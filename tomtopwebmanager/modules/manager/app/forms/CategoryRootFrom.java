package forms;

import util.AppsUtil;

public class CategoryRootFrom {

	private String cname_1 = "";
	private String ctitle_1 = "";
	private String cname_2 = "";
	private String ctitle_2 = "";
	private String cname_3 = "";
	private String ctitle_3 = "";
	private String cname_4 = "";
	private String ctitle_4 = "";
	private String cname_5 = "";
	private String ctitle_5 = "";
	private String cname_6 = "";
	private String ctitle_6 = "";
	private boolean bshow;
	private Integer siteid;
	private Integer parentid;
	private Integer parentid2;

	public String getCname_1() {
		return AppsUtil.trim(cname_1);
	}

	public void setCname_1(String cname_1) {
		this.cname_1 = cname_1;
	}

	public String getCtitle_1() {
		return AppsUtil.trim(ctitle_1);
	}

	public void setCtitle_1(String ctitle_1) {
		this.ctitle_1 = ctitle_1;
	}

	public String getCname_2() {
		if ("".equals(AppsUtil.trim(cname_2))) {
			return getCname_1();
		}
		return AppsUtil.trim(cname_2);
	}

	public void setCname_2(String cname_2) {
		this.cname_2 = cname_2;
	}

	public String getCtitle_2() {
		if ("".equals(AppsUtil.trim(ctitle_2))) {
			return getCtitle_1();
		}
		return AppsUtil.trim(ctitle_2);
	}

	public void setCtitle_2(String ctitle_2) {
		this.ctitle_2 = ctitle_2;
	}

	public String getCname_3() {
		if ("".equals(AppsUtil.trim(cname_3))) {
			return getCname_1();
		}
		return AppsUtil.trim(cname_3);
	}

	public void setCname_3(String cname_3) {
		this.cname_3 = cname_3;
	}

	public String getCtitle_3() {
		if ("".equals(AppsUtil.trim(ctitle_3))) {
			return getCtitle_1();
		}
		return AppsUtil.trim(ctitle_3);
	}

	public void setCtitle_3(String ctitle_3) {
		this.ctitle_3 = ctitle_3;
	}

	public String getCname_4() {
		if ("".equals(AppsUtil.trim(cname_4))) {
			return getCname_1();
		}
		return AppsUtil.trim(cname_4);
	}

	public void setCname_4(String cname_4) {
		this.cname_4 = cname_4;
	}

	public String getCtitle_4() {
		if ("".equals(AppsUtil.trim(ctitle_4))) {
			return getCtitle_1();
		}
		return AppsUtil.trim(ctitle_4);
	}

	public void setCtitle_4(String ctitle_4) {
		this.ctitle_4 = ctitle_4;
	}

	public String getCname_5() {
		if ("".equals(AppsUtil.trim(cname_5))) {
			return getCname_1();
		}
		return AppsUtil.trim(cname_5);
	}

	public void setCname_5(String cname_5) {
		this.cname_5 = cname_5;
	}

	public String getCtitle_5() {
		if ("".equals(AppsUtil.trim(ctitle_5))) {
			return getCtitle_1();
		}
		return AppsUtil.trim(ctitle_5);
	}

	public void setCtitle_5(String ctitle_5) {
		this.ctitle_5 = ctitle_5;
	}

	public String getCname_6() {
		if ("".equals(AppsUtil.trim(cname_6))) {
			return getCname_1();
		}
		return AppsUtil.trim(cname_6);
	}

	public void setCname_6(String cname_6) {
		this.cname_6 = cname_6;
	}

	public String getCtitle_6() {
		if ("".equals(AppsUtil.trim(ctitle_6))) {
			return getCtitle_1();
		}
		return AppsUtil.trim(ctitle_6);
	}

	public void setCtitle_6(String ctitle_6) {
		this.ctitle_6 = ctitle_6;
	}

	public boolean isBshow() {
		return bshow;
	}

	public void setBshow(boolean bshow) {
		this.bshow = bshow;
	}

	public Integer getSiteid() {
		return AppsUtil.checkInt(siteid);
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Integer getParentid() {
		return AppsUtil.checkInt(parentid);
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getParentid2() {
		return AppsUtil.checkInt(parentid2);
	}

	public void setParentid2(Integer parentid2) {
		this.parentid2 = parentid2;
	}

	public Integer getParentCategoryId() {
		Integer parentCategoryid = parentid;
		if (parentid2 != 0) {
			parentCategoryid = parentid2;
		}
		return parentCategoryid;
	}

	public Integer getLevel() {
		Integer level = 1;
		if (parentid2 != 0) {
			level = 3;
		} else if (parentid != 0) {
			level = 2;
		}
		return level;
	}

}
