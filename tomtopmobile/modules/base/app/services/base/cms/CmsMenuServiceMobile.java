package services.base.cms;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.ICmsMenuService;
import context.WebContext;
import extensions.InjectorInstance;

public class CmsMenuServiceMobile {

	@Inject
	ICmsMenuService cmsService;

	public static CmsMenuServiceMobile getInstance() {
		return InjectorInstance.getInjector().getInstance(
				CmsMenuServiceMobile.class);
	}

	public List<dto.CmsMenu> getOneFootCmsMenu(WebContext ctx) {

		return cmsService.getOneFootCmsMenu(ctx);
	}

	public Map<Integer, List<dto.CmsMenu>> getTwoFootCmsMenuMap(WebContext ctx) {

		return cmsService.getTwoFootCmsMenuMap(ctx);
	}

}
