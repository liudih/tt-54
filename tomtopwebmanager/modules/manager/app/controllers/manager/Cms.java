package controllers.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import services.ICmsMenuService;
import services.ILanguageService;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.base.cms.CmsContentService;
import valueobjects.base.Page;
import cms.model.CmsType;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.ContextUtils;
import dto.SimpleLanguage;
import dto.CmsContent;
import dto.CmsContentLanguage;
import dto.CmsMenu;
import dto.CmsMenuComposite;
import dto.CmsMenuLanguage;
import dto.CmsMenuWebsite;
import dto.Website;
import forms.CmsContentForm;
import forms.CmsMenuForm;

@controllers.AdminRole(menuName = "CmsMgr")
public class Cms extends Controller {

	@Inject
	CmsContentService cmsContentService;

	@Inject
	ICmsMenuService cmsMenuService;

	@Inject
	FoundationService foundation;

	@Inject
	ILanguageService languageService;
	
	@Inject
	WebsiteService websiteService;

	public Result list(Integer page) {

		Page<CmsContent> cmsContentList = this.cmsContentService
				.getCmsContentPage(page);

		return ok(views.html.manager.cms.cmslist.render(cmsContentList, null));
	}

	public Result addCmsContent() {
		Form<CmsContentForm> form = Form.form(CmsContentForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {

			return ok(views.html.manager.advertising.error.render());
		}
		CmsContent cmsContent = new CmsContent();

		CmsContentForm uform = form.get();

		BeanUtils.copyProperties(uform, cmsContent);

		boolean cmsContentAdd = cmsContentService.addCmsContent(cmsContent);

		CmsContentLanguage ccLanguage = new CmsContentLanguage();
		ccLanguage.setCcontent(cmsContent.getCcontent());
		ccLanguage.setCkey(cmsContent.getCkey());
		ccLanguage.setCtitle(cmsContent.getCtitle());
		ccLanguage.setIcmscontentid(cmsContent.getIid());
		ccLanguage.setIlanguageid(foundation.getLanguage());

		this.cmsContentService.addCmsContentMoreLanguage(ccLanguage);

		if (cmsContentAdd) {
			return redirect(controllers.manager.routes.Cms.list(1));
		}
		return ok(views.html.manager.advertising.error.render());
	}

	public Result getCmsMenuByLevelId(Integer level, Integer iparentid, Integer siteId) {
		List<CmsMenu> cmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndSiteId(level, iparentid, siteId);

		return ok(Json.toJson(cmsMenuList));
	}

	public Result deleteCmsContent(int iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsContentService.deleteCmsContent(iid)) {
			resultMap.put("errorCode", 0);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);

		return ok(Json.toJson(resultMap));

	}

	public Result deleteCmsMenu(int iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsMenuService.deleteCmsMenu(iid)) {
			resultMap.put("errorCode", 0);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);

