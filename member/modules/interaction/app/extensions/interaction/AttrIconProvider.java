package extensions.interaction;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.AttributeMapper;

import org.apache.commons.lang3.StringUtils;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaction.collect.CollectService;
import services.member.login.LoginService;
import services.search.criteria.ProductLabelType;
import valueobjects.interaction.CollectCount;
import valueobjects.product.ProductAttrName;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import dao.dropship.IDropShipBaseEnquiryDao;
import dao.interaction.IDropshipProductDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductVideoEnquiryDao;
import dto.interaction.DropshipProduct;
import dto.interaction.ProductCollect;
import dto.member.DropShipBase;
import dto.product.ProductBase;
import dto.product.ProductLabel;
import dto.product.ProductVideo;

public class AttrIconProvider implements
		services.product.IProductAttrIconProvider {

	@Inject
	CollectService collectService;
	@Inject
	FoundationService foundationService;
	@Inject
	LoginService loginService;
	@Inject
	AttributeMapper attributeMapper;
	@Inject
	IProductVideoEnquiryDao productVideoEnquityDao;
	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;
	@Inject
	IDropshipProductDao dropshipProductDao;
	@Inject
	IProductBaseEnquiryDao productBaseEnquiryDao;
	@Inject
	IDropShipBaseEnquiryDao dropShipBaseEnquiryDao;

	@Override
	public String getName() {
		return "attr-icon";
	}

	@Override
	public Map<String, Html> getHtml(List<String> listingIds) {
		// 收藏
		List<CollectCount> collectCount = collectService
				.getCollectCountByListingIds(listingIds);
		List<ProductCollect> collects = collectService
				.getCollectByListingIds(listingIds);
		Map<String, CollectCount> countmap = Maps.uniqueIndex(collectCount,
				list -> list.getListingId());
		Multimap<String, ProductCollect> collectmap = Multimaps.index(collects,
				clist -> clist.getClistingid());
		Multimap<String, String> collectmap2 = Multimaps.transformValues(
				collectmap, m -> m.getCemail());
		final String email = foundationService.getLoginContext().isLogin() ? loginService
				.getLoginData().getEmail() : "";
		// 多色属性
		List<ProductAttrName> attrs = attributeMapper.getAttrByListingIds(
				listingIds, "color");
		Multimap<String, ProductAttrName> attrsmap = Multimaps.index(attrs,
				clist -> clist.getClistingid());
		Multimap<String, String> attrsmap2 = Multimaps.transformValues(
				attrsmap, m -> m.getCkeyname());

		// 免邮属性
		List<ProductLabel> labels = this.productLabelEnquiryDao
				.getBatchProductLabelByType(listingIds,
						ProductLabelType.FreeShipping.toString());
		Multimap<String, ProductLabel> freeMaps = Multimaps.index(labels,
				list -> list.getClistingid());

		// 是否有视频
		List<ProductVideo> videos = this.productVideoEnquityDao
				.getVideoBylistingIds(listingIds);
		Multimap<String, ProductVideo> videosmap = Multimaps.index(videos,
				v -> v.getClistingid());

		// 是否dropship收藏
		DropShipBase dropshipBase = dropShipBaseEnquiryDao
				.getDropShipBaseByEmail(email, foundationService.getSiteID());
		List<ProductBase> productBases = productBaseEnquiryDao
				.getRelatedSkuByListingids(listingIds);
		Map<String, String> listingAndSkuMap = Maps.newHashMap();
		Map<String, DropshipProduct> dmap = Maps.newHashMap();
		Boolean bdropshipShow = (null != dropshipBase ? true : false);
		if (bdropshipShow == true && null != productBases
				&& productBases.size() > 0) {
			for (ProductBase p : productBases) {
				listingAndSkuMap.put(p.getClistingid(), p.getCsku());
			}
			List<String> skus = Lists.transform(productBases, i -> i.getCsku());
			List<DropshipProduct> dropshipProducts = this.dropshipProductDao
					.getDropshipProductsByEmailAndSkus(email, skus,
							foundationService.getSiteID());
			Map<String, DropshipProduct> dropshipMap = Maps.uniqueIndex(
					dropshipProducts, i -> i.getCsku());
			dmap = Maps.asMap(Sets.newHashSet(listingIds), i -> {
				String sku = listingAndSkuMap.get(i);
				if (StringUtils.isNotEmpty(sku)) {
					if (dropshipMap.get(sku) != null) {
					}
					return dropshipMap.get(sku);
				} else {
					return null;
				}
			});
		}
		Map<String, DropshipProduct> dmap2 = dmap;

		Map<String, Html> htmlmap = Maps.toMap(listingIds, id -> {
			Integer count = countmap.get(id) != null ? countmap.get(id)
					.getCollectCount() : 0;
			boolean isCollect = false;
			boolean isColor = false;
			boolean isFreeship = false;
			boolean isVideo = false;
			boolean isDropship = false;
			if (!"".equals(email) && collectmap2.get(id) != null
					&& collectmap2.get(id).contains(email)) {
				isCollect = true;
			}
			if (attrsmap2.get(id) != null && attrsmap2.get(id).size() > 0) {
				isColor = true;
			}
			if (freeMaps.size() > 0 && freeMaps.get(id) != null
					&& freeMaps.get(id).size() > 0) {
				isFreeship = true;
			}
			if (videosmap.get(id) != null && videosmap.get(id).size() > 0) {
				isVideo = true;
			}

			if (productBases.size() > 0 && bdropshipShow
					&& dmap2.get(id) != null) {
				isDropship = true;
			}

			List<Html> loginButtons = loginService.getOtherLoginButtons();
			
			return views.html.home.attr_icon.render(id, count, isCollect,
					isColor, isFreeship, isVideo, isDropship, bdropshipShow,loginButtons);
		});
		return htmlmap;
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}
}
