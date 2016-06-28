package controllers.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import javax.inject.Inject;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import services.image.IImageUpdateService;
import services.image.ImageEnquiryService;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

import dto.image.Img;

public class Image extends Controller {

	@Inject
	ImageEnquiryService enquiry;

	@Inject
	IImageUpdateService update;

	@Inject
	HttpRequestFactory httpFactory;

	public Result view(String filename) {
		Img img = enquiry.getImageByPath(filename, true);
		if (null != img) {
			if (null != img.getCdnpath() && !img.getCdnpath().equals("")) {
				Logger.debug("get cdn img " + img.getCdnpath());
				img = getImgFromCdn(img.getCdnpath().trim(),img.getCcontenttype());
			} else {
				String etag = generateETag(img);
				String previous = request().getHeader(IF_NONE_MATCH);
				if (null != etag && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=5");
				response().setHeader(ETAG, etag);
			}
			return ok(img.getBcontent()).as(img.getCcontenttype());
		} else {
			// add by lijun for app
			String url = getOriginalUrl(filename);
			img = getImgFromCdn(url);
			if (null != img) {
				String etag = img.getCmd5();
				String previous = request().getHeader(IF_NONE_MATCH);
				if (null != etag && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=5");
				response().setHeader(ETAG, etag);
				return ok(img.getBcontent()).as(img.getCcontenttype());
			}
		}
		return notFound(filename + " not found");
	}

	public Result viewScaled(String filename, int width, int height)
			throws IOException {
		Img img = enquiry.getCachedImageByPath(filename, width, height);
		if (null == img) {
			// original copy
			img = enquiry.getImageByPath(filename, true);
			if (null != img) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Thumbnails.of(new ByteArrayInputStream(img.getBcontent()))
						.size(width, height).keepAspectRatio(true)
						.useOriginalFormat().toOutputStream(out);
				img.setBcontent(out.toByteArray());
				update.createImageCache(img, width, height);
			} else {
				// add by lijun for app
				String url = this.getOriginalUrl(filename);
				Img orgImg = getImgFromCdn(url);
				if (null != orgImg) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					Thumbnails
							.of(new ByteArrayInputStream(orgImg.getBcontent()))
							.size(width, height).keepAspectRatio(true)
							.useOriginalFormat().toOutputStream(out);

					orgImg.setBcontent(out.toByteArray());
					img = orgImg;
				}
			}
		}
		if (null != img) {
			String etag = generateETag(img);
			String previous = request().getHeader(IF_NONE_MATCH);
			if (null != etag && etag.equals(previous)) {
				return status(NOT_MODIFIED);
			}
			response().setHeader(CACHE_CONTROL, "max-age=5");
			response().setHeader(ETAG, etag);
			return ok(img.getBcontent()).as(img.getCcontenttype());
		}
		return notFound(filename + " not found");
	}

	public Result edmView(String filename) {
		Logger.error("===================================into edm=======");
		Img img = enquiry.getImageByPath(filename, true);
		if (null != img) {
			if (null != img.getCdnpath() && !img.getCdnpath().equals("")) {
				img = getImgFromCdn(img.getCdnpath().trim(),img.getCcontenttype());
			} else {
				String etag = generateETag(img);
				String previous = request().getHeader(IF_NONE_MATCH);
				if (null != etag && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=5");
				response().setHeader(ETAG, etag);
			}
			return ok(img.getBcontent()).as(img.getCcontenttype());
		} else {
			// add by lijun for app
			String url = this.getOriginalUrl(filename);
			img = getImgFromCdn(url);
			if (null != img) {
				String etag = img.getCmd5();
				String previous = request().getHeader(IF_NONE_MATCH);
				if (null != etag && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=5");
				response().setHeader(ETAG, etag);
				return ok(img.getBcontent()).as(img.getCcontenttype());
			}
		}
		return notFound(filename + " not found");
	}

	public Result activityView(String filename) {
		Logger.error("===================================into activity=======");
		Img img = null;
		try {
			img = enquiry.getImageByPath(filename, true);
		if (null != img) {
			if (null != img.getCdnpath() && !"".equals(img.getCdnpath())) {
				Logger.info("cdnpath === " + img.getCdnpath());
				img = getImgFromCdn(img.getCdnpath().trim(),img.getCcontenttype());
			} else {
				String etag = generateETag(img);
				String previous = request().getHeader(IF_NONE_MATCH);
				if (null != etag && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=5");
				response().setHeader(ETAG, etag);
			}
			return ok(img.getBcontent()).as(img.getCcontenttype());
		} else {
			// add by lijun for app
			String url = this.getOriginalUrl(filename);
			img = getImgFromCdn(url);
			if (null != img) {
				String etag = img.getCmd5();
				String previous = request().getHeader(IF_NONE_MATCH);
				if (null != etag && etag.equals(previous)) {
					return status(NOT_MODIFIED);
				}
				response().setHeader(CACHE_CONTROL, "max-age=5");
				response().setHeader(ETAG, etag);
				return ok(img.getBcontent()).as(img.getCcontenttype());
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notFound(filename + " not found");
	}

	protected String generateETag(Img img) {
		return img.getCmd5();
	}

	public Img getImgFromCdn(String imgUrl,String contentType) {
		Logger.info("******************get img from cdn:{}", imgUrl);
		if (StringUtils.isEmpty(imgUrl)) {
			return null;
		}
		try {
			GenericUrl url = new GenericUrl(imgUrl);
			HttpRequest request = httpFactory.buildGetRequest(url);
			HttpResponse response = request.execute();

			String type = response.getContentType();
			if(type == null || "".equals(type)){
				type = contentType;
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.download(baos);

			byte[] imgByte = baos.toByteArray();

			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(imgByte));

			Img img = new Img();
			img.setBcontent(imgByte);
			img.setCcontenttype(type);
			img.setCmd5(md5);
			return img;
		} catch (Exception e) {
			Logger.error("get img from cdn error", e);
			return null;
		}
	}
	
	public Img getImgFromCdn(String imgUrl) {
		Logger.error("*******get img from cdn:{}", imgUrl);
		if (StringUtils.isEmpty(imgUrl)) {
			return null;
		}
		try {
			GenericUrl url = new GenericUrl(imgUrl);
			HttpRequest request = httpFactory.buildGetRequest(url);
			HttpResponse response = request.execute();
			String type = response.getContentType();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.download(baos);

			byte[] imgByte = baos.toByteArray();

			String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5")
					.digest(imgByte));

			Img img = new Img();
			img.setBcontent(imgByte);
			img.setCcontenttype(type);
			img.setCmd5(md5);
			return img;
		} catch (Exception e) {
			Logger.error("get img from cdn error", e);
			return null;
		}
	}
	private String getOriginalUrl(String suburl) {
		if (StringUtils.isEmpty(suburl)) {
			return null;
		}
		String original = Play.application().configuration()
				.getString("image.ip.original");
		if (StringUtils.isNotEmpty(original) && original.startsWith("http://")) {
			if (!original.endsWith("/")) {
				original = original + "/";
			}

			original += suburl;
			return original;
		}
		return null;
	}

}
