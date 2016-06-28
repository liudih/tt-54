package com.tomtop.search.entiry;

import java.io.Serializable;

/**
 * 仓库实体类
 * @author ztiny
 * @Date 2015-12-19
 */
public class DepotEntity  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3464618961420436651L;
	//仓库ID
	private int depotid;
	//仓库名称
	private String depotName;
	 
	public int getDepotid() {
		return depotid;
	}
	public void setDepotid(int depotid) {
		this.depotid = depotid;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
}
