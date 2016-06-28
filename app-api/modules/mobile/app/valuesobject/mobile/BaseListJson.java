package valuesobject.mobile;

import java.util.List;

import com.google.common.collect.Lists;

public class BaseListJson<E> extends BaseJson {

	private static final long serialVersionUID = 6444397332996398664L;

	private List<E> list = Lists.newArrayList();

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

}
