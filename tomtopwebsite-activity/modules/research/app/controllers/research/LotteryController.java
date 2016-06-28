package controllers.research;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.common.collect.Lists;

import controllers.base.Product;
import dto.image.Img;
import entity.activity.page.Page;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import service.activity.IPageService;
import services.base.FoundationService;
import services.image.IImageEnquiryService;
import valueobjects.base.ActivityBaseContext;
import valueobjects.base.activity.result.PrizeResult;

public class LotteryController extends Controller {

	@Inject
	FoundationService foundationService;

	@Inject
	IImageEnquiryService imageEnquiryService;

	@Inject
	IPageService pageService;

	@Inject
	Product product;
	
	@Inject
	HttpRequestFactory httpFactory;

	/**
	 * 
	 * @Title: viewLottery
	 * @Description: TODO(抽奖页面)
	 * @param 
	 * @param title
	 * @param @return
	 * @return Result
	 * @throws 
	 * @author yinfei
	 */
	public Result viewLottery(String title) {
		ActivityBaseContext context = new ActivityBaseContext();
		context.setLanguageId(foundationService.getLanguage());
		context.setWebsiteId(foundationService.getSiteID());
		context.setCurrency(foundationService.getCurrency());
		Page page = pageService.getPageByUrl(title, context.getWebsiteId());
		Logger.debug("img-id:"+page.getItemplateid());
		if (null != page) {
			Img img = imageEnquiryService.getImageById(page.getItemplateid());
			if (null != img) {
				String html = "";
				if(null != img.getCdnpath() && !img.getCdnpath().equals("")){
					img = getImgFromCdn(img.getCdnpath());
				}
				if(null!=img){
					String etag = img.getCmd5();
					response().setHeader(CACHE_CONTROL, "max-age=604800");
					response().setHeader(ETAG, etag);
					try {
						html = new String(img.getBcontent(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						Logger.error("UnsupportedEncodingException", e);
					}
				}else{
					return notFound(title + " not found");
				}
				return ok(views.html.manager.activity.manage.lottery_minimal.render(new Html(html), page));
			} else {
				Logger.debug("img not found");
				return product.view(title);
			}
		} else {
			return product.view(title);
		}
	}

	/**
	 * 
	* @Title: getPrizeResult
	* @Description: TODO(查询获奖结果)
	* @param @return
	* @return Result
	* @throws
	 */
	public Result getPrizeResult() {
		List<PrizeResult> PrizeResultList = Lists.newArrayList();
		JsonNode jnode = request().body().asJson();
		if (null != jnode) {
			int pageId = jnode.get("pageId").asInt();
			int startIndex = 0;
			int i = 0;
			String s = null;
			PrizeResultList = pageService.getPrizeResultByPageId(pageId, foundationService.getSiteID());
			for (PrizeResult pr : PrizeResultList) {
				if (pr.getCemail().indexOf("@") != -1) {
					startIndex = pr.getCemail().indexOf("@");
					s = pr.getCemail().substring(0, startIndex);
					for (; i < pr.getCemail().length() - startIndex; i++) {
						s += "*";
					}
					pr.setCemail(s);
					i = 0;
				}
			}
		}
		return ok(Json.toJson(PrizeResultList));
	}
	
	/**
	 * @Description: 获取cdn图片信息
	 * @param imgUrl
	 * @return
	 */
	public Img getImgFromCdn(String imgUrl) {
		Logger.info("*******get img from cdn:{}", imgUrl);
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
}
