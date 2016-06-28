package services.home.fragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import context.ContextUtils;
import dto.advertising.ProductAdertisingContextExtended;
import play.Logger;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.IAdvertisingService;
import services.base.template.ITemplateFragmentProvider;
import valueobjects.product.AdItem;
import valueobjects.product.MobileAdItem;
import valueobjects.product.ProductAdertisingContext;

public class HomeAdMiddleProvider implements ITemplateFragmentProvider {

	@Override
	public String getName() {
		return "home-middle-advert";
	}

	@Inject
	IAdvertisingService advertService;


	@Override
	public Html getFragment(Context ctx) {

		ProductAdertisingContextExtended pcontext = new ProductAdertisingContextExtended(
				null, 5, 5, ContextUtils.getWebContext(ctx));

		List<AdItem> list = advertService.getAdvertisingsExtended(pcontext);
		List<MobileAdItem> list2=new ArrayList<MobileAdItem>();
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				MobileAdItem mobileAdItem = new MobileAdItem();
				BeanUtils.copyProperties(list.get(i), mobileAdItem);
				String imgUrl = list.get(i).getImgUrl();
				if (imgUrl.startsWith("/img")) {
					imgUrl = imgUrl.replaceFirst("/img", "");
					mobileAdItem.setImgUrl(imgUrl);
				}
				list2.add(mobileAdItem);
			}
		}
		return views.html.product.middle_advert_templet.render(list2);
	}

}
