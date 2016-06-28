package controllers.mobile.about;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductExplainMapper;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Controller;
import play.mvc.Result;
import services.ICmsContentService;
import services.mobile.MobileService;
import services.mobile.product.ProductService;
import dto.CmsContent;
import dto.mobile.ProductImageInfo;
import dto.mobile.ProductVideoInfo;

public class ProductViewController extends Controller {

	@Inject
	ICmsContentService cmsContentService;
	@Inject
	MobileService mobileService;
	@Inject
	ProductService productService;
	@Inject
	ProductExplainMapper productExplainMapper;

	/**
	 * 查询商品详细信息
	 * 
	 * @param des
	 * 
	 * @return
	 */
	public Result productDescription(String gid) {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(gid)) {
			String descript = productService.getDescription(gid);
			List<ProductImageInfo> images = productService.getProductImage(gid);
			List<ProductVideoInfo> videos = productService
					.getProductVideos(gid);
			if (videos != null && videos.size() > 0) {
				for (ProductVideoInfo vd : videos) {
					sb.append("<iframe src='" + vd.getCvideourl()
							+ "'></iframe><br>");
				}
			}
			sb.append(replaceDesc(descript));
			if (images != null && images.size() > 0) {
				for (ProductImageInfo ig : images) {
					sb.append("<img src='/img/" + ig.getCimageurl() + " '><br>");
				}
			}
		}
		String vstr = sb.toString();
		return ok(views.html.mobile.product_description.render(vstr));
	}

	public Result cmsDetail(Integer imenuid) {
		String ckey = "";
		if (imenuid == 80) {
			ckey = "about us";
		}
		if (imenuid == 82) {
			ckey = "Privacy Policy";
		}
		if (imenuid == 84) {
			ckey = "Return Policy";
		}
		if (imenuid == 86) {
			ckey = "Terms of Use";
		}
		List<CmsContent> cmsList = cmsContentService.getCmsContentByKey(1, 1,
				ckey);
		if (cmsList != null) {
			imenuid = cmsList.get(0).getImenuid();
		}
		List<CmsContent> cmsContent = cmsContentService.getCmsContentByMenuId(
				imenuid, mobileService.getWebContext());
		if (cmsContent == null || cmsContent.size() <= 0) {
			cmsContent = cmsContentService.getCmsContentByMenuId(imenuid,
					mobileService.getDefLangWebContext());
		}
		return ok(views.html.mobile.cms_detail.render(cmsContent));
	}

	public Result productExplain(Integer type) {
		String explain = "";
		String ct = "";
		if (type == 1) {
			ct = "paymentexplain";
		}
		if (type == 2) {
			ct = "warrantyexplain";
		}
		if (!"".equals(ct)) {
			explain = productExplainMapper.getContentForSiteAndLanAndType(
					mobileService.getWebSiteID(),
					mobileService.getLanguageID(), ct);

		}
		return ok(views.html.mobile.product_explain.render(explain));
	}

	private String replaceDesc(String desc) {
		if (desc.indexOf("${attributes}") >= 0) {
			desc = desc.replace("${attributes}", "");
		}
		if (desc.indexOf("${product_images}") >= 0) {
			desc = desc.replace("${product_images}", "");
		}

		return desc;
	}
}
