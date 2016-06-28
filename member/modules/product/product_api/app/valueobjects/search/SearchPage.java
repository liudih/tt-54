package valueobjects.search;

import java.util.List;

import valueobjects.base.Page;

public class SearchPage<T> extends Page<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6927464573375583619L;

	final ISearchAggValue iSearchAggValue;

	public SearchPage(List<T> list, int total, int page, int recordPerPage,
			ISearchAggValue iSearchAggValue) {
		super(list, total, page, recordPerPage);
		this.iSearchAggValue = iSearchAggValue;
	}

	public ISearchAggValue getiSearchAggValue() {
		return iSearchAggValue;
	}

}
