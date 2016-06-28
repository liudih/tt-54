package valuesobject.mobile;

/**
 * @author HW
 * @date 2015年4月30日
 * @descr 分页的基类Json
 * @tags base pape json
 */
public class BasePageJson<T> extends BaseListJson<T> {

	private static final long serialVersionUID = 1L;
	private int total;
	private int p;
	private int size;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}