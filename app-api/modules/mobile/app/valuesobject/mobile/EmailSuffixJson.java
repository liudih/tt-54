package valuesobject.mobile;

import java.util.List;

import valuesobject.mobile.BaseJson;

import com.google.common.collect.Lists;

public class EmailSuffixJson extends BaseJson {
	private static final long serialVersionUID = 1L;

	private List<dto.mobile.EmailSuffix> list = Lists.newArrayList();

	public List<dto.mobile.EmailSuffix> getList() {
		return list;
	}

	public void setList(List<dto.mobile.EmailSuffix> list) {
		this.list = list;
	}

}
