package controllers.manager;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ILanguageService;
import services.attribute.AttributeService;
import services.base.FoundationService;
import services.base.StorageService;
import services.base.utils.StringUtils;
import services.manager.ChooseService;
import services.manager.CountService;
import services.manager.product.FormToEntityMapService;
import services.product.CategoryEnquiryService;
import services.product.EntityMapService;
import services.product.ICategoryEnquiryService;
import services.product.IProductUpdateService;
import services.product.ProductBaseTranslateService;
import services.product.ProductEnquiryService;
import services.product.ProductSalePriceService;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import services.search.criteria.CategorySearchCriteria;
import services.search.criteria.KeywordSearchCriteria;
import services.search.criteria.StatusCriteria;
import session.ISessionService;
import valueobjects.base.Page;
import valueobjects.product.ProductBaseTranslate;
import valueobjects.product.category.CategoryComposite;
import valueobjects.search.SearchContext;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import controllers.InterceptActon;
import dto.AttributeKeyItem;
import dto.AttributeValueItem;
import dto.Storage;
import dto.Website;
import dto.product.CategoryBase;
import dto.product.ProductBase;
import dto.product.ProductCategoryMapper;
import dto.product.ProductEntityMap;
import dto.product.ProductSalePrice;
import dto.product.ProductStorageMap;
import dto.product.ProductTranslate;
import dto.product.ProductUrl;
import entity.manager.AdminUser;
import events.product.ProductUpdateEvent;
import forms.ProductEntityMapForm;
import forms.ProductEntityMapSingleForm;
import forms.ProductSalePriceForm;

@With(InterceptActon.class)
public class Product extends Controller {
	@Inject
	SearchContextFactory searchFactory;

	@Inject
	ISearchService genericSearch;

	@Inject
	ICategoryEnquiryService iCategoryEnquiryService;

	@Inject
	ProductBaseTranslateService translateService;

	@Inject
	EntityMapService mapService;

	@Inject
	AttributeService attributeService;

	@Inject
	ISessionService sessionService;

	@Inject
	IProductUpdateService productUpdateService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	ILanguageService languageService;

	@Inject
	ProductEnquiryService enquiryService;

	@Inject
	ChooseService chooseService;

	@Inject
	FormToEntityMapService formToEntityMapService;

	@Inject
	EventBus eventBus;

	@Inject
	StorageService storageEnquiryService;

	@Inject
	FoundationService foundationService;

	@Inject
	ProductSalePriceService productSalePriceService;

	public Result baseList(String q, int p, Integer status, String category,
			Integer site) {
		Map<String, List<ProductSalePrice>> productSalePrriceMap = new HashMap<String, List<ProductSalePrice>>();
		Map<String, List<ProductCategoryMapper>> productCategoryMap = new HashMap<String, List<ProductCategoryMapper>>();
		KeywordSearchCriteria kc = null;
		Page<ProductBaseTranslate> bases = null;
		HashMap<String, String> urlMap = Maps.newHashMap();
		List<Website> websites = chooseService.website();
		Page<String> listingIds = null;
		if (!websites.isEmpty()) {
			if (StringUtils.notEmpty(q)) {
				kc = new KeywordSearchCriteria(q);
			}
			SearchContext context = searchFactory.personalSearch(kc, null,
					null, null);
			if (null != status && 0 != status) {
				StatusCriteria statusCriteria = new StatusCriteria(status);
				context.getCriteria().add(statusCriteria);
			}
			if (StringUtils.notEmpty(category)) {
				Integer categoryId;
				try {
					categoryId = Integer.valueOf(category);
					CategorySearchCriteria categoryCriteria = new CategorySearchCriteria(
							categoryId);
					context.getCriteria().add(categoryCriteria);
				} catch (NumberFormatException e) {
					Logger.debug(
							"Manager product search category: NumberFormatException, category = {}",
							category);
					category = null;
				}
			}
			context.setPage(p);
			site = site == null ? websites.get(0).getIid() : site;
			listingIds = genericSearch.search(context, site, 1);
		}

		if (listingIds != null) {
			if (listingIds.getList().isEmpty()) {
				bases = listingIds
						.batchMap(list -> new ArrayList<ProductBaseTranslate>());
			} else {
				bases = listingIds.batchMap(list -> translateService
						.getTranslateLiteByListingIds(list, languageService
								.getDefaultLanguage().getIid()));
				List<ProductUrl> urls = translateService
						.getUrlByClistingIds(listingIds.getList());
				if(null!=urls&&urls.size()>0){
					for (ProductUrl url : urls) {
						if (!urlMap.containsKey(url.getClistingid())) {
							urlMap.put(url.getClistingid(), url.getCurl());
						}
					}
				}

				if (bases.getList() != null) {
					for (ProductBaseTranslate base : bases.getList()) {
						base.getClistingid();
						List<ProductCategoryMapper> productCategoryMappers = enquiryService
								.selectByListingId(base.getClistingid());
						List<ProductSalePrice> productSalePrices = productSalePriceService
								.getAllProductSalePriceByListingIds(base
										.getClistingid());
						if (productCategoryMappers != null
								&& productCategoryMappers.size() > 0) {
							productCategoryMap.put(base.getClistingid(),
									productCategoryMappers);
						}
						if (productSalePrices != null
								&& productSalePrices.size() > 0) {
							productSalePrriceMap.put(base.getClistingid(),
									productSalePrices);
						}
					}
				}

			}
		}
		HashMap<Integer, String> statusMap = Maps.newHashMap();
		statusMap.put(1, "On Sell");
		statusMap.put(2, "Stop Selling");
		statusMap.put(3, "Out Of Stock");

		return ok(views.html.manager.product.productlist.render(bases,
				productCategoryMap, productSalePrriceMap, urlMap, statusMap,
				status, category, site, websites));
	}

