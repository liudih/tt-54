package valueobjects.order_api.cart;

import java.io.Serializable;
import java.util.Date;

import valueobjects.price.Price;

/**
 * 用来表示商品的某种异常状态：如下架，没找到等情况
 * 
 * @author kmtong
 *
 */
public class NonExistenceCartItem extends CartItem implements Serializable {
	private static final long serialVersionUID = 1L;
	final CartItem original;

	public NonExistenceCartItem(CartItem ci) {
		original = ci;
	}

	public CartItem getOriginal() {
		return original;
	}

	public String getClistingid() {
		return original.getClistingid();
	}

	public void setClistingid(String clistingid) {
		original.setClistingid(clistingid);
	}

	public String getCuuid() {
		return original.getCuuid();
	}

	public void setCuuid(String cuuid) {
		original.setCuuid(cuuid);
	}

	public String getCmemberemail() {
		return original.getCmemberemail();
	}

	public void setCmemberemail(String cmemberemail) {
		original.setCmemberemail(cmemberemail);
	}

	public String getCtitle() {
		return original.getCtitle();
	}

	public void setCtitle(String ctitle) {
		original.setCtitle(ctitle);
	}

	public String getCimageurl() {
		return original.getCimageurl();
	}

	public void setCimageurl(String cimageurl) {
		original.setCimageurl(cimageurl);
	}

	public String getCurl() {
		return original.getCurl();
	}

	public void setCurl(String curl) {
		original.setCurl(curl);
	}

	public Price getPrice() {
		return original.getPrice();
	}

	public void setPrice(Price price) {
		original.setPrice(price);
	}

	public String getCid() {
		return original.getCid();
	}

	public void setCid(String cid) {
		original.setCid(cid);
	}

	public Integer getIqty() {
		return original.getIqty();
	}

	public void setIqty(Integer iqty) {
		original.setIqty(iqty);
	}

	public Boolean getBismain() {
		return original.getBismain();
	}

	public void setBismain(Boolean bismain) {
		original.setBismain(bismain);
	}

	public Date getDcreatedate() {
		return original.getDcreatedate();
	}

	public void setDcreatedate(Date dcreatedate) {
		original.setDcreatedate(dcreatedate);
	}

	public String getSku() {
		return original.getSku();
	}

	public void setSku(String sku) {
		original.setSku(sku);
	}

	public boolean equals(Object obj) {
		return original.equals(obj);
	}

}
