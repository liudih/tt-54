package valueobjects.dodocool.interaction.product;

import java.util.List;
import java.util.Map;

import valueobjects.base.Page;
import valueobjects.product.IProductFragment;
import dto.interaction.ProductPost;

public class InteractionProductPost implements IProductFragment {

	final Page<Map<String, List<dto.interaction.ProductPost>>> faqPage;
	final String listingId;
	final Boolean isLogined;
	final String sku;

	public InteractionProductPost(Page<Map<String, List<ProductPost>>> faqPage,
			String listingId, boolean isLogined, String sku) {
		this.faqPage = faqPage;
		this.listingId = listingId;
		this.isLogined = isLogined;
		this.sku = sku;
	}

	public Map<String, List<dto.interaction.ProductPost>> getFaqMap() {
		if (null != faqPage && null != faqPage.getList()
				&& faqPage.getList().size() > 0) {
			return faqPage.getList().get(0);
		} else {
			return null;
		}
	}

	public String getSku() {
		return sku;
	}

	public Page<Map<String, List<dto.interaction.ProductPost>>> getFaqPage() {
		return faqPage;
	}

	public String getListingId() {
		return listingId;
	}

	public Boolean getIsLogined() {
		return isLogined;
	}

}
