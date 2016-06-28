package facades.cart;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.Logger;
import play.twirl.api.Html;
import services.base.utils.DoubleCalculateUtils;
import services.cart.ICartService;
import services.cart.IExtraLineService;
import services.price.PriceUtils;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.CartOwner;
import valueobjects.order_api.cart.ExtraLine;
import valueobjects.order_api.cart.IExtraLineRule;
import valueobjects.order_api.cart.NonExistenceBundleCartItem;
import valueobjects.order_api.cart.NonExistenceCartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.price.Price;
import valueobjects.price.PriceOnly;

import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import extensions.order.IOrderExtrasProvider;

public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject()
	transient ICartService cartService;

	@Inject(optional = true)
	transient Set<IOrderExtrasProvider> extrasProviders;

	@Inject()
	transient IExtraLineService extraLineService;

	// add by lijun
	@Inject(optional = true)
	transient Set<IExtraLineRule> rules;

	/**
	 * Cart ID
	 */
	final String id;

	final String ccy;

	final int siteID;

	final int languageID;

	private Map<String, CartItem> itemMap;

	private Map<String, ExtraLine> extraLinesMap;

	// add by lijun
	// 已经转换成订单了
	private boolean alreadyTransformOrder;

	private Double allPrefer;

	public Cart(String id, int siteID, int languageID, String ccy) {
		super();
		this.id = id;
		this.ccy = ccy;
		this.siteID = siteID;
		this.languageID = languageID;
	}

	public Double getAllPrefer() {
		return allPrefer;
	}

	public void setAllPrefer(Double allPrefer) {
		this.allPrefer = allPrefer;
	}

	public boolean isAlreadyTransformOrder() {
		return alreadyTransformOrder;
	}

	public void setAlreadyTransformOrder(boolean alreadyTransformOrder) {
		this.alreadyTransformOrder = alreadyTransformOrder;
	}

	public Map<String, Object> addItem(CartItem item) {
		List<CartItem> existingItems = getAllItems();
		CartItemComparator cc = new CartItemComparator();
		Collection<CartItem> dups = Collections2.filter(existingItems,
				i -> cc.compare(i, item) == 0);
		Logger.debug("Dups size: {}", dups.size());
		if (dups.size() == 0) {
			Map<String, Object> res = cartService.addItem(id, item);
			itemMap.put(item.getCid(), item);
			return res;
		} else if (dups.size() > 0) {
			CartItem ci = dups.iterator().next();
			CartItem prev = itemMap.get(ci.getCid());
			if (prev != null) {
				prev.setIqty(prev.getIqty() + ci.getIqty());
			}
			return cartService.addItemQty(ci.getCid(), item.getIqty());
		}
		return null;
	}

	public boolean deleteItem(String itemID) {
		boolean flag = cartService.delCartByCid(itemID);
		if (itemMap != null) {
			itemMap.remove(itemID);
		}
		return flag;
	}

	public boolean addItemHistory(List<String> listingIDs, String email,
			String uuid) {
		boolean flag = cartService.addItemHistory(listingIDs, email, uuid);
		return flag;
	}

	public Map<String, Object> updateItemQty(String itemID, int qty) {
		return cartService.updateItemQty(itemID, qty);
	}

	public List<CartItem> getAllItems() {
		initialize();
		return Lists.newArrayList(itemMap.values());
	}

	public List<String> getListingIDs() {
		List<List<String>> ids = Lists.transform(
				getAllItems(),
				i -> {
					List<String> listingIds = Lists.newArrayList();
					if (i instanceof SingleCartItem) {
						listingIds.add(i.getClistingid());
					} else if (i instanceof BundleCartItem) {
						List<String> childIds = Lists.transform(
								((BundleCartItem) i).getChildList(),
								cl -> cl.getClistingid());
						listingIds.addAll(childIds);
					}
					return listingIds;
				});
		List<String> flatten = Lists.newArrayList();
		for (List<String> l : ids) {
			flatten.addAll(l);
		}
		return flatten;
	}

	public List<String> getListingIDsByItemID(String itemID) {
		List<List<String>> ids = Lists.transform(
				getAllItems(),
				i -> {
					List<String> listingIds = Lists.newArrayList();
					if (i.getCid().equals(itemID)) {
						if (i instanceof SingleCartItem) {
							listingIds.add(i.getClistingid());
						} else if (i instanceof BundleCartItem) {
							List<String> childIds = Lists.transform(
									((BundleCartItem) i).getChildList(),
									cl -> cl.getClistingid());
							listingIds.addAll(childIds);
						} else if (i instanceof NonExistenceBundleCartItem) {
							List<String> childIds = Lists.transform(
									((NonExistenceBundleCartItem) i)
											.getChildList(), cl -> cl
											.getClistingid());
							listingIds.addAll(childIds);
						}
					}
					return listingIds;
				});
		List<String> flatten = Lists.newArrayList();
		for (List<String> l : ids) {
			flatten.addAll(l);
		}
		return flatten;
	}

	public void clear() {
		cartService.delCartItemByCartID(id);
		extraLineService.deleteExtraLineByCartId(id);
	}

	public String addExtraLine(ExtraLine line) {
		/*
		 * @see IExtraLineRule 优惠券如果用过了就不能用推广码 add by lijun
		 */
		// 阻断所有
		List<IExtraLineRule> exclusive = FluentIterable
				.from(rules)
				.filter(c -> {
					if (IExtraLineRule.RuleType.EXCLUDE_ALL == c.getRuleType()) {
						return true;
					}
					return false;
				}).toList();

		if (exclusive.size() > 0) {
			for (IExtraLineRule er : exclusive) {
				if (this.getExtraLines().containsKey(er.getPluginId())) {
					throw new RuntimeException("pluginId:" + er.getPluginId()
							+ " break your operation");
				}
			}
		}
		// 阻断部分
		List<IExtraLineRule> exclusivePart = FluentIterable
				.from(rules)
				.filter(c -> {
					if (IExtraLineRule.RuleType.EXCLUDE_PART == c.getRuleType()) {
						return true;
					}
					return false;
				}).toList();

		if (exclusivePart.size() > 0) {
			for (IExtraLineRule er : exclusivePart) {
				if (this.getExtraLines().containsKey(er.getPluginId())
						&& er.getExcludePluginIDs() != null
						&& er.getExcludePluginIDs()
								.contains(line.getPluginId())) {
					throw new RuntimeException("pluginId:" + er.getPluginId()
							+ " break your operation");
				}
			}
		}

		return extraLineService.addExtraLine(id, line);
	}

	public boolean validExtraLine(String extraId) {
		return extraLineService.validExtraLine(id, extraId);
	}

	public boolean delExtraLine(String extraId) {
		return extraLineService.deleteByCartIdAndPluginId(id, extraId);
	}

	public Map<String, ExtraLine> getExtraLines() {
		initialize();
		return extraLinesMap;
	}

	public List<ExtraLineView> convertExtras(Class<?> clazz) {
		List<IOrderExtrasProvider> extrasSorted = Lists
				.newArrayList(extrasProviders);
		Collections.sort(extrasSorted,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		List<IOrderExtrasProvider> list = Lists.newArrayList(Collections2
				.filter(extrasSorted, e -> !(e.getClass().equals(clazz))));
		List<ExtraLineView> unfiltered = Lists.transform(list,
				e -> e.extralineView(this, getExtraLines().get(e.getId())));
		return Lists.newArrayList(Iterables.filter(unfiltered, o -> o != null));
	}

	public List<Html> renderExtrasInput() {
		List<IOrderExtrasProvider> extrasSorted = Lists
				.newArrayList(extrasProviders);
		Collections.sort(extrasSorted,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		return Lists.transform(extrasSorted,
				e -> e.renderInput(this, getExtraLines().get(e.getId())));
	}

	public double getBaseTotal() {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(getTotal());
		return duti.subtract(getSaveTotal()).doubleValue();
	}

	public double getTotal() {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
		for (CartItem ci : getAllItems()) {
			if (ci instanceof SingleCartItem) {
				duti = duti.add(ci.getPrice().getPrice());
			} else if (ci instanceof BundleCartItem) {
				duti = duti.add(ci.getPrice().getPrice());
			}
		}
		return duti.doubleValue();
	}

	public PriceOnly getTotalPrice() {
		List<Price> prices = FluentIterable.from(getAllItems())
				.transform(ci -> {
					if (ci instanceof SingleCartItem) {
						return ci.getPrice();
					} else if (ci instanceof BundleCartItem) {
						return ci.getPrice();
					}
					return null;
				}).toList();
		List<Price> list = Lists.newArrayList(Collections2.filter(prices,
				p -> p != null));
		return PriceUtils.addAll(list);
	}

	public double getGrandTotal() {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(getTotal());
		for (ExtraLineView b : convertExtras()) {
			duti = duti.add(b.getMoney());
		}
		double result = duti.doubleValue();
		return result < 0.0 ? 0.0 : result;
	}

	public double getGrandTotal(Class<?> clazz) {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(getTotal());
		for (ExtraLineView b : convertExtras(clazz)) {
			duti = duti.add(b.getMoney());
		}
		return duti.doubleValue();
	}

	public List<ExtraLineView> convertExtras() {
		return cartService.convertExtras(this);
	}

	public double getSaveTotal() {
		DoubleCalculateUtils dcu = new DoubleCalculateUtils(0);
		for (CartItem ci : getAllItems()) {
			Price price = null;
			if (ci instanceof SingleCartItem) {
				price = ci.getPrice();
			} else if (ci instanceof BundleCartItem) {
				price = ci.getPrice();
			}
			if (null != price) {
				dcu = dcu.add(price.getUnitBasePrice() - price.getUnitPrice())
						.multiply(ci.getIqty());
			}
		}
		return dcu.multiply(-1).doubleValue();
	}

	public int getSum() {
		int sum = 0;
		for (CartItem ci : getAllItems()) {
			if (ci instanceof SingleCartItem) {
				sum += ci.getIqty();
			} else if (ci instanceof BundleCartItem) {
				sum += ci.getIqty();
			}
		}
		return sum;
	}

	public String getId() {
		return id;
	}

	public CartOwner getOwner() {
		return cartService.getCartOwner(id);
	}

	protected void initialize() {
		if (itemMap == null) {
			List<CartItem> items = cartService.getCartItemsByCartId(id, siteID,
					languageID, ccy, false);
			this.itemMap = Maps.newLinkedHashMap(Maps.uniqueIndex(items,
					i -> i.getCid()));
		}
		if (extraLinesMap == null) {
			extraLinesMap = extraLineService.getExtraLinesByCartId(id);
		}
	}

	public boolean readyForCheckout() {
		return cartService.readyForCheckout(id);
	}

	public boolean checkInventory() {
		List<CartItem> list = getAllItems();
		boolean res = true;
		for (CartItem cartItem : list) {
			if (!cartService.checkItemQty(cartItem, null)) {
				res = false;
			}
		}
		return res;
	}

	public List<String> checkCart() {
		List<CartItem> list = getAllItems();
		List<String> res = Lists.newArrayList();
		for (CartItem cartItem : list) {
			if (cartItem instanceof SingleCartItem
					|| cartItem instanceof NonExistenceCartItem) {
				if (!cartService.checkItemListQty(cartItem, null)) {
					res.add(cartItem.getCid() + "_" + cartItem.getClistingid());
				}
			} else if (cartItem instanceof NonExistenceBundleCartItem) {
				NonExistenceBundleCartItem nbci = (NonExistenceBundleCartItem) cartItem;
				List<CartItem> cis = nbci.getChildList();
				int qty = nbci.getIqty();
				for (CartItem ci : cis) {
					if (!cartService.checkItemListQty(ci, qty * ci.getIqty())) {
						res.add(ci.getCid() + "_" + ci.getClistingid());
					}
				}
			} else if (cartItem instanceof BundleCartItem) {
				BundleCartItem bci = (BundleCartItem) cartItem;
				List<SingleCartItem> cis = bci.getChildList();
				int qty = bci.getIqty();
				for (CartItem ci : cis) {
					if (!cartService.checkItemListQty(ci, qty * ci.getIqty())) {
						res.add(ci.getCid() + "_" + ci.getClistingid());
					}
				}
			}
		}
		return res;
	}

	/**
	 * 绘制优惠小计
	 * 
	 * @author lijun
	 * @return
	 */
	public List<Html> renderOrderSubtotal() {
		List<IOrderExtrasProvider> extrasSorted = Lists
				.newArrayList(extrasProviders);
		Collections.sort(extrasSorted,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		return Lists.transform(extrasSorted, e -> e.renderOrderSubtotal(this,
				getExtraLines().get(e.getId())));
	}

	/**
	 * 只为移动端老购物车流程调用
	 * 
	 * @author xiaoch
	 * @return
	 */
	public double getGrandTotalTerminal() {
		DoubleCalculateUtils duti = new DoubleCalculateUtils(getTotal());
		for (ExtraLineView b : convertExtras()) {
			duti = duti.add(b.getMoney());
		}
		Double allPrefer = this.getAllPrefer();
		if (null != allPrefer && allPrefer < 0) {
			duti = duti.add(allPrefer);
		}
		double result = duti.doubleValue();
		return result < 0.0 ? 0.0 : result;
	}
}
