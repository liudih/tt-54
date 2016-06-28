package events.product;

import java.io.Serializable;

import com.website.dto.product.MultiProduct;
import com.website.dto.product.Product;

/**
 * 判断多属性时 单属性为空，单属性时多属性为空
 *
 * @author Administrator
 *
 */
public class ProductUpdateEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum ProductHandleType {
		insert, update, delete
	}

	ProductHandleType currentHandleType;

	private Product product;
	private MultiProduct mproduct;
	private String listingId;

	public Product getProduct() {
		return product;
	}

	public MultiProduct getMproduct() {
		return mproduct;
	}

	public String getListingId() {
		return listingId;
	}

	public ProductHandleType getCurrentHandleType() {
		return this.currentHandleType;
	}

	public ProductUpdateEvent(Product pitem, String listingId,
			ProductHandleType handleType) {
		this.listingId = listingId;
		this.currentHandleType = handleType;
		if (pitem instanceof MultiProduct) {
			this.mproduct = (MultiProduct) pitem;
		} else {
			this.product = pitem;
		}
	}

	public ProductUpdateEvent(String listingId, ProductHandleType handleType) {
		this(null, listingId, handleType);
	}
}
