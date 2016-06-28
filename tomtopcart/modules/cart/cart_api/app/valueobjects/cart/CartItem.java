package valueobjects.cart;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class CartItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;	//标示的唯一id：由listingid组成

	private String clistingid;

	private String ctitle; //标题

	private String cimageurl; //图片地址

	private String curl;//跳转链接

	private Integer iqty;//数量

	private Price price;//商品价格

	private String sku;//商品sku

	private Map<String, String> attributeMap; //商品属性

	private Integer storageID;//仓库id
	
	private Integer istatus;	//产品状态
	
	private String ccartbaseid;
	private String cdevice;
	private String cid;
	private Boolean bismain;
	private String cuuid;
	private String cmemberemail;
	private Date dcreatedate;
	private String addition;
	private Integer iitemtype;

	public Integer getIitemtype() {
		return iitemtype;
	}

	public void setIitemtype(Integer iitemtype) {
		this.iitemtype = iitemtype;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
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

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Boolean getBismain() {
		return bismain;
	}

	public void setBismain(Boolean bismain) {
		this.bismain = bismain;
	}

	public String getCcartbaseid() {
		return ccartbaseid;
	}

	public void setCcartbaseid(String ccartbaseid) {
		this.ccartbaseid = ccartbaseid;
	}

	public String getCdevice() {
		return cdevice;
	}

	public void setCdevice(String cdevice) {
		this.cdevice = cdevice;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
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

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

}
