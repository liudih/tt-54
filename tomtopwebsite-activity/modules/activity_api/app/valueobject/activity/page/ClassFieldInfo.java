package valueobject.activity.page;

import java.io.Serializable;

/**
 * 类所需自动描述
 * 
 * @author fcl
 *
 */
public class ClassFieldInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String desc;
	/**
	 * Integer.getClass().getName();
	 */
	String type;
	/**
	 * 顺序
	 */
	int priority;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
