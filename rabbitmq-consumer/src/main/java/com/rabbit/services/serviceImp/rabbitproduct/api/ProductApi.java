package com.rabbit.services.serviceImp.rabbitproduct.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.rabbit.common.enums.search.criteria.ProductLabelType;
import com.rabbit.dto.ProductUpdateInfo;
import com.rabbit.dto.product.ProductBase;
import com.rabbit.dto.product.ProductSalePrice;
import com.rabbit.dto.transfer.product.ImageItem;
import com.rabbit.dto.transfer.product.MultiProduct;
import com.rabbit.dto.transfer.product.ProductBack;
import com.rabbit.dto.transfer.product.TranslateItem;
import com.rabbit.services.iservice.product.IProductUpdateService;
import com.rabbit.services.serviceImp.product.ProductEnquiryService;
import com.rabbit.services.serviceImp.product.ProductUpdateElasticsearchService;
import com.rabbit.services.serviceImp.product.UpdateProductStatusAndQuantityAndLabelService;

@Service
public class ProductApi{

	private static Logger log=Logger.getLogger(ProductApi.class.getName());
	private static String USER_API="api";
	@Autowired
	IProductUpdateService productUpdateService;

	@Autowired
	ProductEnquiryService productEnquiryService;

	@Autowired
	UpdateProductStatusAndQuantityAndLabelService updateProductStatusAndQuantityAndLabelService;
	@Autowired
	EventBus eventBus;
	@Autowired
	ProductUpdateElasticsearchService elasticsearchService;

