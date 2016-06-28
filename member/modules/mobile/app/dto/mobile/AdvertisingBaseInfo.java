package dto.mobile;

public class AdvertisingBaseInfo {

	private String imgurl; // 图片地址

	private Integer sit; // 位置 1. 上 2. 中

	private String type; // 类型 1 商品详情 2. H5 专题 3.商品列表

	private String skip; // 对应类型 如果类型为 1则是 商品ID ; 如果类型为2 则为H5 地址 ; 如果为 3
							// 则是商品分类ID

	public Integer getSit() {
		return sit == null ? 0 : sit;
	}

	public String getImgurl() {
		return imgurl == null ? "" : imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public void setSit(Integer sit) {
		this.sit = sit;
	}

	public String getType() {
		return type == null ? "0" : type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSkip() {
		return skip == null ? "" : skip;
	}

	public void setSkip(String skip) {
		this.skip = skip;
	}

}
