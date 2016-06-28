package dto.mobile;

/**
 *	邮箱后缀实体
 */
public class EmailSuffix {
	private Integer iid;
	/**
	 * 邮箱后缀名称
	 */
	private String cname;
	
	public Integer getIid() {
		if(iid==null){
			return 0;
		}
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public String getCname() {
		if(cname==null) return "";
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
}
