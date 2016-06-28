package valuesobject.mobile;

import java.util.List;

import com.google.common.collect.Lists;

public class CountryJson extends BaseJson {
	private static final long serialVersionUID = 1L;

	private List<dto.mobile.Country> list = Lists.newArrayList();

	public List<dto.mobile.Country> getList() {
		return list;
	}

	public void setList(List<dto.mobile.Country> list) {
		this.list = list;
	}

}
