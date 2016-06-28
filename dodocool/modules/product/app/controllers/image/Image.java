package controllers.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;
import services.image.IImageEnquiryService;
import services.image.IImageUpdateService;

import com.google.inject.Inject;

import dto.image.Img;

public class Image extends Controller {

	@Inject
	IImageEnquiryService iImageEnquiryService;

	@Inject
	IImageUpdateService update;

	public Result view(String filename) {
		Logger.debug("" + filename);
		if (filename != null && filename.length() > 0) {
			try {
				Img img = iImageEnquiryService.getImageByPath(filename, false);
				if (img != null) {
					byte[] blist = this.getImagePart(filename); // iImageEnquiryService.getImageByte(filename);
					String previous = request().getHeader(IF_NONE_MATCH);
					String etag = img.getCmd5();
					if (etag != null && etag.equals(previous)) {
						return status(NOT_MODIFIED);
					}
					response().setHeader(CACHE_CONTROL, "max-age=604800");
					response().setHeader(ETAG, etag);
					return ok(blist).as(img.getCcontenttype());
				}
			} catch (Exception ex) {
				Logger.error("error", ex);
			}
		}
		return notFound("img not found");
	}

	public Promise<Result> CategoryView(Integer categoryBackgroundImgId) {
		String imghost = Play.application().configuration()
				.getString("image.category.host");
		Logger.debug("" + imghost);
		return getImage(imghost + categoryBackgroundImgId);
	}

	private Promise<Result> getImage(String url) {
		if (url != null && url.length() > 0) {
			try {
				WSRequestHolder ws = WS.url(url);
				byte[] blist = ws.get().get(20000).asByteArray();
				if (blist != null) {
					response().setHeader(CACHE_CONTROL, "max-age=604800");
					return Promise.pure(ok(blist).as("image/jpg"));
				}
			} catch (Exception ex) {
				Logger.error("error", ex);
			}
		}
		return Promise.pure(notFound("img not found"));
	}

	private byte[] getImagePart(String filename) {
		int start = 0;
		int len = 1024 * 10;
		byte[] blist = null;
		List<Byte> bytelist = new LinkedList<Byte>();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (true) {
				blist = iImageEnquiryService.getImagePart(filename, start, len);
				if (blist == null) {
					break;
				}
				System.out.println("start " + start + "  end  " + len);
				out.write(blist);
				start += len;
			}
			return out.toByteArray();
		} catch (Exception ex) {
			Logger.error("get img error: ", ex);
		} finally {
			try {
				out.flush();
				out.close();
			} catch (Exception ex) {
				Logger.error("close img error: ", ex);
			}
		}
		return null;
	}

	public Result viewScaled(String filename, int width, int height)
			throws IOException {
		ByteArrayOutputStream file = new ByteArrayOutputStream();
		boolean flag = true;
		Integer start = 0;
		Integer length = 1024 * 5;
		while (flag) {
			byte[] imagePart = iImageEnquiryService.getCachedImageByPath(
					filename, width, height, start, length);
			if (imagePart != null) {
				try {
					file.write(imagePart);
					start = start + length;
				} catch (IOException e) {
					e.printStackTrace();
					Logger.debug("image error:{}", e.toString());

					return null;
				}
			} else {
				flag = false;
			}
		}
		if (file.toByteArray().length <= 0) {
			byte[] imagePart = getImagePart(filename);
			file.write(imagePart);
			update.createCacheImage(filename, width, height);
		}
		if (file != null) {
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			return ok(file.toByteArray()).as("image/png");
		}
		return notFound(filename + " not found");
	}

	public Result imageView(String filename, int width, int height)
			throws IOException {
		ByteArrayOutputStream file = new ByteArrayOutputStream();
		Img img = iImageEnquiryService.getCachedImageByPath(filename,
				width, height);
		if (img != null) {
			try {
				file.write(img.getBcontent());
			} catch (IOException e) {
				e.printStackTrace();
				Logger.debug("image error:{}", e.toString());

				return null;
			}
		}
		if (file.toByteArray().length <= 0) {
			byte[] imagePart = getImagePart(filename);
			file.write(imagePart);
			update.createCacheImage(filename, width, height);
		}
		if (file != null) {
			response().setHeader(CACHE_CONTROL, "max-age=604800");
			return ok(file.toByteArray()).as("image/png");
		}
		return notFound(filename + " not found");
	}
}
