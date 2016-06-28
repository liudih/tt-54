package services.product;

import interceptors.CacheResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.category.CategoryBackgroundImagesMapper;
import mapper.category.CategoryFilterAttributeMapper;
import mapper.category.FilterAttributeValueMapper;
import mapper.product.CategoryBaseMapper;
import mapper.product.CategoryLabelNameMapper;
import mapper.product.CategoryNameMapper;
import mapper.product.CategoryWebsiteMapper;
import mapper.product.ProductCategoryMapperMapper;

import org.elasticsearch.search.SearchHit;

import play.Logger;
import services.base.FoundationService;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import valueobjects.base.Page;
import valueobjects.category.CategoryLabelVo;
import valueobjects.category.SubscribeCategory;
import valueobjects.product.category.CategoryAttributeMap;
import valueobjects.product.category.CategoryComposite;
import valueobjects.product.category.CategoryItem;
import valueobjects.product.category.CategoryMessage;
import valueobjects.product.category.CategoryReverseComposite;
import valueobjects.search.ISearchCriteria;
import valueobjects.search.ISearchFilter;
import valueobjects.search.SearchContext;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.website.dto.category.WebsiteCategory;

import context.WebContext;
import dao.product.IProductCategoryEnquiryDao;
import dto.AttributeKeyItem;
import dto.AttributeValueItem;
import dto.Category;
import dto.CategoryAttribute;
import dto.category.CategoryBackgroundImages;
import dto.product.CategoryBase;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductCategoryMapper;
import extensions.InjectorInstance;

public class CategoryEnquiryService implements ICategoryEnquiryService {

	@Inject
	SearchContextFactory searchFactory;
	@Inject
	ISearchService genericSearch;
	@Inject
	CategoryNameMapper categoryNameMapper;

	@Inject
	CategoryBaseMapper categoryBaseMapper;

	@Inject
	CategoryWebsiteMapper cpmapper;

	@Inject
	CategoryFilterAttributeMapper attributeMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	FilterAttributeValueMapper filterAttributeValueMapper;

	@Inject
	ProductCategoryMapperMapper pcMapper;

	@Inject
	CategoryBackgroundImagesMapper categoryBackgroundImagesMapper;

	@Inject
	CategoryLabelNameMapper categoryLabelNameMapper;

	@Inject
	IProductCategoryEnquiryDao productCategoryEnquiryDao;

	final boolean display = true;

	public CategoryName getCategoryNameByCategoryId(Integer icategoryid) {
		return categoryNameMapper.getNameByCategoryId(icategoryid);
	}

	@CacheResult("category")
	public List<CategoryComposite> getRootCategories(final int language,
			final int siteId) {
		List<CategoryWebsiteWithName> pf = cpmapper.getRootCategoriesByDisplay(
				language, siteId, display);
		List<Integer> catIds = Lists.transform(pf, c -> c.getIid());
		ListMultimap<Integer, CategoryComposite> children = getChildCategories(
				catIds, language, siteId, -1);
		List<CategoryComposite> result = FluentIterable
				.from(pf)
				.transform(
						self -> {
							List<CategoryComposite> child = children.get(self
									.getIid());
							if (child == null) {
								child = Lists.newArrayList();
							}

							return new CategoryComposite(self, child);
						}).toList();
		return result;
	}

