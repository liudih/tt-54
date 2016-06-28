package controllers.interaction;

import play.mvc.Controller;
import play.mvc.Result;
import services.interaction.InteractionEnquiryService;
import services.interaction.MemberBrowseHistoryService;

import com.google.inject.Inject;

public class Browse extends Controller {
	@Inject
	InteractionEnquiryService interactionEnquiryService;

	@Inject
	MemberBrowseHistoryService memberBrowseHistoryService;

	public Result getRecommend() {
		interactionEnquiryService.getRecommendByBrowse();
		return ok(" init recommend product ok ! ");
	}

	public Result setBoughtTogetherBayBrowse() {
		memberBrowseHistoryService.setFrequentlyBoughtTogetherBayBrowse();
		return TODO;
	}

}
