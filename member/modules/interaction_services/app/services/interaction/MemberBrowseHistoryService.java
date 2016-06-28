package services.interaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import mapper.interaction.ProductBrowseMapper;
import mapper.member.MemberBaseMapper;
import mapper.order.DetailMapper;

import org.elasticsearch.common.collect.Collections2;
import org.elasticsearch.common.collect.Sets;

import services.base.FoundationService;
import services.base.SystemParameterService;
import services.member.login.LoginService;
import services.product.IProductUpdateService;
import services.product.ProductEnquiryService;
import services.search.criteria.ProductLabelType;
import valueobjects.base.LoginContext;
import valueobjects.interaction.MemberBrowseHistory;

import com.google.api.client.util.Lists;

import context.WebContext;
import dao.interaction.IBrowseEnquiryDao;
import dto.interaction.ListingCount;
import dto.interaction.ProductBrowse;
import dto.member.MemberBase;
import dto.product.ProductBundleSale;

public class MemberBrowseHistoryService implements IMemberBrowseHistoryService {

	@Inject
	IBrowseEnquiryDao browseEnquityDao;

	@Inject
	FoundationService foundation;

	@Inject
	LoginService loginService;

	@Inject
	ProductBrowseMapper mapper;

	@Inject
	IProductUpdateService productUpdateService;

	@Inject
	ProductEnquiryService productEnquiryService;

	@Inject
	DetailMapper detailMapper;

	@Inject
	SystemParameterService systemParameterService;
	
	@Inject
	MemberBaseMapper memberBaseMapper;

	public MemberBrowseHistory getMemberBrowseHistory(int items) {
		LoginContext login = foundation.getLoginContext();
		int siteID = foundation.getSiteID();
		if (login.isLogin()) {
			// view by member ID
			int memberId = loginService.getLoginData().getMemberId();
			List<ProductBrowse> history = mapper.findByMemberID(memberId,
					siteID, items);
			return new MemberBrowseHistory(history);
		} else {
			// view by LTC
			String ltc = foundation.getLoginContext().getLTC();
			List<ProductBrowse> history = mapper.findByLTC(ltc, siteID, items);
			return new MemberBrowseHistory(history);
		}
	}
	
	@Override
	public List<ProductBrowse> getMemberBrowseHistoryByContext(
			WebContext context, int items, boolean isLogin) {
		int siteId = foundation.getSiteID(context);
		if (isLogin) {
			// view by member ID
			String email = foundation.getLoginContext(context).getMemberID();
			MemberBase m = memberBaseMapper.getUserByEmail(email, siteId);
			Integer memberId = m==null?0:m.getIid();
			return mapper.findByMemberID(memberId, siteId, items);
		} else {
			// view by LTC
			String ltc = foundation.getLoginContext(context).getLTC();
			return mapper.findByLTC(ltc, siteId, items);
		}
	}

	public String getLastViewListingIdBySiteIdAndLtc(int siteId, String ltc) {
		return browseEnquityDao.getLastViewListingIdBySiteIdAndLtc(siteId, ltc);
	}

	public void setFrequentlyBoughtTogetherBayBrowse() {
		int siteID = foundation.getSiteID();
		int bundListingShowCount = systemParameterService
				.getSystemParameterAsInt(1, 0, "BundListingShowCount", 1);
		int wholesaleLimit = systemParameterService.getSystemParameterAsInt(1,
				0, "WholesaleLimit", 2);
		Double autoBundDiscount = systemParameterService
				.getSystemParameterAsDouble(1, 0, "AutoBundDiscount", 1.0);

		String dropShipingListing = systemParameterService.getSystemParameter(
				siteID, 1, "DropShipping",
				"0092bcc8-d914-1004-874c-d371c9ab96c0");
		Set<String> setCanShow = productEnquiryService.getListingsExceptType(
				siteID, ProductLabelType.Clearstocks.toString());
		for (String listing : setCanShow) {
			List<String> cltcs = browseEnquityDao
					.getBrowseListingsByListing(listing);
			List<ListingCount> listingCounts = browseEnquityDao
					.getListingAndCountByCltcs(cltcs, 40);
			List<String> browseListings = Lists.newArrayList(Collections2
					.transform(listingCounts, e -> e.getClistingid()));
			Set<String> setBrowseListings = new HashSet(browseListings);

			productUpdateService.alterAutoBundleSaleVisible();
			List<String> result = detailMapper.getBundlingListingFordetail(
					listing, wholesaleLimit, bundListingShowCount);
			Set<String> setOrderDetailListing = new HashSet(result);
			Set<String> resultListings = Sets.difference(
					Sets.union(setOrderDetailListing, setBrowseListings),
					setCanShow);

			for (String bundlistingid : resultListings) {
				if (!bundlistingid.equals(dropShipingListing)) {
					ProductBundleSale productBundleSale = new ProductBundleSale();
					productBundleSale.setClistingid(listing);
					productBundleSale.setCbundlelistingid(bundlistingid);
					productBundleSale.setFdiscount(autoBundDiscount);
					productBundleSale.setCcreateuser("auto");
					productUpdateService.insertBundle(productBundleSale);
				}
			}
		}
	}
	
	public boolean addMemberBrowseHistory(ProductBrowse p){
		int flag = mapper.insert(p);
		return flag>0;
	}

}
