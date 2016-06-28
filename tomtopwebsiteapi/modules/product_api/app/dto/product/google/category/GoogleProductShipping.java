package dto.product.google.category;

import java.io.Serializable;

public class GoogleProductShipping implements Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 6809125364967618309L;
	private java.lang.String country;
	  private java.lang.String locationGroupName;
	  private java.lang.Long locationId;
	  private java.lang.String postalCode;
	  private GooglePrice price;
	  private java.lang.String region;
	  private java.lang.String service;
	
	  public java.lang.String getCountry() {
		return country;
	}
	public void setCountry(java.lang.String country) {
		this.country = country;
	}
	public java.lang.String getLocationGroupName() {
		return locationGroupName;
	}
	public void setLocationGroupName(java.lang.String locationGroupName) {
		this.locationGroupName = locationGroupName;
	}
	public java.lang.Long getLocationId() {
		return locationId;
	}
	public void setLocationId(java.lang.Long locationId) {
		this.locationId = locationId;
	}
	public java.lang.String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(java.lang.String postalCode) {
		this.postalCode = postalCode;
	}
	public GooglePrice getPrice() {
		return price;
	}
	public void setPrice(GooglePrice price) {
		this.price = price;
	}
	public java.lang.String getRegion() {
		return region;
	}
	public void setRegion(java.lang.String region) {
		this.region = region;
	}
	public java.lang.String getService() {
		return service;
	}
	public void setService(java.lang.String service) {
		this.service = service;
	}

}
