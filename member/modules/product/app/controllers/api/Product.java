package controllers.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.time.DateUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.WebsiteService;
import services.base.receivedata.HandleReceivedDataType;
import services.product.IProductUpdateService;
import services.product.ProductEnquiryService;
import services.product.UpdateProductStatusAndQuantityAndLabelService;
import services.search.criteria.ProductLabelType;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.util.Lists;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.website.dto.product.ImageItem;
import com.website.dto.product.MultiProduct;
import com.website.dto.product.TranslateItem;

import controllers.annotation.ApiHistory;
import controllers.annotation.ApiPermission;
import dto.ProductUpdateInfo;
import dto.product.ProductBase;
import dto.product.ProductSalePrice;
import events.search.ProductSpuEvent;

@ApiPermission
public class Product extends Controller {

	@Inject
	IProductUpdateService productUpdateService;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	WebsiteService websiteEnquiryService;

	@Inject
	UpdateProductStatusAndQuantityAndLabelService updateProductStatusAndQuantityAndLabelService;
	@Inject
	EventBus eventBus;

	/**
	 * 单品发布，多属性发布，品类关联
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT, createuser = "api")
	public Result push() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode reNode = this.createProduct(node, user);
			return ok(reNode);
		} catch (Exception p) {
			Logger.error("add product error: ", p);
			return internalServerError(p.getMessage());
		}
	}

	private JsonNode createProduct(JsonNode node, String userName) {
		String result = "";
		ObjectMapper om = new ObjectMapper();
		ArrayNode anode = om.createArrayNode();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				com.website.dto.product.Product cbase = Json.fromJson(
						nodeiterator.next(),
						com.website.dto.product.Product.class);
				anode.add(createProductToDb(om, cbase, userName));
			}
		} else {
			com.website.dto.product.Product cbase = Json.fromJson(node,
					com.website.dto.product.Product.class);
			anode.add(createProductToDb(om, cbase, userName));
		}
		return anode;
	}

	private JsonNode createMutilProduct(JsonNode node, String userName) {
		String result = "";
		ObjectMapper om = new ObjectMapper();
		ArrayNode anode = om.createArrayNode();
		List<String> spulist = new ArrayList<String>();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				MultiProduct cbase = Json.fromJson(nodeiterator.next(),
						MultiProduct.class);
				spulist.add(cbase.getParentSku());
				anode.add(createProductToDb(om, cbase, userName));
			}
		} else {
			MultiProduct cbase = Json.fromJson(node, MultiProduct.class);
			spulist.add(cbase.getParentSku());
			anode.add(createProductToDb(om, cbase, userName));
		}
		for (String spu : spulist) {
			eventBus.post(new ProductSpuEvent(spu));
		}
		return anode;
	}

	/**
	 * 
	 * @param om
	 * @param cbase
	 * @param userName
	 * @return {sku:"",websiteId:"",listingId:"",errorResult:""}
	 */
	private JsonNode createProductToDb(ObjectMapper om,
			com.website.dto.product.Product cbase, String userName) {
		ObjectNode on = om.createObjectNode();
		on.put("sku", cbase.getSku());
		on.put("websiteId", cbase.getWebsiteId());
		try {
			String listingid = productUpdateService.createProduct(cbase,
					userName);
			on.put("listingId", listingid);
			on.put("errorResult", "");
		} catch (Exception ex) {
			Logger.error("push error: ",ex);
			on.put("listingId", "");
			on.put("errorResult", "failed :" + ex.getMessage());
		}
		return on;
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_MULTI_PRODUCT, createuser = "api")
	public Result pushMutil() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode reNode = this.createMutilProduct(node, user);
			return ok(reNode);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/*
	 * @BodyParser.Of(BodyParser.Json.class) public Result pushPromotionPrice()
	 * {
	 * 
	 * try { String user = request().getHeader("user-token"); JsonNode node =
	 * request().body().asJson(); if (node == null) { return
	 * internalServerError("Expecting Json data"); }
	 * 
	 * String result = ""; if (node.isArray()) {
	 * com.website.dto.product.PromotionPrice[] cbase = Json.fromJson( node,
	 * com.website.dto.product.PromotionPrice[].class); result +=
	 * productUpdateService.saveProductPromotions( Arrays.asList(cbase), user);
	 * 
	 * } else { com.website.dto.product.PromotionPrice cbase = Json.fromJson(
	 * node, com.website.dto.product.PromotionPrice.class);
	 * List<com.website.dto.product.PromotionPrice> pplist = Lists
	 * .newArrayList(); pplist.add(cbase); result =
	 * productUpdateService.saveProductPromotions(pplist, user); }
	 * 
	 * if (result == null || result.length() == 0) { return ok("successfully");
	 * } return internalServerError("failure: " + result); } catch (Exception p)
	 * { p.printStackTrace(); return internalServerError(p.getMessage()); }
	 * 
	 * }
	 */

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_COST_PRICE, createuser = "api")
	public Result updateCostPrice() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			String re = "";
			if (pitem.isArray()) {

				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					re += this.updateCostPirce(list.next());
				}
			} else {
				re += this.updateCostPirce(pitem);
			}
			if (re.length() == 0)
				return ok("successfully");
			return internalServerError("failure: " + re);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	private String updateCostPirce(JsonNode pitem) {
		int wid = pitem.get("websiteId").asInt();
		double cprice = pitem.get("costPrice").asDouble();
		String sku = pitem.get("sku").asText();
		int rows = productUpdateService.updateCostPrice(sku, wid, cprice);
		if (rows <= 0)
			return "sku invalid: " + sku + System.lineSeparator();
		return "";
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_CATEGORY, createuser = "api")
	public Result pushProductCategory() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			String re = "";
			if (pitem.isArray()) {

				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					re += updateProductCategory(list.next());
				}
			} else {
				re += updateProductCategory(pitem);
			}
			if (re.length() == 0)
				return ok("successfully");
			return internalServerError("failure: " + re);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	private String updateProductCategory(JsonNode pitem) {
		int wid = pitem.get("websiteId").asInt();
		String categoryPath = pitem.get("categoryPath").asText();
		String sku = pitem.get("sku").asText();
		return productUpdateService.saveProductCategory(sku, categoryPath, wid);
	}

	/**
	 * update price
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_PRICE, createuser = "api")
	public Result updateProductPrice() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			if (pitem.isArray()) {
				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					JsonNode tnode = list.next();
					productUpdateService.updatePrice(tnode.get("sku").asText(),
							tnode.get("websiteId").asInt(), tnode.get("price")
									.asDouble(), tnode.get("cost").asDouble(),
							tnode.get("freight").asDouble(),
							tnode.get("freight").asDouble() > 0);
				}
			} else {
				productUpdateService.updatePrice(pitem.get("sku").asText(),
						pitem.get("websiteId").asInt(), pitem.get("price")
								.asDouble(), pitem.get("cost").asDouble(),
						pitem.get("freight").asDouble(), pitem.get("freight")
								.asDouble() > 0);
			}
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/**
	 * add product url
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_URL, createuser = "api")
	public Result addProductUrl() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			if (pitem.isArray()) {
				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					JsonNode tnode = list.next();
					Integer langid = null;
					if (tnode.get("languageId") != null) {
						langid = tnode.get("languageId").asInt();
					}
					productUpdateService.addUrl(tnode.get("sku").asText(),
							tnode.get("websiteId").asInt(), tnode.get("url")
									.asText(), langid);
				}
			} else {
				Integer langid = null;
				if (pitem.get("languageId") != null) {
					langid = pitem.get("languageId").asInt();
				}
				productUpdateService.addUrl(pitem.get("sku").asText(), pitem
						.get("websiteId").asInt(), pitem.get("url").asText(),
						langid);
			}
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/**
	 * update price
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_STATUS, createuser = "api")
	public Result updateProductStatus() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			if (pitem.isArray()) {
				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					JsonNode tnode = list.next();
					productUpdateService.updateStatus(
							tnode.get("sku").asText(), tnode.get("websiteId")
									.asInt(), tnode.get("status").asInt());
				}
			} else {
				productUpdateService.updateStatus(pitem.get("sku").asText(),
						pitem.get("websiteId").asInt(), pitem.get("status")
								.asInt());
			}
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_SELLING_POINTS, createuser = "api")
	public Result addProductSellingPoints() {
		String re = "";
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			if (pitem.isArray()) {
				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					re += this.addsellingpoints(list.next());
				}
			} else {
				re += this.addsellingpoints(pitem);
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	private String addsellingpoints(JsonNode tnode) {
		try {
			JsonNode jn = tnode.get("sellingPoints");
			List<String> sellpoints = new ArrayList<String>();
			Iterator<JsonNode> its = jn.iterator();
			while (its.hasNext()) {
				sellpoints.add(its.next().asText());
			}
			return productUpdateService.addSellpoints(
					tnode.get("sku").asText(), tnode.get("websiteId").asInt(),
					tnode.get("languageId").asInt(), sellpoints, "");
		} catch (Exception ex) {
			return ex.getMessage();
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_IMAGE, createuser = "api")
	public Result saveProductImage() {
		String re = "";
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					re += productUpdateService.updateImages(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(), this
							.getImgs(pi.get("images")));
					re += productUpdateService.addProductImages(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(), this
							.getImgs(pi.get("images")));
				}
			} else {
				re += productUpdateService.updateImages(node.get("sku")
						.asText(), node.get("websiteId").asInt(), this
						.getImgs(node.get("images")));
				re += productUpdateService.addProductImages(node.get("sku")
						.asText(), node.get("websiteId").asInt(), this
						.getImgs(node.get("images")));
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	private List<ImageItem> getImgs(JsonNode jnode) {
		if (jnode == null || jnode.isArray() == false) {
			return null;
		}
		Iterator<ImageItem> img = Iterators.transform(jnode.iterator(),
				obj -> {
					ImageItem ii = new ImageItem();
					ii.setBaseImage(obj.get("baseImage").asBoolean());
					ii.setImageUrl(obj.get("imageUrl").asText());
					ii.setLabel(obj.get("label").asText());
					ii.setOrder(obj.get("order").asInt());
					ii.setSmallImage(obj.get("smallImage").asBoolean());
					ii.setThumbnail(obj.get("thumbnail").asBoolean());
					ii.setShowOnDetails(obj.get("showOnDetails").asBoolean());
					return ii;
				});
		return Lists.newArrayList(img);
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_LABLE, createuser = "api")
	public Result addProductLabelType() {
		String re = "";
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					List<String> types = Lists.newArrayList(Iterators
							.transform(pi.get("labelTypes").iterator(),
									obj -> obj.asText()));
					re += productUpdateService.addProductLable(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(), types);
				}
			} else {
				List<String> types = Lists
						.newArrayList(Iterators.transform(node
								.get("labelTypes").iterator(), obj -> obj
								.asText()));
				re += productUpdateService.addProductLable(node.get("sku")
						.asText(), node.get("websiteId").asInt(), types);
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_TRANSLATION, createuser = "api")
	public Result AddTranslation() {
		String re = "";
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					re += productUpdateService.addTransate(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(), this
							.getTranItems(pi.get("items")));
				}
			} else {
				re += productUpdateService.addTransate(
						node.get("sku").asText(),
						node.get("websiteId").asInt(),
						this.getTranItems(node.get("items")));
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_TRANSLATION, createuser = "api")
	public Result updateTranslation() {
		String re = "";
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					re += productUpdateService.updateTransate(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(), this
							.getTranItems(pi.get("items")));
				}
			} else {
				re += productUpdateService.updateTransate(node.get("sku")
						.asText(), node.get("websiteId").asInt(), this
						.getTranItems(node.get("items")));
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	private List<TranslateItem> getTranItems(JsonNode jnode) {
		if (jnode == null || jnode.isArray() == false) {
			return null;
		}
		Iterator<TranslateItem> img = Iterators
				.transform(
						jnode.iterator(),
						obj -> {
							TranslateItem ii = new TranslateItem();
							ii.setLanguageId(obj.get("languageId").asInt());
							ii.setTitle(obj.get("title").asText());
							ii.setDescription(obj.get("description").asText());
							ii.setShortDescription(obj.get("shortDescription")
									.asText());
							ii.setKeyword(obj.get("keyword").asText());
							ii.setMetaTitle(obj.get("metaTitle").asText());
							ii.setMetaKeyword(obj.get("metaKeyword").asText());
							ii.setUrl(obj.get("url").asText());
							ii.setMetaDescription(obj.get("metaDescription").asText());
							JsonNode jn = obj.get("sellingPoints");
							if (null != jn) {
								List<String> sellpoints = new ArrayList<String>();
								Iterator<JsonNode> its = jn.iterator();
								while (its.hasNext()) {
									sellpoints.add(its.next().asText());
								}
								ii.setSellingPoints(sellpoints);
								;
							}
							return ii;
						});
		return Lists.newArrayList(img);
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_STORAGE, createuser = "api")
	public Result updateProductStorage() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			JsonNode pitem = Json.fromJson(node, JsonNode.class);
			if (pitem.isArray()) {
				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					JsonNode tnode = list.next();
					productUpdateService.updateStorages(tnode.get("sku")
							.asText(), tnode.get("websiteId").asInt(), this
							.getStorages(tnode.get("storages")));
				}
			} else {
				productUpdateService.updateStorages(pitem.get("sku").asText(),
						pitem.get("websiteId").asInt(),
						this.getStorages(pitem.get("storages")));
			}
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	private List<Integer> getStorages(JsonNode jn) {
		if (null == jn) {
			return null;
		}
		List<Integer> storagess = new ArrayList<Integer>();
		Iterator<JsonNode> its = jn.iterator();
		while (its.hasNext()) {
			storagess.add(its.next().asInt());
		}
		return storagess;
	}

	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_STATUS_AND_QUANTITY_AND_LABEL, createuser = "erp")
	@JsonGetter
	public Result changeProductLabel() {
		JsonNode data = Json
				.fromJson(request().body().asJson(), JsonNode.class);
		JsonNode productData = data.get("data");
		String type = data.get("type").asText();
		HashMap<String, Object> result = new HashMap<String, Object>();
		String istatus = null;
		if ("clear".equals(type)) {
			type = ProductLabelType.Clearstocks.toString();
		} else if ("stopsale".equals(type)) {
			istatus = "stopsale";
		} else {
			result.put("error", "the type is error");
			return internalServerError(Json.toJson(result));
		}
		List<ProductUpdateInfo> productUpdateInfos = new ArrayList<ProductUpdateInfo>();
		Integer websiteId = websiteEnquiryService.getDefaultWebsite().getIid();
		List<String> errorData = new ArrayList<String>();
		List<String> errorSku = new ArrayList<String>();
		if (productData.isArray()) {
			Iterator<JsonNode> list = productData.iterator();
			while (list.hasNext()) {
				JsonNode tnode = list.next();
				if (tnode == null || tnode.get("sku") == null
						|| tnode.get("qty") == null
						|| tnode.get("storageName") == null) {
					continue;
				}
				String sku = tnode.get("sku").asText();
				List<ProductBase> products = productEnquiryService.getProduct(
						sku, websiteId);
				if (products == null) {
					errorSku.add(sku);
					continue;
				}
				String storageName = tnode.get("storageName").asText();
				Integer qty = null;
				if (tnode.get("qty").isInt()) {
					qty = tnode.get("qty").asInt();
				} else {
					errorData.add(sku);
					continue;
				}
				for (ProductBase product : products) {
					productUpdateInfos.add(new ProductUpdateInfo(type, product
							.getIwebsiteid(), storageName, istatus, qty,
							product.getClistingid(), sku));
				}
			}
		}

		// ERP推送的数据，更新产品状态，库存，和产品标签
		updateProductStatusAndQuantityAndLabelService
				.updateProductStatusAndQuantityAndLabel(productUpdateInfos);

		if (!errorData.isEmpty() || !errorSku.isEmpty()) {
			result.put("errorSku", errorSku);
			result.put("errorData", errorData);
			return internalServerError(Json.toJson(result));
		}
		result.put("result", "success");
		return ok(Json.toJson(result));
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_FREIGHT, createuser = "api")
	public Result updateProductFreight() {
		String re = "";
		try {
			// String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					re += productUpdateService.updateFregiht(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(),
							pi.get("freight").asDouble(), pi.get("freight")
									.asDouble() > 0);
				}
			} else {
				re += productUpdateService.updateFregiht(node.get("sku")
						.asText(), node.get("websiteId").asInt(),
						node.get("freight").asDouble(), node.get("freight")
								.asDouble() > 0);
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_LABEL, createuser = "api")
	public Result deleteProductLabel() {
		String re = "";
		try {
			// String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					List<String> types = Lists.newArrayList(Iterators
							.transform(pi.get("labelTypes").iterator(),
									obj -> obj.asText()));
					re += productUpdateService.deleteProductLabel(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(), types);
				}
			} else {
				List<String> types = Lists
						.newArrayList(Iterators.transform(node
								.get("labelTypes").iterator(), obj -> obj
								.asText()));
				re += productUpdateService.deleteProductLabel(node.get("sku")
						.asText(), node.get("websiteId").asInt(), types);
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.UPDATE_PRODUCT_QTY, createuser = "api")
	public Result updatePorductQty() {
		String re = "";
		try {
			// String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					re += productUpdateService.updateProductQty(pi.get("sku")
							.asText(), pi.get("websiteId").asInt(),
							pi.get("qty").asInt());
				}
			} else {
				re += productUpdateService.updateProductQty(node.get("sku")
						.asText(), node.get("websiteId").asInt(),
						node.get("qty").asInt());
			}
			if (re.length() == 0)
				return ok("successfully");
			else
				return internalServerError(re);
		} catch (Exception p) {
			return internalServerError(re + "--" + p.getMessage());
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getProducts() {
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			if (node.get("websiteId") == null) {
				return internalServerError("not found websiteId param!");
			}
			if (node.get("beginDate") == null) {
				return internalServerError("not found beginDate param!");
			}
			if (node.get("endDate") == null) {
				return internalServerError("not found endDate param!");
			}
			node.get("beginDate").asText();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date bdate = df.parse(node.get("beginDate").asText());
			Date edate = df.parse(node.get("endDate").asText());
			List<com.website.dto.product.Product> plist = productEnquiryService
					.getProducts(node.get("websiteId").asInt(), bdate, edate);
			if (null != plist) {
				ObjectMapper om = new ObjectMapper();
				return ok(om.writeValueAsString(plist));
			}
			return ok("[]");
		} catch (Exception p) {
			p.printStackTrace();
			return internalServerError("get product error --" + p.getMessage());
		}
	}

	/**
	 * 如果当前优惠价时间段在数据库中已经被包含，则不会入库
	 * {"listingId":"","beginDate":"","endDate":"","sku":"","salePrice":""}
	 * 
	 * @author lijun
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_SALE_PRICE, createuser = "api")
	public Result addProductSalePrice() {
		try {

			List<JsonNode> jsonlist = new ArrayList<JsonNode>();
			JsonNode parastmp = request().body().asJson();
			if (parastmp == null) {
				return internalServerError("Expecting : please pass json format data");
			}
			if (parastmp.isArray()) {
				jsonlist = Lists.newArrayList(parastmp.iterator());
			} else {
				jsonlist.add(parastmp);
			}
			String errmsg = "";
			for (JsonNode paras : jsonlist) {

				JsonNode listingIdJson = paras.get("listingId");
				if (listingIdJson == null) {
					errmsg += "Expecting : listingId can not find"
							+ listingIdJson + System.lineSeparator();
					continue;
				}

				JsonNode beginDateJson = paras.get("beginDate");
				if (beginDateJson == null) {
					errmsg += "Expecting : beginDate can not find"
							+ listingIdJson + System.lineSeparator();
					continue;
				}

				JsonNode endDateJson = paras.get("endDate");
				if (endDateJson == null) {
					errmsg += "Expecting : endDate can not find"
							+ listingIdJson + System.lineSeparator();
					continue;
				}

				JsonNode skuJson = paras.get("sku");
				if (skuJson == null) {
					errmsg += "Expecting : sku can not find" + listingIdJson
							+ System.lineSeparator();
					continue;
				}

				JsonNode salePriceJson = paras.get("salePrice");
				if (salePriceJson == null) {
					errmsg += "Expecting : salePrice can not find"
							+ listingIdJson + System.lineSeparator();
					continue;
				}

				String listingId = listingIdJson.asText();
				String beginDate = beginDateJson.asText();
				String endDate = endDateJson.asText();
				String sku = skuJson.asText();
				double salePrice = salePriceJson.asDouble();
				ProductBase pbase = productEnquiryService
						.getBaseByListingId(listingId);
				if (pbase == null) {
					errmsg += "Expecting : invalid listingid" + listingIdJson
							+ System.lineSeparator();
					continue;
				}
				if (pbase.getCsku().equals(sku) == false) {
					errmsg += "Expecting : invalid sku" + listingIdJson
							+ System.lineSeparator();
					continue;
				}
				if (pbase.getFprice() <= salePrice) {
					errmsg += "Expecting : saleprice can not > current price"
							+ listingIdJson + System.lineSeparator();
					continue;
				}

				Date bdPara = DateUtils.parseDate(beginDate,
						"yyyy-MM-dd HH:mm:ss");
				Date edPara = DateUtils.parseDate(endDate,
						"yyyy-MM-dd HH:mm:ss");

				// SimpleDateFormat dateFormater = new
				// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 用户传的结束时间必须大于当前系统时间
				Date currentDate = new Date();
				currentDate = DateUtils.setHours(currentDate, 0);
				currentDate = DateUtils.setMinutes(currentDate, 0);
				currentDate = DateUtils.setSeconds(currentDate, 0);
				currentDate = DateUtils.setMilliseconds(currentDate, 0);

				if (edPara.before(currentDate)) {
					errmsg += "Expecting : endDate must after currentDate"
							+ listingIdJson + System.lineSeparator();
					continue;
				}

				ProductSalePrice record = new ProductSalePrice();
				record.setClistingid(listingId);
				record.setCsku(sku);
				record.setDbegindate(bdPara);
				record.setDenddate(edPara);
				record.setCcreateuser("api");
				record.setDcreatedate(new Date());
				record.setFsaleprice(salePrice);

				Map<String, Object> queryParas = new HashMap<String, Object>();
				queryParas.put("listingId", listingId);
				queryParas.put("currentDate", currentDate);
				List<ProductSalePrice> pspList = productUpdateService
						.getProductSalePriceAfterCurrentDate(queryParas);

				if (pspList == null) {
					if (Logger.isDebugEnabled()) {
						Logger.debug("数据库无该listingId数据,直接入库");
					}
					productUpdateService.insertProductSalePrice(record);
				} else {
					/*
					 * 判断用户传的时间参数是否在数据库时间段内 如果在则不让用户对数据库有任何的操作
					 */
					boolean isExist = this.ValidationSalePriceDate(record,
							pspList);
					if (!isExist) {
						int isSucceed = productUpdateService
								.insertProductSalePrice(record);
						if (1 == isSucceed) {
							if (Logger.isDebugEnabled()) {
								Logger.debug("入库成功");
							}
						} else {
							if (Logger.isDebugEnabled()) {
								Logger.debug("入库失败");
							}
						}

					} else {
						if (Logger.isDebugEnabled()) {
							Logger.debug("时间上有重合,入库失败");
						}
						errmsg += "Expecting : 时间上有重合,不能入库" + listingIdJson
								+ System.lineSeparator();
						continue;
					}
				}
			}
			if ("".equals(errmsg) == false) {
				return internalServerError(errmsg);
			}
		} catch (Exception e) {
			if (Logger.isDebugEnabled()) {
				Logger.debug("", e);
			}
			// Logger.error("添加优惠时间失败", e);
			return internalServerError(e.getMessage());
		}

		return ok("successfully");
	}

	/**
	 * 校验
	 * 
	 * @author lijun
	 * @return
	 */
	private boolean ValidationSalePriceDate(ProductSalePrice psp,
			List<ProductSalePrice> compareList) {
		boolean isExist = false;
		for (ProductSalePrice cell : compareList) {
			if ((psp.getDenddate().after(cell.getDenddate()) && psp
					.getDbegindate().before(cell.getDenddate()))
					|| (psp.getDbegindate().before(cell.getDbegindate()) && psp
							.getDenddate().after(cell.getDbegindate()))
					|| (psp.getDbegindate().equals(cell.getDbegindate()) || psp
							.getDenddate().equals(cell.getDenddate()))) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result getProductsByListingid() {
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			List<String> listingids = Lists.newArrayList();
			if (node.isArray()) {
				listingids.addAll(Lists.newArrayList(Iterators.transform(
						node.iterator(), obj -> obj.asText())));
			} else {
				listingids.add(node.asText());
			}
			List<com.website.dto.product.Product> plist = productEnquiryService
					.getProductsByListingIds(listingids);
			if (null != plist) {
				ObjectMapper om = new ObjectMapper();
				return ok(om.writeValueAsString(plist));
			} else {
				return ok("[]");
			}
		} catch (Exception p) {
			p.printStackTrace();
			Logger.error("getProductsByListingid error: ", p);
			return internalServerError("get product error --" + p.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	@ApiHistory(type = HandleReceivedDataType.DELETE_CURRENT_PRODUCTSALEPRICE, createuser = "api")
	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteProductSalePrice() {
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}

			String listingId = node.get("listingId").asText();
			Date begindate = DateUtils.parseDate(
					node.get("beginDate").asText(), "yyyy-MM-dd HH:mm:ss");
			Date endate = DateUtils.parseDate(node.get("endDate").asText(),
					"yyyy-MM-dd HH:mm:ss");
			productUpdateService.deleteProductSalePriceByDate(listingId,
					begindate, endate);
			return ok("successfully");
		} catch (Exception p) {
			p.printStackTrace();
			Logger.error("deleteProductCurrentSalePrice error: ", p);
			return internalServerError("deleteProductCurrentSalePrice error --"
					+ p.getMessage());
		}
	}

	/**
	 * 
	 * @return
	 */
	@ApiHistory(type = HandleReceivedDataType.DELETE_CURRENT_PRODUCTSALEPRICE, createuser = "api")
	public Result deleteProductCurrentSalePrice(String listingId) {
		try {
			productUpdateService.deleteProductCurrentSalePrice(listingId);
			return ok("successfully");
		} catch (Exception p) {
			p.printStackTrace();
			Logger.error("deleteProductCurrentSalePrice error: ", p);
			return internalServerError("deleteProductCurrentSalePrice error --"
					+ p.getMessage());
		}
	}

	@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_SELLINGPOINT, createuser = "api")
	public Result deleteProductSellingPoints(String listingId,
			Integer languageId) {
		try {
			this.productUpdateService.deleteProductSellingPoints(listingId,
					languageId);
			return ok("successfully");
		} catch (Exception p) {
			p.printStackTrace();
			Logger.error("deleteProductSellingPoints error: ", p);
			return internalServerError(" deleteProductSellingPoints error --"
					+ p.getMessage());
		}

	}

	/**
	 * 复制产品
	 * {"websiteId":1,"listingId":"","qty":1000,"status":1,"categoryIds":[1,
	 * 5,3]}
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public Result copy() {
		JsonNode node = request().body().asJson();
		if (node == null) {
			return internalServerError("Expecting Json data");
		}
		Map<String, JsonNode> map = Maps.newHashMap();
		if (node.isArray()) {
			map = Maps.uniqueIndex(node.iterator(), p -> p.get("listingId")
					.asText());
		} else {
			map.put(node.get("listingId").asText(), node);
		}
		String result = "";
		List<com.website.dto.product.Product> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(map.keySet()));
		for (com.website.dto.product.Product p : plist) {
			try {
				JsonNode jnode = map.get(p.getListingId());
				JsonNode canode = jnode.get("categoryIds");
				p.setCategoryIds(null);
				if (canode != null) {
					Iterator<Integer> ints = Iterators.transform(
							jnode.get("categoryIds").iterator(),
							it -> it.asInt());
					p.setCategoryIds(Lists.newArrayList(ints));
				}
				p.setWebsiteId(jnode.get("websiteId").asInt());
				p.getItems().forEach(item -> item.setUrl(""));
				p.setQty(jnode.get("qty").asInt());
				p.setStatus(jnode.get("status").asInt());
				productUpdateService.createProduct(p, "copy");
			} catch (Exception ex) {
				Logger.error("copy product error: ", ex);
				result += p.getListingId() + "--->" + ex.getMessage()
						+ System.lineSeparator();
			}
		}
		if (result.trim().length() == 0) {
			return ok("successfully");
		} else {
			return internalServerError(result);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_IMAGE, createuser = "api")
	public Result deleteProductImage(String listingId, Integer websiteId) {
		try {
			boolean result = productUpdateService
					.deleteProductImageByListingId(listingId, websiteId);
			if (result) {
				Logger.debug("delete product image successfully:{}", listingId);
				return ok("successfully");
			}
		} catch (Exception e) {
			Logger.error("delete product image error:", e);

			return internalServerError("delete product image error:"
					+ e.toString());
		}

		return ok("successfully");
	}
	
	/**
	 * 
	 * @Title: addProductCategory
	 * @Description: TODO(增加产品与品类映射)
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_CATEGORY, createuser = "api")
	public Result addProductCategory() {
		String re = "";
		try {
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			List<String> listingIdList = null;
			if (node.isArray()) {
				Iterator<JsonNode> list = node.iterator();
				while (list.hasNext()) {
					JsonNode pi = list.next();
					List<Integer> categoryIds = Lists.newArrayList(Iterators
							.transform(pi.get("categoryIds").iterator(),
									obj -> obj.asInt()));
					listingIdList = Lists.newArrayList(Iterators.transform(pi
							.get("listingId").iterator(), obj -> obj.asText()));
					re += productUpdateService.addProductCategoryMapper(
							listingIdList, categoryIds);
				}
			} else {
				List<Integer> categoryIds = Lists.newArrayList(Iterators
						.transform(node.get("categoryIds").iterator(),
								obj -> obj.asInt()));
				listingIdList = Lists.newArrayList(Iterators.transform(node
						.get("listingId").iterator(), obj -> obj.asText()));
				re += productUpdateService.addProductCategoryMapper(
						listingIdList, categoryIds);
			}
			if (re.length() == 0) {
				return ok("successfully");
			} else {
				return internalServerError(re);
			}
		} catch (Exception e) {
			return internalServerError(re + "--" + e.getMessage());
		}
	}
}