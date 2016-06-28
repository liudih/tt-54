package services.home.fragment;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.base.template.ITemplateFragmentProvider;

public class GadgetDropDownProvider implements ITemplateFragmentProvider {
	
	@Override
	public String getName() {
		return "gadget-dropdown";
	}

	@Override
	public Html getFragment(Context context) {
		return views.html.product.fragment.gadget_dropdown.render();
	}
}
