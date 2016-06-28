package valueobjects.order;

import java.io.Serializable;

public class ShippingMethod implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String title;
	private String description;
	private Double price;
	private Boolean isStrack;
	private Integer id;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsStrack() {
		return isStrack;
	}

	public void setIsStrack(Boolean isStrack) {
		this.isStrack = isStrack;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
