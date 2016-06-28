package forms;

import java.util.List;

public class ProductEntityMapSingleForm {
	private boolean check;
	private Integer ikey;
	private Integer iid;
	private List<Integer> valueList;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public Integer getIkey() {
		return ikey;
	}

	public void setIkey(Integer ikey) {
		this.ikey = ikey;
	}

	public List<Integer> getValueList() {
		return valueList;
	}

	public void setValueList(List<Integer> valueList) {
		this.valueList = valueList;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	@Override
	public String toString() {
		return "ProductEntityMapSingleForm [check=" + check + ", ikey=" + ikey
				+ ", iid=" + iid + ", valueList=" + valueList + "]";
	}

}
