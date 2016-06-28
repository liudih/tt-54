package views.product;

public class ImageUtils {

	public static String getSrcPath(String imageUrl) {
		if (imageUrl != null) {
			if (imageUrl.startsWith("http://")
					|| imageUrl.startsWith("https://")) {
				return imageUrl;
			} else {
				return controllers.image.routes.Image.view(imageUrl).url();
			}
		}
		return imageUrl;
	}
}
