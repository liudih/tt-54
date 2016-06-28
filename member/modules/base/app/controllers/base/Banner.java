package controllers.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import services.base.BannerService;

public class Banner extends Controller {

	@Inject
	BannerService bannerService;

	public Result at(int iid) {
		dto.Banner b = bannerService.getBannerEntityById(iid);
		if (b != null) {
			DateFormat df = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzz");
			long timestamp = b.getDcreatedate() != null ? b.getDcreatedate()
					.getTime() : 0;
			String stime = "\"" + Long.toString(timestamp) + "\"";
			String etag = request().getHeader(IF_NONE_MATCH);
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, stime);
			response().setHeader(LAST_MODIFIED, df.format(b.getDcreatedate()));
			if (etag != null && etag.equals(stime)) {
				return status(NOT_MODIFIED);
			}
			return ok(b.getBfile()).as("image/png");
		}
		return notFound();
	}

	public Result bgimg(int iid) {
		dto.Banner b = bannerService.getBannerEntityById(iid);
		if (b != null) {
			DateFormat df = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzz");
			long timestamp = b.getDcreatedate() != null ? b.getDcreatedate()
					.getTime() : 0;
			String stime = "\"" + Long.toString(timestamp) + "\"";
			String etag = request().getHeader(IF_NONE_MATCH);
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, stime);
			response().setHeader(LAST_MODIFIED, df.format(b.getDcreatedate()));
			if (etag != null && etag.equals(stime)) {
				return status(NOT_MODIFIED);
			}
			return ok(b.getBbgimagefile()).as("image/png");
		}
		return notFound();
	}

}
