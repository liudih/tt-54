package controllers.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.ILanguageService;
import services.base.utils.FileUtils;
import services.category.recommend.CategoryProductRecommendService;
import services.label.IRecommendLabelService;
import session.ISessionService;

import com.google.common.eventbus.EventBus;

import controllers.InterceptActon;
import dto.KeyValue;
import dto.SimpleLanguage;
import dto.label.RecommendLabelAndName;
import dto.label.RecommendLabelBase;
import dto.label.RecommendLabelName;
import dto.product.CategoryWebsiteWithName;
import entity.manager.AdminUser;
import forms.RecommendLabelSearchForm;

@With(InterceptActon.class)
public class RecommendLabel extends Controller {
	@Inject
	CategoryProductRecommendService categoryProductRecommendService;
	@Inject
	IRecommendLabelService recommendLabelService;
	@Inject
	ILanguageService languageService;
	@Inject
	ISessionService sessionService;
	@Inject
	EventBus eventBus;

	/**
	 * 品类推荐产品设置管理
	 * 
	 * 
	 */
	public Result recommendLabelManager(Integer websiteid, String device) {
		RecommendLabelSearchForm recommendLabelSearchForm = new RecommendLabelSearchForm();
		recommendLabelSearchForm.setSiteId(websiteid);
		recommendLabelSearchForm.setCdevice(device);
		recommendLabelSearchForm.setPageNum(1);
		recommendLabelSearchForm.setPageSize(10);
		return ok(views.html.manager.recommendlabel.recommendlabel_manager
				.render(getList(recommendLabelSearchForm)));
	}

	public Html getList(RecommendLabelSearchForm recommendLabelSearchForm) {
		Integer langid = 1;
		Integer siteId = recommendLabelSearchForm.getSiteId();
		String cdevice = recommendLabelSearchForm.getCdevice();
		Integer pageSize = recommendLabelSearchForm.getPageSize();
		Integer pageNum = recommendLabelSearchForm.getPageNum();
		boolean bshow = true;

		List<RecommendLabelAndName> recommendlabels = recommendLabelService
				.getRecommendLabelAndName(langid, siteId, cdevice, bshow,
						pageSize, pageNum);
		if (recommendlabels == null || recommendlabels.size() == 0) {
			return views.html.manager.recommendlabel.recommendlabel_table_list
					.render(new ArrayList<RecommendLabelAndName>(),
							recommendLabelSearchForm.getSiteId(),
							recommendLabelSearchForm.getCdevice(), 0,
							recommendLabelSearchForm.getPageNum(), 0);
		} else {
			Integer count = recommendLabelService
					.getRecommendLabelAndNameCount(langid, siteId, cdevice,
							bshow);
			Integer pageTotal = count
					/ recommendLabelSearchForm.getPageSize()
					+ ((count % recommendLabelSearchForm.getPageSize() > 0) ? 1
							: 0);

			return views.html.manager.recommendlabel.recommendlabel_table_list
					.render(recommendlabels,
							recommendLabelSearchForm.getSiteId(),
							recommendLabelSearchForm.getCdevice(), count,
							recommendLabelSearchForm.getPageNum(), pageTotal);
		}
	}

	public Result search() {
		Form<RecommendLabelSearchForm> recommendLabelSearchForm = Form.form(
				RecommendLabelSearchForm.class).bindFromRequest();
		return ok(getList(recommendLabelSearchForm.get()));
	}

	public Result RecommendLabelNameSaveOrEdit(Integer labelId,
			Integer websiteid, String cdevice) {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		LinkedHashMap<SimpleLanguage, RecommendLabelAndName> data = new LinkedHashMap<SimpleLanguage, RecommendLabelAndName>();
		Integer languageId = 1;
		if (websiteid == null) {
			websiteid = 1;
		}
		List<CategoryWebsiteWithName> rootCategories = categoryProductRecommendService
				.getRootCategory(languageId, websiteid);
		if (labelId == 0) {

			for (SimpleLanguage simpleLanguage : languageList) {
				data.put(simpleLanguage, new RecommendLabelAndName());
			}

			return ok(views.html.manager.recommendlabel.recommendlabelname_add
					.render(data, rootCategories));
		} else {
			for (SimpleLanguage simpleLanguage : languageList) {
				languageId = simpleLanguage.getIid();
				RecommendLabelAndName recommendLabelAndName = recommendLabelService
						.getRecommendLabelAndNameByLabelIdAndLanguageId(
								labelId, languageId, websiteid, cdevice);
				if (recommendLabelAndName == null) {
					recommendLabelAndName = new RecommendLabelAndName();
					recommendLabelAndName.setIid(labelId);
					recommendLabelAndName.setIlanguageid(languageId);
					recommendLabelAndName.setIwebsiteid(websiteid);
					recommendLabelAndName.setCdevice(cdevice);
				}
				data.put(simpleLanguage, recommendLabelAndName);
			}
			return ok(views.html.manager.recommendlabel.recommendlabelname_edit
					.render(data, labelId, websiteid, cdevice));
		}
	}

