package valuesobject.mobile;

import java.util.List;

import com.google.common.collect.Lists;

public class LanguageJson extends BaseJson {
	private static final long serialVersionUID = 1L;

	private List<dto.mobile.Language> list = Lists.newArrayList();

	public List<dto.mobile.Language> getList() {
		return list;
	}

	public void setList(List<dto.mobile.Language> list) {
		this.list = list;
	}

}
