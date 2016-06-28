package utils;

public class ImageUtils {

	public static String getSrcPath(String imageUrl) {
		if (imageUrl != null) {
			if (imageUrl.startsWith("http://")
					|| imageUrl.startsWith("https://")) {
				return imageUrl;
			} else {
				return "";
			}
		}
		return imageUrl;
	}
}
