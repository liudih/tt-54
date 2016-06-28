package valueobjects.interaction;


import java.io.Serializable;
import java.util.Map;

import dto.interaction.PriceMatch;
import valueobjects.base.Page;

public class PriceBadge implements Serializable {
	private static final long serialVersionUID = 1L;
	final Page<PriceMatch> priceMatch;
	final Map<String,String> imageUrl;
	final Map<String,String> titleUrl;
	final Map<String,String> productUrl;
	public PriceBadge(Page<PriceMatch> priceMatch, Map<String, String> imageUrl,Map<String,String> titleUrl,Map<String,String> productUrl) {
		super();
		this.priceMatch = priceMatch;
		this.imageUrl = imageUrl;
		this.titleUrl=titleUrl;
		this.productUrl=productUrl;
	}
	public Page<PriceMatch> getPriceMatch() {
		return priceMatch;
	}
	public Map<String, String> getImageUrl() {
		return imageUrl;
	}
	public Map<String, String> getTitleUrl() {
		return titleUrl;
	}
	public Map<String, String> getProductUrl() {
		return productUrl;
	}
	

}
