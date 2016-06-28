package controllers.interaction;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import services.label.IRecommendLabelService;
import dto.label.RecommendLabelName;

public class LabelImage extends Controller {

	@Inject
	IRecommendLabelService recommendLabelService;

	public Result view(Integer iid, Integer langid) {
		RecommendLabelName recommendLabelName = recommendLabelService
				.getRecommendLabelNameByIdAndLangId(iid, langid);

		if (recommendLabelName != null
				&& null != recommendLabelName.getCimages()) {
			return ok(recommendLabelName.getCimages()).as("image/png");
		}

		return badRequest();
	}
}
