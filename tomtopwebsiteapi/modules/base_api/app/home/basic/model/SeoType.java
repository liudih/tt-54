package home.basic.model;

/**
 * 首页的类型的枚举对象类
 * @author guozy
 */
public enum SeoType {
	//tomtop的数据类型
	TOMTOP_INDEX("TOMTOP"),
	//dodocool的数据类型
	DODOCOOL_INDEX("DODOCOOL");
	private String index;
	private SeoType(String index){
		this.index=index;
	};
	public String getIndex() {
		return this.index;
	}
}