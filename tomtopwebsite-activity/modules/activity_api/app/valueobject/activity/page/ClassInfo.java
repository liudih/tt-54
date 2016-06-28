package valueobject.activity.page;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述
 * 
 * @author fcl
 *
 */
public class ClassInfo implements Serializable {
	private static final long serialVersionUID = 6994225851040262463L;
	String name;
	String desc;
	List<ClassFieldInfo> selfField;
	List<ClassFieldInfo> extraField;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ClassFieldInfo> getSelfField() {
		return selfField;
	}

	public void setSelfField(List<ClassFieldInfo> selfField) {
		this.selfField = selfField;
	}

	public List<ClassFieldInfo> getExtraField() {
		return extraField;
	}

	public void setExtraField(List<ClassFieldInfo> extraField) {
		this.extraField = extraField;
	}

}
