package controllers.product;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import services.product.ProductImageService;

public class ProductImage extends Controller {

	@Inject
	ProductImageService imageService;

	public Result convertBatch(int limit) {
		int count = imageService.internalizeAllProductImages(limit);
		return ok("Internalized Count: " + count + "/" + limit);
	}
}
