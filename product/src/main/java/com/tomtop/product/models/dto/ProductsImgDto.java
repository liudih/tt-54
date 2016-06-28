package com.tomtop.product.models.dto;

public class ProductsImgDto extends BaseDto{
	/**
	 * 商品图片Id
	 */
	private Integer imgId;
	/**
	 * 图片名称
	 */
	private String imgName;
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
	 * 商品编码
	 */
	private String productsCode;
	/**
	 * 图片路径
	 */
	private String imgPath;
	
	/**
	 * 图片顺序
	 */
	private Integer imgSort;
	
	/**
	 * @return the imgSort
	 */
	public Integer getImgSort() {
		return imgSort;
	}
	/**
	 * @param imgSort the imgSort to set
	 */
	public void setImgSort(Integer imgSort) {
		this.imgSort = imgSort;
	}
	private static final long serialVersionUID = -2136088987674987961L;
	
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
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
}
