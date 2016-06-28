package services.product.fragment;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import services.search.KeywordSuggestService;
import dto.search.KeywordSuggest;

public class HotProductFragment implements ITemplateFragmentProvider {

	@Inject
	KeywordSuggestService suggestService;

	@Inject
	FoundationService foundation;

	@Override
	public String getName() {
		return "hot-product";
	}

	@Override
	public Html getFragment(Context context) {
		int siteid = 1;
		int lang = 1;
		if (context != null) {
			siteid = foundation.getSiteID(context);
			lang = foundation.getLanguage(context);
		}
		List<KeywordSuggest> keylist = suggestService.getHotProductSuggestions(
				siteid, lang);
		return views.html.product.hotProduct.render(keylist);
	}

}
