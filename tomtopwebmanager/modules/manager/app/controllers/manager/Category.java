package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ILanguageService;
import services.attribute.AttributeService;
import services.category.recommend.CategoryProductRecommendService;
import services.manager.CategoryManagerService;
import services.product.CategoryEnquiryService;
import util.AppsUtil;
import valueobjects.product.category.CategoryComposite;
import valueobjects.product.category.CategoryMessage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.InterceptActon;
import dto.AttributeKeyItem;
import dto.SimpleLanguage;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;
import forms.product.category.CategoryMessageForm;

@With(InterceptActon.class)
public class Category extends Controller {
	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	CategoryManagerService categoryManagerService;

	@Inject
	AttributeService attributeService;
	@Inject
	ILanguageService languageService;
	@Inject
	CategoryProductRecommendService categoryProductRecommendService;

	public Result categoryManager() {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		Integer langIdSelected = 1;
		List<CategoryWebsiteWithName> rootCategories = categoryProductRecommendService
				.getRootCategory(1, 1);
		// Collection<dto.Website> websiteChoose = categoryManagerService
		// .getWebsiteChoose();

		return ok(views.html.manager.category.categoryedit.render(languageList,
				langIdSelected, rootCategories));
	}

	public Result getCategoryList(int websiteid, int languageid) {
		List<CategoryComposite> rootCategories = categoryEnquiryService
				.getAllSimpleCategories(languageid, websiteid);
		List<Integer> selected = new ArrayList<Integer>();

		return ok(views.html.manager.category_tree.render(rootCategories,
				selected, ""));
	}

	public Result categoryEdit(Integer categorywebsiteid, Integer languageid,
			Integer websiteId) {
		CategoryMessage categoryMessage = categoryEnquiryService
				.getCategoryMessageByCategoryIdAndLanguage(categorywebsiteid,
						languageid, websiteId);
		CategoryMessageForm categoryMessageForm = new CategoryMessageForm();
		BeanUtils.copyProperties(categoryMessage, categoryMessageForm);
		Form<CategoryMessageForm> categoryMessageupdateForm = Form.form(
				CategoryMessageForm.class).fill(categoryMessageForm);

		return ok(views.html.manager.category.category_message
				.render(categoryMessageupdateForm, websiteId));
	}

	public Result categoryUpdate() {
		Form<CategoryMessageForm> categoryNameupdateForm = Form.form(
				CategoryMessageForm.class).bindFromRequest();
		if (categoryNameupdateForm.hasErrors()) {
			return ok(views.html.manager.category.category_message
					.render(categoryNameupdateForm, 1));
		}
		CategoryMessageForm categoryNameForm = categoryNameupdateForm.get();
		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		boolean categoryUpdate = categoryManagerService.categoryUpdate(
				categoryNameForm, body);
		if (categoryUpdate) {
			return redirect(controllers.manager.routes.Category
					.categoryManager());
		} else {
			return ok(views.html.manager.category.category_message
					.render(categoryNameupdateForm, categoryNameForm.getIwebsiteid()));
		}
	}

	public Result view(Integer iid, String imageType) {
		CategoryName categoryName = categoryEnquiryService
				.getCategoryNameByIid(iid);
		if ("backgroundimages".equals(imageType)) {
			if (categoryName != null
					&& null != categoryName.getCbackgroundimages()) {
				return ok(categoryName.getCbackgroundimages()).as("image/png");
			}
		}

		return badRequest();
	}

	public Result getCategoryAttributeManager(Integer categoryid,
			Integer languageid) {
		Collection<AttributeKeyItem> categoryAttributes = categoryEnquiryService
				.getCategoryAttributesByCategoryIdAndLanguageId(categoryid,
						languageid);

		return ok(views.html.manager.category.category_attribute_manager
				.render(categoryAttributes, categoryid, languageid));
	}

	public Result getAttributeToCategory(Integer categoryid,
			Integer attributekeyid, Integer ilanguageid) {
		AttributeKeyItem attributeKey = attributeService
				.getAttributeKeyByAttributeIdAndLanguageId(attributekeyid,
						ilanguageid);
		List<Integer> categoryAttributeKey = categoryEnquiryService
				.getCategoryAttributeKey(categoryid, ilanguageid,
						attributekeyid);
		Logger.debug(categoryAttributeKey.toArray().toString());
		return ok(views.html.manager.category.category_attribute_edit.render(
				attributeKey, categoryAttributeKey));
	}

	public Result updateCategoryAttribute() {
		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(asJson, Map.class);
		Logger.debug("asJson" + asJson.toString());
		Integer categoryid = Integer.parseInt(result.get("categoryid")
				.toString());
		Integer attributeKeyId = Integer.parseInt(result.get("attributeKeyId")
				.toString());
		String valueid = (String) result.get("valueid");
		boolean updateCategoryAttribute = categoryManagerService
				.updateCategoryAttribute(attributeKeyId, categoryid, valueid);

		return ok(updateCategoryAttribute + "");
	}

	public Result getAllAtributeByLanguageId(Integer languageid) {
		List<AttributeKeyItem> attributeKeys = attributeService
				.getAttributeKeyByLanguageId(languageid);

		return ok(views.html.manager.category.category_attribute_add
				.render(attributeKeys));
	}

	public Result addCategoryRoot() {
		DynamicForm in = Form.form().bindFromRequest();
		try {
			categoryManagerService.addCategory(in);
		} catch (RuntimeException re) {
			return ok(re.toString());
		}
		return redirect(controllers.manager.routes.Category.categoryManager());
	}

	public Result checkCpath(String cname, Integer parentid, Integer parentid2,Integer siteId) {
		Map<Integer, Object> object = new HashMap<Integer, Object>();
		String cpath = AppsUtil.replaceNoEnStr(cname);
		
		boolean flag = categoryManagerService.valiedateCategoryWebsite(cpath, siteId);
		
//		cpath = categoryManagerService.generatePath(cpath, parentid, parentid2);
//		boolean b = categoryManagerService.isPathExist(cpath);
		if (flag) {
			object.put(1, "");
		} else {
			object.put(-1, "cpath is existed");
		}
		return ok(Json.toJson(object));
	}
}
