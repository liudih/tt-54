package services.dodocool.cms;

import java.util.List;
import java.util.Map;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.ICmsMenuService;
import services.dodocool.base.template.ITemplateFragmentProvider;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.CmsMenu;

public class IndexCmsFragmentProvider implements ITemplateFragmentProvider {

	@Inject
	ICmsMenuService cmsMenuService;

	@Override
	public String getName() {
		return "index-cms";
	}

	@Override
	public Html getFragment(Context context) {
		WebContext webContext = ContextUtils.getWebContext(context.current());
		List<CmsMenu> onemenus = cmsMenuService.getOneFootCmsMenu(webContext);
		Map<Integer, List<CmsMenu>> twomenus = cmsMenuService
				.getTwoFootCmsMenuMap(webContext);
		return views.html.base.index_cms.render(onemenus, twomenus);
	}

}
