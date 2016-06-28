package services.interaction.product.post;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import mapper.interaction.ProductPostMapper;
import services.base.FoundationService;
import valueobjects.base.Page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import context.WebContext;
import dto.interaction.ProductPost;
import dto.interaction.ProductPostWithHeplQty;

public class ProductPostService implements IProductPostService {

	@Inject
	FoundationService foundationService;

	@Inject
	ProductPostMapper mapper;

	@Override
	public boolean addProductPost(ProductPost faq) {
		int result = mapper.insert(faq);
		return result > 0 ? true : false;
	}

	@Override
	public Page<dto.ProductPost> getByListingidPage(String listingid,
			int typeid, int page, int pageSize) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("listingid", listingid);
		paramMap.put("state", ProductPostMapper.CHECKED);
		paramMap.put("typeid", typeid > 0 ? typeid : null);
		paramMap.put("page", page);
		paramMap.put("pageSize", pageSize);

		return getProductPostPageByParamMap(paramMap, page, pageSize);
	}

	public List<dto.ProductPost> getByListingId(String listingid, int limit) {
		List<ProductPostWithHeplQty> list = mapper.getByListingIdLimit(
				listingid, ProductPostMapper.CHECKED, limit);
		return Lists.transform(list, f -> {
			dto.ProductPost v = new dto.ProductPost();
			v.setSelf(f);
			return v;
		});
	}

	public Page<dto.ProductPost> getProductPostPage(String email, int reply,
			int type, int date, int page, int pageSize) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("typeid", type > 0 ? type : null);
		paramMap.put("page", page);
		paramMap.put("email", email);
		paramMap.put("reply", reply == 0 ? null : (reply == 1 ? false : true));
		paramMap.put("pageSize", pageSize);

		if (date != 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar calender = Calendar.getInstance();
			calender.add(Calendar.MONTH, -date);
			paramMap.put("date", sdf.format(calender.getTime()));

		}
		return getProductPostPageByParamMap(paramMap, page, pageSize);
	}

	protected Page<dto.ProductPost> getProductPostPageByParamMap(
			Map<String, Object> paramMap, int page, int pageSize) {
		List<ProductPostWithHeplQty> list = mapper.getByParamMapPage(paramMap);
		int total = mapper.getCountByparamMap(paramMap);
		List<dto.ProductPost> tlist = Lists.transform(list, f -> {
			dto.ProductPost v = new dto.ProductPost();
			v.setSelf(f);
			return v;
		});
		return new Page<dto.ProductPost>(tlist, total, page, pageSize);
	}

	public dto.ProductPost getProductPostById(int id) {
		ProductPostWithHeplQty f = mapper.getProductPostById(id);
		dto.ProductPost post = new dto.ProductPost();
		post.setSelf(f);
		return post;
	}

	public int getTotalRecordByEamil(String email) {
		return mapper.getTotalRecordByEamil(email);
	}

	@Override
	public Page<Map<String, List<ProductPost>>> getProductPostList(
			String listingId, WebContext context, Integer itypeid,
			Integer state, Integer page, Integer pageSize) {
		Integer siteId = foundationService.getSiteID(context);
		Integer lanugageId = foundationService.getLanguage(context);
		List<String> ctitle = mapper.getQuestionsTitleByListingId(listingId,
				siteId, lanugageId, state, itypeid, page, pageSize);
		Integer total = mapper.getTotalQuestionsByListingId(listingId, siteId,
				lanugageId, state, itypeid);
		List<Map<String, List<ProductPost>>> productList = Lists.newArrayList();
		Map<String, List<ProductPost>> productMap = Maps.newLinkedHashMap();
		for (String title : ctitle) {
			List<ProductPost> productPosts = mapper
					.getProductPostByListingIdAndTitle(listingId, title,
							siteId, lanugageId, state, itypeid);
			productMap.put(title, productPosts);
		}
		productList.add(productMap);
		Page<Map<String, List<ProductPost>>> productPostPage = new Page<>(
				productList, total, page, pageSize);
		return productPostPage;
	}

	public int getTotalRecord(String listingid, Integer state,
			Integer languageId, Integer siteId, Integer typeId) {
		return mapper.getByListingIdCount(listingid, state, languageId, siteId,
				typeId);
	}

	@Override
	public List<ProductPost> getProductPostList(WebContext context,
			Integer itypeid, Integer state, Integer page, Integer pageSize) {
		Integer siteId = foundationService.getSiteID(context);
		Integer lanugageId = foundationService.getLanguage(context);
		List<ProductPost> productPost = this.getProductPostListBySearch(siteId,
				lanugageId, state, itypeid, page, pageSize);

		return productPost;
	}

	@Override
	public List<ProductPost> getProductPostListBySearch(WebContext context,
			String key, Integer itypeid, Integer state, Integer page,
			Integer pageSize) {
		Integer siteId = foundationService.getSiteID(context);
		Integer lanugageId = foundationService.getLanguage(context);
		List<ProductPost> posts = mapper.getProductPostListBySearch(key,
				siteId, lanugageId, state, itypeid, page, pageSize);

		return posts;
	}

	public int getTotalBySearch(Integer websiteId, Integer lanugageId,
			Integer itypeid, Integer state) {
		return mapper.getTotalBySearch(websiteId, lanugageId, state, itypeid);
	}

	public List<ProductPost> getProductPostListBySearch(Integer websiteId,
			Integer lanugageId, Integer itypeid, Integer state, Integer page,
			Integer pageSize) {
		List<ProductPost> posts = mapper.getQuestions(websiteId, lanugageId,
				state, itypeid, page, pageSize);

		return posts;
	}

	public boolean updateProductPost(ProductPost productPost) {
		return mapper.updateByPrimaryKeySelective(productPost) > 0 ? true
				: false;
	}

	@Override
	public ProductPost getProductPostByIid(Integer iid) {
		return mapper.selectByPrimaryKey(iid);
	}
}
