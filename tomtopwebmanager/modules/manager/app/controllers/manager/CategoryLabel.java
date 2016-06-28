package controllers.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.ILanguageService;
import services.categorylable.CategoryLabelManagerService;
import services.product.CategoryEnquiryService;
import services.product.CategoryLabelBaseService;
import session.ISessionService;
import valueobjects.product.category.CategoryComposite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import controllers.InterceptActon;
import dto.SimpleLanguage;
import dto.product.CategoryLabelName;
import entity.manager.AdminUser;
import forms.CategoryLabelSearchForm;

@With(InterceptActon.class)
public class CategoryLabel extends Controller {
	@Inject
	CategoryLabelManagerService categoryLabelManagerService;
	@Inject
	CategoryEnquiryService categoryEnquiryService;
	@Inject
	CategoryLabelBaseService categoryLabelBaseService;
	@Inject
	ISessionService sessionService;
	@Inject
	ILanguageService languageService;

	public Result getCategoryLabelList() {
		CategoryLabelSearchForm categoryLabelSearchForm = new CategoryLabelSearchForm();
		categoryLabelSearchForm.setType("hot");
		categoryLabelSearchForm.setSiteId(1);
		return ok(views.html.manager.categorylabel.categorylabel_manager
				.render(getList(categoryLabelSearchForm)));
	}

	public Html getList(CategoryLabelSearchForm categoryLabelSearchForm) {
		return categoryLabelManagerService.getList(categoryLabelSearchForm);
	}

	public Result search() {
		Form<CategoryLabelSearchForm> categoryLabelSearchForm = Form.form(
				CategoryLabelSearchForm.class).bindFromRequest();
		return ok(getList(categoryLabelSearchForm.get()));
	}

	public Result addCategoryLabelChoose(Integer websiteid, String type) {
		List<CategoryComposite> rootCategories = categoryEnquiryService
				.getAllSimpleCategories(1, websiteid);

		List<Integer> selectIds = categoryLabelBaseService
				.getCategoryIdByWebsiteIdAndType(websiteid, type);

		return ok(views.html.manager.category_tree.render(rootCategories,
				selectIds, "chooseDefault"));
	}

	public Result saveCategoryLabel() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}

		JsonNode asJson = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		Map result = mapper.convertValue(asJson, Map.class);

		String type = result.get("type").toString();
		Integer websiteId = Integer
				.parseInt(result.get("websiteId").toString());
		List<Object> categoryIds = (List<Object>) result.get("categoryids");

		List<dto.product.CategoryLabel> categoryLabels = new ArrayList<dto.product.CategoryLabel>();
		for (Object icategoryId : categoryIds) {
			dto.product.CategoryLabel categoryLabel = new dto.product.CategoryLabel();
			categoryLabel.setIcategoryid(Integer.parseInt(icategoryId
					.toString()));
			categoryLabel.setCtype(type);
			categoryLabel.setIwebsiteid(websiteId);
			categoryLabel.setCcreateuser(user.getCusername());

			categoryLabels.add(categoryLabel);
		}
		boolean status = categoryLabelBaseService
				.batchInsertCategoryLabel(categoryLabels);
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		resultMap.put("result", status);

		return ok(Json.toJson(resultMap));
	}

	public Result categoryLabelNameEdit(Integer labelId) {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		LinkedHashMap<SimpleLanguage, CategoryLabelName> data = new LinkedHashMap<SimpleLanguage, CategoryLabelName>();
		for (SimpleLanguage simpleLanguage : languageList) {
			Integer languageId = simpleLanguage.getIid();
			CategoryLabelName categoryLabelName = categoryLabelBaseService
					.getCategoryLabelNameByLabelIdAndLanguageId(labelId,
							languageId);
			if (categoryLabelName == null) {
				categoryLabelName = new CategoryLabelName();
				categoryLabelName.setIcategorylabelid(labelId);
				categoryLabelName.setIlanguageid(languageId);
			}
			data.put(simpleLanguage, categoryLabelName);
		}

		return ok(views.html.manager.categorylabel.categorylabelname_edit
				.render(data));
	}

	public Result categoryLabelNameSave() throws IOException {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		Form<CategoryLabelName> form = Form.form(CategoryLabelName.class)
				.bindFromRequest();
		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		CategoryLabelName categoryLabelName = form.get();
		categoryLabelName.setCcreateuser(user.getCusername());
		categoryLabelManagerService.saveCategoryLabelName(categoryLabelName,
				body);

		return redirect(controllers.manager.routes.CategoryLabel
				.getCategoryLabelList());
	}

	public Result view(Integer iid) {
		CategoryLabelName categoryLabelName = categoryLabelBaseService
				.getCategoryLabelName(iid);

		if (categoryLabelName != null && null != categoryLabelName.getCimages()) {
			return ok(categoryLabelName.getCimages()).as("image/png");
		}

		return badRequest();
	}

	public Result deleteCategoryLabel(Integer labelId) {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		boolean deleteCategoryLabel = categoryLabelBaseService
				.deleteCategoryLabel(labelId);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", deleteCategoryLabel);

		return ok(Json.toJson(result));
	}
}