	public List<CategoryComposite> getAllSimpleCategories(final int language,
			final int siteId) {
		List<CategoryWebsiteWithName> pf = cpmapper.getRootCategories(language,
				siteId);
		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, CategoryComposite>() {
					@Override
					public CategoryComposite apply(CategoryWebsiteWithName self) {
						return new CategoryComposite(self, getChildCategory(
								self.getIid(), language, siteId, -1));
					}
				});
	}

	private List<CategoryComposite> getChildCategory(final int categoryId,
			final int language, final int siteId, final int depth) {
		List<CategoryWebsiteWithName> pf = cpmapper.getChildCategoriesAll(
				categoryId, language, siteId);
		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, CategoryComposite>() {
					@Override
					public CategoryComposite apply(CategoryWebsiteWithName self) {
						if (depth == 0) {
							return new CategoryComposite(self,
									new LinkedList<CategoryComposite>());
						}
						return new CategoryComposite(self, getChildCategory(
								self.getIid(), language, siteId, depth - 1));
					}
				});
	}

	/**
	 * Retrieve all Children (recursively) for <code>depth</code> levels. A
	 * negative depth means recursive all down to the bottom.
	 *
	 * @param categoryId
	 * @param language
	 * @param siteId
	 * @param depth
	 * @return
	 */
	@CacheResult("category")
	public List<CategoryComposite> getChildCategories(final int categoryId,
			final int language, final int siteId, final int depth) {
		List<CategoryWebsiteWithName> pf = cpmapper.getChildCategories(
				categoryId, language, siteId);
		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, CategoryComposite>() {
					@Override
					public CategoryComposite apply(CategoryWebsiteWithName self) {
						if (depth == 0) {
							return new CategoryComposite(self,
									new LinkedList<CategoryComposite>());
						}
						return new CategoryComposite(self, getChildCategories(
								self.getIid(), language, siteId, depth - 1));
					}
				});
	}

	@CacheResult("category")
	public ListMultimap<Integer, CategoryComposite> getChildCategories(
			final List<Integer> parentIds, final int language,
			final int siteId, final int depth) {
		if (parentIds == null || parentIds.size() == 0) {
			return LinkedListMultimap.create();
		}
		List<CategoryWebsiteWithName> pf = cpmapper
				.getMultiChildCategoriesByDisplay(parentIds, language, siteId,
						display);
		List<Integer> nextParentsIds = Lists.transform(pf, c -> c.getIid());
		if (depth != 0 && nextParentsIds.size() > 0) {
			ListMultimap<Integer, CategoryComposite> children = getChildCategories(
					nextParentsIds, language, siteId, depth - 1);
			ListMultimap<Integer, CategoryComposite> result = Multimaps
					.transformValues(
							FluentIterable.from(pf)
									.index(c -> c.getIparentid()),
							self -> {
								List<CategoryComposite> child = children
										.get(self.getIid());
								if (child == null) {
									child = Lists.newArrayList();
								}
								return new CategoryComposite(self, child);
							});
			return result;
		} else {
			ListMultimap<Integer, CategoryComposite> result = Multimaps
					.transformValues(
							FluentIterable.from(pf)
									.index(c -> c.getIparentid()),
							self -> new CategoryComposite(self, Lists
									.newArrayList()));
			return result;
		}
	}

	@CacheResult("category")
	public CategoryReverseComposite getReverseCategory(final int categoryId,
			final int language, final int siteId) {

		CategoryWebsiteWithName pf = cpmapper.getCategory(categoryId, language,
				siteId);
		if (pf != null) {
			List<CategoryComposite> immediateChildren = getChildCategories(
					pf.getIid(), language, siteId, 0);
			if (pf.getIparentid() != null) {
				CategoryWebsiteWithName cw = cpmapper.getCategoryWebsiteById(pf
						.getIparentid());
				return new CategoryReverseComposite(pf, getReverseCategory(
						cw.getIcategoryid(), language, siteId),
						immediateChildren);
			}
			return new CategoryReverseComposite(pf, null, immediateChildren);
		}
		return null;
	}

	@Override
	public CategoryReverseComposite getReverseCategory(final int categoryId,
			WebContext context) {
		int language = foundationService.getLanguage(context);
		int siteId = foundationService.getSiteID(context);
		return this.getReverseCategory(categoryId, language, siteId);
	}

	/**
	 * loop and query the db for all parent categories (excluding itself)
	 *
	 * @param categoryId
	 * @param language
	 * @param siteId
	 * @return
	 */
	@CacheResult("category")
	public List<CategoryWebsiteWithName> getAllParentCategories(
			final int categoryId, final int language, final int siteId) {
		List<CategoryWebsiteWithName> all = Lists.newArrayList();
		Integer icategoryId = categoryId;
		while (icategoryId != null) {
			CategoryWebsiteWithName pf = cpmapper.getCategory(icategoryId,
					language, siteId);
			if (pf == null) {
				break;
			}
			if (pf.getIcategoryid() != categoryId) {
				all.add(pf);
			}
			icategoryId = pf.getIparentid();
		}
		return all;
	}

	public List<CategoryBase> getAllCategory() {
		return categoryBaseMapper.getAllCategory();
	}

	public CategoryName getCategoryNameByCategoryIdAndLanguage(
			Integer categoryId, Integer language) {
		return categoryNameMapper.getCategoryNameByCategoryIdAndLanguage(
				categoryId, language);
	}

	@CacheResult("category")
	public CategoryName getCategoryNameByPath(String path, int language,
			int siteId) {
		return categoryNameMapper.getCategoryNameByPathAndLanguage(path,
				language, siteId);
	}

	public CategoryName getCategoryNameByPath(String path, WebContext wc) {

		return categoryNameMapper.getCategoryNameByPathAndLanguage(path,
				foundationService.getLanguage(wc),
				this.foundationService.getSiteID(wc));
	}

	@CacheResult("category")
	public CategoryAttributeMap getCategoryAttributeMapByPath(String path) {
		List<CategoryAttribute> list = attributeMapper.getAttributeList(path,
				foundationService.getLanguage());
		LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer, String>>> msHashMap = new LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<Integer, String>>>();
		LinkedHashMap<String, LinkedHashMap<Integer, String>> keyvalueMap = new LinkedHashMap<String, LinkedHashMap<Integer, String>>();
		if (null != list && !list.isEmpty()) {
			for (CategoryAttribute categoryAttribute : list) {
				if (null == msHashMap.get(categoryAttribute.getAttributeKey())) {
					Integer keyid = categoryAttribute.getAttributeKeyId();
					Integer valueid = categoryAttribute.getAttributeValueId();
					LinkedHashMap<Integer, String> valueMap = new LinkedHashMap<Integer, String>();
					String keyvalue = categoryAttribute.getAttributeKey();
					if (keyvalueMap.containsKey(keyvalue)) {
						valueMap = keyvalueMap.get(keyvalue);
					} else {
						keyvalueMap = new LinkedHashMap<String, LinkedHashMap<Integer, String>>();
					}
					valueMap.put(valueid, categoryAttribute.getAttributeValue());
					keyvalueMap.put(keyvalue, valueMap);
					msHashMap.put(keyid, keyvalueMap);
				}
			}
		}

		return new CategoryAttributeMap(msHashMap);

	}

	@CacheResult("category")
	public List<CategoryComposite> getChildCategoriesByPath(final String path,
			final int language, final int siteId) {
		List<CategoryWebsiteWithName> pf = cpmapper.getChildCategoriesByPath(
				path, language, siteId);
		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, CategoryComposite>() {
					@Override
					public CategoryComposite apply(CategoryWebsiteWithName self) {
						return new CategoryComposite(self, getChildCategories(
								self.getIid(), language, siteId, -1));
					}
				});
	}

	@Override
	public List<CategoryWebsiteWithName> getChildCategoriesByPath(String path,
			WebContext webContext) {
		int siteId = foundationService.getSiteID(webContext);
		int languageid = foundationService.getLanguage(webContext);
		return cpmapper.getChildCategoriesByPath(path, languageid, siteId);

	}

	@CacheResult("category")
	public List<WebsiteCategory> getAllCategories(int siteid, int languageid) {
		List<CategoryWebsiteWithName> list = cpmapper.getCategories(languageid,
				siteid);
		return Lists.transform(list, obj -> {
			WebsiteCategory citem = new WebsiteCategory();
			citem.setId(obj.getIid());
			citem.setContent(obj.getCcontent());
			citem.setLanguageId(languageid);
			citem.setLevel(obj.getIlevel());
			citem.setName(obj.getCname());
			citem.setParentId(obj.getIparentid());
			citem.setPath(obj.getCpath());
			citem.setPosition(obj.getIposition());
			citem.setWebsiteId(obj.getIwebsiteid());
			citem.setCategoryId(obj.getIcategoryid());
			return citem;
		});
	}

	/**
	 * @param listingID
	 * @param languageid
	 * @param websiteid
	 * @return List<CategoryItem>
	 * @Title: getCategoriesByListingids
	 * @Description: TODO(根据listingID取不重复的level为1的category)
	 * @author liudi
	 */
	@CacheResult("category")
	public List<CategoryItem> getCategoriesByListingids(List<String> listingID,
			int languageid, int websiteid) {
		if (listingID == null || listingID.size() == 0) {
			return Lists.newArrayList();
		}
		List<CategoryWebsiteWithName> list = cpmapper
				.getCategoriesByListingids(listingID, languageid, websiteid);
		List<CategoryItem> clist = Lists.transform(
				list,
				obj -> {
					if (obj.getIlevel() > 1) {
						obj = getCategoryForLevelOne(obj.getIparentid(),
								languageid, websiteid);
						// Logger.debug("the level>1 category name is  {}",obj.getCname());
					}
					CategoryItem citem = new CategoryItem();
					citem.setChildrenCount(obj.getIchildrencount());
					citem.setDescription(obj.getCcontent());
					citem.setLanguageId(languageid);
					citem.setLevel(obj.getIlevel());
					citem.setName(obj.getCname());
					citem.setParentId(obj.getIparentid());
					citem.setPath(obj.getCpath());
					citem.setPosition(obj.getIposition());
					citem.setId(obj.getIcategoryid());
					citem.setCategoryid(obj.getIcategoryid());
					return citem;
				});
		// 过滤重复key的list
		HashMap<String, CategoryItem> mapCategory = new HashMap<String, CategoryItem>();
		for (CategoryItem c : clist) {
			mapCategory.put(c.getName(), c);
		}
		List<CategoryItem> clist2 = new ArrayList<CategoryItem>();
		for (String name : mapCategory.keySet()) {
			if (mapCategory.get(name) != null) {
				clist2.add(mapCategory.get(name));
			}
		}
		return clist2;
	}

	@CacheResult("category")
	public Multimap<String, CategoryWebsiteWithName> getCategoriesByListingIdsGroupByListingId(
			List<String> listingIds, int languageid, int websiteid) {
		List<ProductCategoryMapper> pcm = pcMapper
				.selectByListingIds(listingIds);
		ListMultimap<String, ProductCategoryMapper> map = Multimaps.index(pcm,
				p -> p.getClistingid());
		return Multimaps.transformValues(
				map,
				(ProductCategoryMapper pc) -> cpmapper.getCategory(
						pc.getIcategory(), languageid, websiteid));
	}

	@CacheResult("category")
	public CategoryName getCategoryNameByIid(Integer iid) {
		return categoryNameMapper.selectByPrimaryKey(iid);
	}

	/**
	 * @param categoryId
	 * @param languageid
	 * @param websiteid
	 * @return CategoryWebsiteWithName
	 * @Title: getCategoryForLevelOne
	 * @Description: TODO(递归向上查找category的level为1 的父类)
	 * @author liudi
	 */
	@CacheResult("category")
	public CategoryWebsiteWithName getCategoryForLevelOne(Integer categoryId,
			Integer languageid, Integer websiteid) {
		CategoryWebsiteWithName c = cpmapper.getCategory(categoryId,
				languageid, websiteid);
		if (c.getIlevel() > 1) {
			if (c.getIparentid() == null) {
				throw new RuntimeException("Category has no parentId");
			}
			CategoryWebsiteWithName c2 = getCategoryForLevelOne(
					c.getIparentid(), languageid, websiteid);
			return c2 != null ? c2 : c;
		} else {
			return c;
		}

	}

	@CacheResult("category")
	public Page<CategoryName> getSecondCategoryNamesPage(Integer pageNum,
			Integer pageSize, Integer site, Integer language) {
		int pageIndex = pageSize * (pageNum - 1);
		List<CategoryName> result = categoryNameMapper
				.getSecondCategoryNamesPage(site, language, pageIndex, pageSize);
		int total = categoryNameMapper.getSecondCategoryNamesPageCount(site,
				language);
		return new Page<CategoryName>(result, total, pageNum, pageSize);
	}

	@CacheResult("category")
	public List<com.website.dto.category.CategoryAttribute> getAllCategoryAttributes() {
		return filterAttributeValueMapper.selectAll();
	}

	@CacheResult("category")
	public Collection<AttributeKeyItem> getCategoryAttributesByCategoryIdAndLanguageId(
			Integer categoryid, Integer languageid) {
		// 得到类别下所有的keyid

		List<CategoryAttribute> attributeList = attributeMapper
				.getAttributeListByCategoryIdAndLanguageId(categoryid,
						languageid);
		Multimap<String, CategoryAttribute> cartItemListIndex = Multimaps
				.index(attributeList, cilist -> cilist.getAttributeKey());
		Collection<AttributeKeyItem> attributeMap = Collections2
				.transform(
						cartItemListIndex.keySet(),
						(String key) -> {
							Collection<CategoryAttribute> collection = cartItemListIndex
									.get(key);
							List<AttributeValueItem> attributeValueList = new ArrayList<AttributeValueItem>();
							Integer iid = 0;
							Integer ikeyid = 0;
							for (CategoryAttribute categoryAttribute : collection) {
								AttributeValueItem attributeValue = new AttributeValueItem();
								iid = categoryAttribute.getAttributeKeyId();
								ikeyid = categoryAttribute.getIkeyid();
								attributeValue.setCvaluename(categoryAttribute
										.getAttributeValue());
								attributeValue.setIid(categoryAttribute
										.getAttributeValueId());
								attributeValueList.add(attributeValue);
							}

							return new AttributeKeyItem(iid, key, ikeyid,
									languageid, attributeValueList);
						});

		return attributeMap;
	}

	@CacheResult("category")
	public List<Integer> getCategoryAttributeKey(Integer categoryid,
			Integer languageid, Integer attributekeyid) {
		List<Integer> attributeValueId = attributeMapper
				.getAttributeValueIdsByCategoryIdAndLanguageId(categoryid,
						languageid, attributekeyid);
		return attributeValueId;
	}

	public boolean deleteCategoryAttribute(Integer icategoryid,
			Integer iattributekeyid) {
		return attributeMapper.deleteCategoryFilterAttribute(icategoryid,
				iattributekeyid);
	}

	/**
	 * 根据listingId获取最下级品类id
	 *
	 * @param listingId
	 * @return
	 * @author luojiaheng
	 */
	@CacheResult("category")
	public Integer getLastCategoryId(String listingId) {
		return pcMapper.getLastCategoryIdtByListingId(listingId);
	}

	@CacheResult("category")
	public List<Category> rootCategories(Integer languageid, Integer websiteid) {

		List<CategoryWebsiteWithName> pf = cpmapper.getRootCategories(
				languageid, websiteid);
		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, Category>() {
					@Override
					public Category apply(CategoryWebsiteWithName self) {
						return new Category(self.getIid(), self.getIparentid(),
								self.getCname(), self.getIcategoryid());
					}
				});

	}

	@CacheResult("category")
	public List<CategoryWebsiteWithName> getCategoriesByLevel(
			Integer languageid, Integer websiteid, int level) {

		List<CategoryWebsiteWithName> pf = cpmapper.getCategoriesByLevel(
				languageid, websiteid, level);

		return pf;

	}

	@CacheResult("category")
	public List<Category> getChildCategoriesByCategoryId(Integer languageid,
			Integer websiteid, int categoryId) {

		List<CategoryWebsiteWithName> pf = cpmapper.getChildCategoriesByBshow(
				categoryId, languageid, websiteid);

		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, Category>() {
					@Override
					public Category apply(CategoryWebsiteWithName self) {
						return new Category(self.getIid(), self.getIparentid(),
								self.getCname(), self.getIcategoryid(), self
										.getCpath());
					}
				});

	}

	@CacheResult("category")
	public List<SubscribeCategory> getCategoryNameForSubscribe(int languageid,
			int websiteid) {
		List<CategoryWebsiteWithName> catelist = this.getCategoriesByLevel(
				languageid, websiteid, 1);
		List<Integer> categoryIds = Lists.transform(catelist,
				c -> c.getIcategoryid());
		List<CategoryLabelVo> labels = categoryLabelNameMapper
				.getByCategoryIdsAndType(categoryIds, "hot", websiteid,
						languageid);
		Multimap<Integer, CategoryLabelVo> labelmap = Multimaps.index(labels,
				l -> l.getCategoryId());
		List<SubscribeCategory> scate = Lists.newArrayList();
		for (CategoryWebsiteWithName c : catelist) {
			SubscribeCategory s = new SubscribeCategory();
			s.setCname(c.getCname());
			s.setIcategoryid(c.getIcategoryid());
			if (labelmap.get(c.getIcategoryid()).size() > 0) {
				s.setLabelNameId(labelmap.get(c.getIcategoryid()).iterator()
						.next().getCategoryLabelNameId());
			}
			scate.add(s);
		}
		return scate;
	}

	@CacheResult("category")
	public List<CategoryName> getCategoryNameByCategoryIds(
			List<Integer> categoryids, int languageid, int websiteid) {
		return categoryNameMapper.getCategoryNameByCategoryIds(categoryids,
				languageid, websiteid);
	}

	public CategoryMessage getCategoryMessageByCategoryIdAndLanguage(
			Integer categorywebsiteid, Integer language, Integer websiteId) {
		return categoryNameMapper.getCategoryMessageByCategoryIdAndLanguage(
				categorywebsiteid, language, websiteId);
	}

	public List<Integer> getCategoryIds(String categoryPath) {
		return categoryBaseMapper.getFullCategoryIds(categoryPath);
	}

	public CategoryBase getCategoryBaseByIid(Integer iid) {
		return categoryBaseMapper.selectByPrimaryKey(iid);
	}

	public List<CategoryWebsiteWithName> getMultiChildCategoriesByDisplay(
			final List<Integer> parentIds, final int language, final int siteId) {

		List<CategoryWebsiteWithName> pf = cpmapper
				.getMultiChildCategoriesByDisplay(parentIds, language, siteId,
						display);

		return pf;
	}

	public static CategoryEnquiryService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				CategoryEnquiryService.class);
	}

	public Map<Integer, CategoryName> getIdCategoryNameMap(Integer websiteId,
			Integer languageId) {
		List<CategoryName> categoryNames = categoryNameMapper
				.getCategoryNameByLanguageIdAndWebsiteId(languageId, websiteId);
		Map<Integer, CategoryName> map = Maps.uniqueIndex(categoryNames,
				o -> o.getIcategoryid());

		return map;
	}

	@CacheResult("category")
	public CategoryBackgroundImages getBackgroundImagesById(Integer iid) {
		return categoryBackgroundImagesMapper.selectByPrimaryKey(iid);
	}

	/**
	 * 
	 * @param websiteid
	 * @param languageid
	 * @param display
	 * @return linhl
	 */

	// @CacheResult("category")
	public List<CategoryWebsiteWithName> getCategoryItemRootByDisplay(
			int websiteid, int languageid, boolean display) {
		List<CategoryWebsiteWithName> list = cpmapper
				.getRootCategoriesByDisplay(languageid, websiteid, display);
		return list;
	}

	@Override
	public List<CategoryWebsiteWithName> getCategoryItemRootByDisplay(
			WebContext context, boolean display) {
		int siteId = foundationService.getSiteID(context);
		int languageid = foundationService.getLanguage(context);
		return this.getCategoryItemRootByDisplay(siteId, languageid, display);
	}

	@Override
	public List<CategoryWebsiteWithName> getChildCategories(Integer categoryId,
			WebContext context) {
		int siteId = foundationService.getSiteID(context);
		int language = foundationService.getLanguage(context);
		List<CategoryWebsiteWithName> list = cpmapper.getChildCategories(
				categoryId, language, siteId);
		return list;
	}

	/**
	 * 根据品类名称生成html链接
	 * 
	 * @param languageid
	 * @param websiteid
	 * @param names
	 * @return
	 */
	public String getCategoryLinksByNames(int languageid, int websiteid,
			List<String> names) {
		List<CategoryWebsiteWithName> clist = cpmapper.getCategoriesByNames(
				languageid, websiteid, names);
		StringBuilder html = new StringBuilder();
		for (CategoryWebsiteWithName c : clist) {
			html.append(packLinkHtml(c.getCpath(), c.getCname()));
		}
		if (names.contains("New Arrivals")) {
			html.append(packLinkHtml("product/newarrivals", "New Arrivals"));
		}
		if (names.contains("Daily Deal")) {
			html.append(packLinkHtml("product/dailydeal", "Daily Deals"));
		}
		if (names.contains("Clearance")) {
			html.append(packLinkHtml("product/clearance", "Clearance"));
		}
		return html.toString();
	}

	String packLinkHtml(String path, String name) {
		StringBuilder html = new StringBuilder();
		html.append("<a href=\"http://www.tomtop.com/" + path + "\" ");
		html.append("style=\"color:#666; font-weight:bold; text-decoration:none;\" target=\"_blank\">");
		html.append(name + "</a><br />");
		return html.toString();
	}

	/**
	 * @param siteId
	 * @param lastViewListingId
	 * @根据listingId去查找最顶级的品类Id
	 */
	@CacheResult
	public Integer getRootCategoryIdBySiteIdAndListingId(int siteId,
			String listingId) {
		List<Integer> categoryIds = pcMapper
				.getCategoryIdByListingId(listingId);
		if (null != categoryIds && categoryIds.size() > 0) {
			CategoryBase categoryBase = cpmapper
					.getParentCategoryIdBycategoryId(siteId, categoryIds.get(0));
			if (null != categoryBase) {
				Integer categoryId = categoryBase.getIid();
				while (null != categoryBase.getIparentid()
						|| 1 != categoryBase.getIlevel()) {
					categoryBase = cpmapper.getParentCategoryIdBycategoryId(
							siteId, categoryId);
					if (null == categoryBase) {
						return categoryId;
					}
					categoryId = categoryBase.getIid();
				}
			} else {
				return categoryIds.get(0);
			}
		}
		return null;
	}

	public List<Integer> getAllRootCategoryIdBySite(Integer siteId) {
		return cpmapper.getAllRootCategoryIdBySite(siteId);
	}

	public List<CategoryWebsiteWithName> getCategoriesByCategoryIds(
			List<Integer> ids, int languageid, int websiteid) {
		return cpmapper.getCategoriesByCategoryIds(ids, languageid, websiteid);
	}

	public Integer getRootCategoryIdByListingId(String listingId,
			Integer languageId) {
		return productCategoryEnquiryDao.getProductRootCategoryIdByListingId(
				listingId, languageId);
	}

	public List<String> getAllListingIdsByRootId(Integer rootCategoryId) {
		return productCategoryEnquiryDao
				.getAllListingIdsByRootId(rootCategoryId);
	}

	public String getRootCategoryNameByRootIdAndLanguageId(
			Integer rootCategoryId, Integer languageId) {
		CategoryName categoryName = productCategoryEnquiryDao
				.getCategoryNameByCategoryIdAndLanguage(rootCategoryId,
						languageId);
		if (null != categoryName) {
			return categoryName.getCname();
		}
		return null;
	}

	/**
	 * 通过listingid获取产品按大->小规则的目录级别
	 * 
	 * @author lijun
	 * @param listingid
	 * @return
	 */
	@Override
	public List<ProductCategoryMapper> selectByListingId(String listingid) {
		return pcMapper.selectByListingId(listingid);
	}

	/*
	 * app专用，加载新增分类
	 * 
	 * @param categoryId
	 * 
	 * @param maxCategoryId
	 * 
	 * @param depth
	 * 
	 * @param languageid
	 * 
	 * @param websiteid
	 * 
	 * @return
	 */
	public List<CategoryComposite> getNewChildCategory(int categoryId,
			int maxCategoryId, WebContext webContext, int depth) {
		int lang = foundationService.getLanguage(webContext);
		int websiteid = foundationService.getSiteID(webContext);
		List<CategoryWebsiteWithName> pf = cpmapper.getNewChildCategory(
				categoryId, maxCategoryId,  lang, websiteid);
		return Lists.transform(pf,
				new Function<CategoryWebsiteWithName, CategoryComposite>() {
					@Override
					public CategoryComposite apply(CategoryWebsiteWithName self) {
						if (depth == 0) {
							return new CategoryComposite(self,
									new LinkedList<CategoryComposite>());
						}
						return new CategoryComposite(self, getNewChildCategory(
								self.getIid(), maxCategoryId, webContext, depth - 1));
					}
				});
	}

	/**
	 * 获取有产品的一级类目
	 * 
	 * @param pt
	 * @param site
	 * @param lang
	 * @return
	 */
	public List<CategoryWebsiteWithName> getProductCatelist(
			List<ISearchCriteria> crs, List<ISearchFilter> fs, int site,
			int lang) {
		// 品类过滤使用
		SearchContext context2 = searchFactory.fromQueryString(null,
				new HashMap<String, String[]>(), null);
		if (crs != null) {
			for (ISearchCriteria s : crs) {
				context2.getCriteria().add(s);
			}
		}
		if (fs != null) {
			for (ISearchFilter s : fs) {
				context2.getFilter().add(s);
			}
		}
		Page<String> listingids2 = genericSearch.search(context2, site, lang);
		context2.setPageSize(listingids2.totalCount());
		Page<Map<String, Object>> listingids3 = genericSearch.searchByP(
				context2, site, lang, (SearchHit hit) -> hit.getSource());
		// 类目list
		List<CategoryWebsiteWithName> catelist = this
				.getCategoryItemRootByDisplay(site, lang, true);
		List<Integer> cateids = Lists.transform(catelist,
				c -> c.getIcategoryid());
		List<Integer> cateids2 = Lists.newArrayList();
		for (Map<String, Object> m : listingids3.getList()) {
			String idarry = m.get("categories").toString();
			if (idarry.length() > 2) {
				idarry = idarry.substring(1, idarry.length() - 1);
				String[] arr2 = idarry.split(", ");
				for (String id : arr2) {
					int idd = Integer.parseInt(id);
					if (cateids.contains(idd) && !cateids2.contains(idd)) {
						cateids2.add(idd);
					}
				}
			}
		}
		catelist = Lists.newArrayList(Collections2.filter(catelist,
				c -> cateids2.contains(c.getIcategoryid())));
		return catelist;
	}
	
	public CategoryBase getCategoryBaseByiid(int iid) {
		return categoryBaseMapper.getCategoryBaseByiid(iid);
	}

}
