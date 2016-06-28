package services.mobile.home;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import services.IAdvertisingService;
import services.mobile.MobileService;
import valueobjects.product.ProductAdertisingContext;
import valuesobject.mobile.member.MobileContext;

import com.google.common.collect.Lists;

import dto.advertising.Advertising;
import dto.mobile.AdvertisingBaseInfo;

public class AdService {

	@Inject
	IAdvertisingService advertisingService;

	@Inject
	MobileService mobileService;

	public List<AdvertisingBaseInfo> getAdvertising(int type) {
		ProductAdertisingContext context = new ProductAdertisingContext(null,
				type, mobileService.getWebSiteID(),
				mobileService.getLanguageID(), null, "app");
		List<Advertising> advertisings = advertisingService
				.getProductAdvertising(context);
		if (advertisings == null || advertisings.size() <= 0) {
			context = new ProductAdertisingContext(null, type,
					mobileService.getWebSiteID(), 1, null, "app");
			advertisings = advertisingService.getProductAdvertising(context);
		}
		if (advertisings == null) {
			return null;
		}
		return convertList(advertisings);
	}

	private List<AdvertisingBaseInfo> convertList(
			List<Advertising> advertisingList) {
		return Lists.transform(advertisingList,
				(advertising) -> convertAdvertising(advertising));
	}

	private AdvertisingBaseInfo convertAdvertising(Advertising advertising) {
		AdvertisingBaseInfo advertisingBaseInfo = new AdvertisingBaseInfo();
		advertisingBaseInfo.setImgurl("http://www.tomtop.com/img/"
				+ advertising.getCimageurl());
		advertisingBaseInfo.setSit(advertising.getIposition());
		String chrefurl = advertising.getChrefurl();
		String itype = "0";
		String skip = "";
		if (StringUtils.isNotBlank(chrefurl)) {
			itype = chrefurl.substring(0, 1);
			skip = chrefurl.substring(2);
		}
		MobileContext mc = mobileService.getMobileContext();
		if (mc != null) {
			int appid = mc.getIplatform();
			int vs = mc.getCurrentversion();
			if ((appid == 1 && vs < 4)) {
				if (itype.equals("3") && skip.equals("151225")) {
					itype = "1";
					skip = "d578b4f0-d929-1004-835b-90389054983d";
				}
			}
		}
		advertisingBaseInfo.setType(itype);
		advertisingBaseInfo.setSkip(skip);
		return advertisingBaseInfo;
	}
}
