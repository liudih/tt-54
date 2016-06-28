package controllers.api;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.website.dto.category.WebsiteCategory;

import controllers.annotation.ApiPermission;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.product.CategoryEnquiryService;
import services.product.CategoryUpdateService;
import valueobjects.product.category.CategoryItem;

@ApiPermission
public class Category extends Controller {

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CategoryUpdateService categoryUpdateService;

	@BodyParser.Of(BodyParser.Json.class)
	public Result push() {
		try {
			// String user = request().getHeader("user-token");
			Logger.debug("----" + request().body().asJson());
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");
			}
			this.saveOrUpdateCategoryBase(node);
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	public void saveOrUpdateCategoryBase(JsonNode node) {
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				CategoryItem cbase = Json.fromJson(nodeiterator.next(),
						CategoryItem.class);
				categoryUpdateService.saveOrUpdateCategoryBase(cbase);
			}
		} else {
			CategoryItem cbase = Json.fromJson(node, CategoryItem.class);
			categoryUpdateService.saveOrUpdateCategoryBase(cbase);
		}
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result platformPush() {
		try {
			// String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return badRequest("Expecting Json data");
			}
			this.saveOrUpdatePlatformCategory(node);
			return ok("successfully");
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	public void saveOrUpdatePlatformCategory(JsonNode node) {
		if (node.isArray()) {
			Iterator<JsonNode> nodeiterator = node.iterator();
			while (nodeiterator.hasNext()) {
				WebsiteCategory cbase = Json.fromJson(nodeiterator.next(),
						WebsiteCategory.class);
				categoryUpdateService.saveOrUpdatePlatformCategory(cbase);
			}
		} else {
			WebsiteCategory cbase = Json.fromJson(node, WebsiteCategory.class);
			categoryUpdateService.saveOrUpdatePlatformCategory(cbase);
		}
	}

	public Result get(Integer websiteid, Integer languageid) {
		try {
			// String user = request().getHeader("user-token");
			List<WebsiteCategory> list = categoryEnquiryService
					.getAllCategories(websiteid, languageid);
			return ok(Json.toJson(list));
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}

	public Result getAllCategoryAttributes() {
		return ok(Json
				.toJson(categoryEnquiryService.getAllCategoryAttributes()));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Result saveCategoryAttributes() {
		try {
			// String user = request().getHeader("user-token");
			JsonNode node = request().body().asJson();
			if (node == null) {
				return internalServerError("Expecting Json data");
			}
			String result = "";
			if (node.isArray()) {
				Iterator<JsonNode> nodes = node.iterator();
				while (nodes.hasNext()) {
					JsonNode no = nodes.next();
					com.website.dto.Attribute[] attrs = Json.fromJson(
							no.get("attributes"),
							com.website.dto.Attribute[].class);
					result = categoryUpdateService.saveCategoryAttribute(no
							.get("categoryPath").asText(),
							Arrays.asList(attrs), "");
				}
			} else {
				com.website.dto.Attribute[] attrs = Json.fromJson(
						node.get("attributes"),
						com.website.dto.Attribute[].class);
				result = categoryUpdateService.saveCategoryAttribute(
						node.get("categoryPath").asText(),
						Arrays.asList(attrs), "");
			}
			if (result.length() == 0)
				return ok("successfully");
			else
				return internalServerError(result);
		} catch (Exception p) {
			return internalServerError(p.getMessage());
		}
	}
}
