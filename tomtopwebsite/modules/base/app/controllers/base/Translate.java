package controllers.base;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.MicroTranslate;

import com.google.inject.Inject;

public class Translate extends Controller {

	@Inject
	MicroTranslate microTranslate;

	public Result testTranslate() throws Exception {
		String test = microTranslate
				.translate("Microsoft does not claim ownership of the content you or your customers submitted to the Service. Your content remains your content. Microsoft does not control, verify, or endorse the content that you, your customers or others submit to the Service","en","ru");
		Logger.debug("----------------------------" + test);
		return TODO;
	}

}
