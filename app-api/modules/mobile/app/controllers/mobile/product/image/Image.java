package controllers.mobile.product.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;

import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import services.image.IImageEnquiryService;
import services.image.IImageUpdateService;
import dto.image.Img;

public class Image extends Controller {

	final static int ACCEPT_SIZE = 50;

	@Inject
	IImageEnquiryService enquiry;

	@Inject
	IImageUpdateService update;

	public Promise<Result> view(String filename, int width, int height)
			throws IOException {
		Img img = enquiry.getCachedImageByPath(filename, width, height);
		if (img == null) {
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
				return Promise.pure(status(NOT_MODIFIED));

			}
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			response().setHeader(ETAG, etag);
			return Promise
					.pure(ok(img.getBcontent()).as(img.getCcontenttype()));

		}
		return Promise.pure(notFound(filename + " not found"));
	}

	protected String generateETag(Img img) {
		return img.getCmd5();
	}

	// 头像还是 评论
	public Result uploadImage(String type) {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart part = body.getFile("img");
		String contentType = part.getContentType();
		if (checkContentType(contentType)) {
			File file = part.getFile();
			if (file != null && checkSize(file.length())) {
				byte[] buff;
				try {
					buff = IOUtils.toByteArray(new FileInputStream(file));

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private boolean checkSize(long size) {
		int _size = new Integer((int) (size / 1024));
		return _size <= ACCEPT_SIZE ? true : false;
	}

	private boolean checkContentType(String contentType) {
		Pattern pattern = Pattern.compile("^(image)/(bmp|gif|png|jpe?g)$");
		Matcher matcher = pattern.matcher(contentType);
		return matcher.matches();

	}
}
