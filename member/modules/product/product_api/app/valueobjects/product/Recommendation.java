package valueobjects.product;

import java.util.List;

public class Recommendation implements IProductFragment {

	final List<ProductBadge> recommendations;

	public Recommendation(List<ProductBadge> rec) {
		this.recommendations = rec;
	}

	public List<ProductBadge> getRecommendations() {
		return recommendations;
	}
}
