
package com.rabbit.dto.search;


/**
 * 仓库实体类
 * @author ztiny
 * @Date 2015-12-19
 */
public class DepotEntity {
	//仓库ID
	private Integer depotid;
	//仓库名称
	private String depotName;
//	//邮寄方式
//	private List<ShippingMethodBo> shippings = new ArrayList<ShippingMethodBo>();
	
	public Integer getDepotid() {
		return depotid;
	}
	public void setDepotid(Integer depotid) {
		this.depotid = depotid;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
//	public List<ShippingMethodBo> getShippings() {
//		return shippings;
//	}
//	public void setShippings(List<ShippingMethodBo> shippings) {
//		this.shippings = shippings;
//	};
	 
}
