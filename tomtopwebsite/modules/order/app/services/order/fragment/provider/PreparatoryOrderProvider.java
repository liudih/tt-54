package services.order.fragment.provider;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.order.FreightService;
import services.order.IOrderFragmentProvider;
import services.shipping.ShippingMethodService;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.PreparatoryOrderListVO;
import valueobjects.order_api.ShippingMethodInformations;
import valueobjects.order_api.shipping.ShippingMethodRequst;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.order.FullPreparatoryOrder;
import dto.order.PreparatoryDetail;
import dto.order.PreparatoryOrder;

public class PreparatoryOrderProvider implements IOrderFragmentProvider {
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private FreightService freightSerivce;

	@SuppressWarnings("unchecked")
	@Override
	public IOrderFragment getFragment(OrderContext context) {
		List<FullPreparatoryOrder> orders = (List<FullPreparatoryOrder>) context
				.get("orders");
		if (orders == null || orders.isEmpty()) {
			return null;
		}
		PreparatoryOrderListVO vo = new PreparatoryOrderListVO();
		vo.setOrders(orders);
		Map<Integer, ShippingMethodInformations> infoMap = Maps.newHashMap();
		Map<Integer, Double> subTotalMap = Maps.newHashMap();
		for (FullPreparatoryOrder order : orders) {
			ShippingMethodRequst requst = createRequset(order, context);
			ShippingMethodInformations infos;
			if (requst.getCountry() != null) {
				infos = shippingMethodService
						.getShippingMethodInformations(requst);
			} else {
				infos = new ShippingMethodInformations(Lists.newArrayList());
			}
			infoMap.put(order.getOrder().getIid(), infos);
			subTotalMap.put(order.getOrder().getIid(), requst.getGrandTotal());
		}
		vo.setShippingInfoMap(infoMap);
		vo.setSubTotalMap(subTotalMap);
		return vo;
	}

	private ShippingMethodRequst createRequset(FullPreparatoryOrder order,
			OrderContext context) {
		if (context.getCountry() == null) {
			return createRequset(order.getOrder(), order.getDetails(), null,
					context.getLangID());
		}
		return createRequset(order.getOrder(), order.getDetails(), context
				.getCountry().getCshortname(), context.getLangID());
	}

	public ShippingMethodRequst createRequset(PreparatoryOrder o,
			Collection<PreparatoryDetail> ds, String countrySN, int langID) {
		ShippingMethodRequst requst = new ShippingMethodRequst();
		Map<String, Integer> qtyMap = createQtyMap(ds);
		List<String> listingIDs = Lists.newArrayList(qtyMap.keySet());
		Double weight = 0.0;
		Double shippingWeight = 0.0;
		Double grandTotal = 0.0;
		if (!qtyMap.isEmpty()) {
			for (PreparatoryDetail d : ds) {
				weight += d.getFweight() * d.getIqty();
				grandTotal += d.getFtotalprices();
			}
			shippingWeight = freightSerivce.getTotalWeight(qtyMap, true);
		}
		requst.setCountry(countrySN);
		requst.setCurrency(o.getCcurrency());
		requst.setGrandTotal(grandTotal);
		requst.setIsSpecial(shippingMethodService.isSpecial(listingIDs));
		requst.setLang(langID);
		requst.setListingIds(listingIDs);
		requst.setShippingWeight(shippingWeight);
		requst.setStorageId(o.getIstorageid());
		requst.setWebsiteId(o.getIwebsiteid());
		requst.setWeight(weight);
		return requst;
	}

	private Map<String, Integer> createQtyMap(Collection<PreparatoryDetail> ds) {
		Map<String, Integer> map = Maps.newHashMap();
		if (ds != null && !ds.isEmpty()) {
			for (PreparatoryDetail d : ds) {
				if (map.get(d.getClistingid()) != null) {
					map.put(d.getClistingid(),
							map.get(d.getClistingid()) + d.getIqty());
				} else {
					map.put(d.getClistingid(), d.getIqty());
				}
			}
		}
		return map;
	}

	@Override
	public IOrderFragment getExistingFragment(ExistingOrderContext context) {
		return null;
	}

}
