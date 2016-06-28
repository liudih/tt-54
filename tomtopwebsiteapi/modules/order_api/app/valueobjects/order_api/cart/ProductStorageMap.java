package valueobjects.order_api.cart;

import java.util.List;
import java.util.Map;

import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.product.IProductFragment;
import dto.Currency;
import dto.Storage;

public class ProductStorageMap implements IProductFragment {

	final List<dto.product.ProductStorageMap> productStorageMapLists;
	final Map<Integer, Storage> storageMap;
	private ShippingMethodInformations shippingMethodInformations;
	private Currency currency;

	public ProductStorageMap(
			List<dto.product.ProductStorageMap> productStorageMapLists,
			Map<Integer, Storage> storageMap) {
		this.productStorageMapLists = productStorageMapLists;
		this.storageMap = storageMap;
	}

	public List<dto.product.ProductStorageMap> getProductStorageMapLists() {
		return productStorageMapLists;
	}

	public Map<Integer, Storage> getStorageMap() {
		return storageMap;
	}

	public ShippingMethodInformations getShippingMethodInformations() {
		return shippingMethodInformations;
	}

	public void setShippingMethodInformations(
			ShippingMethodInformations shippingMethodInformations) {
		this.shippingMethodInformations = shippingMethodInformations;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
