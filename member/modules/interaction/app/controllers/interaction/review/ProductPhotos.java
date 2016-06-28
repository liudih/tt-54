package controllers.interaction.review;

import java.util.List;

import javax.inject.Inject;

import dto.interaction.InteractionProductMemberPhotos;
import play.mvc.Controller;
import play.mvc.Result;
import services.interaction.InteractionProductPhotosService;

public class ProductPhotos extends Controller
{
	@Inject
	InteractionProductPhotosService interactionProductPhotosService;
	
	public Result showAll(String clistingid)
	{
		List<InteractionProductMemberPhotos> listphotos=interactionProductPhotosService.getAll(clistingid);
		return ok(views.html.interaction.fragment.interaction_show_all_photos.render(listphotos));
	}
}