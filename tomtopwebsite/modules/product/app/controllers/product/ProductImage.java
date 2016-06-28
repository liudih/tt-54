package controllers.product;

import javax.inject.Inject;

import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import services.product.ProductImageService;

public class ProductImage extends Controller {

	@Inject
	ProductImageService imageService;

	public Result convertBatch(int limit) {
		// modify by lijun
		int count = 0;
		Boolean fetch = Play.application().configuration()
				.getBoolean("image.fetch");
		if (fetch == null || fetch) {
			count = imageService.internalizeAllProductImages(limit);
		}

		return ok("Internalized Count: " + count + "/" + limit);
	}
}
