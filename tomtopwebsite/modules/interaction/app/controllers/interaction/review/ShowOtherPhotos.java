package controllers.interaction.review;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import dto.interaction.InteractionProductMemberPhotos;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.JsonFormatUtils;
import services.interaction.ShowOtherPhotosService;

public class ShowOtherPhotos extends Controller
{
	@Inject
	ShowOtherPhotosService showOtherPhotosService;
	
	public Result getOtherPhotos(String data)
	{
		JsonNode jsonNode=play.libs.Json.parse(data);
		JsonNode jNode = jsonNode.get(0);
		String clistingid =jNode.get("clistingid").asText();
		Integer id = jNode.get("id").asInt();
		String email=jNode.get("email").asText();
		List<InteractionProductMemberPhotos> getOtherPhotos=showOtherPhotosService.getOhterphotos(id, clistingid, email);
		String jsontxt = JsonFormatUtils.beanToJson(getOtherPhotos);
		return ok(jsontxt);
	}
}
