package cms.model;

public enum CmsType {

	HELPCENTER("HelpCenter"), // 帮助中心Cms菜单类型
	FOOT("Foot"); // 页面底部菜单

	private String type;

	private CmsType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}
}
