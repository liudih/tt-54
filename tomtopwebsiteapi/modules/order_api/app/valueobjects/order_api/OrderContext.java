package valueobjects.order_api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import valueobjects.cart.CartItem;
import dto.Country;
import facades.cart.Cart;

public class OrderContext implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Cart cart;
	private String memberEmail;
	private Integer storageId;
	private Double allPrice;
	private Country country;
	private Boolean isFromLogin;
	private int langID;
	private String currency;
	private int siteID;

	private HashMap<String, Object> attribute = new HashMap<String, Object>();

	// add by lijun
	private List<CartItem> items;

	public OrderContext(String memberEmail, Cart cart) {
		this.memberEmail = memberEmail;
		this.cart = cart;
	}

	public OrderContext(String memberEmail, List<CartItem> items) {
		this.memberEmail = memberEmail;
		this.items = items;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public Object get(String key) {
		return attribute.get(key);
	}

	public void put(String key, Object value) {
		attribute.put(key, value);
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public Double getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(Double allPrice) {
		this.allPrice = allPrice;
	}

	public HashMap<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(HashMap<String, Object> attribute) {
		this.attribute = attribute;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Boolean getIsFromLogin() {
		return isFromLogin;
	}

	public void setIsFromLogin(Boolean isFromLogin) {
		this.isFromLogin = isFromLogin;
	}

	public int getLangID() {
		return langID;
	}

	public void setLangID(int langID) {
		this.langID = langID;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

}