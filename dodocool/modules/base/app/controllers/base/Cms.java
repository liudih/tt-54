package controllers.base;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICmsContentService;
import services.ICmsMenuService;
import context.ContextUtils;
import context.WebContext;
import dto.CmsContent;
import dto.CmsMenu;

public class Cms extends Controller {
	@Inject
	ICmsContentService cmsContentService;

	@Inject
	ICmsMenuService cmsMenuService;

	public Result getCmsContentByMenuId(Integer imenuid) {
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		List<CmsMenu> onemenus = cmsMenuService.getOneFootCmsMenu(webContext);
		Map<Integer, List<CmsMenu>> twomenus = cmsMenuService
				.getTwoFootCmsMenuMap(webContext);
		List<CmsContent> cmsContetList = cmsContentService
				.getCmsContentByMenuId(imenuid, webContext);
		return ok(views.html.base.cms_detail.render(onemenus, twomenus,
				cmsContetList));
	}
}
