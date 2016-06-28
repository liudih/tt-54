package controllers.mobile.about;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import base.util.httpapi.ApiUtil;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICmsContentService;
import services.mobile.MobileService;
import services.mobile.product.ProductService;
import services.product.IProductExplainService;
import dto.CmsContent;
import dto.mobile.ProductImageInfo;
import dto.mobile.ProductVideoInfo;
import dto.product.ProductExplain;

public class ProductViewController extends Controller {

	@Inject
	ICmsContentService cmsContentService;
	@Inject
	MobileService mobileService;
	@Inject
	ProductService productService;
	@Inject
	IProductExplainService productExplainService;

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
//			String descript = productService.getDescription(gid);
			String url = "http://product.api.tomtop.com/ic/v1/product/desc/"+gid;
			Logger.info("before send get date- url:"+url);
			String resultBody = new ApiUtil().get(url);
			String descript = "";
			if(!StringUtils.isBlank(resultBody)){
				try{
					JSONObject jsono = JSONObject.parseObject(resultBody);
					if(jsono!=null && jsono.getJSONObject("data")!=null){
						String desc =jsono.getJSONObject("data").getString("desc");
						descript = desc==null ? "" : desc;
					}
				}catch(Exception e){
					Logger.error("json转换失败",e);
				}
			}
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
					if (ig.getCimageurl().indexOf("http://") >= 0
							|| ig.getCimageurl().indexOf("https://") >= 0) {
						sb.append("<img src='" + ig.getCimageurl() + " '><br>");
					} else {
						sb.append("<img src='http://www.tomtop.com/img/"
								+ ig.getCimageurl() + " '><br>");
					}
				}
			}
		}
		String vstr = sb.toString();
		return ok(views.html.mobile.product_description.render(vstr));
	}

	public Result cmsDetail(Integer imenuid) {
		String ckey = "";
		if (imenuid == 80) {
			// about us (test service id = 81)
			imenuid = 85;
		}
		if (imenuid == 82) {
			// Privacy Policy (test service id = 83)
			imenuid = 86;
		}
		if (imenuid == 84) {
			// Return Policy (test service id = 85)
			imenuid = 88;
		}
		if (imenuid == 86) {
			// Terms of Use (test service id = 87)
			imenuid = 87;
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
			ProductExplain pe = productExplainService
					.getProductExplainBySiteIdAndLanIdAndType(
							mobileService.getWebSiteID(),
							mobileService.getLanguageID(), ct);
			if (pe == null || null == pe.getCcontent()
					|| "".equals(pe.getCcontent())) {
				pe = productExplainService
						.getProductExplainBySiteIdAndLanIdAndType(1, 1, ct);
			} else {
				explain = pe.getCcontent();
			}

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