	public Result RecommendLabelNameSave() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		if (null == user) {
			return badRequest();
		}
		DynamicForm in = Form.form().bindFromRequest();
		String siteId = in.get("siteId");
		Integer websiteid = Integer.parseInt(siteId);
		String cdevice = in.get("cdevice");
		String category1 = in.get("category1");
		String category2 = in.get("category2");
		String category3 = in.get("category3");
		Integer iid = recommendLabelService.getRecommendLabelMaxIid();
		if (iid == null) {
			iid = 1;
		} else {
			iid++;
		}
		KeyValue kv = getCategoryKeyValue(category1, category2, category3);

		RecommendLabelBase recommendLabel = new RecommendLabelBase();
		recommendLabel.setIid(iid);
		recommendLabel.setIwebsiteid(websiteid);
		recommendLabel.setCdevice(cdevice);
		recommendLabel.setItype(1);
		recommendLabel.setBshow(true);
		recommendLabel.setCcreateuser(user.getCusername());
		recommendLabel.setIcategoryid(kv.getKey());
		int rec = recommendLabelService.addRecommendLabel(recommendLabel);
		if (rec > 0) {
			List<SimpleLanguage> languageList = languageService
					.getAllSimpleLanguages();
			RecommendLabelName recommendLabelname = null;
			Integer languageId = 1;
			MultipartFormData body = request().body().asMultipartFormData();

			for (SimpleLanguage simpleLanguage : languageList) {
				languageId = simpleLanguage.getIid();
				recommendLabelname = new RecommendLabelName();
				recommendLabelname.setClabelname(in.get("clabelname"
						+ languageId));
				recommendLabelname.setIlanguageid(languageId);
				recommendLabelname.setCvalue(kv.getValue());
				if (null != body) {
					FilePart file = body.getFile("image" + languageId);
					if (null != file) {
						String contentType = file.getContentType();
						if (contentType.startsWith("image/")) {
							byte[] buff = FileUtils.toByteArray(file.getFile());
							recommendLabelname.setCimages(buff);
							String url = controllers.interaction.routes.LabelImage
									.view(iid, languageId).url();
							recommendLabelname.setCimageurl(url);
						}
					}
				}
				recommendLabelname.setIlabelid(iid);
				recommendLabelService.addRecommendLabelName(recommendLabelname);
			}
		}
		return redirect(controllers.manager.routes.RecommendLabel
				.recommendLabelManager(websiteid, cdevice));
	}

	public Result RecommendLabelNameUpdate() {
		DynamicForm in = Form.form().bindFromRequest();
		String labelid = in.get("labelid");
		String siteId = in.get("siteId");
		String cdevice = in.get("cdevice");
		Integer iid = Integer.parseInt(labelid);
		Integer websiteid = Integer.parseInt(siteId);
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		RecommendLabelName recommendLabelname = null;
		Integer languageId = 1;
		MultipartFormData body = request().body().asMultipartFormData();

		for (SimpleLanguage simpleLanguage : languageList) {
			languageId = simpleLanguage.getIid();
			recommendLabelname = new RecommendLabelName();
			recommendLabelname.setIlabelid(iid);
			recommendLabelname.setClabelname(in.get("clabelname" + languageId));
			recommendLabelname.setIlanguageid(languageId);
			if (null != body) {
				FilePart file = body.getFile("image" + languageId);
				if (null != file) {
					String contentType = file.getContentType();
					if (contentType.startsWith("image/")) {
						byte[] buff = FileUtils.toByteArray(file.getFile());
						recommendLabelname.setCimages(buff);
						String url = controllers.interaction.routes.LabelImage
								.view(iid, languageId).url();
						recommendLabelname.setCimageurl(url);
					}
				}
			}
			recommendLabelService.updateRecommendLabelName(recommendLabelname);
		}

		return redirect(controllers.manager.routes.RecommendLabel
				.recommendLabelManager(websiteid, cdevice));
	}

	private KeyValue getCategoryKeyValue(String category1, String category2,
			String category3) {
		KeyValue kv = new KeyValue();
		Integer categoryid = 0;
		String categoryName = "";
		String[] cs1 = null;
		String[] cs2 = null;
		String[] cs3 = null;
		if (!"".equals(category1)) {
			cs1 = category1.split("\\,");
			categoryid = Integer.parseInt(cs1[0]);
			categoryName += cs1[1];
		}
		if (category2 != null && !"".equals(category2)) {
			cs2 = category2.split("\\,");
			categoryid = Integer.parseInt(cs2[0]);
			categoryName += "/" + cs2[1];
		}
		if (category3 != null && !"".equals(category3)) {
			cs3 = category3.split("\\,");
			categoryid = Integer.parseInt(cs3[0]);
			categoryName += "/" + cs3[1];
		}
		kv.setKey(categoryid);
		kv.setValue(categoryName);
		return kv;
	}

	public Result deleteRecommendLabel(Integer labelId) {
		Integer rec = recommendLabelService.deleteRecommendLabelById(labelId);
		boolean boo = false;
		if (rec > 0) {
			boo = true;
		}
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", boo);

		return ok(Json.toJson(result));
	}

}