		return ok(Json.toJson(resultMap));

	}

	public Result getCmsMenuList() {
		List<CmsMenuComposite> rootCmsMenus = cmsMenuService
				.getCmsMenuCompositeList(ContextUtils.getWebContext(Context.current()));

		return ok(views.html.manager.cms.cms_menu_tree.render(rootCmsMenus));
	}

	public Result cmsMenuList() {
		List<CmsMenuComposite> rootCmsMenus = cmsMenuService
				.getCmsMenuCompositeList(ContextUtils.getWebContext(Context.current()));

		return ok(views.html.manager.cms.cms_menulist.render(rootCmsMenus));
	}

	public Result addCmsMenu() {
		Form<CmsMenuForm> form = Form.form(CmsMenuForm.class).bindFromRequest();

		CmsMenu cmsMenu = new CmsMenu();
		CmsMenuForm uform = form.get();
		BeanUtils.copyProperties(uform, cmsMenu);

		cmsMenuService.addCmsMenu(cmsMenu);

		CmsMenuLanguage ccLanguage = new CmsMenuLanguage();
		ccLanguage.setCmenuname(cmsMenu.getCname());
		ccLanguage.setIlanguageid(foundation.getLanguage());
		ccLanguage.setImenuid(cmsMenu.getIid());
		this.cmsMenuService.addCmsMenuMoreLanguage(ccLanguage);

		List<CmsMenuComposite> rootCmsMenus = cmsMenuService
				.getCmsMenuCompositeList(ContextUtils.getWebContext(Context.current()));

		return ok(views.html.manager.cms.cms_menulist.render(rootCmsMenus));
	}

	public Result addChildrenMenu() {
		Form<CmsMenuForm> form = Form.form(CmsMenuForm.class).bindFromRequest();

		CmsMenu cmsMenu = new CmsMenu();
		CmsMenuForm uform = form.get();
		BeanUtils.copyProperties(uform, cmsMenu);

		cmsMenu.setIlevel(cmsMenu.getIlevel() + 1);
		cmsMenuService.addCmsMenu(cmsMenu);

		CmsMenuLanguage ccLanguage = new CmsMenuLanguage();
		ccLanguage.setCmenuname(cmsMenu.getCname());
		ccLanguage.setIlanguageid(foundation.getLanguage());
		ccLanguage.setImenuid(cmsMenu.getIid());
		this.cmsMenuService.addCmsMenuMoreLanguage(ccLanguage);

		List<CmsMenuComposite> rootCmsMenus = cmsMenuService
				.getCmsMenuCompositeList(ContextUtils.getWebContext(Context.current()));

		return ok(views.html.manager.cms.cms_menulist.render(rootCmsMenus));
	}

	public Result updateCmsMenu() {
		Form<CmsMenuForm> form = Form.form(CmsMenuForm.class).bindFromRequest();

		CmsMenu cmsMenu = new CmsMenu();
		CmsMenuForm uform = form.get();
		BeanUtils.copyProperties(uform, cmsMenu);

		cmsMenuService.updateCmsMenu(cmsMenu);

		List<CmsMenuComposite> rootCmsMenus = cmsMenuService
				.getCmsMenuCompositeList(ContextUtils.getWebContext(Context.current()));

		return ok(views.html.manager.cms.cms_menulist.render(rootCmsMenus));
	}

	public Result editCmsChildrenMenu(Integer iparentid) {
		CmsMenu cmsMenu = this.cmsMenuService.selectByPrimaryKey(iparentid);
		return ok(views.html.manager.cms.cms_children_edit.render(cmsMenu));
	}

	public Result editMessageMenu(Integer iid) {
		CmsMenu cmsMenu = this.cmsMenuService.selectByPrimaryKey(iid);
		return ok(views.html.manager.cms.cms_message_edit.render(cmsMenu));
	}

	/**
	 * 编辑CMS内容
	 * 
	 * @param iid
	 * @return
	 */
	public Result editCmsContentForm(Integer iid, Integer page) {
		CmsContent cmsContent = this.cmsContentService.getCmsContent(iid,
				foundation.getLanguage());

		CmsMenu cmsMenu = this.cmsMenuService.getCmsMenu(cmsContent
				.getImenuid());

		CmsMenu parentMenu = this.cmsMenuService.getCmsMenu(cmsMenu
				.getIparentid());

		List<CmsMenu> oneCmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndType(1, 0, CmsType.HELPCENTER.getType(),
						ContextUtils.getWebContext(Context.current()));

		List<CmsMenu> twoCmsMenuList = this.cmsMenuService
				.getCmsMenuByLevelIdAndType(2, parentMenu.getIid(),
						CmsType.HELPCENTER.getType(),
						ContextUtils.getWebContext(Context.current()));

		Collection<dto.CmsMenu> oneMenuList = Collections2.transform(
				oneCmsMenuList, new Function<CmsMenu, dto.CmsMenu>() {
					@Override
					public dto.CmsMenu apply(CmsMenu cm) {
						return new dto.CmsMenu(cm.getIid(), cm.getCname());
					}
				});

		Collection<dto.CmsMenu> twoMenuList = Collections2.transform(
				twoCmsMenuList, new Function<CmsMenu, dto.CmsMenu>() {
					@Override
					public dto.CmsMenu apply(CmsMenu cm) {
						return new dto.CmsMenu(cm.getIid(), cm.getCname());
					}
				});

		return ok(views.html.manager.cms.cms_content_edit.render(cmsContent,
				parentMenu, oneMenuList, twoMenuList, page));
	}

	public Result updateCmsContent() {
		Form<CmsContentForm> form = Form.form(CmsContentForm.class)
				.bindFromRequest();

		CmsContent cmsContent = new CmsContent();
		CmsContentForm uform = form.get();
		BeanUtils.copyProperties(uform, cmsContent);

		this.cmsContentService.updateCmsContent(cmsContent);

		return this.list(uform.getPage());
	}

	public Result search() {
		DynamicForm in = Form.form().bindFromRequest();
		String ckey = in.get("ckey");

		Page<CmsContent> cmsContentList = this.cmsContentService
				.searchCmsContentPage(1, ckey);

		return ok(views.html.manager.cms.cmslist.render(cmsContentList, ckey));
	}

	public Result editCmsMenuLanguage(Integer iid) {

		CmsMenu cmsMenu = this.cmsMenuService.getCmsMenu(iid);
		List<CmsMenuLanguage> cmsMenuList = this.cmsMenuService
				.getAllLangMenuByMenuId(iid);

		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		Map<Integer, SimpleLanguage> languageMap = Maps.uniqueIndex(
				languageList, new Function<SimpleLanguage, Integer>() {
					public Integer apply(SimpleLanguage lang) {
						return lang.getIid();
					}
				});
		List<CmsMenuLanguage> cmsLangList = Lists.transform(cmsMenuList,
				menu -> {
					Integer langId = menu.getIlanguageid();
					SimpleLanguage lang = languageMap.get(langId);
					menu.setLanguagename(lang.getCname());

					return menu;
				});

		return ok(views.html.manager.cms.cmsmenu_language_edit.render(cmsMenu,
				cmsLangList));
	}

	public Result addCmsMenuMoreLanguage(Integer imenuid, String cname,
			Integer ilanguageid) {

		CmsMenuLanguage cmsMenuLanguage = new CmsMenuLanguage();
		cmsMenuLanguage.setImenuid(imenuid);
		cmsMenuLanguage.setIlanguageid(ilanguageid);
		cmsMenuLanguage.setCmenuname(cname);
		cmsMenuLanguage = this.cmsMenuService
				.addCmsMenuMoreLanguage(cmsMenuLanguage);

		return ok(Json.toJson(cmsMenuLanguage));
	}

	public Result deleteCmsMenuMoreLanguage(Integer iid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsMenuService.deleteCmsMenuMoreLanguage(iid)) {
			resultMap.put("errorCode", 0);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result validateCmsMenuMoreLanguage(Integer imenuid,
			Integer ilanguageid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsMenuService.validateCmsMenuMoreLanguage(imenuid,
				ilanguageid)) {
			resultMap.put("errorCode", 0);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}


	public Result editCmsMenuWebsite(Integer iid) {

		CmsMenu cmsMenu = this.cmsMenuService.getCmsMenu(iid);
		List<CmsMenuWebsite> cmsMenuList = this.cmsMenuService
				.getAllMenuWebsiteByMenuId(iid);

		List<dto.Website> websiteList = websiteService.getAll();

		Map<Integer, dto.Website> websiteMap = Maps.uniqueIndex(websiteList,
				new Function<dto.Website, Integer>() {
					public Integer apply(dto.Website website) {
						return website.getIid();
					}
				});

		List<CmsMenuWebsite> menuList = Lists
				.transform(cmsMenuList, ab -> {
					Integer websiteId = ab.getIwebsiteid();
					dto.Website website = websiteMap.get(websiteId);
					if(website != null){
						ab.setWebsiteName(website.getCcode());
					}

					return ab;
				});
		
		return ok(views.html.manager.cms.cmsmenu_website_edit.render(cmsMenu,
				menuList));
	}

	public Result addCmsMenuMoreWebsite(Integer imenuid, Integer iwebsiteid, String cdevice) {

		CmsMenuWebsite cmsMenuWebsite = new CmsMenuWebsite();
		cmsMenuWebsite.setImenuid(imenuid);
		cmsMenuWebsite.setIwebsiteid(iwebsiteid);
		cmsMenuWebsite.setCdevice(cdevice);
		
		cmsMenuWebsite = this.cmsMenuService
				.addCmsMenuMoreWebsite(cmsMenuWebsite);

		return ok(Json.toJson(cmsMenuWebsite));
	}

	public Result deleteCmsMenuMoreWebsite(Integer iid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsMenuService.deleteCmsMenuMoreWebsite(iid)) {
			resultMap.put("errorCode", 0);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result validateCmsMenuMoreWebsite(Integer imenuid, Integer iwebsiteid, String cdevice) {

		Map<String, Object> resultMap = Maps.newHashMap();
		if (this.cmsMenuService.validateCmsMenuMoreWebsite(imenuid, iwebsiteid, cdevice)) {
			resultMap.put("errorCode", 0);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result editCmsContentLanguage(Integer iid) {

		CmsContent cmsContent = this.cmsContentService.getCmsContent(iid,
				foundation.getLanguage());

		// 小语种如果没有数据，默认为英文
		if (null == cmsContent) {
			cmsContent = this.cmsContentService.getCmsContent(iid,
					foundation.getDefaultLanguage());
		}
		List<CmsContentLanguage> cmsContentList = this.cmsContentService
				.getAllLangContentByMenuId(iid);

		List<SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();

		Map<Integer, SimpleLanguage> languageMap = Maps.uniqueIndex(
				languageList, new Function<SimpleLanguage, Integer>() {
					public Integer apply(SimpleLanguage lang) {
						return lang.getIid();
					}
				});
		List<CmsContentLanguage> contentLangList = Lists.transform(
				cmsContentList, content -> {
					Integer langId = content.getIlanguageid();
					SimpleLanguage lang = languageMap.get(langId);
					content.setLanguagename(lang.getCname());

					return content;
				});

		return ok(views.html.manager.cms.cmscontent_language_edit.render(
				cmsContent, contentLangList));
	}

	public Result addCmsContentMoreLanguage() {
		DynamicForm df = Form.form().bindFromRequest();
		String icmscontentid = df.get("icmscontentid_more_language");
		String ilanguageid = df.get("ilanguageid_more_language");
		String ckey = df.get("ckey_more_language");
		String ctitle = df.get("ctitle_more_language");
		String ccontent = df.get("ccontent_more_language");

		CmsContentLanguage cmsContentLanguage = new CmsContentLanguage();
		cmsContentLanguage.setIcmscontentid(Integer.parseInt(icmscontentid));
		cmsContentLanguage.setIlanguageid(Integer.parseInt(ilanguageid));
		cmsContentLanguage.setCkey(ckey);
		cmsContentLanguage.setCtitle(ctitle);
		cmsContentLanguage.setCcontent(ccontent);

		cmsContentLanguage = this.cmsContentService
				.addCmsContentMoreLanguage(cmsContentLanguage);

		cmsContentLanguage.setCcontent(null);
		return ok(Json.toJson(cmsContentLanguage));
	}

	public Result deleteCmsContentMoreLanguage(Integer iid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsContentService.deleteCmsContentMoreLanguage(iid)) {
			resultMap.put("errorCode", 0);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result validateCmsContentMoreLanguage(Integer icmscontentid,
			Integer ilanguageid) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (this.cmsContentService.validateCmsContentMoreLanguage(
				icmscontentid, ilanguageid)) {
			resultMap.put("errorCode", 0);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", 1);
		return ok(Json.toJson(resultMap));
	}

	public Result getCmsContentByLangIdAndContentId(Integer icmscontentid,
			Integer ilanguageid) {

		CmsContentLanguage cmsContentLanguage = this.cmsContentService
				.getCmsContentByLangIdAndContentId(icmscontentid, ilanguageid);
		if (null == cmsContentLanguage) {
			return ok();
		}
		return ok(Json.toJson(cmsContentLanguage));
	}

}
