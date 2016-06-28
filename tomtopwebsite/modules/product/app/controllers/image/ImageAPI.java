package controllers.image;

import javax.inject.Inject;

import play.libs.F.Either;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import services.image.IImageUpdateService;
import services.image.ImageEnquiryService;

import com.fasterxml.jackson.databind.JsonNode;

import dto.image.ImageMeta;
import dto.image.Img;

public class ImageAPI extends Controller {

	@Inject
	ImageEnquiryService enquiry;

	@Inject
	IImageUpdateService update;

	@BodyParser.Of(BodyParser.Raw.class)
	public Result create(String filename) throws Exception {
		Img image = new Img();
		image.setCpath(filename);
		image.setBcontent(request().body().asRaw().asBytes());
		image.setCcontenttype(request().getHeader("Content-Type"));
		Either<Exception, Long> result = update.createImage(image);
		return ok("Result: " + result);
	}

	public Result meta(String filename) throws Exception {
		Img image = enquiry.getImageByPath(filename, false);
		if (image != null) {
			return ok(Json.toJson(new ImageMeta(image.getIid(), image
					.getCpath(), image.getCcontenttype(), image.getCmd5())));
		}
		return notFound("Image " + filename + " not found");
	}

	@BodyParser.Of(BodyParser.Json.class)
	public Promise<Result> copyFrom(final String filename) throws Exception {
		JsonNode node = request().body().asJson();
		String url = node.get("url").asText();
		Promise<Either<Exception, Long>> result = update.copyImageFrom(url,
				filename);
		return result.map(r -> ok("Result: " + r));
	}

}
