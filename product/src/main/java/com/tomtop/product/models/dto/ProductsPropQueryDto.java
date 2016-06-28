package com.tomtop.product.models.dto;

public class ProductsPropQueryDto extends BaseQueryDto {
	/**
	 * 属性分组编码
	 */
	private Integer groupId;
	/**
	 * 属性分组名称
	 */
	private String groupName;
	
	/**
     * 属性描叙
     */
    private String groupDesc;
	
	/**
	 * 属性类型编码
	 */
	private Integer typeCode;
	/**
	 * 属性值(内容)编码
	 */
	private Integer valueCode;
	
	/**
	 * 属性类型名称
	 */
	private String typeName;
	/**
	 * 属性类型描述
	 */
	private String typeDesc;
	
	/**
	 * 属性值内容
	 */
	private String propValue;
	/**
	 * 属性值图片路径
	 */
	private String valueImgPath;
	/**
	 * 属性值内容描述
	 */
	private String propValueDesc;
	
	private static final long serialVersionUID = -4808610269790440614L;
	
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public Integer getValueCode() {
		return valueCode;
	}
	public void setValueCode(Integer valueCode) {
		this.valueCode = valueCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public String getValueImgPath() {
		return valueImgPath;
	}
	public void setValueImgPath(String valueImgPath) {
		this.valueImgPath = valueImgPath;
	}
	public String getPropValueDesc() {
		return propValueDesc;
	}
	public void setPropValueDesc(String propValueDesc) {
		this.propValueDesc = propValueDesc;
	}
	public String getGroupDesc() {
        return groupDesc;
    }
    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }
}
