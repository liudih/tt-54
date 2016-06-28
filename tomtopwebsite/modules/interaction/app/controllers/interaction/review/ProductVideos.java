package controllers.interaction.review;

import java.util.List;

import com.google.inject.Inject;

import dto.interaction.InteractionProductMemberVideo;
import play.mvc.Controller;
import play.mvc.Result;
import services.interaction.InteractionProductVideoService;

public class ProductVideos extends Controller
{
	@Inject
	InteractionProductVideoService productVideoService;
	
	public Result getAllVideo(String clistingid)
	{
		List<InteractionProductMemberVideo> listVideo=productVideoService.getAll(clistingid);
		return ok(views.html.interaction.fragment.interaction_show_all_video.render(listVideo));
	}
}
