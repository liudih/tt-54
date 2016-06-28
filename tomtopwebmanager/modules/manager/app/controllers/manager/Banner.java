package controllers.manager;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.elasticsearch.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.Request;
import play.mvc.Result;
import services.ILanguageService;
import services.base.BannerService;
import services.base.utils.FileUtils;
import services.manager.CategoryManagerService;
import valueobjects.base.Page;

import com.google.common.collect.Lists;

import dto.Website;
import dto.Language;
import forms.BannerForm;

public class Banner extends Controller {

	final int imageAllowheight = 400;

	final int imageAllowWidth = 1200;

	final String allowSizeError = "1";

	final String notError = "2";

	final String serverError = "3";

	@Inject
	BannerService bannerService;

	@Inject
	CategoryManagerService categoryManagerService;

	@Inject
	ILanguageService languageService;

	public Result list(int page, int pageSize, int websiteid, int langugesid) {

		final Collection<Website> websiteChoose = categoryManagerService
				.getWebsiteChoose();

		List<Integer> websiteIds = Lists.newArrayList();

		if (websiteid != 0) {
			websiteIds.add(websiteid);
		} else {
			for (Iterator<Website> it = websiteChoose.iterator(); it.hasNext();) {
				websiteIds.add(it.next().getIid());
			}
		}
		Page<dto.Banner> bannerPage = bannerService.getBannerPage(page,
				pageSize, langugesid, websiteIds);

		final List<Language> languges = languageService.getAllLanguage();

		List<dto.Banner> list = Lists.transform(
				bannerPage.getList(),
				o -> {
					String languageName = queryLanguesName(languges,
							o.getIlanguageid());
					String websiteName = queryWebsiteName(websiteChoose,
							o.getIwebsiteid());
					o.setWebsite(websiteName);
					o.setLanguage(languageName);
					return o;

				});

		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("aaData", list);
		resultMap.put("iTotalDisplayRecords", bannerPage.totalCount());
		resultMap.put("iTotalRecords", bannerPage.totalCount());
		return ok(Json.toJson(resultMap));
	}

	private String queryLanguesName(List<Language> languages, int languageid) {
		for (Language item : languages) {
			if (item.getIid() == languageid) {
				return item.getCname();
			}
		}
		return "";
	}

	private String queryWebsiteName(Collection<Website> websites, int websiteid) {
		for (Website item : websites) {
			if (item.getIid() == websiteid) {
				return item.getCcode();
			}
		}
		return "";
	}

	public Result index() {
		Collection<Website> websiteChoose = categoryManagerService
				.getWebsiteChoose();
		List<Language> languges = languageService.getAllLanguage();
		return ok(views.html.manager.banner.index.render(websiteChoose,
				languges));
	}

	public Result editForm(int iid) {
		Collection<Website> websiteChoose = categoryManagerService
				.getWebsiteChoose();
		dto.Banner banner = bannerService.getBannerByiid(iid);
		List<Language> languges = languageService.getAllLanguage();
		return ok(views.html.manager.banner.edit.render(banner, websiteChoose,
				languges));

	}

	public Result addBanner() {
		Form<BannerForm> form = Form.form(BannerForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return notFound(form.errorsAsJson());
		}

		BannerForm bform = form.get();
		dto.Banner banner = new dto.Banner();
		Request request = request();

		byte[] filebuff = getRequestByteArrayData(request, "bfile");
		byte[] bgfilebuff = getRequestByteArrayData(request, "bbgimagefile");

		if (filebuff == null) {
			flash().put("error", notError);
		}
		BeanUtils.copyProperties(bform, banner);
		if (bgfilebuff != null) {

			banner.setBbgimagefile(bgfilebuff);
			banner.setBhasbgimage(true);
		}
		if (filebuff != null) {

			if (!checkImageSize(filebuff)) {
				flash().put("error", allowSizeError);
				return redirect(controllers.manager.routes.Banner.index());
			}
			banner.setBfile(filebuff);
		}
		int index = bannerService.getMaxIndex();
		index = index == 0 ? 1 : index + 1;
		banner.setIindex(index);

		boolean result = bannerService.addBanner(banner);
		if (!result) {
			flash().put("error", serverError);
		}
		return redirect(controllers.manager.routes.Banner.index());
	}

	public Result delete(int index) {
		boolean result = bannerService.deleteBannerByIid(index);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("errorCode", result ? notError : serverError);
		return ok(Json.toJson(resultMap));
	}

	public Result up(int index) {
		boolean result = bannerService.up(index);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("errorCode", result ? notError : serverError);
		return ok(Json.toJson(resultMap));

	}

	public Result down(int iid) {
		boolean result = bannerService.down(iid);
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("errorCode", result ? notError : serverError);
		return ok(Json.toJson(resultMap));
	}

	public Result editBanner() {
		Form<BannerForm> form = Form.form(BannerForm.class).bindFromRequest();
		if (form.hasErrors()) {

		}
		BannerForm bform = form.get();
		dto.Banner banner = new dto.Banner();
		Request request = request();

		byte[] filebuff = getRequestByteArrayData(request, "bfile");
		byte[] bgfilebuff = getRequestByteArrayData(request, "bbgimagefile");

		if (bgfilebuff != null) {

			banner.setBbgimagefile(bgfilebuff);
			banner.setBhasbgimage(true);
		}
		if (filebuff != null) {
			if (!checkImageSize(filebuff)) {
				flash().put("error", allowSizeError);
				return redirect(controllers.manager.routes.Banner.index());
			}
			banner.setBfile(filebuff);
		}

		BeanUtils.copyProperties(bform, banner);
		boolean result = bannerService.upadeBanner(banner);
		if (!result) {
			flash().put("error", serverError);
		}
		return redirect(controllers.manager.routes.Banner.index());
	}

	private boolean checkImageSize(byte[] bytes) {
		if (bytes == null) {
			return false;
		}

		try {
			InputStream buffin = new ByteArrayInputStream(bytes, 0,
					bytes.length);
			BufferedImage img = ImageIO.read(buffin);

			if (img.getWidth() == imageAllowWidth
					&& img.getHeight() == imageAllowheight) {
				return true;
			}
		} catch (IOException e) {
			Logger.error("checkImageSize error:{}", e.getMessage());
		}
		return false;
	}

	private byte[] getRequestByteArrayData(Request request, String param) {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart file = body.getFile(param);
		if (file != null) {
			File f = file.getFile();
			byte[] buff = FileUtils.toByteArray(f);

			return buff;
		}
		return null;
	}

	public Result at(int iid) {
		dto.Banner b = bannerService.getBannerEntityById(iid);
		if (b != null) {
			return ok(b.getBfile()).as("image/png");
		}
		return notFound();
	}

}
