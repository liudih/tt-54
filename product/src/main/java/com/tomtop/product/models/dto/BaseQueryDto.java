package com.tomtop.product.models.dto;

import com.tomtop.framework.core.utils.Page;

/**
 * 查询DTO基类
 * 
 * @author 李豪
 *
 */
public class BaseQueryDto extends BaseDto {

	private static final long serialVersionUID = 3684606377558055839L;

	/**
	 * 排序字段
	 */
	private String sort;

	/**
	 * 正反序
	 */
	private String order;

	/**
	 * 当前页
	 */
	private int currentPage;

	/**
	 * 每页显示的条数
	 */
	private int pageSize;

	public BaseQueryDto() {
		super();
		this.currentPage = Page.CURRENT_PAGE_DEFAULT; // 当前页 ,缺省值1
		this.pageSize = Page.PAGE_SIZE_DEFAULT; // 每页显示的条数,缺省值10
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}