package valueobjects.interaction;

import java.io.Serializable;
import java.util.List;

/**
 * 查询super deal数据的查询条件
 * @author jiang
 */
public class SuperDealContext implements Serializable  {
	private static final long serialVersionUID = 1L;

	Integer siteId;
	Integer languageId;
	Integer browseLimit;
	Integer saleLimit;
	Integer browseTimeRange;
	Integer saleTimeRange;
	List<Double> priceRange;
	List<Double> discountRange;

	public SuperDealContext(Integer siteId, Integer languageId,
			Integer browseLimit, Integer saleLimit, Integer browseTimeRange,
			Integer saleTimeRange, List<Double> priceRange,
			List<Double> discountRange) {
		super();
		this.siteId = siteId;
		this.languageId = languageId;
		this.browseLimit = browseLimit;
		this.saleLimit = saleLimit;
		this.browseTimeRange = browseTimeRange;
		this.saleTimeRange = saleTimeRange;
		this.priceRange = priceRange;
		this.discountRange = discountRange;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public Integer getBrowseTimeRange() {
		return browseTimeRange;
	}

	public void setBrowseTimeRange(Integer browseTimeRange) {
		this.browseTimeRange = browseTimeRange;
	}

	public Integer getSaleTimeRange() {
		return saleTimeRange;
	}

	public void setSaleTimeRange(Integer saleTimeRange) {
		this.saleTimeRange = saleTimeRange;
	}

	public List<Double> getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(List<Double> priceRange) {
		this.priceRange = priceRange;
	}

	public Integer getBrowseLimit() {
		return browseLimit;
	}

	public void setBrowseLimit(Integer browseLimit) {
		this.browseLimit = browseLimit;
	}

	public Integer getSaleLimit() {
		return saleLimit;
	}

	public void setSaleLimit(Integer saleLimit) {
		this.saleLimit = saleLimit;
	}

	public List<Double> getDiscountRange() {
		return discountRange;
	}

	public void setDiscountRange(List<Double> discountRange) {
		this.discountRange = discountRange;
	}

}
