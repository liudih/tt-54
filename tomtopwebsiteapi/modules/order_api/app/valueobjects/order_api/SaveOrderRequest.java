package valueobjects.order_api;

import java.io.Serializable;
import java.util.Map;

import valueobjects.member.MemberInSession;
import dto.member.MemberAddress;

public class SaveOrderRequest implements Serializable {
	private Map<String, Integer> listingQtyMap;
	private MemberInSession mis;
	private int websiteID;
	private int languageID;
	private String currency;
	private MemberAddress address;
	private String vhost;
	private String ip;
	private String message;
	private String origin;
	private Integer shippingMethodID;
	private String detailProviderID;
	private Boolean isNeedShippingMethod;

	public Map<String, Integer> getListingQtyMap() {
		return listingQtyMap;
	}

	public void setListingQtyMap(Map<String, Integer> listingQtyMap) {
		this.listingQtyMap = listingQtyMap;
	}

	public MemberInSession getMis() {
		return mis;
	}

	public void setMis(MemberInSession mis) {
		this.mis = mis;
	}

	public int getWebsiteID() {
		return websiteID;
	}

	public void setWebsiteID(int websiteID) {
		this.websiteID = websiteID;
	}

	public int getLanguageID() {
		return languageID;
	}

	public void setLanguageID(int languageID) {
		this.languageID = languageID;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public MemberAddress getAddress() {
		return address;
	}

	public void setAddress(MemberAddress address) {
		this.address = address;
	}

	public String getVhost() {
		return vhost;
	}

	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Integer getShippingMethodID() {
		return shippingMethodID;
	}

	public void setShippingMethodID(Integer shippingMethodID) {
		this.shippingMethodID = shippingMethodID;
	}

	public String getDetailProviderID() {
		return detailProviderID;
	}

	public void setDetailProviderID(String detailProviderID) {
		this.detailProviderID = detailProviderID;
	}

	public Boolean getIsNeedShippingMethod() {
		return isNeedShippingMethod;
	}

	public void setIsNeedShippingMethod(Boolean isNeedShippingMethod) {
		this.isNeedShippingMethod = isNeedShippingMethod;
	}

}
