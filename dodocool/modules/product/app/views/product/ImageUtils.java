package views.product;

public class ImageUtils {

	public static String getSrcPath(String imageUrl, Integer width,
			Integer height) {
		if (imageUrl != null) {
			if (imageUrl.startsWith("http://")
					|| imageUrl.startsWith("https://")) {
				return imageUrl;
			} else {
				return controllers.image.routes.Image.viewScaled(imageUrl,
						width, height).url();
			}
		}
		return imageUrl;
	}
}
