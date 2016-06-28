package controllers.api;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.receivedata.HandleReceivedDataType;
import services.product.AttributeUpdateService;
import services.product.IProductUpdateService;

import com.fasterxml.jackson.databind.JsonNode;
import com.website.dto.product.MultiProduct;

import controllers.annotation.ApiHistory;
import controllers.annotation.ApiPermission;
import dao.product.IAttributeEnquiryDao;

@ApiPermission
public class Attribute extends Controller {

	@Inject
	IAttributeEnquiryDao attributeEnquityDao;
	@Inject
	AttributeUpdateService attributeUpdateService;
	@Inject
	IProductUpdateService productUpdateService;

	public Result getAll(Integer languageid) {
		return ok(Json.toJson(attributeEnquityDao.getAll(languageid)));
	}

	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_ATTRIBUTE, createuser = "api")
	public Result push() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			com.website.dto.Attribute attr = Json.fromJson(node,
					com.website.dto.Attribute.class);
			String result = attributeUpdateService.Insert(attr, user);
			if (result == null || result.length() == 0) {
				return ok("successfully");
			}
			return internalServerError("failure: " + result);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/**
	 * add product mutil attr
	 * {"sku":"","websiteId":1,"multiAttributes":[{"keyId":null,"key":
	 * "","valueId": null,"value":"","languangeId":1,"showImg":"false"}]}
	 * 
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_MULTI_ATTRIBUTE, createuser = "api")
	public Result addProductMultiAttribute() {
		String re = "";
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}

			if (node.isArray()) {
				MultiProduct[] pitems = Json.fromJson(node,
						MultiProduct[].class);
				for (MultiProduct pitem : pitems) {
					re += productUpdateService.addProductMultiAttribute(
							pitem.getSku(), pitem.getParentSku(),
							pitem.getWebsiteId(), pitem.getMultiAttributes(),
							"");
				}
			} else {
				MultiProduct pitem = Json.fromJson(node, MultiProduct.class);
				re += productUpdateService.addProductMultiAttribute(
						pitem.getSku(), pitem.getParentSku(),
						pitem.getWebsiteId(), pitem.getMultiAttributes(), "");
			}
			if (re.length() == 0) {
				return ok("successfully");
			}
			return internalServerError("failure: " + re);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/**
	 * {"parentSku":"","key":"","languageId":1,"websiteId":1}
	 * 
	 * @return
	 */
	@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_MULTI_ATTRIBUTE, createuser = "api")
	public Result deleteMultiProductAttribute(String parentSku, String key,
			Integer languageId, Integer websiteId) {
		try {
			// String user = request().getHeader("user-token");
			String re = productUpdateService.deleteMultiProductAttribute(
					parentSku, websiteId, key, languageId);
			if (re.length() == 0) {
				return ok("successfully");
			}
			return internalServerError("failure: " + re);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/**
	 * {"listingId":"","key":"","languageId":""}
	 * 
	 * @return
	 */
	@ApiHistory(type = HandleReceivedDataType.DELETE_PRODUCT_ATTRIBUTE, createuser = "api")
	public Result deleteProductAttribute(String listingid, String key,
			Integer languageId) {
		try {
			// String user = request().getHeader("user-token");
			String re = productUpdateService.deleteProductAttribute(listingid,
					key, languageId);
			if (re.length() == 0) {
				return ok("successfully");
			}
			return internalServerError("failure: " + re);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	/**
	 * {"listingId":"","sku":"","websiteId":1,"attributes":[{"keyId":null,"key":
	 * "","valueId": null,"value":"","languangeId":1,"showImg":"false"}]}
	 * 
	 * @return
	 */
	@ApiHistory(type = HandleReceivedDataType.ADD_PRODUCT_ATTRIBUTE, createuser = "api")
	public Result addProductAttribute() {
		try {
			String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			String re = "";
			if (node.isArray()) {
				com.website.dto.product.Product[] pitems = Json.fromJson(node,
						com.website.dto.product.Product[].class);
				for (com.website.dto.product.Product pitem : pitems) {
					re += productUpdateService.addProductAttribute(
							pitem.getSku(), pitem.getWebsiteId(),
							pitem.getListingId(), pitem.getAttributes(), "");
				}
			} else {
				com.website.dto.product.Product pitem = Json.fromJson(node,
						com.website.dto.product.Product.class);
				re += productUpdateService.addProductAttribute(pitem.getSku(),
						pitem.getWebsiteId(), pitem.getListingId(),
						pitem.getAttributes(), "");
			}
			if (re.length() == 0) {
				return ok("successfully");
			}
			return internalServerError("failure: " + re);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

}
