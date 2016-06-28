package valueobjects.interaction;

import java.util.List;

import dto.interaction.ProductBrowse;
import valueobjects.product.IProductFragment;

public class MemberBrowseHistory implements IProductFragment {

	final List<ProductBrowse> history;

	public MemberBrowseHistory(List<ProductBrowse> history) {
		this.history = history;
	}

	public List<ProductBrowse> getHistory() {
		return history;
	}

}