package controllers.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ILanguageService;
import services.advertising.AdvertisingContentService;
import services.advertising.AdvertisingService;
import services.base.WebsiteService;
import services.manager.CategoryManagerService;
import services.product.CategoryEnquiryService;
import valueobjects.base.Page;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.SimpleLanguage;
import dto.advertising.AdvertisingContent;
import dto.advertising.AdvertisingDistribution;
import forms.AdvertisingForm;

@controllers.AdminRole(menuName = "AdvertOneLevelMgr")
public class Advertising extends Controller {

	final static int NOI_ERROR = 0;

	final static int DELETE_ERROR = 1;

	@Inject
	AdvertisingService advertisingService;

	@Inject
	AdvertisingContentService adContentService;

	@Inject
	ILanguageService languageService;

	@Inject
	CategoryManagerService categoryManagerService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	WebsiteService websiteService;

	public Result advertisingList(int page) {

		Page<dto.advertising.Advertising> p = advertisingService
				.getAdvertisingPage(page);
		HashMap<Long, String> map = getImgUrl(p.getList());
		return ok(views.html.manager.advertising.advertisinglist.render(map, p,
				-1, -1, -1, -1));
	}

	public Result editAdvertContent(Long iid) {
		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();

		dto.advertising.Advertising advertising = advertisingService
				.getAdvertising(iid);

		List<dto.advertising.AdvertisingContent> adContentLists = adContentService
				.getAdvertisingContentList(iid);

		Map<Integer, SimpleLanguage> languageMap = Maps.uniqueIndex(
				languageList, new Function<SimpleLanguage, Integer>() {
					public Integer apply(SimpleLanguage lang) {
						return lang.getIid();
					}
				});
		List<dto.advertising.AdvertisingContent> adContentList = Lists
				.transform(adContentLists, ab -> {
					Integer langId = ab.getIlanguageid();
					SimpleLanguage lang = languageMap.get(langId);
					if (lang != null) {
						ab.setLanguagename(lang.getCname());
					}
					return ab;
				});

		if (advertising != null) {
			return ok(views.html.manager.advertising.advertising_edit.render(
					advertising, adContentList));
		}
		return notFound();
	}

	public Result editAdvertRelation(Long iid) {

		Collection<dto.Website> websiteChoose = categoryManagerService
				.getWebsiteChoose();

		dto.advertising.Advertising advertising = advertisingService
				.getAdvertising(iid);

		List<dto.advertising.AdvertisingDistribution> adRelationLists = advertisingService
				.getAdvertRelationList(iid);

		Map<Integer, dto.Website> websiteMap = Maps.uniqueIndex(websiteChoose,
				new Function<dto.Website, Integer>() {
					public Integer apply(dto.Website website) {
						return website.getIid();
					}
				});

		List<dto.advertising.AdvertisingDistribution> adRelationList = Lists
				.transform(adRelationLists, ab -> {
					Integer websiteId = ab.getIwebsiteid();
					dto.Website website = websiteMap.get(websiteId);
					if (website != null) {
						ab.setWebsitename(website.getCcode());
					}

					return ab;
				});

		if (advertising != null) {
			return ok(views.html.manager.advertising.advert_relation_edit
					.render(advertising, adRelationList));
		}
		return notFound();

	}

	// public Result editAdvertisingForm() {
	// Form<AdvertisingForm> form = Form.form(AdvertisingForm.class)
	// .bindFromRequest();
	// if (form.hasErrors()) {
	// return ok(views.html.manager.advertising.error.render());
	// }
	//
	// try {
	// dto.advertising.Advertising advertising = new
	// dto.advertising.Advertising();
	// AdvertisingForm uform = form.get();
	// BeanUtils.copyProperties(uform, advertising);
	//
	// if (advertisingService.updateAdvertising(advertising)) {
	// return redirect(controllers.manager.routes.Advertising
	// .advertisingList(1));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return ok(views.html.manager.advertising.error.render());
	// }

	// /**
	// * 获取广告的关联关系
	// *
	// * @return
	// */
	// public Result editAdvertRelationForm() {
	// Form<AdvertisingForm> form = Form.form(AdvertisingForm.class)
	// .bindFromRequest();
	// if (form.hasErrors()) {
	// return ok(views.html.manager.advertising.error.render());
	// }
	//
	// try {
	// dto.advertising.Advertising advertising = new
	// dto.advertising.Advertising();
	// AdvertisingForm uform = form.get();
	// BeanUtils.copyProperties(uform, advertising);
	//
	// if (advertisingService.updateAdvertising(advertising)) {
	// return redirect(controllers.manager.routes.Advertising
	// .advertisingList(1));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return ok(views.html.manager.advertising.error.render());
	// }

	public Result addAdvertising() {

		Form<AdvertisingForm> form = Form.form(AdvertisingForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		dto.advertising.Advertising advertising = new dto.advertising.Advertising();

		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		AdvertisingForm uform = form.get();

		BeanUtils.copyProperties(uform, advertising);

		boolean advertisingAdd = advertisingService.addAdvertising(advertising,
				body);

		if (advertisingAdd) {
			return redirect(controllers.manager.routes.Advertising
					.advertisingList(1));
		}
		return ok(views.html.manager.advertising.error.render());
	}

	public Result deleteAdvertising(int iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (advertisingService.deleteAdvertising(iid)) {
			resultMap.put("errorCode", NOI_ERROR);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));

	}

