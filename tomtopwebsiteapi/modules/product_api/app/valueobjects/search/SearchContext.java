package valueobjects.search;

import java.io.Serializable;
import java.util.List;

import play.libs.F;
import play.mvc.Http.Context;
import valueobjects.base.Page;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * 搜索上下文：一次搜索会创建一个上下文，为了方便调整搜索条件、过滤、排序、翻页等二次操作。 一次“新”的搜索根据常理定义如下：
 * 
 * <ul>
 * <li>输入一个关键字，敲搜索按钮</li>
 * <li>按品类菜单</li>
 * </ul>
 * 
 * 是否新建一个搜索上下文，取决于是否会对返回结果有二次操作。
 * 
 * @author kmtong
 *
 */
public class SearchContext implements Serializable {

	private static final long serialVersionUID = 970283913627066406L;

	/**
	 * UUID of this search context
	 */
	String id;

	List<ISearchCriteria> criteria = Lists.newArrayList();

	List<ISortOrder> sort = Lists.newArrayList();

	List<ISearchFilter> filter = Lists.newArrayList();

	List<IScriptField> scriptField = Lists.newArrayList();

	List<ISearchUIProvider> uiProviders;
	
	ISearchAggregation iSearchAggregation;

	transient Context httpContext;

	int page = 0;

	int pageSize = 20;

	Page<String> result;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ISearchCriteria> getCriteria() {
		return criteria;
	}

	public void setCriteria(List<ISearchCriteria> criteria) {
		this.criteria = criteria;
	}

	public List<IScriptField> getScriptField() {
		return scriptField;
	}

	public void setScriptField(List<IScriptField> scriptField) {
		this.scriptField = scriptField;
	}

	public List<ISortOrder> getSort() {
		return sort;
	}

	public void setSort(List<ISortOrder> sort) {
		this.sort = sort;
	}

	public List<ISearchFilter> getFilter() {
		return filter;
	}

	public void setFilter(List<ISearchFilter> filter) {
		this.filter = filter;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public <T> List<T> extract(Class<T> clazz) {
		List<Object> all = Lists.newArrayList(getCriteria());
		Iterables.addAll(all, getSort());
		Iterables.addAll(all, getFilter());
		return FluentIterable.from(all).filter(clazz).toList();
	}

	public <T> F.Option<T> extractFirst(Class<T> clazz) {
		List<T> list = extract(clazz);
		T first = Iterables.getFirst(list, null);
		if (first != null) {
			return F.Option.Some(first);
		}
		return F.Option.<T> None();
	}

	public List<ISearchUIProvider> getUIProviders() {
		return uiProviders;
	}

	public void setUIProviders(List<ISearchUIProvider> uiProviders) {
		this.uiProviders = uiProviders;
	}

	public void setResult(Page<String> result) {
		this.result = result;
	}

	public Page<String> getResult() {
		return result;
	}

	public ISearchAggregation getiSearchAggregation() {
		return iSearchAggregation;
	}

	public void setiSearchAggregation(ISearchAggregation iSearchAggregation) {
		this.iSearchAggregation = iSearchAggregation;
	}

}
