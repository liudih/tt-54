package forms;

import util.AppsUtil;

public class CategoryRecommendFrom {

	private String category1 = "";
	private String category2 = "";
	private String category3 = "";
	private String sku = "";
	private String isTop = "";
	private Integer websiteid = 1;
	private String device = "";

	public String getCategory1() {
		return AppsUtil.trim(category1);
	}

	public void setCategory1(String category1) {
		this.category1 = category1;
	}

	public String getCategory2() {
		return AppsUtil.trim(category2);
	}

	public void setCategory2(String category2) {
		this.category2 = category2;
	}

	public String getCategory3() {
		return AppsUtil.trim(category3);
	}

	public void setCategory3(String category3) {
		this.category3 = category3;
	}

	public String getSku() {
		return AppsUtil.trim(sku);
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getIsTop() {
		return AppsUtil.trim(isTop);
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public Integer getWebsiteid() {
		return websiteid;
	}

	public void setWebsiteid(Integer websiteid) {
		this.websiteid = websiteid;
	}

	public String getDevice() {
		return AppsUtil.trim(device);
	}

	public void setDevice(String device) {
		this.device = device;
	}

}
