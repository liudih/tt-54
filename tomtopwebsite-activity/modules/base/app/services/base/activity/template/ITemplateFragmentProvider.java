package services.base.activity.template;

import play.mvc.Http.Context;
import play.twirl.api.Html;

/**
 * Provide basic information to the base template used to render HTML pages
 * 
 * @author kmtong
 *
 */
public interface ITemplateFragmentProvider {

	/**
	 * A name to identify the fragment / region.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Process rendering of this fragment.
	 * @param context TODO
	 * 
	 * @return
	 */
	Html getFragment(Context context);
}