	/**
	 * 单品发布，多属性发布，品类关联
	 * 
	 * @return
	 * @throws Exception 
	 */
	public void push(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi push Expecting Json data!");
				throw new Exception("CategoryApi push Expecting Json data");
			}
			JsonNode reNode = this.createProduct(node, USER_API);
			log.debug("productApi push reNode:"+reNode);
		} catch (Exception p) {
			log.error("ProductApi push error!",p);
			throw p;
		}
	}

	private JsonNode createProduct(JsonNode node, String userName) throws Exception {
		ObjectMapper om = new ObjectMapper();
		ArrayNode anode = om.createArrayNode();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				ProductBack cbase = om.convertValue(
						nodeiterator.next(),
						ProductBack.class);
				anode.add(createProductToDb(om, cbase, userName));
			}
		} else {
			ProductBack cbase = om.convertValue(node,
					ProductBack.class);
			anode.add(createProductToDb(om, cbase, userName));
		}
		return anode;
	}

	private JsonNode createMutilProduct(JsonNode node, String userName) throws Exception {
		ObjectMapper om = new ObjectMapper();
		ArrayNode anode = om.createArrayNode();
		List<String> spulist = new ArrayList<String>();
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				MultiProduct cbase = om.convertValue(nodeiterator.next(),
						MultiProduct.class);
				spulist.add(cbase.getParentSku());
				anode.add(createProductToDb(om, cbase, userName));
			}
		} else {
			MultiProduct cbase = om.convertValue(node, MultiProduct.class);
			spulist.add(cbase.getParentSku());
			anode.add(createProductToDb(om, cbase, userName));
		}
		return anode;
	}

	/**
	 * 
	 * @param om
	 * @param cbase
	 * @param userName
	 * @return {sku:"",websiteId:"",listingId:"",errorResult:""}
	 * @throws Exception 
	 */
	private JsonNode createProductToDb(ObjectMapper om,
			ProductBack cbase, String userName) throws Exception {
		ObjectNode on = om.createObjectNode();
		on.put("sku", cbase.getSku());
		on.put("websiteId", cbase.getWebsiteId());
		try {
			String listingid = productUpdateService.createProduct(cbase,
					userName);
			on.put("listingId", listingid);
			on.put("errorResult", "");
		} catch (Exception ex) {
			log.error("push error: ",ex);
			on.put("listingId", "");
			on.put("errorResult", "failed :" + ex.getMessage());
			throw ex;
		}
		return on;
	}

	public void pushMutil(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi pushMutil Expecting Json data!");
				throw new Exception("CategoryApi pushMutil Expecting Json data");
			}
			JsonNode reNode = this.createMutilProduct(node, USER_API);
			log.debug("productApi pushMutil reNode:"+reNode);
		} catch (Exception p) {
			log.error("ProductApi pushMutil error!",p);
			throw p;
		}
	}


	public String updateCostPrice(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi updateCostPrice Expecting Json data!");
				throw new Exception("CategoryApi updateCostPrice Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
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
				return "successfully";
			
			log.info("failure: " + re);
			throw new Exception("failure: " + re);
		} catch (Exception p) {
			log.error("ProductApi updateCostPrice error!",p);
			throw p;
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

	public String pushProductCategory(JsonNode node ) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi pushProductCategory Expecting Json data!");
				throw new Exception("CategoryApi pushProductCategory Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
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
				return "successfully";
			log.info("failure: " + re);
			throw new Exception("failure: " + re);
		} catch (Exception p) {
			log.error("ProductApi pushProductCategory error!",p);
			throw p;
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
	 * @throws Exception 
	 */
	public String updateProductPrice(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi updateProductPrice Expecting Json data!");
				throw new Exception("CategoryApi updateProductPrice Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
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
			return "successfully";
		} catch (Exception p) {
			log.error("ProductApi updateProductPrice error!",p);
			throw p;
		}
	}

	/**
	 * add product url
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String addProductUrl(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi addProductUrl Expecting Json data!");
				throw new Exception("CategoryApi addProductUrl Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
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
			return "successfully";
		} catch (Exception p) {
			log.error("ProductApi addProductUrl error!",p);
			throw p;
		}
	}

	/**
	 * update price
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String updateProductStatus(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi updateProductStatus Expecting Json data!");
				throw new Exception("CategoryApi updateProductStatus Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
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
			return "successfully";
		} catch (Exception p) {
			log.error("ProductApi updateProductStatus error!",p);
			throw p;
		}
	}

	public String addProductSellingPoints(JsonNode node ) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("ProductApi addProductSellingPoints Expecting Json data!");
				throw new Exception("CategoryApi addProductSellingPoints Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
			if (pitem.isArray()) {
				Iterator<JsonNode> list = pitem.iterator();
				while (list.hasNext()) {
					re += this.addsellingpoints(list.next());
				}
			} else {
				re += this.addsellingpoints(pitem);
			}
			if (re.length() == 0)
				return "successfully";
			else{
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi addProductSellingPoints error!re:"+re,p);
			throw p;
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
			log.error("addsellingpoints error:",ex); 
			throw ex;
		}
	}

	public String saveProductImage(JsonNode node) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("ProductApi saveProductImage Expecting Json data!");
				throw new Exception("CategoryApi saveProductImage Expecting Json data");
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
				return "successfully";
			else{
				
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi saveProductImage error!re:"+re,p);
			throw p;
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

	public String addProductLabelType(JsonNode node ) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("ProductApi addProductLabelType Expecting Json data!");
				throw new Exception("CategoryApi addProductLabelType Expecting Json data");
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
				return "successfully";
			else{
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi addProductLabelType error!re:"+re,p);
			throw p;
		}
	}

	public String AddTranslation(JsonNode node) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("ProductApi AddTranslation Expecting Json data!");
				throw new Exception("CategoryApi AddTranslation Expecting Json data");
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
				return "successfully";
			else{
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi AddTranslation error!re:"+re,p);
			throw p;
		}
	}

	public String updateTranslation(JsonNode node) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("ProductApi updateTranslation Expecting Json data!");
				throw new Exception("CategoryApi updateTranslation Expecting Json data");
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
				return "successfully";
			else{
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi updateTranslation error!re:"+re,p);
			throw p;
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

	public String updateProductStorage(JsonNode node ) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi updateProductStorage Expecting Json data!");
				throw new Exception("CategoryApi updateProductStorage Expecting Json data");
			}
			ObjectMapper om=new ObjectMapper();
			JsonNode pitem = om.convertValue(node, JsonNode.class);
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
			return "successfully";
		} catch (Exception p) {
			log.error("ProductApi updateProductStorage error!",p);
			throw p;
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

	public String changeProductLabel(JsonNode data ) throws Exception {
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
			log.info("failure: " + JSON.toJSONString(result));
			throw new Exception("failure: " + JSON.toJSONString(result));
		}
		List<ProductUpdateInfo> productUpdateInfos = new ArrayList<ProductUpdateInfo>();
		Integer websiteId = 1;
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
			log.info("failure: " + JSON.toJSONString(result));
			throw new Exception("failure: " + JSON.toJSONString(result));
		}
		result.put("result", "success");
		return JSON.toJSONString(result);
	}

	public String updateProductFreight(JsonNode node) throws Exception {
		String re = "";
		try {
			// String user = request().getHeader("user-token");
			if (node == null) {
				log.info("ProductApi updateProductFreight Expecting Json data!");
				throw new Exception("CategoryApi updateProductFreight Expecting Json data");
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
				return "successfully";
			else{
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi updateProductFreight error!re:"+re,p);
			throw p;
		}
	}

	public String deleteProductLabel(JsonNode node) throws Exception {
		String re = "";
		try {
			// String user = request().getHeader("user-token");
			if (node == null) {
				log.info("ProductApi deleteProductLabel Expecting Json data!");
				throw new Exception("CategoryApi deleteProductLabel Expecting Json data");
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
				return "successfully";
			else{
				log.info("failure: " + re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi deleteProductLabel error!re:"+re,p);
			throw p;
		}
	}

	public String updatePorductQty(JsonNode node) throws Exception {
		String re = "";
		try {
			// String user = request().getHeader("user-token");
			if (node == null) {
				log.info("ProductApi updatePorductQty Expecting Json data!");
				throw new Exception("CategoryApi updatePorductQty Expecting Json data");
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
				return "successfully";
			else{
				log.info("failure: " +re);
				throw new Exception("failure: " + re);
			}
		} catch (Exception p) {
			log.error("ProductApi updatePorductQty error!re:"+re,p);
			throw p;
		}
	}

	public String getProducts(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi getProducts Expecting Json data!");
				throw new Exception("CategoryApi getProducts Expecting Json data");
			}
			if (node.get("websiteId") == null) {
				log.info("ProductApi getProducts not found websiteId param!");
				throw new Exception("CategoryApi getProducts not found websiteId param!");
			}
			if (node.get("beginDate") == null) {
				log.info("ProductApi getProducts not found beginDate param!");
				throw new Exception("CategoryApi getProducts not found beginDate param!");
			}
			if (node.get("endDate") == null) {
				log.info("ProductApi getProducts not found endDate param!");
				throw new Exception("CategoryApi getProducts not found endDate param!");
			}
			node.get("beginDate").asText();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date bdate = df.parse(node.get("beginDate").asText());
			Date edate = df.parse(node.get("endDate").asText());
			List<ProductBack> plist = productEnquiryService
					.getProducts(node.get("websiteId").asInt(), bdate, edate);
			if (null != plist) {
				ObjectMapper om = new ObjectMapper();
				return om.writeValueAsString(plist);
			}
			return "[]";
		} catch (Exception p) {
			log.error("ProductApi updatePorductQty error!",p);
			throw p;
		}
	}

	/**
	 * 如果当前优惠价时间段在数据库中已经被包含，则不会入库
	 * {"listingId":"","beginDate":"","endDate":"","sku":"","salePrice":""}
	 * 
	 * @author lijun
	 * @return
	 * @throws Exception 
	 */
	public String addProductSalePrice(JsonNode parastmp) throws Exception {
		try {

			List<JsonNode> jsonlist = new ArrayList<JsonNode>();
			if (parastmp == null) {
				log.info("ProductApi addProductSalePrice Expecting : please pass json format data!");
				throw new Exception("CategoryApi addProductSalePrice Expecting : please pass json format data");
				
			}
			if (parastmp.isArray()) {
				jsonlist = Lists.newArrayList(parastmp.iterator());
			} else {
				jsonlist.add(parastmp);
			}
			String errmsg = "";
			
//			List<PromotionSalePrice> promotionPrice = Lists.newArrayList();
			
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
					if (log.isDebugEnabled()) {
						log.debug("数据库无该listingId数据,直接入库");
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
							if (log.isDebugEnabled()) {
								log.debug("入库成功");
							}
						} else {
							if (log.isDebugEnabled()) {
								log.debug("入库失败");
							}
						}

					} else {
						if (log.isDebugEnabled()) {
							log.debug("时间上有重合,入库失败");
						}
						errmsg += "Expecting : 时间上有重合,不能入库" + listingIdJson
								+ System.lineSeparator();
						continue;
					}
				}
				
//				List<com.rabbit.dto.search.PromotionSalePrice> promotionPrice = Lists.newArrayList();
//				com.rabbit.dto.search.PromotionSalePrice pp = new com.rabbit.dto.search.PromotionSalePrice();
//				pp.setBeginDate(beginDate);
//				pp.setEndDate(endDate);
//				pp.setPrice(salePrice);
//				pp.setWebSiteId(pbase.getIwebsiteid());
//				pp.setSku(sku);
//				promotionPrice.add(pp);
//				JSONObject jsonobj = new JSONObject();
//				jsonobj.put("promotionPrice", promotionPrice);
////				String json = elasticsearchService.getElasticsearchJson(plist.get(0));
//				elasticsearchService.updateProductPart(listingId, JSON.toJSONString(jsonobj));
			
				
			}
			for (JsonNode paras : jsonlist) {

				JsonNode listingIdJson = paras.get("listingId");
				if (listingIdJson == null) {
					errmsg += "Expecting : listingId can not find"
							+ listingIdJson + System.lineSeparator();
				}

				String listingId = listingIdJson.asText();
				List<ProductBack> plist = productEnquiryService
						.getProductsByListingIds(Lists.newArrayList(listingId));
				String json = elasticsearchService.getElasticsearchJson(plist.get(0));
				elasticsearchService.updateProductPart(listingId, json);
			}
			
			if ("".equals(errmsg) == false) {
				log.info("failure: " + errmsg);
				
//				throw new Exception("failure: " + errmsg);
			}
			
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("", e);
			}
			// Logger.error("添加优惠时间失败", e);
			throw e;
		}

		return "successfully";
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

	public String getProductsByListingid(JsonNode node) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi getProductsByListingid Expecting Json data!");
				throw new Exception("CategoryApi getProductsByListingid Expecting Json data");
			}
			List<String> listingids = Lists.newArrayList();
			if (node.isArray()) {
				listingids.addAll(Lists.newArrayList(Iterators.transform(
						node.iterator(), obj -> obj.asText())));
			} else {
				listingids.add(node.asText());
			}
			List<ProductBack> plist = productEnquiryService
					.getProductsByListingIds(listingids);
			if (null != plist) {
				ObjectMapper om = new ObjectMapper();
				return om.writeValueAsString(plist);
			} else {
				return "[]";
			}
		} catch (Exception p) {
			log.error("ProductApi getProductsByListingid error!",p);
			throw p;
		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String deleteProductSalePrice(JsonNode node ) throws Exception {
		try {
			if (node == null) {
				log.info("ProductApi deleteProductSalePrice Expecting Json data!");
				throw new Exception("CategoryApi deleteProductSalePrice Expecting Json data");
			}

			String listingId = node.get("listingId").asText();
			Date begindate = DateUtils.parseDate(
					node.get("beginDate").asText(), "yyyy-MM-dd HH:mm:ss");
			Date endate = DateUtils.parseDate(node.get("endDate").asText(),
					"yyyy-MM-dd HH:mm:ss");
			productUpdateService.deleteProductSalePriceByDate(listingId,
					begindate, endate);
			return "successfully";
		} catch (Exception p) {
			log.error("ProductApi deleteProductSalePrice error!",p);
			throw p;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String deleteProductCurrentSalePrice(String listingId) {
		try {
			productUpdateService.deleteProductCurrentSalePrice(listingId);
			return "successfully";
		} catch (Exception p) {
			p.printStackTrace();
			log.error("deleteProductCurrentSalePrice error: ", p);
			throw p;
		}
	}

	public String deleteProductSellingPoints(String listingId,
			Integer languageId) {
		try {
			this.productUpdateService.deleteProductSellingPoints(listingId,
					languageId);
			return "successfully";
		} catch (Exception p) {
			log.error("ProductApi deleteProductSalePrice error!",p);
			throw p;
		}

	}

	/**
	 * 复制产品
	 * {"websiteId":1,"listingId":"","qty":1000,"status":1,"categoryIds":[1,
	 * 5,3]}
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String copy(JsonNode node ) throws Exception {
		if (node == null) {
			log.info("ProductApi copy Expecting Json data!");
			throw new Exception("CategoryApi copy Expecting Json data");
		}
		Map<String, JsonNode> map = Maps.newHashMap();
		if (node.isArray()) {
			map = Maps.uniqueIndex(node.iterator(), p -> p.get("listingId")
					.asText());
		} else {
			map.put(node.get("listingId").asText(), node);
		}
		String result = "";
		List<ProductBack> plist = productEnquiryService
				.getProductsByListingIds(Lists.newArrayList(map.keySet()));
		for (ProductBack p : plist) {
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
				log.error("copy product error: ", ex);
				result += p.getListingId() + "--->" + ex.getMessage()
						+ System.lineSeparator();
			}
		}
		if (result.trim().length() == 0) {
			return "successfully";
		} else {
			log.info("failure: " + result);
			throw new Exception("failure: " +result);
		}
	}

	public String deleteProductImage(String listingId, Integer websiteId) {
		try {
			boolean result = productUpdateService
					.deleteProductImageByListingId(listingId, websiteId);
			if (result) {
				log.debug("delete product image successfully:{}"+ listingId);
				return "successfully";
			}
		} catch (Exception e) {
			log.error("ProductApi deleteProductImage error!",e);
			throw e;
		}

		return "successfully";
	}
	
	/**
	 * 
	 * @Title: addProductCategory
	 * @Description: TODO(增加产品与品类映射)
	 * @param @return
	 * @return Result
	 * @throws Exception 
	 * @throws 
	 * @author yinfei
	 */
	public String addProductCategory(JsonNode node) throws Exception {
		String re = "";
		try {
			if (node == null) {
				log.info("ProductApi addProductCategory Expecting Json data!");
				throw new Exception("CategoryApi addProductCategory Expecting Json data");
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
				return "successfully";
			} else {
				log.info("failure: " + re);
				throw new Exception("failure: " +re);
			}
		} catch (Exception e) {
			log.error("ProductApi addProductCategory error!",e);
			throw e;
		}
	}
}