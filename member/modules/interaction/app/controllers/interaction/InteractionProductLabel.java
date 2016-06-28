package controllers.interaction;

import play.mvc.Controller;
import play.mvc.Result;
import services.interaction.InteractionEnquiryService;

import com.google.inject.Inject;

public class InteractionProductLabel extends Controller  {
	
	@Inject
	InteractionEnquiryService interactionEnquiryService;
	
	public Result getFeatured() {
		interactionEnquiryService.selectFeatured();
		return TODO;
	}
}
