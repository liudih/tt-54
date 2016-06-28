package dto.mobile;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import utils.ValidataUtils;

public class OdItem {

	private Integer iid;// 订单序号
	private Double odtotal;// 订单总价
	private String shipid;// 运单ID
	private Integer status;// 状态
	private String pay;// 付款方式
	private Long cdate;// 创建时间
	private String oid;// 订单编号
	private String purl;// 未支付状态为url链接.其他状态为""值
	private String csymbol;// 币种符号
	private List<OdGoods> oglist;// 订单下商品的集合

	public Integer getIid() {
		return ValidataUtils.validataInt(iid);
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Double getOdtotal() {
		return ValidataUtils.validataDouble(odtotal);
	}

	public void setOdtotal(Double odtotal) {
		this.odtotal = odtotal;
	}

	public String getShipid() {
		return ValidataUtils.validataStr(shipid);
	}

	public void setShipid(String shipid) {
		this.shipid = shipid;
	}

	public Integer getStatus() {
		return ValidataUtils.validataInt(status);
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPay() {
		return ValidataUtils.validataStr(pay);
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public Long getCdate() {
		return ValidataUtils.validataLong(cdate);
	}

	public void setCdate(Long cdate) {
		this.cdate = cdate;
	}

	public String getOid() {
		return ValidataUtils.validataStr(oid);
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCsymbol() {
		if (StringUtils.isBlank(csymbol)) {
			return "US$";
		}
		return csymbol;
	}

	public void setCsymbol(String csymbol) {
		this.csymbol = csymbol;
	}

	public void setOglist(List<OdGoods> oglist) {
		this.oglist = oglist;
	}

	public List<OdGoods> getOglist() {
		return oglist;
	}

	public String getPurl() {
		purl = "";
		if (status == 1) {
			purl = "h5/payod?oid=" + oid;
		}
		return purl;
	}

	public void setPurl(String purl) {
		this.purl = purl;
	}

}
