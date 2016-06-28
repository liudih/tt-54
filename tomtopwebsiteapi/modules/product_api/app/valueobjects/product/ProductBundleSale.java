package valueobjects.product;

import java.util.Collection;
import java.util.List;

import play.Logger;

import com.google.common.collect.Collections2;

public class ProductBundleSale implements IProductFragment {
	final List<dto.ProductBundleSaleLite> productBundleSales;
	String mainListingId = "";

	public ProductBundleSale(
			List<dto.ProductBundleSaleLite> productBundleSales,
			String mainlistingId) {
		this.productBundleSales = productBundleSales;
		this.mainListingId = mainlistingId;
	}

	public dto.ProductBundleSaleLite getProductListing() {
		if (this.productBundleSales.size() <= 1) {
			return null;
		}
		Collection<dto.ProductBundleSaleLite> list = Collections2.filter(
				productBundleSales, (pBundleSales) -> {
					if (pBundleSales.getIsMain() == true) {
						return true;
					}
					return false;
				});
		if (null == list || list.size() <= 0) {
			Logger.error("can not find bundlesale main product: "
					+ mainListingId);
			return null;
		}
		return (dto.ProductBundleSaleLite) list.toArray()[0];
	}

	public List<dto.ProductBundleSaleLite> getProductBundleSales() {
		return productBundleSales;
	}

	public Boolean hasBundleSale() {
		return (productBundleSales.size() > 1);
	}
}
