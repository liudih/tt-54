package controllers.manager;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import services.ILanguageService;
import services.base.WebsiteService;
import services.manager.AdminUserWebsiteMapService;
import services.topic.TopicPageService;
import session.ISessionService;
import valueobjects.base.Page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.SimpleLanguage;
import dto.Website;
import entity.manager.AdminUser;
import dto.topic.TopicPage;

public class Collections extends Controller {
	@Inject
	TopicPageService pageService;
	@Inject
	AdminUserWebsiteMapService userWebsiteMapService;
	@Inject
	ISessionService sessionService;
	@Inject
	WebsiteService websiteService;
	@Inject
	ILanguageService languageService;

	public Result manager(int page) {
		int pageSize = 16;
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		Integer total = pageService.count();
		List<Website> websites = websiteService.getAll();
		List<Integer> siteIds = null;
		if (user.isBadmin()) {
			siteIds = Lists.transform(websites, w -> w.getIid());
		} else {
			siteIds = userWebsiteMapService.getAdminUserWebsitMapsByUserId(user
					.getIid());
		}
		Map<Integer, Website> websiteMap = Maps.uniqueIndex(websites,
				w -> w.getIid());
		List<SimpleLanguage> languages = languageService
				.getAllSimpleLanguages();
		Map<Integer, SimpleLanguage> languageMap = Maps.uniqueIndex(languages,
				l -> l.getIid());
		List<TopicPage> list = pageService.getAll(siteIds, page, pageSize);
		return ok(views.html.manager.collections.manager.render(
				new Page<TopicPage>(list, total, page, pageSize), websiteMap,
				languageMap));
	}

	public Result edit(Integer id) {
		TopicPage page = pageService.getTopicPageById(id);
		return ok(views.html.manager.collections.edit.render(page));
	}

	public Result save() {
		Form<TopicPage> form = Form.form(TopicPage.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		TopicPage page = form.get();
		MultipartFormData body = request().body().asMultipartFormData();
		page.setIcreateuserid(user.getIid());
		boolean isSuccess = pageService.saveOrUpdate(page, body);
		if (isSuccess) {
			return redirect(controllers.manager.routes.Collections.manager(1));
		}
		return badRequest();
	}

	public Result delete(Integer id) {
		boolean isSuccess = pageService.deleteTopicPageById(id);
		if (isSuccess) {
			return redirect(controllers.manager.routes.Collections.manager(1));
		}
		return badRequest();
	}
}
