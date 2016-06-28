package valueobjects.research.vote;

import java.util.List;

import valueobjects.product.ProductBadge;

public class ProductItemFragment implements IVoteFragment{
	List<ProductBadge> ProductBadgeList;

	public List<ProductBadge> getProductBadgeList() {
		return ProductBadgeList;
	}

	public void setProductBadgeList(List<ProductBadge> productBadgeList) {
		ProductBadgeList = productBadgeList;
	}
	
}
