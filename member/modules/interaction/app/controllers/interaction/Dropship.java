package controllers.interaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.base.utils.ExcelUtils;
import services.interaction.dropship.DropshipService;
import services.member.login.LoginService;
import valueobjects.base.Page;
import valueobjects.member.DropshipBase;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.annotation.DropShippingAuthenticator;
import dto.interaction.DropshipProduct;
import dto.product.ProductDropShip;

public class Dropship extends Controller {

	@Inject
	FoundationService foundationService;

	@Inject
	LoginService loginService;

	@Inject
	DropshipService dropshipService;

	@DropShippingAuthenticator
	public Result addDropshipProductView() {
		String email = loginService.getLoginEmail();
		Integer websiteid = foundationService.getSiteID();
		DropshipBase dropshipBase = dropshipService.getDropshipBaseByEmail(
				email, websiteid);
		// leftCount表示该dropship用户最多还可以添加多少个产品
		Integer dropshipCount = dropshipService.getCountDropShipSkuByEmail(
				email, websiteid);
		Integer leftCount = dropshipBase.getMaxDropshipNumberLimit()
				- dropshipCount;
		return ok(views.html.interaction.dropship.add_dropship_product
				.render(leftCount));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result batchAddDropshipProduct() {
		Map<String, Object> resultMap = Maps.newHashMap();
		Integer siteId = foundationService.getSiteID();
		List<String> errSkus = Lists.newLinkedList();
		JsonNode json = request().body().asJson();
		Integer leftCount = json.get("leftCount").asInt();
		String skusString = json.get("skus").asText();
		String[] skuArray = skusString.split(",");
		if (null != skuArray && skuArray.length > 0) {
			if (leftCount < skuArray.length) {
				resultMap.put("errorCode", "over");
				return ok(Json.toJson(resultMap));
			}
			String email = loginService.getLoginEmail();
			for (String sku : skuArray) {
				sku = sku.trim();
				if (null != sku && !"".equals(sku)) {
					boolean result = dropshipService
							.checkSkuIsExsitInProductBase(sku, siteId);
					if (result) {
						boolean result2 = dropshipService
								.checkSkuIsExistInDropProduct(sku, siteId,
										email);
						if (!result2) {
							DropshipProduct dropship = new DropshipProduct();
							dropship.setBstate(true);
							dropship.setCemail(email);
							dropship.setCsku(sku);
							dropship.setIwebsiteid(siteId);
							dropship.setDcreatedate(new Date());
							boolean result3 = dropshipService
									.addDropshipProduct(dropship);
							if (!result3) {
								errSkus.add(sku);
							}
						} else {
							errSkus.add(sku);
						}
					} else {
						errSkus.add(sku);
					}
				}
			}
		}
		resultMap.put("errSkus", errSkus);
		return ok(Json.toJson(resultMap));
	}

	@DropShippingAuthenticator
	public Result dropshipProductList(Integer page, Integer pageSize,
			String status, String sku) {
		String email = loginService.getLoginEmail();
		Integer websiteid = foundationService.getSiteID();
		DropshipBase dropshipBase = dropshipService.getDropshipBaseByEmail(
				email, websiteid);
		Integer languageid = foundationService.getLanguage();
		String currency = foundationService.getCurrency();
		Integer siteid = foundationService.getSiteID();
		List<valueobjects.interaction.Dropship> dropships = dropshipService
				.getdropshipProductListByPage(status, sku, email, siteid,
						languageid, currency);
		Integer total = dropships.size();// 总记录数
		// 计算需要显示的结果数据
		List<valueobjects.interaction.Dropship> dropships2 = Lists
				.newArrayList();
		for (int i = ((page - 1) * pageSize); i < dropships.size()
				&& i < ((page) * pageSize) && page > 0; i++) {
			dropships2.add(dropships.get(i));
		}
		Page<valueobjects.interaction.Dropship> dPage = new Page<valueobjects.interaction.Dropship>(
				dropships2, total, page, pageSize);

		return ok(views.html.interaction.dropship.dropship_product_list.render(
				dPage, dropshipBase, pageSize, status, sku));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result dropshipPutOff() {
		String email = loginService.getLoginEmail();
		JsonNode json = request().body().asJson();
		Integer id = json.get("id").asInt();
		Boolean status = false;
		Boolean result = dropshipService.setDropShipStatus(id, email, status);
		if (result) {
			return ok("success");
		}
		return ok("falure");
	}

	public Result downloadDropshipProducts() {
		String email = loginService.getLoginEmail();
		Integer languageid = foundationService.getLanguage();
		Integer siteid = foundationService.getSiteID();
		String currency = foundationService.getCurrency();
		List<ProductDropShip> dropShips = dropshipService
				.getDropshipProductDownloadInfoByEmail(email, languageid,
						siteid, currency);
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> title = new ArrayList<Object>();
		title.add("id");
		title.add("sku");
		title.add("title");
		title.add("price(US$)");
		title.add("description");
		title.add("weight");
		title.add("url");
		title.add("picture");
		data.add(title);
		int count = 1;
		for (ProductDropShip cr : dropShips) {
			ArrayList<Object> row = new ArrayList<Object>();
			row.add(count);
			row.add(cr.getSku());
			row.add(cr.getTitle());
			row.add(cr.getPrice());
			row.add(cr.getDescription());
			row.add(cr.getWeight());
			row.add("www.tomtop.com/" + cr.getUrl() + ".html");
			List<String> urls = cr.getPicture();
			StringBuffer sBuffer = new StringBuffer();
			if (null != urls && urls.size() > 0) {
				for (String url : urls) {
					sBuffer.append(url).append(",");
				}
			}
			row.add(sBuffer.toString());
			data.add(row);
			count += 1;
		}
		String filename = "dropship-products-"
				+ DateFormatUtils.getDateTimeYYYYMMDD(new Date()) + ".xlsx";
		Logger.debug(filename);
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}

	@DropShippingAuthenticator
	public Result dropshipWishlist() {
		Integer languageid = foundationService.getLanguage();
		Integer siteid = foundationService.getSiteID();
		String email = loginService.getLoginEmail();
		DropshipBase dropshipBase = dropshipService.getDropshipBaseByEmail(
				email, siteid);
		Integer dropshipCount = dropshipService.getCountDropShipSkuByEmail(
				email, siteid);
		Integer leftCount = dropshipBase.getMaxDropshipNumberLimit()
				- dropshipCount;
		String currency = foundationService.getCurrency();
		Map<String, valueobjects.interaction.DropshipProduct> dropshipProducts = dropshipService
				.getDripshipProductsByEmail(email, languageid, siteid, currency);
		return ok(views.html.interaction.dropship.dropship_wishlist.render(
				dropshipProducts, leftCount));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result batchSetDropshipProduct() {
		String email = loginService.getLoginEmail();
		JsonNode json = request().body().asJson();
		String idsString = json.get("ids").asText();
		if (StringUtils.isNotEmpty(idsString)) {
			String[] idsArray = idsString.split("-");
			List<Integer> ids = Lists.newArrayList();
			for (String idString : idsArray) {
				if (!"".equals(idString)) {
					ids.add(Integer.valueOf(idString));
				}
			}
			try {
				dropshipService.batchSetDropshipProductStatus(email, ids, true);
				return ok("success");
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("batch set dropship product status to true error========="
						+ e);
			}
		}
		return ok("failure");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result deleteDropshipProduct() {
		JsonNode json = request().body().asJson();
		Integer id = json.get("id").asInt();
		boolean result = dropshipService.deleteDropshipProduct(id);
		if (result) {
			return ok("success");
		}
		return ok("failure");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result batchDeleteDropshipProduct() {
		JsonNode json = request().body().asJson();
		String idsString = json.get("ids").asText();
		String[] idsArray = idsString.split("-");
		List<Integer> ids = Lists.newArrayList();
		if (idsArray.length > 0) {
			for (String idString : idsArray) {
				if (!"".equals(idString)) {
					ids.add(Integer.valueOf(idString));
				}
			}
			try {
				dropshipService.batchDeleteDropshipProduct(ids);
				return ok("success");
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("batch delete dropship product error =====" + e);
			}
		}
		return ok("failure");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result batchPutOffDropshipProduct() {
		String email = loginService.getLoginEmail();
		JsonNode json = request().body().asJson();
		String idsString = json.get("ids").asText();
		if (StringUtils.isNotEmpty(idsString)) {
			String[] idsArray = idsString.split("-");
			List<Integer> ids = Lists.newArrayList();
			for (String idString : idsArray) {
				if (!"".equals(idString)) {
					ids.add(Integer.valueOf(idString));
				}
			}
			try {
				dropshipService
						.batchSetDropshipProductStatus(email, ids, false);
				return ok("success");
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error("batch put off dropship status to true error======"
						+ e);
			}
		}
		return ok("failure");
	}

	@BodyParser.Of(BodyParser.Json.class)
	@DropShippingAuthenticator
	public Result addDropshipProduct() {
		String email = loginService.getLoginEmail();
		JsonNode json = request().body().asJson();
		String listingId = json.get("listingid").asText();
		Integer websiteid = foundationService.getSiteID();

		if (null != email && null != listingId && !"".equals(listingId)) {
			boolean result = dropshipService
					.addDropshipProductBylistingidAndEmail(listingId, email,
							websiteid);
			if (result) {
				return ok("success");
			}
		}
		return ok("failure");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result isDropshipProduct() {
		Integer websiteid = foundationService.getSiteID();
		try {
			JsonNode json = request().body().asJson();
			if (json.get("listingid") != null) {
				String listingId = json.get("listingid").asText();
				String email = loginService.getLoginEmail();
				if (null != email) {
					DropshipBase dropShipBase = dropshipService
							.getDropshipBaseByEmail(email, websiteid);
					if (null != dropShipBase) {
						boolean isExsit = dropshipService
								.getDropshipProductByListingAndEmail(email,
										listingId, websiteid);
						if (isExsit) {
							return ok("showAndYes");
						}
						return ok("show");
					}
				}
			}
		} catch (Exception ex) {
			Logger.error("Dropship.isDropshipProduct error", ex);
		}
		return ok("noshow");
	}
}