package services.home.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.IAdvertisingService;
import services.base.FoundationService;
import services.base.template.ITemplateFragmentProvider;
import utils.HttpSendRequest;
import valueobjects.product.MobileAdItem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BannerFragmentProvider implements ITemplateFragmentProvider {

	@Inject
	IAdvertisingService advertService;

	@Inject
	FoundationService foundation;

	@Override
	public String getName() {
		return "home-banner";
	}

	@Override
	public Html getFragment(Context context) {
		List<MobileAdItem> advertList = new ArrayList<MobileAdItem>();

		try {
			String advertRemoteUrl = Play.application().configuration()
					.getString("advert_remote_url");
			if (advertRemoteUrl == null || advertRemoteUrl.length() == 0) {
				throw new NullPointerException(
						"can not get advert_remote_url config from application.conf");
			}

			// GET ${website}/ic/v2/base/banners_content
			// layoutCode String 布局标识，主页:HOME\CATEGORY 不是必须 默认为HOME
			// client int 客户端: 1 TOMTOP-PC,2 TOMTOP-Mobile,3 TOMTOP-APP-IOS,4
			// TOMTOP-APP-Android 不是必须 默认为1
			// lang int 语言 1 en 不是必须 默认为1
			// categoryId int 类目id ,默认0，0:表示首页,不是必须
			Integer language = foundation.getLanguage();
			if (language == null || language == 0) {
				language = 1;
			}
			String getAdvertUrl = advertRemoteUrl
					+ "/ic/v2/base/banners_content?";
			String param = "layoutCode=HOME&client=2&categoryId=0&lang="
					+ language;

			String advertUrlJson = HttpSendRequest
					.sendGet(getAdvertUrl + param);

			ObjectMapper om = new ObjectMapper();
			JsonNode jsonNode = om.readTree(advertUrlJson);

			JsonNode node = jsonNode.get("data");
			if (node != null) {
				JsonNode mobileNode = node.get("MOBILE-BANNER-SLIDER");
				if (null != mobileNode) {
					if (mobileNode.isArray()) {
						java.util.Iterator<JsonNode> list = mobileNode
								.iterator();
						while (list.hasNext()) {
							JsonNode pi = list.next();
							// String name = pi.get("name") == null ? "" :
							// pi.get("name")
							// .asText();
							String title = pi.get("title") == null ? "" : pi
									.get("title").asText();
							String url = pi.get("url") == null ? "" : pi.get(
									"url").asText();
							String imageUrl = pi.get("imgUrl") == null ? ""
									: pi.get("imgUrl").asText();
							Integer sort = pi.get("sort") == null ? 0 : pi.get(
									"sort").asInt();
							MobileAdItem mobileAdItem = new MobileAdItem();
							mobileAdItem.setImgUrl(imageUrl);
							mobileAdItem.setUrl(url);
							mobileAdItem.setTitle(title);
							mobileAdItem.setSort(sort);
							advertList.add(mobileAdItem);
						}
					}
					if (null != advertList && advertList.size() > 1) {
						Collections.sort(advertList,
								new Comparator<MobileAdItem>() {
									public int compare(MobileAdItem arg0,
											MobileAdItem arg1) {
										return arg0.getSort().compareTo(
												arg1.getSort());
									}
								});
					}
				}
			}

		} catch (Exception e) {
			Logger.error("get paypal form error", e);
		}

		// ProductAdertisingContextExtended ctx = new
		// ProductAdertisingContextExtended(
		// null, 5, 3, ContextUtils.getWebContext(context));
		//
		// Logger.debug("==============================");
		// List<AdItem> list = advertService.getAdvertisingsExtended(ctx);
		// List<MobileAdItem> list2 = new ArrayList<MobileAdItem>();
		// if (null != list && list.size() > 0) {
		// for (int i = 0; i < list.size(); i++) {
		// MobileAdItem mobileAdItem = new MobileAdItem();
		// BeanUtils.copyProperties(list.get(i), mobileAdItem);
		// String imgUrl = list.get(i).getImgUrl();
		// if (imgUrl.startsWith("/img")) {
		// imgUrl = imgUrl.replaceFirst("/img", "");
		// mobileAdItem.setImgUrl(imgUrl);
		// }
		// list2.add(mobileAdItem);
		// }
		// }

		return views.html.home.banner.render(advertList);
	}

}
