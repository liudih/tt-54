package utils;

import valuesobject.mobile.member.MobileContext;

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

	// CDN图片支持尺寸
	// {x:560,y:560},{x:500,y:500},{x:377,y:377},{x:265,y:265},{x:220,y:220},
	// {x:168,y:168},{x:120,y:120},{x:60,y:60}
	public static String getWebPath(String imageUrl, int width, int height,
			MobileContext mc) {
		if (imageUrl != null) {
			if (imageUrl.startsWith("http://")
					|| imageUrl.startsWith("https://")) {
				return imageUrl;
			} else if (imageUrl.startsWith("/imgxy")
					|| imageUrl.startsWith("/img")) {
				return "http://www.tomtop.com" + imageUrl;
				// 版本判断，只有最新版调用CDN图片
			}
			if (mc != null) {
				int platid = mc.getIplatform();
				int vs = mc.getCurrentversion();
				if ((platid == 1 && vs > 4) || (platid == 2 && vs > 206)) {
					if (width > 500) {
						return "http://img.tomtop-cdn.com/product/original/"
								+ imageUrl;
					} else {
						return "http://img.tomtop-cdn.com/product/xy/" + width
								+ "/" + height + "/" + imageUrl;
					}
				} else {
					return "http://www.tomtop.com/imgxy/" + width + "/"
							+ height + "/" + imageUrl;
				}
			} else {
				return "http://www.tomtop.com/imgxy/" + width + "/" + height
						+ "/" + imageUrl;
			}
		}
		return imageUrl;
	}
}
