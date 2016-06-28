package services.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import services.product.ProductEnquiryService;
import services.shipping.ShippingServices;
import valueobjects.member.MemberInSession;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.price.Price;
import valueobjects.product.Weight;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import dto.Storage;
import dto.order.FullPreparatoryOrder;
import dto.order.PreparatoryDetail;
import dto.order.PreparatoryOrder;
import facades.cart.Cart;

public class PreparatoryOrderCreator {
	@Inject
	private ProductEnquiryService productEnquiry;
	@Inject
	private ShippingServices shippingServices;
	@Inject
	private IPreparatoryOrderService preparatoryOrderService;
	@Inject
	private IPreparatoryDetailService preparatoryDetailService;

	public List<FullPreparatoryOrder> create(Cart cart, int siteID, int langID,
			String currency, MemberInSession mis) {
		List<CartItem> items = cart.getAllItems();
		List<Weight> weights = productEnquiry.getWeightList(cart
				.getListingIDs());
		Map<String, Weight> weightMap = Maps.uniqueIndex(weights,
				w -> w.getListingId());
		Multimap<Integer, PreparatoryDetail> map = transformToDetail(items,
				weightMap);
		List<FullPreparatoryOrder> orders = transformToOrder(map, siteID,
				cart.getId(), mis.getEmail(), currency);
		return saveFullPreparatoryOrder(orders);
	}

	private List<FullPreparatoryOrder> saveFullPreparatoryOrder(
			List<FullPreparatoryOrder> orders) {
		orders.forEach(order -> {
			PreparatoryOrder o = order.getOrder();
			Collection<PreparatoryDetail> ds = order.getDetails();
			preparatoryOrderService.insert(o);
			ds.forEach(d -> d.setIorderid(o.getIid()));
			preparatoryDetailService.batchInsert(Lists.newArrayList(ds));
		});
		List<PreparatoryOrder> list = Lists.transform(orders,
				od -> od.getOrder());
		if (!list.isEmpty()) {
			List<Integer> ids = Lists.transform(list, o -> o.getIid());
			preparatoryOrderService.updateNoByIDs(ids, ids.get(0));
		}
		return orders;
	}

	public List<FullPreparatoryOrder> transformToOrder(
			Multimap<Integer, PreparatoryDetail> map, int siteID,
			String cartID, String email, String cy) {
		handleNullStorage(map, siteID);
		Collection<Entry<Integer, Collection<PreparatoryDetail>>> entries = map
				.asMap().entrySet();
		Collection<FullPreparatoryOrder> res = Collections2.transform(
				entries,
				entry -> {
					FullPreparatoryOrder order = createFullPreparatoryOrder(
							entry.getKey(), entry.getValue(), siteID, cartID,
							email, cy);
					return order;
				});
		return Lists.newArrayList(Collections2.filter(res, e -> e != null));
	}

	private void handleNullStorage(Multimap<Integer, PreparatoryDetail> map,
			int siteID) {
		Collection<PreparatoryDetail> details = map.get(null);
		if (!details.isEmpty()) {
			List<String> listingIDs = FluentIterable.from(details)
					.transform(d -> d.getClistingid()).toList();
			Storage storage = shippingServices.getShippingStorage(siteID,
					listingIDs);
			if (storage == null) {
				Logger.error(
						"PreparatoryOrderCreator.handleNullStorage Exception: storage == null>>> listingIDs: {}, siteID: {}",
						Json.toJson(listingIDs), siteID);
				throw new RuntimeException();
			}
			map.putAll(storage.getIid(), details);
			map.removeAll(null);
		}
	}

	private FullPreparatoryOrder createFullPreparatoryOrder(Integer storageID,
			Collection<PreparatoryDetail> details, int siteID, String cartID,
			String email, String cy) {
		PreparatoryOrder order = new PreparatoryOrder();
		order.setCcartid(cartID);
		order.setCmemberemail(email);
		order.setIstorageid(storageID);
		order.setIwebsiteid(siteID);
		order.setCcurrency(cy);
		return new FullPreparatoryOrder(order, details);
	}

	private Multimap<Integer, PreparatoryDetail> transformToDetail(
			List<CartItem> items, Map<String, Weight> weightMap) {
		Multimap<Integer, PreparatoryDetail> map = ArrayListMultimap.create();
		if (items == null || items.isEmpty()) {
			return map;
		}
		for (CartItem item : items) {
			if (item instanceof SingleCartItem) {
				addSingleItem((SingleCartItem) item, map, weightMap);
			} else if (item instanceof BundleCartItem) {
				addBundleItem((BundleCartItem) item, map, weightMap);
			}
		}
		return map;
	}

	private void addBundleItem(BundleCartItem item,
			Multimap<Integer, PreparatoryDetail> map,
			Map<String, Weight> weightMap) {
		for (SingleCartItem i : item.getChildList()) {
			PreparatoryDetail detail = null;
			Integer storageID = 1;// luojh 捆绑商品默认中国仓
			if (i.getBismain()) {
				// storageID = item.getStorageID();
				detail = singleItemToDetail(i, null,
						item.getIqty() * i.getIqty(), weightMap);
			} else {
				detail = singleItemToDetail(i, item.getCid(), item.getIqty()
						* i.getIqty(), weightMap);
			}
			if (detail != null) {
				map.put(storageID, detail);
			}
		}
	}

	private void addSingleItem(SingleCartItem item,
			Multimap<Integer, PreparatoryDetail> map,
			Map<String, Weight> weightMap) {
		PreparatoryDetail detail = singleItemToDetail(item, null,
				item.getIqty(), weightMap);
		if (detail != null) {
			map.put(item.getStorageID(), detail);
		}
	}

	private PreparatoryDetail singleItemToDetail(SingleCartItem item,
			String parentID, int qty, Map<String, Weight> weightMap) {
		PreparatoryDetail detail = new PreparatoryDetail();
		Price price = item.getPrice();
		Weight weight = weightMap.get(item.getClistingid());
		if (price == null || weight == null) {
			Logger.error(
					"Create PreparatoryOrder Exception cartItemID: {}, price: {}, weight: {}",
					item.getCid(), price, weight);
			return null;
		}
		detail.setCid(item.getCid());
		detail.setClistingid(item.getClistingid());
		detail.setCparentid(parentID);
		detail.setCsku(item.getSku());
		detail.setCtitle(item.getCtitle());
		detail.setForiginalprice(price.getUnitBasePrice());
		detail.setFprice(price.getUnitPrice());
		detail.setFtotalprices(price.getPrice());
		detail.setIqty(qty);
		detail.setFweight(weight.getWeight());
		detail.setBismain(item.getBismain());
		return detail;
	}
}
