package controllers.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import controllers.InterceptActon;
import dto.Website;
import entity.manager.EnumAffiliateBanner;
import entity.manager.EnumAffiliateBanner.BannerType;
import entity.manager.EnumAffiliateBanner.Target;
import entity.tracking.AffiliateBanner;
import forms.AffiliateBannerForm;
import forms.AffiliateInfoForm;
import forms.AffiliateUserForm;
import forms.CaidForm;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.mvc.Http.MultipartFormData.FilePart;
import play.twirl.api.Html;
import services.base.utils.FileUtils;
import services.base.utils.StringUtils;
import services.base.WebsiteService;
import services.manager.AffiliateBannerService;
import services.manager.AffiliateService;
import services.product.CategoryEnquiryService;
import valueobjects.base.Page;

@With(InterceptActon.class)
public class Affiliate extends Controller {

	@Inject
	AffiliateService affiliateService;

	@Inject
	AffiliateBannerService affiliateBannerService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	WebsiteService websiteService;

	public Result index(int page) {
		AffiliateInfoForm info = new AffiliateInfoForm();
		info.setPage(page);
		return ok(getUsers(info));
	}

	public Result search() {
		Form<AffiliateInfoForm> affillateForm = Form.form(
				AffiliateInfoForm.class).bindFromRequest();
		AffiliateInfoForm affilate = affillateForm.get();
		return ok(getUsers(affilate));
	};

	public Html getUsers(AffiliateInfoForm affiliateInfoForm) {
		Page<dto.AffiliateInfo> list = affiliateService
				.getAffiliateUserPage(affiliateInfoForm);
		List<Website> websites=websiteService.getAll();
		return views.html.manager.affiliate.affiliateInfo_manager.render(websites,list,
				affiliateInfoForm);
	}

	public Result doUser() {

		Form<AffiliateUserForm> form = Form.form(AffiliateUserForm.class)
				.bindFromRequest();

		// 验证
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}

		AffiliateUserForm info = form.get();

