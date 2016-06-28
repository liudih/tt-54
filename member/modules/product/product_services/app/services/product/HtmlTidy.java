package services.product;

import org.jsoup.Jsoup;

public class HtmlTidy {

	public static String tidy(String html) {
		if (html != null) {
			return Jsoup.parseBodyFragment(html).body().html();
		}
		return null;
	}

}
