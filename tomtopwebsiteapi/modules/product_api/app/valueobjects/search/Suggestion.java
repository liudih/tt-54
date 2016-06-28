package valueobjects.search;

import java.io.Serializable;

/**
 * 关键字推荐信息：定义了每一个关键字推荐的结果信息，可以达到推荐结果如下：
 * <ul>
 * <li>iphone</li>
 * <li>iphone in category</li>
 * <li>iphone in category (4000 results)</li>
 * <li>iphone in category (further information) (4000 results)</li>
 * </ul>
 * 
 * @author kmtong
 *
 */
public class Suggestion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final String keywords;

	final int rank;

	final Integer categoryId;

	final String categoryName;

	final Integer results;

	final String info;

	public Suggestion(String keywords, int rank, Integer categoryId,
			String categoryName, Integer results, String info) {
		super();
		this.keywords = keywords;
		this.rank = rank;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.results = results;
		this.info = info;
	}

	public String getKeywords() {
		return keywords;
	}

	public int getRank() {
		return rank;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public Integer getResults() {
		return results;
	}

	public String getInfo() {
		return info;
	}

}
