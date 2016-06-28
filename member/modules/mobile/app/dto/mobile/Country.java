package dto.mobile;

/**
 *	国家实体
 */
public class Country {
	Integer iid;
	/**
	 * 国家名称
	 */
	String cname;
	/**
	 * 国家名称简写
	 */
	String cshortname;
	/**
	 * 语言编号
	 */
	Integer ilanguageid;
	/**
	 * 货币类型
	 */
	String ccurrency;
	/**
	 * 默认发货仓库
	 */
	Integer idefaultstorage;
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
		if(cname==null){
			return "";
		}
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getCshortname() {
		if(cshortname==null){
			return "";
		}
		return cshortname;
	}
	public void setCshortname(String cshortname) {
		this.cshortname = cshortname;
	}
	public Integer getIlanguageid() {
		if(ilanguageid==null){
			return 0;
		}
		return ilanguageid;
	}
	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
	public String getCcurrency() {
		if(ccurrency==null){
			return "";
		}
		return ccurrency;
	}
	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}
	public Integer getIdefaultstorage() {
		if(idefaultstorage==null){
			return 0;
		}
		return idefaultstorage;
	}
	public void setIdefaultstorage(Integer idefaultstorage) {
		this.idefaultstorage = idefaultstorage;
	}
}
