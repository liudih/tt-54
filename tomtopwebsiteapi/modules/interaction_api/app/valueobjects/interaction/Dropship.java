package valueobjects.interaction;

import java.io.Serializable;

import dto.product.ProductBase;

public class Dropship implements Serializable  {
	private static final long serialVersionUID = 1L;

	Integer dropshipid;
	String title;
	String url;
	String imageUrl;
	ProductBase productBase;
	String status;

	public Dropship(Integer dropshipid, String title, String url,
			String imageUrl, ProductBase productBase, String status) {
		super();
		this.dropshipid = dropshipid;
		this.title = title;
		this.url = url;
		this.imageUrl = imageUrl;
		this.productBase = productBase;
		this.status = status;
	}

	public Dropship() {
		// TODO Auto-generated constructor stub
	}

	public Integer getDropshipid() {
		return dropshipid;
	}

	public void setDropshipid(Integer dropshipid) {
		this.dropshipid = dropshipid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public ProductBase getProductBase() {
		return productBase;
	}

	public void setProductBase(ProductBase productBase) {
		this.productBase = productBase;
	}

	public void setStatus(String status) {
		
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

}
