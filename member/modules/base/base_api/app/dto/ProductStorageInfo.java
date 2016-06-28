package dto;

/**
 * 
 * @ClassName: ProductStorageInfo
 * @Description: TODO(产品仓库信息（用于订单导出）)
 * @author yinfei
 * @date 2015年9月14日
 *
 */
public class ProductStorageInfo {
	private Integer istorageid;
	private String clistingid;
	private String cstoragename;
	
	public Integer getIstorageid() {
		return istorageid;
	}
	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
	public String getCstoragename() {
		return cstoragename;
	}
	public void setCstoragename(String cstoragename) {
		this.cstoragename = cstoragename;
	}
	
}
