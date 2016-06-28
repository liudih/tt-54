package dto;

import java.io.Serializable;

/**
 * 登陆终端实体类
 * 
 * @author xiaoch
 *
 */
public class LoginTerminal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	// 登陆终端类型名称
	private String cterminaltype;

	public Integer getIid() {
		return iid;
	}

	public void setIidInteger(Integer iid) {
		this.iid = iid;
	}

	public String getCterminaltype() {
		return cterminaltype;
	}

	public void setCterminaltype(String cterminaltype) {
		this.cterminaltype = cterminaltype;
	}

}
