package services.base.utils;

import play.mvc.Http;

public class HostnameUtils {

	public static String getHostname(Http.Context ctx) {
		String vhost = ctx.request().host();
		String hostname = vhost.contains(":") ? vhost.substring(0,
				vhost.indexOf(":")) : vhost;
		return hostname;
	}

}
