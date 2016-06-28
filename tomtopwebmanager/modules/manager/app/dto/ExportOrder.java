package dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @ClassName: ExportOrder
 * @Description: TODO(导出订单（用于订单导出）)
 * @author yinfei
 * @date 2015年9月14日
 *
 */
public class ExportOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer iid;
	private String cemail;
	private String ccountry;
	private String ccountrysn;
	private String cprovince;
	private String ccity;
	private String cstreetaddress;
	private String cpostalcode;
	private String ctelephone;
	private String cfirstname;
	private String cmiddlename;
	private String clastname;
	private Integer istorageid;
	private Integer ishippingmethodid;
	private Double fshippingprice;
	private double fordersubtotal;
	private double fextra;
	private double fgrandtotal;
	private String ccartid;
	private Integer istatus;
	private String cpaymentid;
	private String ccurrency;
	private Date dcreatedate;
	private Date dpaymentdate;
	private String corigin;
	private Integer iwebsiteid;
	private String cmemberemail;
	private String cmessage;
	private Integer ishow;
	private String ctransactionid;
	private String cip;
	private String cremark;
	private String creceiveraccount;
	private String cshippingcode;
	private String cordernumber;
	private String cvhost;
	private String warehouse;//所在仓库
	private String category;//类目
	private String SKU;//SKU
	private Double listingPrice;//原价
	private Double price;//售价
	private Integer qty;//数量

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCcountry() {
		return ccountry;
	}

	public void setCcountry(String ccountry) {
		this.ccountry = ccountry;
	}

	public String getCcountrysn() {
		return ccountrysn;
	}

	public void setCcountrysn(String ccountrysn) {
		this.ccountrysn = ccountrysn;
	}

	public String getCprovince() {
		return cprovince;
	}

	public void setCprovince(String cprovince) {
		this.cprovince = cprovince;
	}

	public String getCcity() {
		return ccity;
	}

	public void setCcity(String ccity) {
		this.ccity = ccity;
	}

	public String getCstreetaddress() {
		return cstreetaddress;
	}

	public void setCstreetaddress(String cstreetaddress) {
		this.cstreetaddress = cstreetaddress;
	}

	public String getCpostalcode() {
		return cpostalcode;
	}

	public void setCpostalcode(String cpostalcode) {
		this.cpostalcode = cpostalcode;
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone;
	}

	public String getCfirstname() {
		return cfirstname;
	}

	public void setCfirstname(String cfirstname) {
		this.cfirstname = cfirstname;
	}

	public String getCmiddlename() {
		return cmiddlename;
	}

	public void setCmiddlename(String cmiddlename) {
		this.cmiddlename = cmiddlename;
	}

	public String getClastname() {
		return clastname;
	}

	public void setClastname(String clastname) {
		this.clastname = clastname;
	}

	public Integer getIstorageid() {
		return istorageid;
	}

	public void setIstorageid(Integer istorageid) {
		this.istorageid = istorageid;
	}

	public Integer getIshippingmethodid() {
		return ishippingmethodid;
	}

	public void setIshippingmethodid(Integer ishippingmethodid) {
		this.ishippingmethodid = ishippingmethodid;
	}

	public Double getFshippingprice() {
		return fshippingprice;
	}

	public void setFshippingprice(Double fshippingprice) {
		this.fshippingprice = fshippingprice;
	}

	public double getFordersubtotal() {
		return fordersubtotal;
	}

	public void setFordersubtotal(double fordersubtotal) {
		this.fordersubtotal = fordersubtotal;
	}

	public double getFextra() {
		return fextra;
	}

	public void setFextra(double fextra) {
		this.fextra = fextra;
	}

	public double getFgrandtotal() {
		return fgrandtotal;
	}

	public void setFgrandtotal(double fgrandtotal) {
		this.fgrandtotal = fgrandtotal;
	}

	public String getCcartid() {
		return ccartid;
	}

	public void setCcartid(String ccartid) {
		this.ccartid = ccartid;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDpaymentdate() {
		return dpaymentdate;
	}

	public void setDpaymentdate(Date dpaymentdate) {
		this.dpaymentdate = dpaymentdate;
	}

	public String getCpaymentid() {
		return cpaymentid;
	}

	public void setCpaymentid(String cpaymentid) {
		this.cpaymentid = cpaymentid;
	}

	public String getCorigin() {
		return corigin;
	}

	public void setCorigin(String corigin) {
		this.corigin = corigin;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public String getCmessage() {
		return cmessage;
	}

	public void setCmessage(String cmessage) {
		this.cmessage = cmessage;
	}

	public Integer getIshow() {
		return ishow;
	}

	public void setIshow(Integer ishow) {
		this.ishow = ishow;
	}

	public String getCtransactionid() {
		return ctransactionid;
	}

	public void setCtransactionid(String ctransactionid) {
		this.ctransactionid = ctransactionid;
	}

	public String getCip() {
		return cip;
	}

	public void setCip(String cip) {
		this.cip = cip;
	}

	public String getCremark() {
		return cremark;
	}

	public void setCremark(String cremark) {
		this.cremark = cremark;
	}

	public String getCreceiveraccount() {
		return creceiveraccount;
	}

	public void setCreceiveraccount(String creceiveraccount) {
		this.creceiveraccount = creceiveraccount;
	}

	public String getCshippingcode() {
		return cshippingcode;
	}

	public void setCshippingcode(String cshippingcode) {
		this.cshippingcode = cshippingcode;
	}

	public String getCordernumber() {
		return cordernumber;
	}

	public void setCordernumber(String cordernumber) {
		this.cordernumber = cordernumber;
	}

	public String getCvhost() {
		return cvhost;
	}

	public void setCvhost(String cvhost) {
		this.cvhost = cvhost;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}

	public Double getListingPrice() {
		return listingPrice;
	}

	public void setListingPrice(Double listingPrice) {
		this.listingPrice = listingPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

}
