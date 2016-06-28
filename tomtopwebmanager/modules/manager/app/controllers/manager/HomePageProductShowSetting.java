package controllers.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.IWebsiteService;
import services.product.ProductLabelService;
import session.ISessionService;
import valueobjects.base.Page;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;

import dao.product.IProductBaseEnquiryDao;
import dto.Website;
import dto.product.ProductBase;
import dto.product.ProductLabel;
import events.product.ProductUpdateEvent;

public class HomePageProductShowSetting extends Controller {

	@Inject
	ISessionService sessionService;

	@Inject
	ProductLabelService productLabelServices;

	@Inject
	IProductBaseEnquiryDao productBaseEnquiryDao;

	@Inject
	IWebsiteService websiteService;

	@Inject
	EventBus eventBus;

	final int PAGE_SIZE =20;

	@controllers.AdminRole(menuName = "HomePageSetting")
	public Result pageProductShowSetting(int page, int siteId, String sku,
			String type) {
		sku = "".equals(sku) ? null : sku;
		type = "".equals(type) ? null : type;
		// 获取站点
		List<Website> websites = websiteService.getAll();
		Map<Integer, String> siteMap = null;
		if (null != websites) {
			siteMap = websites.stream().collect(
					Collectors.toMap(a -> a.getIid(), a -> a.getCcode()));
		}
		List<String> types = Lists.newArrayList("hot", "clearstocks",
				"featured");// 首页产品的标签
		List<String> listingIds = null;
		List<ProductLabel> productLabels = null;
		Page<ProductLabel> productLabelPage = null;
		int total = 0;
		Map<String, String> listingIdAndSkuMap = Maps.newHashMap();

		// 根据sku 获取listingids
		if (null != sku) {
			List<ProductBase> productBases = productBaseEnquiryDao
					.getProductBaseBySkuAndWebsiteId(sku, siteId);
			if (null != productBases && productBases.size() > 0) {
				listingIds = Lists.transform(productBases,
						i -> i.getClistingid());
			}
			if (null == listingIds || listingIds.size() == 0) {
				productLabelPage = new Page<>(productLabels, 0, 0, PAGE_SIZE);
				return ok(views.html.manager.homepage.product_show_setting_manager
						.render(productLabelPage, types, page, PAGE_SIZE, siteId, type,
								sku, listingIdAndSkuMap, siteMap));
			}
		}
		productLabels = productLabelServices.getListingIdByTypeByPage(type,
				siteId, PAGE_SIZE, page, listingIds);

		total = productLabelServices.getListingIdByTypeByPageTotalCount(type,
				siteId, listingIds);
		productLabelPage = new Page<>(productLabels, total, page, PAGE_SIZE);

		if (null != productLabels && productLabels.size() > 0) {
			List<String> productLabelListingIds = productLabels.stream()
					.map(i -> i.getClistingid()).collect(Collectors.toList());
			List<ProductBase> bases = productBaseEnquiryDao
					.getRelatedSkuByListingids(productLabelListingIds);
			if (null != bases && bases.size() > 0) {
				listingIdAndSkuMap = bases.stream().collect(
						Collectors.toMap(a -> a.getClistingid(),
								a -> a.getCsku()));
			}
		}
		return ok(views.html.manager.homepage.product_show_setting_manager
				.render(productLabelPage, types, page, PAGE_SIZE, siteId, type,
						sku, listingIdAndSkuMap, siteMap));
	}

	public Result updateProductLabelTime(Integer id, String type, Integer siteId) {
		ProductLabel productLabel = productLabelServices
				.getProductLabelById(id);
		if (null != productLabel) {
			productLabel.setDcreatedate(new Date());
			boolean result = productLabelServices
					.updateProductLable(productLabel);
			if (result) {
				String listingId = productLabel.getClistingid();
				eventBus.post(new ProductUpdateEvent(null, listingId,
						ProductUpdateEvent.ProductHandleType.update));
				return redirect(controllers.manager.routes.HomePageProductShowSetting
						.pageProductShowSetting(1, siteId, null, type));
			}
		}
		Logger.error("update product label error ,productlabel id : {} ", id);
		return internalServerError();
	}
}
