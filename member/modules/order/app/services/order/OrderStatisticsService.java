package services.order;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import mapper.order.DetailMapper;
import services.base.SystemParameterService;
import services.product.CategoryEnquiryService;
import services.product.IProductUpdateService;
import services.product.ProductEnquiryService;
import services.search.criteria.ProductLabelType;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.eventbus.EventBus;

import dto.order.ProductStatistics;
import dto.product.ProductBundleSale;
import dto.product.ProductLabel;
import events.search.ProductIndexingRequestEvent;

/**
 * 订单统计相关方法
 * 
 * @author kmtong
 *
 */
public class OrderStatisticsService {

	@Inject
	DetailMapper detailMapper;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	IProductUpdateService productUpdateService;

	@Inject
	SystemParameterService systemParameterService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Inject
	EventBus eventBus;

	/**
	 * 1、获取所有未绑定其它商品的listing; 2、从业务参数获取到该站点"单个订单多少订单行被认为是批发商--排除";
	 * 3、从业务参数获取到该站点在同一订单一起出现次数达到所少次，被认为有捆绑必要; 4、根据这个listing到所有合适的listing
	 */
	public void bundProductByOrder(int siteID) {

	}

	public void selectHotProduct(int siteID) {
		List<ProductStatistics> list = detailMapper.getProductStatistics();
		List<ProductStatistics> newlist = Lists.transform(list, e -> {
			e.setCategoryid(categoryEnquiryService.getLastCategoryId(e
					.getListingid()));
			return e;
		});
		Multimap<Integer, ProductStatistics> mapx = Multimaps.index(newlist,
				e -> e.getCategoryid());
		productUpdateService.deleteProductLabel(siteID,
				ProductLabelType.Hot.toString());
		for (Integer key : mapx.keySet()) {
			List<ProductStatistics> listgroup = (List<ProductStatistics>) mapx
					.get(key);

			Comparator<ProductStatistics> countComparator = new Comparator<ProductStatistics>() {
				public int compare(final ProductStatistics p1,
						final ProductStatistics p2) {
					return p1.getQty().compareTo(p2.getQty());
				}
			};
			List<ProductStatistics> sortedCopy = Ordering.from(countComparator)
					.reverse().sortedCopy(listgroup);

			for (int i = 0; i <= 15; i++) {
				try {
					ProductStatistics p = sortedCopy.get(i);
					ProductLabel productLabel = new ProductLabel();
					productLabel.setCtype(ProductLabelType.Hot.toString());
					productLabel.setClistingid(p.getListingid());
					productLabel.setCcreateuser("auto");
					productLabel.setIwebsiteid(p.getSiteid());
					productUpdateService.insertProductLabel(productLabel);
				} catch (IndexOutOfBoundsException ex) {
					break;
				}
			}
			List<String> listingidlist = Lists.transform(sortedCopy,
					e -> e.getListingid());
			eventBus.post(new ProductIndexingRequestEvent(listingidlist));
		}

	}
}
