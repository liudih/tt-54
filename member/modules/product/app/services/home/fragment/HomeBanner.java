package services.home.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.BannerService;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;

public class HomeBanner implements ITemplateFragmentProvider {

	@Inject
	BannerService bannerService;

	@Override
	public String getName() {
		return "home-banner";
	}

	@Inject
	FoundationService foundationService;

	@Override
	public Html getFragment(Context context) {
		int siteid = 1;
		int lang = 1;
		if (context != null) {
			siteid = foundationService.getSiteID(context);
			lang = foundationService.getLanguage(context);
		}
		List<dto.Banner> list = bannerService.getBanner(lang, siteid, true);
		return views.html.home.banner.render(list);
	}

}
