package valueobjects.order_api.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import valueobjects.price.Price;

public class CartItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String cid;

	private String cparentId;

	private String clistingid;

	private String cuuid;

	private String cmemberemail;

	private String ctitle;

	private String cimageurl;

	private String curl;

	private Integer iqty;

	private Price price;

	private Boolean bismain;

	private Date dcreatedate;

	private String sku;

	private String addition;

	private Map<String, String> attributeMap;

	private Integer storageID;
	
	private String device;

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}

	public String getCuuid() {
		return cuuid;
	}

	public void setCuuid(String cuuid) {
		this.cuuid = cuuid;
	}

	public String getCmemberemail() {
		return cmemberemail;
	}

	public void setCmemberemail(String cmemberemail) {
		this.cmemberemail = cmemberemail;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCimageurl() {
		return cimageurl;
	}

	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Boolean getBismain() {
		return bismain;
	}

	public void setBismain(Boolean bismain) {
		this.bismain = bismain;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}

	public String getCparentId() {
		return cparentId;
	}

	public void setCparentId(String cparentId) {
		this.cparentId = cparentId;
	}

	public Map<String, String> getAttributeMap() {
		return attributeMap;
	}

	public void setAttributeMap(Map<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}

	public Integer getStorageID() {
		return storageID;
	}

	public void setStorageID(Integer storageID) {
		this.storageID = storageID;
	}
	

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "CartItem [cid=" + cid + ", clistingid=" + clistingid
				+ ", cuuid=" + cuuid + ", cmemberemail=" + cmemberemail
				+ ", ctitle=" + ctitle + ", cimageurl=" + cimageurl + ", curl="
				+ curl + ", iqty=" + iqty + ", price=" + price + "]";
	}

}
