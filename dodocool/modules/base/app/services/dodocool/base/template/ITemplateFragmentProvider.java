package services.dodocool.base.template;

import play.mvc.Http.Context;
import play.twirl.api.Html;

public interface ITemplateFragmentProvider {

	String getName();

	Html getFragment(Context context);
}
