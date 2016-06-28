package services.order.dropShipping;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.order.IFreightService;
import services.product.ProductLabelService;
import services.search.criteria.ProductLabelType;
import services.shipping.IShippingMethodService;
import services.shipping.ShippingServices;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.DropShippingOrderDTO;
import dto.Storage;
import dto.order.dropShipping.DropShippingOrder;
import dto.order.dropShipping.DropShippingOrderDetail;

public class DSDTOTransformService {
	@Inject
	private DropShippingMapEnquiryService dropShippingMapEnquiry;
	@Inject
	private DropShippingOrderEnquiryService orderEnquiry;
	@Inject
	private DropShippingOrderDetailEnquiryService detailEnquiry;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	private IFreightService freightService;
	@Inject
	private ShippingServices shippingService;
	@Inject
	private ProductLabelService productLabelService;

	public List<DropShippingOrderDTO> getDropShippingOrderDTOs(
			String dropShippingID, int languageID, String currency,
			int websiteID) {
		if (dropShippingID == null) {
			return Lists.newArrayList();
		}
		List<Integer> dropShippingOrderIDs = dropShippingMapEnquiry
				.getDropShippingOrderIDsByShippingID(dropShippingID);
		if (dropShippingOrderIDs.isEmpty()) {
			return Lists.newArrayList();
		}
		List<DropShippingOrder> orders = orderEnquiry
				.getListByIDs(dropShippingOrderIDs);
		List<DropShippingOrderDTO> list = Lists.transform(
				orders,
				o -> transformToDropShippingOrderDTO(o, languageID, currency,
						websiteID));
		list = Lists.newArrayList(Collections2.filter(list, e -> e != null));
		return list;
	}

	private DropShippingOrderDTO transformToDropShippingOrderDTO(
			DropShippingOrder o, int languageID, String currency, int websiteID) {
		if (o == null) {
			return null;
		}
		DropShippingOrderDTO d = new DropShippingOrderDTO(o);
		List<DropShippingOrderDetail> list = detailEnquiry
				.getByDropShippingOrderID(d.getIid());
		List<String> listingIDs = Lists.transform(list, e -> e.getClistingid());
		d.setDetails(list);
		ShippingMethodInformations infos = getShippingMehtods(d, languageID,
				currency, listingIDs, websiteID);
		d.setShippingMethods(infos);
		return d;
	}

	private ShippingMethodInformations getShippingMehtods(
			DropShippingOrderDTO dto, int languageID, String currency,
			List<String> listingIDs, int websiteID) {
		if (listingIDs == null || listingIDs.isEmpty()) {
			return new ShippingMethodInformations(Lists.newArrayList());
		}
		Boolean isSpecial = shippingMethodService.isSpecial(listingIDs);
		Storage storage = shippingService.getShippingStorage(websiteID,
				listingIDs);
		if (storage == null) {
			return new ShippingMethodInformations(Lists.newArrayList());
		}
		Map<String, Integer> qtyMap = Maps.newHashMap();
		List<DropShippingOrderDetail> list = dto.getDetails();
		for (DropShippingOrderDetail d : list) {
			qtyMap.put(d.getClistingid(), d.getIqty());
		}
		Double weight = freightService.getTotalWeight(qtyMap, false);
		Double shippingWeight = freightService.getTotalWeight(qtyMap, true);
		ShippingMethodRequst requst = new ShippingMethodRequst(
				storage.getIid(), dto.getCcountrysn(), weight, shippingWeight,
				languageID, dto.getFtotal(), listingIDs, isSpecial, currency,
				websiteID, hasAllFreeShipping(listingIDs));
		return shippingMethodService.getShippingMethodInformations(requst);
	}

	/**
	 * 所有免邮
	 * 
	 * @return
	 */
	private boolean hasAllFreeShipping(List<String> listingids) {
		// ~ 所有免邮
		List<String> allfp = productLabelService.getListByListingIdsAndType(
				listingids, ProductLabelType.AllFreeShipping.toString());
		return (allfp != null && allfp.size() > 0);
	}
}
