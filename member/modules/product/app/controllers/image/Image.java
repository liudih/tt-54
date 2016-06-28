package controllers.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import net.coobird.thumbnailator.Thumbnails;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.image.IImageUpdateService;
import services.image.ImageEnquiryService;
import dto.image.Img;

public class Image extends Controller {

	@Inject
	ImageEnquiryService enquiry;

	@Inject
	IImageUpdateService update;

	public Result view(String filename) {
		Img img = enquiry.getImageByPath(filename, true);
		if (img != null) {
			String etag = generateETag(img);
			String previous = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(img.getBcontent()).as(img.getCcontenttype());
		}
		return notFound(filename + " not found");
	}

	public Result viewScaled(String filename, int width, int height)
			throws IOException {
		Img img = enquiry.getCachedImageByPath(filename, width, height);
		if (img == null) {
			// original copy
			img = enquiry.getImageByPath(filename, true);
			if (img != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Thumbnails.of(new ByteArrayInputStream(img.getBcontent()))
						.size(width, height).keepAspectRatio(true)
						.useOriginalFormat().toOutputStream(out);
				img.setBcontent(out.toByteArray());
				update.createImageCache(img, width, height);
			}
		}
		if (img != null) {
			String etag = generateETag(img);
			String previous = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(img.getBcontent()).as(img.getCcontenttype());
		}
		return notFound(filename + " not found");
	}

	public Result edmView(String filename) {
		Img img = enquiry.getImageByPath(filename, true);
		if (img != null) {
			String etag = generateETag(img);
			String previous = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(img.getBcontent()).as(img.getCcontenttype());
		}
		return notFound(filename + " not found");
	}

	public Result activityView(String filename) {
		Img img = enquiry.getImageByPath(filename, true);
		if (img != null) {
			String etag = generateETag(img);
			String previous = request().getHeader(IF_NONE_MATCH);
			if (etag != null && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return ok(img.getBcontent()).as(img.getCcontenttype());
		}
		return notFound(filename + " not found");
	}

	protected String generateETag(Img img) {
		return img.getCmd5();
	}
}
