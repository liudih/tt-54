package com.tomtop.product.models.dto;

public class ProductsPropLinkDto extends BaseDto {
	private static final long serialVersionUID = -7709183113645968526L;
	/**
	 * 商品编码
	 */
	private String productsCode;
    /**
     * 商品名称
     */
    private String productsName;
    /**
     * 商品分类编码
     */
    private String categoryCode;
    /**
     * 可用库存
     */
    private Integer availableStock;
    /**
     * 是否上架
     */
    private Integer isOpenShelf;
    /**
     * 是否启用状态(1:启用,0:停用)
     */
    private Integer isEffective;
	/**
	 * 商品规格编号
	 */
	private String specCode;
	/**
	 * 属性类型编码
	 */
	private Integer typeCode;
	/**
	 * 属性类型名称
	 */
	private String typeName;
	/**
	 * 属性内容编码
	 */
	private Integer valueCode;
	/**
	 * 属性内容值
	 */
	private String propValue;
	/**
	 * 属性内容值描述
	 */
	private String propValueDesc;
	/**
	 * 属性值图片路径
	 */
	private String valueImgPath;
	
	
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Integer getIsOpenShelf() {
		return isOpenShelf;
	}

	public void setIsOpenShelf(Integer isOpenShelf) {
		this.isOpenShelf = isOpenShelf;
	}

	public Integer getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(Integer isEffective) {
		this.isEffective = isEffective;
	}

	public ProductsPropLinkDto() {
		super();
	}
	public String getproductsName() {
		return productsName;
	}

	public void setproductsName(String productsName) {
		this.productsName = productsName;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public ProductsPropLinkDto(String propValue, Integer typeCode, Integer valueCode) {
		super();
		this.propValue=propValue;
		this.typeCode = typeCode;
		this.valueCode = valueCode;
	}
	
	public String getproductsCode() {
		return productsCode;
	}
	public void setproductsCode(String productsCode) {
		this.productsCode = productsCode;
	}
	public String getSpecCode() {
		return specCode;
	}
	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getValueCode() {
		return valueCode;
	}
	public void setValueCode(Integer valueCode) {
		this.valueCode = valueCode;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public String getPropValueDesc() {
		return propValueDesc;
	}
	public void setPropValueDesc(String propValueDesc) {
		this.propValueDesc = propValueDesc;
	}
	public String getValueImgPath() {
		return valueImgPath;
	}
	public void setValueImgPath(String valueImgPath) {
		this.valueImgPath = valueImgPath;
	}
}