	public Result editAttribute(String listingId) {
		List<ProductEntityMap> mapList = mapService
				.getProductEntityMapByListingId(listingId, 1);
		List<String> attributes = new ArrayList<String>();
		if (mapList != null && mapList.size() > 0) {
			List<String> tempAttributes = new ArrayList<String>();
			for (ProductEntityMap productEntityMap : mapList) {
				tempAttributes.add(productEntityMap.getCkeyname());
			}
			HashSet<String> set = new HashSet<String>(tempAttributes);
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String str = (String) iterator.next();
				attributes.add(str);
			}
		}
		return ok(views.html.manager.product.edit_attribute.render(attributes,
				mapList));
	}

	public Result editTranslate(String listingId) {
		List<dto.product.ProductTranslate> translateList = translateService
				.getTranslatesByListingId(listingId);
		List<dto.Language> languageList = languageService.getAllLanguage();
		Map<Integer, dto.Language> langIdMap = Maps.uniqueIndex(languageList,
				e -> e.getIid());
		return ok(views.html.manager.product.edit_translate.render(
				translateList, langIdMap, listingId));
	}

	public Result getValueItem(int ikey, int count) {
		List<AttributeValueItem> valueItems = attributeService
				.getAttributeKeyByAttributeId(ikey, 1);
		return ok(views.html.manager.product.valueselect.render(valueItems,
				count));
	}

	public Result saveTranslate() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		Form<ProductTranslate> form = Form.form(ProductTranslate.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProductTranslate translate = form.get();
		Field[] fieldArr = ProductTranslate.class.getDeclaredFields();
		for (Field field : fieldArr) {
			field.setAccessible(true);
			try {
				Object obj = field.get(translate);
				if (null != obj && StringUtils.isEmpty(obj.toString())) {
					field.set(translate, null);
				}
			} catch (Exception e) {
				Logger.error("Set Field Error", e);
				return badRequest();
			}
		}
		if (translate.getIid() != null) {
			translateService.updateTranslates(translate);
		} else {
			translateService.insertTranslates(translate);
		}
		ProductUpdateEvent event = new ProductUpdateEvent(
				translate.getClistingid(),
				ProductUpdateEvent.ProductHandleType.update);
		eventBus.post(event);
		return redirect(controllers.manager.routes.Product
				.editTranslate(translate.getClistingid()));
	}

	public Result deleteTranslate(Integer id, String listingId) {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		translateService.deleteTranslates(id, listingId);
		ProductUpdateEvent event = new ProductUpdateEvent(listingId,
				ProductUpdateEvent.ProductHandleType.update);
		eventBus.post(event);
		return redirect(controllers.manager.routes.Product
				.editTranslate(listingId));
	}

	public Result saveAttribute() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		Form<ProductEntityMapForm> form = Form.form(ProductEntityMapForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProductEntityMapForm maps = form.get();
		if (maps.getList() != null) {
			List<ProductEntityMapSingleForm> entityMaps = Lists.transform(
					maps.getList(), e -> {
						if (e.isCheck()) {
							return e;
						} else {
							return null;
						}
					});
			entityMaps = Lists.newArrayList(Collections2.filter(entityMaps,
					e -> e != null));
			formToEntityMapService.saveEntityMap(entityMaps,
					user.getCusername(), maps.getListingId());
			ProductUpdateEvent event = new ProductUpdateEvent(
					maps.getListingId(),
					ProductUpdateEvent.ProductHandleType.update);
			eventBus.post(event);
			return ok();
		}
		return badRequest();
	}

	public Result deleteEntityMap(int id, String listingId) {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		mapService.deleteByListingIdAndKeyId(listingId, id);
		ProductUpdateEvent event = new ProductUpdateEvent(listingId,
				ProductUpdateEvent.ProductHandleType.update);
		eventBus.post(event);
		return ok();
	}

	public Result getCategoryCheckTree(Integer websiteid, Integer languageid,
			String listingid) {
		List<CategoryComposite> rootCategories = categoryEnquiryService
				.getAllSimpleCategories(languageid, websiteid);
		List<Integer> categoryIds = productUpdateService
				.getProductCategoryMapperByListingId(listingid);
		return ok(views.html.manager.category_tree.render(rootCategories,
				categoryIds, "noChooseDefault"));
	}

	@JsonGetter
	public Result updateProductCategory() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}

		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(asJson, Map.class);

		String listingId = result.get("listingid").toString();
		List<Object> categoryIds = (List<Object>) result.get("categoryids");
		ProductBase productBase = enquiryService.getBaseByListingId(listingId);
		String cparentsku = productBase.getCparentsku();
		boolean updateParentSku = (boolean) result.get("updateParentSku");
		if (!updateParentSku) {
			cparentsku = null;
		}
		List<ProductBase> productBases = new ArrayList<ProductBase>();
		if (cparentsku != null && cparentsku != "") {
			List<ProductBase> productsWithSameParentSku = mapService
					.getProductsWithSameParentSku(cparentsku);
			if (productsWithSameParentSku.size() > 0) {
				productBases.addAll(productsWithSameParentSku);
			}
		} else {
			productBases.add(productBase);
		}
		List<String> updateSkus = new ArrayList<String>();
		for (ProductBase product : productBases) {
			ArrayList<ProductCategoryMapper> productCategoryMappers = new ArrayList<ProductCategoryMapper>();
			for (Object icategoryId : categoryIds) {
				ProductCategoryMapper productCategoryMapper = new ProductCategoryMapper();
				productCategoryMapper.setClistingid(product.getClistingid());
				productCategoryMapper.setIcategory(Integer.parseInt(icategoryId
						.toString()));
				productCategoryMapper.setCsku(product.getCsku());
				productCategoryMapper.setCcreateuser(user.getCusername());

				productCategoryMappers.add(productCategoryMapper);
			}
			boolean saveOrUpdateProductCategory = productUpdateService
					.updateProductCategoryWithSomeListingId(
							productCategoryMappers, product.getClistingid());
			if (saveOrUpdateProductCategory) {
				updateSkus.add(product.getCsku());
			}
		}

		if (updateSkus.size() > 0) {
			ProductUpdateEvent event = new ProductUpdateEvent(listingId,
					ProductUpdateEvent.ProductHandleType.update);
			eventBus.post(event);
			return ok(Json.toJson(updateSkus));
		} else {
			return ok(false + "");
		}

	}

	public Result editBaseInfo(String listingId) {
		ProductBase productBase = enquiryService.getBaseByListingId(listingId);
		HashMap<Integer, String> statusMap = Maps.newHashMap();
		statusMap.put(1, "On Sell");
		statusMap.put(2, "Stop Selling");
		statusMap.put(3, "Out Of Stock");
		return ok(views.html.manager.product.edit_base_info.render(productBase,
				statusMap));
	}

	public Result saveBaseInfo() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		Form<ProductBase> form = Form.form(ProductBase.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		ProductBase productBase = form.get();
		Field[] fieldArr = ProductBase.class.getDeclaredFields();
		for (Field field : fieldArr) {
			field.setAccessible(true);
			try {
				Object obj = field.get(productBase);
				if (null != obj && StringUtils.isEmpty(obj.toString())) {
					field.set(productBase, null);
				}
			} catch (Exception e) {
				Logger.error("Set Field Error", e);
				return badRequest();
			}
		}
		boolean b = productUpdateService
				.updateByListingIdSelective(productBase);
		if (b) {
			ProductUpdateEvent event = new ProductUpdateEvent(
					productBase.getClistingid(),
					ProductUpdateEvent.ProductHandleType.update);
			eventBus.post(event);

			productUpdateService.updateQtyInventory(
					productBase.getClistingid(), productBase.getIqty(),
					productBase.getIwebsiteid());
			return ok();
		}
		return internalServerError("Save error");
	}

	public Result updateSalePrice() throws Exception {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		Form<ProductSalePriceForm> bindFromRequest = Form.form(
				ProductSalePriceForm.class).bindFromRequest();
		ProductSalePriceForm productSalePriceForm = bindFromRequest.get();
		Logger.debug("productSalePriceForm:{}", productSalePriceForm);
		ProductSalePrice productSalePrice = new ProductSalePrice();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String startTime = productSalePriceForm.getDbegindate();
		String endTime = productSalePriceForm.getDenddate();
		productSalePrice.setDbegindate(sdf.parse(startTime));
		productSalePrice.setDenddate(sdf.parse(endTime));
		productSalePrice.setCsku(productSalePriceForm.getCsku());
		productSalePrice.setClistingid(productSalePriceForm.getClistingid());
		productSalePrice.setFsaleprice(productSalePriceForm.getFsaleprice());
		if (productSalePriceForm.getIid() != null) {
			productSalePrice.setIid(productSalePriceForm.getIid());
		} else {
			productSalePrice.setCcreateuser(user.getCusername());
			productSalePrice.setDcreatedate(new Date());
		}

		boolean flag = productUpdateService
				.saveOrUpdateProductSalePrice(productSalePrice);
		ProductUpdateEvent event = new ProductUpdateEvent(
				productSalePriceForm.getClistingid(),
				ProductUpdateEvent.ProductHandleType.update);
		eventBus.post(event);
		return ok(flag + "");
	}

	public Result editStorageMap(String listingId) {
		List<Storage> storages = storageEnquiryService.getAllStorages();
		List<ProductStorageMap> productStorageMaps = enquiryService
				.getProductStorageMapsByListingId(listingId);
		List<Integer> list = Lists.newArrayList(Collections2.transform(
				productStorageMaps, p -> p.getIstorageid()));
		ProductBase productBase = enquiryService.getBaseByListingId(listingId);
		return ok(views.html.manager.product.edit_storages_map.render(storages,
				list, productBase));
	}

	@JsonGetter
	public Result updateStorageMap() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}

		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map data = mapper.convertValue(asJson, Map.class);
		String listingId = data.get("listingid").toString();
		String csku = data.get("csku").toString();
		List<Integer> storagesAdd = Lists
				.transform((List<String>) data.get("storagesAdd"),
						e -> Integer.valueOf(e));
		List<Integer> storagesDel = Lists
				.transform((List<String>) data.get("storagesDel"),
						e -> Integer.valueOf(e));
		String createruser = user.getCusername();
		boolean updateFlag = productUpdateService.updateStorageMapper(
				storagesAdd, storagesDel, listingId, csku, createruser);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", updateFlag);

		return ok(Json.toJson(result));
	}

	public Result editDetailsSalePrice(String listingId) {
		List<ProductSalePrice> productSalePrices = productSalePriceService
				.getAllProductSalePriceByListingIds(listingId);
		return ok(views.html.manager.product.display_detail_sale_price
				.render(productSalePrices));
	};

	public Result editProductCategory(String clistingId) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<ProductCategoryMapper> productCategoryMappers = enquiryService
				.selectByListingId(clistingId);
		for (ProductCategoryMapper p : productCategoryMappers) {
			CategoryBase base = iCategoryEnquiryService.getCategoryBaseByiid(p
					.getIcategory());
			map.put(p.getIid(), base.getCpath());
		}
		return ok(views.html.manager.product.display_product_category_map
				.render(map, productCategoryMappers));
	};
}
