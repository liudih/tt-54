package valueobjects.product;

import java.util.List;

public class CategoryRankProductList implements IProductFragment {
	private List<ProductBadge> list;

	public CategoryRankProductList(List<ProductBadge> list) {
		this.list = list;
	}

	public List<ProductBadge> getList() {
		return list;
	}

	public void setList(List<ProductBadge> list) {
		this.list = list;
	}

}
