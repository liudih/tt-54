package forms.mobile;

import java.io.Serializable;

import utils.ValidataUtils;

public class AddOrderForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cartid;// 购物车ID
	private Integer addrid;// 用户地址ID
	private Integer shipid;// 邮寄ID
	private String msg;// 用户订单留言
	
	
	public String getCartid() {
		return ValidataUtils.validataStr(cartid);
	}
	public void setCartid(String cartid) {
		this.cartid = cartid;
	}
	public Integer getAddrid() {
		return ValidataUtils.validataInt(addrid);
	}
	public void setAddrid(Integer addrid) {
		this.addrid = addrid;
	}
	public Integer getShipid() {
		return ValidataUtils.validataInt(shipid);
	}
	public void setShipid(Integer shipid) {
		this.shipid = shipid;
	}
	public String getMsg() {
		return ValidataUtils.validataStr(msg);
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
