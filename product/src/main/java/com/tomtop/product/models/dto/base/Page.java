package com.tomtop.product.models.dto.base;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = -6287141621150731167L;
	final private List<T> list;
	final private int total;
	final private int page;
	final private int recordPerPage;

	public Page(List<T> list, int total, int page, int recordPerPage) {
		super();
		this.list = list;
		this.total = total;
		this.page = page;
		this.recordPerPage = recordPerPage;
	}

	public List<T> getList() {
		return this.list;
	}

	public int totalCount() {
		return total;
	}

	public int pageNo() {
		return page;
	}

	public int pageSize() {
		return recordPerPage;
	}

	public int totalPages() {
		return totalCount() / pageSize()
				+ ((totalCount() % pageSize() > 0) ? 1 : 0);
	}

	public <S> Page<S> map(Function<T, S> func) {
		return new Page<S>(Lists.transform(list, func), total, page,
				recordPerPage);
	}

	public <S> Page<S> batchMap(Function<List<T>, List<S>> func) {
		return new Page<S>(func.apply(list), total, page, recordPerPage);
	}

	@Override
	public String toString() {
		return "Page [list=" + list + ", total=" + total + ", page=" + page
				+ ", recordPerPage=" + recordPerPage + "]";
	}

}
