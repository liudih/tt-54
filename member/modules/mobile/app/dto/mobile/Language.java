package dto.mobile;

/**
 *	语言实体
 *
 */
public class Language {
	
	private Integer iid;	

	/**
	 * 语言名称
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
		this.cname = cname == null ? null : cname.trim();
	}
}