	public Result deleteAdvertContent(int iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (advertisingService.deleteAdvertContent(iid)) {
			resultMap.put("errorCode", NOI_ERROR);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));

	}

	public Result deleteAdvertRelation(int iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (advertisingService.deleteAdvertRelation(iid)) {
			resultMap.put("errorCode", NOI_ERROR);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));

	}

	/**
	 * 根据站点与语言查询品类
	 * 
	 * @param websiteid
	 * @param languageid
	 * @return Result - json
	 */
	public Result getCategoryList(int websiteid, int languageid) {
		List<dto.Category> rootCategories = categoryEnquiryService
				.rootCategories(languageid, websiteid);

		return ok(Json.toJson(rootCategories));
	}

	public Result getChildCategoriesByCategoryId(int websiteid, int languageid,
			int categoryId) {
		List<dto.Category> childCategories = categoryEnquiryService
				.getChildCategoriesByCategoryId(languageid, websiteid,
						categoryId);

		return ok(Json.toJson(childCategories));
	}

	/**
	 * 保存广告内容
	 * 
	 * @param iadvertisingid
	 *            - 广告ID
	 * @param ctitle
	 *            - 广告标题
	 * @param ilanguageid
	 *            - 广告语言
	 * @param chrefurl
	 *            - 广告链接
	 * @return Result
	 */
	public Result editAdvertContentForm() {

		Form<AdvertisingForm> form = Form.form(AdvertisingForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		AdvertisingContent content = new AdvertisingContent();

		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		AdvertisingForm uform = form.get();
		BeanUtils.copyProperties(uform, content);
		content = adContentService.addAdvertContent(content, body);
		return ok(Json.toJson(content));
	}

	/**
	 * 保存广告关系
	 * 
	 * @param iadvertisingid
	 *            - 广告ID
	 * @param iwebsiteid
	 *            - 广告站点
	 * @param cbusinessid
	 *            - 业务ID：例如类型为产品，则值为：SKU或者 clistingid，如果类型为品类，则为品类ID
	 * @return Result
	 */
	public Result editAdvertRelationForm() {

		Form<AdvertisingForm> form = Form.form(AdvertisingForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		AdvertisingForm uform = form.get();

		AdvertisingDistribution record = new AdvertisingDistribution();
		BeanUtils.copyProperties(uform, record);

		record = advertisingService.addAdvertRelation(record);

		return ok(Json.toJson(record));
	}

	public Result search() {
		DynamicForm in = Form.form().bindFromRequest();
		String ilanguageidStr = in.get("ilanguageid_search");
		String iwebsiteidStr = in.get("iwebsiteid_relation_search");
		String ipositionStr = in.get("iposition_search");
		String itypeStr = in.get("advert_itype_relation_search");

		Integer ilanguageid = null != ilanguageidStr ? Integer
				.parseInt(ilanguageidStr) : null;
		Integer iwebsiteid = null != iwebsiteidStr ? Integer
				.parseInt(iwebsiteidStr) : null;
		Integer iposition = null != ipositionStr ? Integer
				.parseInt(ipositionStr) : null;
		Integer itype = null != itypeStr ? Integer.parseInt(itypeStr) : null;

		Page<dto.advertising.Advertising> p = advertisingService
				.searchAdvertisingPage(1, ilanguageid, iwebsiteid, iposition,
						itype);
		HashMap<Long, String> map = getImgUrl(p.getList());
		return ok(views.html.manager.advertising.advertisinglist.render(map, p,
				ilanguageid, iwebsiteid, iposition, itype));
	}

	public Result searchAdvertisingPage(int page, Integer ilanguageid,
			Integer iwebsiteid, Integer iposition, Integer itype) {

		Page<dto.advertising.Advertising> p = advertisingService
				.searchAdvertisingPage(page, ilanguageid, iwebsiteid,
						iposition, itype);
		HashMap<Long, String> map = getImgUrl(p.getList());
		return ok(views.html.manager.advertising.advertisinglist.render(map, p,
				ilanguageid, iwebsiteid, iposition, itype));
	}

	public Result getAdvertContentByAdvertIdAndLangId(Integer iadvertisingid,
			Integer ilanguageid) {

		AdvertisingContent content = this.adContentService
				.getAdvertContentByAdvertIdAndLangId(iadvertisingid,
						ilanguageid);
		if (null == content) {
			return ok();
		}
		return ok(Json.toJson(content));
	}

	public Result validateAdvertContent(Integer iadvertisingid,
			Integer ilanguageid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.adContentService.validateAdvertContent(iadvertisingid,
				ilanguageid)) {
			resultMap.put("errorCode", 0);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));

	}

	/**
	 * 获取广告图片的域名地址
	 * 
	 * @param list
	 * @return
	 */
	public HashMap<Long, String> getImgUrl(
			List<dto.advertising.Advertising> list) {
		String rootPath = Play.application().configuration().getConfig("host")
				.getString("link");
		HashMap<Long, String> map = new HashMap<Long, String>();
		if (rootPath != null) {
			for (dto.advertising.Advertising advert : list) {
				String imgurl = advert.getCimageurl();
				if (imgurl != null) {
					int imgCount = imgurl.indexOf("http");
					if (imgCount < 0) {
						imgCount = imgurl.indexOf("advertising");
						if (imgCount >= 0) {
							imgurl = rootPath + "img/" + imgurl;
						} else {
							imgurl = rootPath + imgurl;
						}
					}
				}
				map.put(advert.getIid(), imgurl);
			}
		}
		return map;
	}
}
