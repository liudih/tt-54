package com.tomtop.product.models.dto;

public class ProductsImgQueryDto extends BaseQueryDto {
	/**
	 * 图片分类编码
	 */
	private Integer imgTypeCode;
	/**
	 * 图片分类别名
	 */
	private String imgTypeAlias;
	/**
	 * 图片分类名称
	 */
	private String  imgTypeName;
	/**
	 * 图片编码
	 */
	private Integer imgId;
	/**
	 * 商品编码
	 */
	private String productsCode;
	/**
	 * 图片路径
	 */
	private String imgPath;
	/**
	 * 图片名称
	 */
	private String imgName;
	
	private static final long serialVersionUID = 5205254653717973482L;

	public Integer getImgTypeCode() {
		return imgTypeCode;
	}
	public void setImgTypeCode(Integer imgTypeCode) {
		this.imgTypeCode = imgTypeCode;
	}
	public String getImgTypeAlias() {
		return imgTypeAlias;
	}
	public void setImgTypeAlias(String imgTypeAlias) {
		this.imgTypeAlias = imgTypeAlias;
	}
	public String getImgTypeName() {
		return imgTypeName;
	}
	public void setImgTypeName(String imgTypeName) {
		this.imgTypeName = imgTypeName;
	}
	public String getproductsCode() {
		return productsCode;
	}
	public void setproductsCode(String productsCode) {
		this.productsCode = productsCode;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
}
