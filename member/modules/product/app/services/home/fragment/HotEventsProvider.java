package services.home.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.topic.TopicPageService;
import dto.topic.TopicPage;

public class HotEventsProvider implements ITemplateFragmentProvider {
	@Inject
	TopicPageService topicPageService;

	@Inject
	FoundationService foundationService;

	@Override
	public String getName() {
		return "hot-events";
	}

	@Override
	public Html getFragment(Context context) {
		int site = 1;
		int language = 1;
		if (context != null) {
			site = foundationService.getSiteID(context);
			language = foundationService.getLanguage(context);
		}
		List<TopicPage> topicPageList = topicPageService.filterTopicPage(null,
				site, language, null, null, 3);
		return views.html.home.hot_events.render(topicPageList);
	}

}
