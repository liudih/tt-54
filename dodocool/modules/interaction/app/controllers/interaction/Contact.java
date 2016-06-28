package controllers.interaction;

import interaction.form.EmailSupportForm;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.dodocool.base.FoundationService;
import services.dodocool.product.ProductMessageService;
import services.interaction.product.post.IProductPostService;
import services.product.ICategoryEnquiryService;
import services.product.IProductCategoryEnquiryService;
import services.search.ISearchContextFactory;
import services.search.ISearchService;
import services.search.criteria.CategorySearchCriteria;
import valueobjects.base.Page;
import valueobjects.search.SearchContext;

import com.google.api.client.util.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.interaction.ProductPost;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductMessage;

public class Contact extends Controller {
	@Inject
	IProductPostService productPostService;

	@Inject
	ICategoryEnquiryService categoryService;

	@Inject
	ISearchService searchService;

	@Inject
	ISearchContextFactory factory;

	@Inject
	ProductMessageService productMessageService;

	@Inject
	IProductCategoryEnquiryService productCategoryService;

	@Inject
	FoundationService foundationService;

	public Result home() {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> rootCategorys = categoryService
				.getCategoryItemRootByDisplay(webContext, true);
		List<CategoryWebsiteWithName> nextCategorys = Lists.newArrayList();
		Map<Integer, String> rootCategoryMaps = Maps.newHashMap();
		Map<Integer, String> nextCategoryMaps = Maps.newHashMap();
		Map<Integer, String> productMaps = Maps.newHashMap();
		List<ProductMessage> productMessageData = Lists.newArrayList();
		if (null != rootCategorys && 0 < rootCategorys.size()) {
			rootCategoryMaps = rootCategorys.stream()
					.collect(
							Collectors.toMap(a -> a.getIcategoryid(),
									a -> a.getCname()));
			CategoryWebsiteWithName categoryWebsiteWithName = rootCategorys
					.get(0);
			Integer categoryId = categoryWebsiteWithName.getIcategoryid();
			nextCategorys = categoryService.getChildCategories(categoryId,
					webContext);
			if (null != nextCategorys && 0 < nextCategorys.size()) {
				nextCategoryMaps = nextCategorys.stream().collect(
						Collectors.toMap(a -> a.getIcategoryid(),
								a -> a.getCname()));
				CategoryWebsiteWithName nextCategoryWebsite = nextCategorys
						.get(0);
				Integer icategoryId = nextCategoryWebsite.getIcategoryid();
				int i = 1;
				while (i > 0) {
					Map<String, String[]> queryStrings = request()
							.queryString();
					SearchContext context = factory.fromQueryString(
							new CategorySearchCriteria(icategoryId),
							queryStrings, Sets.newHashSet("popularity"));
					context.setPageSize(30);
					context.setPage(i);
					Page<String> listingsProm = searchService.searchByContext(
							context, webContext);
					List<String> listingIdsByRootId = listingsProm.getList();
					if (null == listingIdsByRootId
							|| 0 >= listingIdsByRootId.size()) {
						break;
					}
					i++;
					List<ProductMessage> productMessages = productMessageService
							.getSimpleProductMessages(listingIdsByRootId,
									webContext);
					if (null != productMessages) {
						productMessageData.addAll(productMessages);
					}
				}
				if (null != productMessageData && 0 < productMessageData.size()) {
					productMessageData.stream().collect(
							Collectors.toMap(a -> a.getListingid(),
									a -> a.getTitle()));
				}
			}

		}

		return ok(views.html.interaction.support.support_contact.render(
				rootCategoryMaps, nextCategoryMaps, productMessageData));
	}

	public Result getNextCategory(Integer categoryId) {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		List<CategoryWebsiteWithName> nextCategorys = categoryService
				.getChildCategories(categoryId, webContext);
		Map<Integer, String> nextCategoryMaps = Maps.newHashMap();
		if (null != nextCategorys && 0 < nextCategorys.size()) {
			nextCategoryMaps = nextCategorys.stream()
					.collect(
							Collectors.toMap(a -> a.getIcategoryid(),
									a -> a.getCname()));
		}

		return ok(views.html.interaction.support.select_base.render(
				"nextCategory", nextCategoryMaps));
	}

	public Result getProduct(Integer categoryId) {
		Logger.debug("categoryId:{}", categoryId);
		List<ProductMessage> productMessageData = Lists.newArrayList();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		int i = 1;
		while (i > 0) {
			Map<String, String[]> queryStrings = request().queryString();
			SearchContext context = factory.fromQueryString(
					new CategorySearchCriteria(categoryId), queryStrings,
					Sets.newHashSet("popularity"));
			context.setPageSize(30);
			context.setPage(i);
			Page<String> listingsProm = searchService.searchByContext(context,
					webContext);
			List<String> listingIds1 = listingsProm.getList();
			Logger.debug("listingsProm:{}", listingsProm);
			List<String> listingIds = productCategoryService
					.getListingIdsByRootId(categoryId, 30, i);
			Logger.debug("listingIds:{}", listingIds);
			if (null == listingIds || 0 >= listingIds.size()) {
				break;
			}
			i++;
			List<ProductMessage> productMessages = productMessageService
					.getSimpleProductMessages(listingIds, webContext);
			if (null != productMessages) {
				productMessageData.addAll(productMessages);
			}
		}
		Logger.debug("productMessageData:{}", productMessageData);
		return ok(views.html.interaction.support.product_select_base
				.render(productMessageData));
	}

	public Result saveEmailContact() {
		Integer ilanguageid = foundationService.getLanguageId();
		Integer iwebsiteid = foundationService.getSiteID();
		Form<EmailSupportForm> form = Form.form(EmailSupportForm.class)
				.bindFromRequest();
		ProductPost productPost = new ProductPost();
		BeanUtils.copyProperties(form.get(), productPost);
		productPost.setDcreatedate(new Date());
		productPost.setItypeid(5); // 5是faq的类型
		productPost.setIlanguageid(ilanguageid);
		productPost.setIstate(0); // 1是表示审核通过
		productPost.setIwebsiteid(iwebsiteid);
		boolean result = productPostService.addProductPost(productPost);
		Map<String, String> resultMap = Maps.newHashMap();
		if (result) {
			resultMap.put("resultCode", "success");
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("resultCode", "failure");
		Logger.debug("form:{}", form);
		return ok(Json.toJson(resultMap));
	}
}
