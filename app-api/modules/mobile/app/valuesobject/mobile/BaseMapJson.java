package valuesobject.mobile;

import java.util.Map;

public class BaseMapJson extends BaseJson {

	private static final long serialVersionUID = 507451294367262808L;

	private Map<String, Object> data;

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