		if (affiliateService.addAffilateInfo(info)) {
			return redirect(controllers.manager.routes.Affiliate.index(1));
		} else {
			return badRequest();
		}

	}

	public Result delUser(String aid) {
		if (aid == null)
			return badRequest();

		boolean flag = affiliateService.delUser(aid);

		if (!flag)
			flash().put("error", "error");
		return redirect(controllers.manager.routes.Affiliate.index(1));
	}

	public Result checkCaid() {

		Form<CaidForm> form = Form.form(CaidForm.class).bindFromRequest();

		return ok(Json.toJson(affiliateService.isHasCaid(form.get().getCaid(), form.get().getWebsite())));
	}

	public Result editForm(String aid) {
		if (aid == null)
			return badRequest();
		dto.AffiliateInfo info = affiliateService.getInfo(aid);
		if (info != null) {
			List<Website> websites=websiteService.getAll();
			return ok(views.html.manager.affiliate.affiliateInfo_edit
					.render(websites,info));
		}
		return notFound();
	}

	public Result doEdit() {
		Form<AffiliateUserForm> form = Form.form(AffiliateUserForm.class)
				.bindFromRequest();

		AffiliateUserForm info = form.get();

		// 不存在iid
		if (info.getIid() == 0) {
			return ok(views.html.manager.affiliate.error
					.render("iid is not exist"));
		}

		// 验证修改会员邮箱的唯一性
		String originalEmail = affiliateService.getEmail(info.getCaid());
		if (!(originalEmail.equals(info.getCemail()))) {
			boolean emailIsExist = affiliateService
					.isNotExist(info.getCemail());
			if (!emailIsExist) {
				return ok(views.html.manager.affiliate.error
						.render("User is exist"));
			}
		}

		if (affiliateService.updateAffiliateInfo(info)) {
			return redirect(controllers.manager.routes.Affiliate.index(1));
		}
		return ok(views.html.manager.affiliate.error.render("error"));
	}

	public Result getAffiliateBanners(int page, int pageSize) {
		if (page == 0) {
			page = 1;
		}
		AffiliateBanner affiliateBanner = new AffiliateBanner();
		Page<AffiliateBanner> list = affiliateBannerService
				.getAffiliateBanners(affiliateBanner, page, pageSize);
		BannerType[] type = EnumAffiliateBanner.BannerType.values();
		Target[] target = EnumAffiliateBanner.Target.values();
		List<Target> targetList = Arrays.asList(target);
		List<BannerType> bannertypelist = Arrays.asList(type);
		return ok(views.html.manager.affiliate.banner_index.render(list,
				bannertypelist, targetList));
	}

	public Result getBanner(Integer id) {
		AffiliateBanner banner = affiliateBannerService.get(id);
		BannerType[] type = EnumAffiliateBanner.BannerType.values();
		Target[] target = EnumAffiliateBanner.Target.values();
		List<Target> targetList = Arrays.asList(target);
		List<BannerType> bannertypelist = Arrays.asList(type);
		return ok(views.html.manager.affiliate.banner_edit.render(banner,
				targetList, bannertypelist));
	}

	public Result editBanner() {
		play.data.Form<AffiliateBannerForm> sourceForm = Form.form(
				AffiliateBannerForm.class).bindFromRequest();
		if (sourceForm.hasErrors()) {
			return ok(views.html.manager.affiliate.error.render("error"));
		}
		AffiliateBannerForm bannerForm = sourceForm.get();
		AffiliateBanner affiliateBanner = new AffiliateBanner();
		BeanUtils.copyProperties(bannerForm, affiliateBanner);
		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		if (null != body) {
			FilePart file = body.getFile("bbannerfile");
			formUtil(file, affiliateBanner);
		}
		boolean flag = affiliateBannerService.edit(affiliateBanner);
		if (flag) {
			return redirect(controllers.manager.routes.Affiliate
					.getAffiliateBanners(1, 10));
		} else {
			return ok(views.html.manager.affiliate.error.render("edit error"));
		}

	}

	public Result delBanner(Integer id) {
		Map<String, Object> mjson = new HashMap<String, Object>();
		Integer[] ids = { id };
		boolean flag = affiliateBannerService.delete(ids);
		if (flag) {
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

	public Result addBanner() {
		play.data.Form<AffiliateBannerForm> sourceForm = Form.form(
				AffiliateBannerForm.class).bindFromRequest();
		if (sourceForm.hasErrors()) {
			return ok(views.html.manager.affiliate.error.render("error"));
		}
		AffiliateBannerForm bannerForm = sourceForm.get();
		AffiliateBanner affiliateBanner = new AffiliateBanner();
		BeanUtils.copyProperties(bannerForm, affiliateBanner);
		play.mvc.Http.MultipartFormData body = request().body()
				.asMultipartFormData();
		if (null != body) {
			FilePart file = body.getFile("bbannerfile");
			formUtil(file, affiliateBanner);
		}
		boolean flag = affiliateBannerService.add(affiliateBanner);
		if (flag) {
			return redirect(controllers.manager.routes.Affiliate
					.getAffiliateBanners(1, 10));
		}
		return ok(views.html.manager.affiliate.error.render("error"));

	}

	/**
	 * 验证Saler唯一性，可以添加则返回true
	 * 
	 * @param email
	 * @return
	 */
	public Result checkEmail(String email, Integer website) {
		boolean isNotExist = affiliateService.emaiIsNotExist(email, website);
		if (StringUtils.notEmpty(email) && isNotExist) {
			return ok("true");
		}
		return ok("false");
	}

	/**
	 * 获取商品根类别
	 * 
	 * @param language
	 * @param websiteid
	 * @return
	 */
	public Result getCategory(int language, int websiteid) {
		List<dto.Category> category = categoryEnquiryService.rootCategories(
				language, websiteid);
		Map<String, Object> categoryMap = new HashMap<>();
		categoryMap.put("data", category);
		categoryMap.put("result", "success");
		return ok(Json.toJson(categoryMap));

	}

	/**
	 * 对表单文件做处理
	 */
	public void formUtil(FilePart file, AffiliateBanner affiliateBanner) {
		if (file != null) {
			File fileimg = file.getFile();
			BufferedImage sourceImg;
			try {
				sourceImg = ImageIO.read(new FileInputStream(fileimg));
				affiliateBanner.setIwidth(sourceImg.getWidth());
				affiliateBanner.setIheight(sourceImg.getHeight());
			} catch (FileNotFoundException e) {
				Logger.error("File not found" + e.getMessage(), e);
			} catch (IOException e) {
				Logger.error("File exception" + e.getMessage(), e);
			}
			String contentType = file.getContentType();
			if (contentType.startsWith("image/")) {
				byte[] buff = FileUtils.toByteArray(file.getFile());
				affiliateBanner.setBbannerfile(buff);
			}
		}
	}

}
