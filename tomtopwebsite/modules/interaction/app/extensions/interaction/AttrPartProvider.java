package extensions.interaction;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.AttributeMapper;

import org.apache.commons.lang3.StringUtils;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.StorageService;
import services.interaction.collect.CollectService;
import services.member.login.LoginService;
import services.product.IProductBadgePartProvider;
import services.search.criteria.ProductLabelType;
import valueobjects.interaction.CollectCount;
import valueobjects.product.ProductAttrName;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import dao.dropship.IDropShipBaseEnquiryDao;
import dao.interaction.IDropshipProductDao;
import dao.product.IProductBaseEnquiryDao;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductStorageMapEnquiryDao;
import dao.product.IProductVideoEnquiryDao;
import dto.interaction.DropshipProduct;
import dto.interaction.ProductCollect;
import dto.member.DropShipBase;
import dto.product.ProductBase;
import dto.product.ProductLabel;
import dto.product.ProductStorageMap;
import dto.product.ProductVideo;
import dto.Storage;

public class AttrPartProvider implements
		services.product.IProductAttrPartProvider {

	@Inject
	CollectService collectService;
	@Inject
	FoundationService foundationService;
	@Inject
	LoginService loginService;
	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;
	@Inject
	IProductStorageMapEnquiryDao productStorageMapEnquityDao;
	@Inject
	StorageService storageService;

	@Override
	public String getName() {
		return "attr-icon";
	}

	@Override
	public Map<String, Map<String,Html>> getHtml(List<String> listingIds) {
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

		// 免邮属性
		List<ProductLabel> labels = this.productLabelEnquiryDao
				.getBatchProductLabelByType(listingIds,
						ProductLabelType.FreeShipping.toString());
		Multimap<String, ProductLabel> freeMaps = Multimaps.index(labels,
				list -> list.getClistingid());

		//仓库
		List<ProductStorageMap> storagelist = this.productStorageMapEnquityDao
				.getProductStorages(listingIds);
		Multimap<String, ProductStorageMap> storageMap = Multimaps.index(storagelist,
				obj -> obj.getClistingid());
		List<Storage> stlist = storageService.getAllStorages();
		Map<Integer, Storage> stmap = Maps.uniqueIndex(stlist, s -> s.getIid());
		

		Map<String, Map<String,Html>> htmlmap = Maps.toMap(listingIds, id -> {
			Integer count = countmap.get(id) != null ? countmap.get(id)
					.getCollectCount() : 0;
			boolean isCollect = false;
			boolean isFreeship = false;
			Map<String, Html> mhtml = Maps.newHashMap();
			
			if (!"".equals(email) && collectmap2.get(id) != null
					&& collectmap2.get(id).contains(email)) {
				isCollect = true;
			}
			if (freeMaps.size() > 0 && freeMaps.get(id) != null
					&& freeMaps.get(id).size() > 0) {
				isFreeship = true;
				mhtml.put("freeship", views.html.interaction.badge_part.freeshipping.render());
			}
			List<ProductStorageMap> slist = Lists.newArrayList(storageMap.get(id));
			List<String> storageNameList = Lists.transform(slist, s -> {
				if(stmap.get(s.getIstorageid())!=null){
					return stmap.get(s.getIstorageid()).getCstoragename();
				}else{
					return "";
				}
			});
			if(storageNameList!=null && storageNameList.size()>0){
				storageNameList = Ordering.natural().sortedCopy(storageNameList);
			}
			
			List<Html> loginButtons = loginService.getOtherLoginButtons();
			
			mhtml.put("collect", views.html.interaction.badge_part.collect.render(id,count,isCollect,loginButtons));
			mhtml.put("storage", views.html.interaction.badge_part.storage.render(storageNameList));
			return mhtml;
		});
		return htmlmap;
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}
}
