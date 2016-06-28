package services.product;

import java.util.List;
import java.util.Set;

import mapper.product.CategoryLabelMapper;
import mapper.product.ProductBaseMapper;
import play.Logger;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.search.criteria.ProductLabelType;
import valueobjects.product.NewArrival;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductLabelUpdateDao;
import dto.product.CategoryLabel;
import dto.product.ProductLabel;
import events.search.ProductIndexingRequestEvent;

public class NewArrivalCategoryService {

	@Inject
	ProductBaseMapper productBaseMapper;

	@Inject
	FoundationService foundationService;

	@Inject
	CategoryLabelMapper categoryLabelMapper;

	@Inject
	SystemParameterService parameterService;
	
	@Inject
	IProductLabelUpdateDao productLabelUpdateDao;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;
	
	@Inject
	EventBus eventBus;

	public void newArrivalCategory() {
		Integer iwebsiteid = foundationService.getSiteID();
		Integer languageid = foundationService.getLanguage();
		String type = ProductLabelType.NewArrial.toString();

		String newValidDays = parameterService.getSystemParameter(iwebsiteid,
				languageid, "NewarrivalsValidDays");
		Integer days = Integer.parseInt(newValidDays); // 新品的间隔时间

		List<NewArrival> newArrivals = productBaseMapper.getNewArrivals(
				iwebsiteid, days);
		if (null != newArrivals && newArrivals.size() > 0) {
			Set<Integer> icategoryids = Sets.newLinkedHashSet(Lists.transform(
					newArrivals, n -> {
						return n.getIcategoryid();
					}));

			List<Integer> idList = Lists.newArrayList();
			idList.addAll(icategoryids);
			List<CategoryLabel> categoryLabels = Lists.transform(idList, i -> {
				CategoryLabel categoryLabel = new CategoryLabel();
				categoryLabel.setCtype(type);
				categoryLabel.setIcategoryid(i);
				categoryLabel.setIwebsiteid(1);
				categoryLabel.setCcreateuser("auto");
				return categoryLabel;
			});

			categoryLabelMapper.deleteCategoryLabelByType(iwebsiteid, type);
			Logger.debug("-------insert "
					+ categoryLabels.size()
					+ "  new arrivals categories into t_category_label----begin---");
			for (CategoryLabel categoryLabel : categoryLabels) {
				categoryLabelMapper.insert(categoryLabel);
			}
			Logger.debug("-------insert new arrivals categories into t_category_label----over----");

			List<ProductLabel> productLabels = com.google.common.collect.Lists
					.transform(newArrivals, l -> {
						ProductLabel productLabel = new ProductLabel();
						productLabel.setCtype(type);
						productLabel.setClistingid(l.getClistingid());
						productLabel.setIwebsiteid(l.getIwebsiteid());
						productLabel.setCcreateuser("auto");
						return productLabel;
					});

			List<ProductLabel> proList = this.productLabelEnquiryDao
					.getProductLabelByTypeAndWebsite(iwebsiteid, type);
			Set<String> set = Sets.newLinkedHashSet();
			set.addAll(Lists.transform(proList, p -> {
				return p.getClistingid();
			}));
			set.addAll(Lists.transform(newArrivals, n -> {
				return n.getClistingid();
			}));
			List<String> changedListingIds = Lists.newArrayList(set);

			productLabelUpdateDao.deleteBySiteAndType(iwebsiteid, type);

			Logger.debug("---insert " + newArrivals.size()
					+ " new arrivals  into t_product_label----begin---");
			for (ProductLabel productLabel : productLabels) {
				this.productLabelUpdateDao.insert(productLabel);
			}
			Logger.debug("-------insert new arrivals  into t_category_label----over----");
			eventBus.post(new ProductIndexingRequestEvent(changedListingIds));
		} else {
			Logger.debug("not found any new arrivals in product base ！");
		}

	}
}
