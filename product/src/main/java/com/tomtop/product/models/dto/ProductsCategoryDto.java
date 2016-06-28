package com.tomtop.product.models.dto;

public class ProductsCategoryDto extends BaseDto {
    /**
     * 商品分类编码
     */
    private String categoryCode;
    /**
     * 商品分类名称
     */
    private String categoryName;
    /**
     * 上级分类编码
     */
    private String parentCode;
    /**
     * 排序索引值
     */
    private Integer sortIndex;
    /**
     * 是否前台可见
     */
    private Integer isFrontShow;
    /**
     * 商品属性类型编号
     */
    private Integer propTypeCode;
    /**
     * 是否为规格属性
     */
    private Integer propSpec;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Integer getIsFrontShow() {
        return isFrontShow;
    }

    public void setIsFrontShow(Integer isFrontShow) {
        this.isFrontShow = isFrontShow;
    }

    public Integer getPropTypeCode() {
        return propTypeCode;
    }

    public void setPropTypeCode(Integer propTypeCode) {
        this.propTypeCode = propTypeCode;
    }

    public Integer getPropSpec() {
        return propSpec;
    }

    public void setPropSpec(Integer propSpec) {
        this.propSpec = propSpec;
    }

    private static final long serialVersionUID = 4501126169873492698L;

}
