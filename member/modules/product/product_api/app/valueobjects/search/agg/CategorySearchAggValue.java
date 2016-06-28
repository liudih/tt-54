package valueobjects.search.agg;

import java.io.Serializable;
import java.util.Map;

import valueobjects.search.ISearchAggValue;

public class CategorySearchAggValue implements ISearchAggValue, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7348647203108852949L;
	private Map<Integer, Long> categoryCounts;

	public CategorySearchAggValue(Map<Integer, Long> categoryCounts) {
		this.categoryCounts = categoryCounts;
	}

	/**
	 * 
	 * 
	 * @return categoryid,count
	 */
	public Map<Integer, Long> getCategoryCounts() {
		return categoryCounts;
	}

}
