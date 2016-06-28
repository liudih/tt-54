package utils;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	final private List<T> list;
	final private int total;
	final private int p;
	final private int size;

	public Page(List<T> list, int total, int p, int size) {
		super();
		this.list = list;
		this.total = total;
		this.p = p;
		this.size = size;
	}

	public List<T> getList() {
		return this.list;
	}

	public int getTotal() {
		return total;
	}

	public int getP() {
		return p;
	}

	public int getSize() {
		return size;
	}

	public int totalPages() {
		return getTotal() / getSize() + ((getTotal() % getSize() > 0) ? 1 : 0);
	}

	public <S> Page<S> map(Function<T, S> func) {
		return new Page<S>(Lists.transform(list, func), total, p, size);
	}

	public <S> Page<S> batchMap(Function<List<T>, List<S>> func) {
		return new Page<S>(func.apply(list), total, p, size);
	}

	@Override
	public String toString() {
		return "Page [list=" + list + ", total=" + total + ", page=" + p
				+ ", size=" + size + "]";
	}

}
