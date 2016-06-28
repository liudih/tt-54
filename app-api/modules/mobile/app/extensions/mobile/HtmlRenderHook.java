package extensions.mobile;

import play.mvc.Http.Context;

public interface HtmlRenderHook {

	void beforeRender(Context current);

	void afterRender(Context current);

}
