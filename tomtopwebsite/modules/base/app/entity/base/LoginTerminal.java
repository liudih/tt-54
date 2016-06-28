package entity.base;

/**
 * 登陆终端实体类
 * 
 * @author xiaoch
 *
 */
public class LoginTerminal {

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
