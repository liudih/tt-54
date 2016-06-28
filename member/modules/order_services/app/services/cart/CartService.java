package services.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mapper.cart.CartBaseMapper;
import mapper.cart.CartItemHistoryMapper;
import mapper.cart.CartItemListAdditionMapper;
import mapper.cart.CartItemListMapper;
import mapper.cart.CartItemMapper;
import mapper.product.ProductBaseMapper;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.collect.Lists;
import org.mybatis.guice.transactional.Transactional;

import play.Logger;
import play.mvc.Http.Context;
import services.common.UUIDGenerator;
import services.member.login.LoginService;
import services.price.PriceService;
import services.product.EntityMapService;
import services.product.ProductEnquiryService;
import services.product.ProductValidation;
import services.product.inventory.IInventoryEnquiryService;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.cart.BundleCartItem;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.CartItemType;
import valueobjects.order_api.cart.CartOwner;
import valueobjects.order_api.cart.NonExistenceBundleCartItem;
import valueobjects.order_api.cart.NonExistenceCartItem;
import valueobjects.order_api.cart.SingleCartItem;
import valueobjects.price.BundlePrice;
import valueobjects.price.Price;
import valueobjects.product.ProductLite;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import dto.cart.CartBase;
import dto.cart.CartItemHistory;
import dto.cart.CartItemList;
import dto.cart.CartItemListAddition;
import extensions.order.IOrderExtrasProvider;
import facades.cart.Cart;

public class CartService implements ICartService, Serializable {
	private static final long serialVersionUID = 1L;
	@Inject
	CartBaseMapper cartBaseMapper;
	@Inject
	CartItemMapper cartItemMapper;
	@Inject
	CartItemListMapper cartItemListMapper;
	@Inject
	ProductEnquiryService productEnquiryService;
	@Inject
	PriceService priceService;
	@Inject
	LoginService loginService;
	@Inject
	CartItemHistoryMapper cartItemHistoryMapper;
	@Inject
	CartItemListAdditionMapper cartItemListAdditionMapper;
	@Inject
	ProductBaseMapper productBaseMapper;
	@Inject
	ProductValidation productValidation;
	@Inject
	IInventoryEnquiryService inventoryService;
	@Inject
	EntityMapService entityMapService;
	@Inject(optional = true)
	transient Set<IOrderExtrasProvider> extrasProviders;

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#getCartItemsByCartId(java.lang.String,
	 * int, int, java.lang.String, boolean)
	 */
	@Override
	public List<CartItem> getCartItemsByCartId(String cartId, int siteID,
			int languageID, String ccy, boolean generateorders) {
		// first: prepare listing ID, qty
		List<dto.cart.CartItem> cartItem = cartItemMapper
				.getCartItemsByCid(cartId);
		List<CartItemList> cartItemList = cartItemListMapper
				.getCartItemListByCartID(cartId, generateorders);
		Multimap<String, CartItemList> cartItemListIndex = Multimaps.index(
				cartItemList, cilist -> cilist.getCcartitemid());
		Map<String, dto.cart.CartItem> cartItemIndex = Maps.uniqueIndex(
				cartItem, i -> i.getCid());
		Map<String, CartItem> result = Maps.transformValues(cartItemIndex,
				ci -> convertFromEntity(ci, cartItemListIndex, ccy));

		// second: prepare product information / pricing
		Map<String, List<String>> idMap = Maps.transformValues(result, (
				CartItem civo) -> {
			if (civo instanceof SingleCartItem) {
				return Lists.newArrayList(civo.getClistingid());
			} else if (civo instanceof BundleCartItem) {
				return Lists.transform(((BundleCartItem) civo).getChildList(),
						c -> c.getClistingid());
			} else if (civo instanceof NonExistenceCartItem) {
				return Lists.newArrayList(civo.getClistingid());
			} else if (civo instanceof NonExistenceBundleCartItem) {
				return Lists.transform(
						((NonExistenceBundleCartItem) civo).getChildList(),
						c -> c.getClistingid());
			}
			throw new RuntimeException("Not Single/Bundle Cart Item Found!");
		});
		Set<String> ids = Sets.newHashSet();
		for (Collection<String> i : idMap.values()) {
			ids.addAll(i);
		}
		List<String> distinctIds = Lists.newLinkedList(ids);
		List<ProductLite> products = productEnquiryService
				.getProductLiteByListingIDs(distinctIds, siteID, languageID);
		products = ImmutableSet.copyOf(products).asList();
		Map<String, ProductLite> productMap = Maps.uniqueIndex(products,
				p -> p.getListingId());

		Map<String, CartItem> resultDetails = Maps
				.transformValues(
						result,
						ci -> {
							ProductLite p = productMap.get(ci.getClistingid());
							if (p == null) {
								return ci;
							}
							ci.setCtitle(p.getTitle());
							ci.setCurl(p.getUrl());
							ci.setCimageurl(p.getImageUrl());
							ci.setSku(p.getSku());
							Map<String, String> attributeMap = entityMapService
									.getAttributeMap(ci.getClistingid(),
											languageID);
							ci.setAttributeMap(attributeMap);
							CartItemListAddition cil = cartItemListAdditionMapper
									.getCartItemListAdditionByItemIdAndListingId(
											ci.getCid(), ci.getClistingid());
							if (cil != null && cil.getStatus() == 0) {
								ci.setAddition(cil.getDescription());
							}
							if (ci instanceof BundleCartItem) {
								BundleCartItem bi = (BundleCartItem) ci;
								bi.setChildList(Lists.transform(
										bi.getChildList(),
										sci -> {
											ProductLite cp = productMap.get(sci
													.getClistingid());
											sci.setCtitle(cp.getTitle());
											sci.setCurl(cp.getUrl());
											sci.setCimageurl(cp.getImageUrl());
											sci.setSku(cp.getSku());
											Map<String, String> attributeMap2 = entityMapService.getAttributeMap(
													sci.getClistingid(),
													languageID);
											sci.setAttributeMap(attributeMap2);
											CartItemListAddition cil2 = cartItemListAdditionMapper
													.getCartItemListAdditionByItemIdAndListingId(
															sci.getCid(),
															sci.getClistingid());
											if (cil2 != null
													&& cil2.getStatus() == 0) {
												sci.setAddition(cil2
														.getDescription());
											}
											return sci;
										}));
							}
							if (ci instanceof NonExistenceBundleCartItem) {
								NonExistenceBundleCartItem bi = (NonExistenceBundleCartItem) ci;
								bi.setChildList(Lists.transform(
										bi.getChildList(),
										sci -> {
											ProductLite cp = productMap.get(sci
													.getClistingid());
											sci.setCtitle(cp.getTitle());
											sci.setCurl(cp.getUrl());
											sci.setCimageurl(cp.getImageUrl());
											sci.setSku(cp.getSku());
											Map<String, String> attributeMap3 = entityMapService.getAttributeMap(
													sci.getClistingid(),
													languageID);
											sci.setAttributeMap(attributeMap3);
											CartItemListAddition cil2 = cartItemListAdditionMapper
													.getCartItemListAdditionByItemIdAndListingId(
															sci.getCid(),
															sci.getClistingid());
											if (cil2 != null
													&& cil2.getStatus() == 0) {
												sci.setAddition(cil2
														.getDescription());
											}
											return sci;
										}));
							}
							// XXX ci.setPrice(priceService.);
							return ci;
						});
		List<CartItem> resultItems = Lists.newArrayList(resultDetails.values());
		Collections.sort(resultItems,
				(a, b) -> a.getDcreatedate().compareTo(b.getDcreatedate()));
		return resultItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.cart.ICartService#getCartItemsLiteByCartId(java.lang.String)
	 */
	@Override
	public List<CartItem> getCartItemsLiteByCartId(String cartId) {
		// first: prepare listing ID, qty
		List<dto.cart.CartItem> cartItem = cartItemMapper
				.getCartItemsByCid(cartId);
		List<CartItemList> cartItemList = cartItemListMapper
				.getCartItemListByCartID(cartId, false);
		Multimap<String, CartItemList> cartItemListIndex = Multimaps.index(
				cartItemList, cilist -> cilist.getCcartitemid());
		Map<String, dto.cart.CartItem> cartItemIndex = Maps.uniqueIndex(
				cartItem, i -> i.getCid());
		Map<String, CartItem> result = Maps.transformValues(cartItemIndex,
				ci -> convertFromEntityLite(ci, cartItemListIndex));
		List<CartItem> resultItems = Lists.newArrayList(result.values());
		return resultItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#delCartByCid(java.lang.String)
	 */
	@Override
	@Transactional
	public boolean delCartByCid(String id) {
		// int flag = cartItemListMapper.delAllItemListByCartItemID(id);
		int flag2 = cartItemMapper.delItemByCartItemID(id);
		return (flag2 > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#delCartItemByCartID(java.lang.String)
	 */
	@Override
	@Transactional
	public void delCartItemByCartID(String id) {
		cartItemListMapper.delByCartID(id);
		cartItemMapper.delByCartID(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#addItemHistory(java.util.List,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public boolean addItemHistory(List<String> listingIDS, String email,
			String uuid) {
		for (String listingid : listingIDS) {
			CartItemHistory c = new CartItemHistory();
			c.setCmemberemail(email);
			c.setClistingid(listingid);
			c.setCuuid(uuid);
			c.setDcreatedate(new Date());
			int flag = cartItemHistoryMapper.insert(c);
			if (flag < 1)
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#addItem(java.lang.String,
	 * valueobjects.order_api.cart.CartItem)
	 */
	@Override
	@Transactional
	public Map<String, Object> addItem(String cartId, CartItem cartItem) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("res", "error");
		dto.cart.CartItem item = new dto.cart.CartItem();
		item.setCcartbaseid(cartId);
		item.setCdevice(cartItem.getDevice());
		item.setIqty(cartItem.getIqty());
		String lineID = UUIDGenerator.createAsString();
		item.setCid(lineID);
		// XXX BAD style, consider it as a return value
		if (cartItem.getCid() == null) {
			cartItem.setCid(lineID);
		}
		item.setDcreatedate(new Date());
		List<CartItemList> itemList = Lists.newArrayList();
		List<String> listingids = new ArrayList<String>();
		if (cartItem instanceof BundleCartItem) {
			item.setIitemtype(CartItemType.Bundle.value());
			for (SingleCartItem b : ((BundleCartItem) cartItem).getChildList()) {
				CartItemList cl = new CartItemList();
				cl.setClistingid(b.getClistingid());
				cl.setBismain(b.getBismain());
				cl.setIqty(b.getIqty());
				cl.setCcartitemid(item.getCid());
				cl.setIstorageid(b.getStorageID());
				itemList.add(cl);
				listingids.add(b.getClistingid());
			}
		} else if (cartItem instanceof SingleCartItem) {
			item.setIitemtype(CartItemType.Single.value());
			CartItemList cl = new CartItemList();
			cl.setClistingid(cartItem.getClistingid());
			cl.setBismain(cartItem.getBismain());
			cl.setIqty(cartItem.getIqty());
			cl.setCcartitemid(item.getCid());
			cl.setIstorageid(cartItem.getStorageID());
			itemList.add(cl);
			listingids.add(cartItem.getClistingid());
		}
		// 判断库存
		boolean isEnough = isEnoughQty(null, cartItem.getIqty(), listingids);
		if (!isEnough) {
			map.put("res", "notenough");
			return map;
		}

		int flag = cartItemMapper.insertSelective(item);
		if (flag > 0) {
			map.put("res", "success");
		}
		for (CartItemList il : itemList) {
			cartItemListMapper.insert(il);
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#addItemQty(java.lang.String,
	 * java.lang.Integer)
	 */
	@Override
	@Transactional
	public Map<String, Object> addItemQty(String cartItemID, Integer qty) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("res", "error");
		dto.cart.CartItem cartItem = cartItemMapper
				.selectByPrimaryKey(cartItemID);
		// 判断库存
		boolean isEnough = isEnoughQty(cartItemID, (cartItem.getIqty() + qty),
				null);
		if (!isEnough) {
			map.put("res", "notenough");
			return map;
		}
		if (cartItem != null) {
			// 限定加入购物车后的数量最多为9999
			Integer totalQty = cartItem.getIqty() + qty < 9999 ? cartItem
					.getIqty() + qty : 9999;
			cartItem.setIqty(totalQty);
			int flag = cartItemMapper.updateByPrimaryKeySelective(cartItem);
			if (flag > 0) {
				map.put("res", "success");
			}
		}
		return map;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#updateItemQty(java.lang.String, int)
	 */
	@Override
	@Transactional
	public Map<String, Object> updateItemQty(String itemID, int qty) {
		Map<String, Object> map = new HashMap<String, Object>();
		dto.cart.CartItem cartItem = cartItemMapper.selectByPrimaryKey(itemID);
		map.put("oldnum", cartItem.getIqty());
		map.put("res", "error");
		// 判断库存
		boolean isEnough = isEnoughQty(itemID, qty, null);
		if (!isEnough) {
			map.put("res", "notenough");
			return map;
		}
		if (cartItem != null) {
			cartItem.setIqty(qty);
			int flag = cartItemMapper.updateByPrimaryKeySelective(cartItem);
			if (flag > 0) {
				map.put("res", "success");
			}
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#isEnoughQty(java.lang.String,
	 * java.lang.Integer, java.util.List)
	 */
	@Override
	public boolean isEnoughQty(String itemID, Integer qty,
			List<String> listingids) {
		boolean isEnough = true;
		List<String> listings = Lists.newArrayList();
		if (itemID != null) {
			List<CartItemList> itemlists = cartItemListMapper
					.getCartItemListByCartItemId(itemID);
			listings = Lists.transform(itemlists, list -> list.getClistingid());
		} else {
			listings = listingids;
		}
		for (String lID : listings) {
			if (!inventoryService.checkInventory(lID, qty)) {
				isEnough = false;
				break;
			}
		}
		return isEnough;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#getPriceByCartItemId(java.lang.String,
	 * int)
	 */
	@Override
	public Map<String, Object> getPriceByCartItemId(String id, int qty) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CartItemList> clist = cartItemListMapper
				.getCartItemListByCartItemId(id);
		if (clist.size() == 1) {
			IProductSpec spec = ProductSpecBuilder
					.build(clist.get(0).getClistingid()).setQty(qty).get();
			map.put("main", priceService.getPrice(spec));
			map.put("qty", 1);
		} else if (clist.size() > 1) {
			List<CartItemList> main = Lists.newArrayList(Collections2.filter(
					clist, li -> li.getBismain()));
			CartItemList mainProduct = main.iterator().next();
			Iterable<String> childIDs = Lists.transform(clist,
					li -> li.getClistingid());
			IProductSpec spec = ProductSpecBuilder
					.build(mainProduct.getClistingid()).bundleWith(childIDs)
					.setQty(qty).get();
			BundlePrice p = (BundlePrice) priceService.getPrice(spec);
			map.put("main", p);
			Map<String, Price> mapprice1 = Maps.uniqueIndex(p.getBreakdown(),
					objprice -> {
						return objprice.getListingId();
					});
			map.putAll(mapprice1);
			map.put("qty", clist.size());
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#convertFromEntity(dto.cart.CartItem,
	 * com.google.common.collect.Multimap, java.lang.String)
	 */
	@Override
	public CartItem convertFromEntity(dto.cart.CartItem ci,
			Multimap<String, CartItemList> cartItemListIndex, String ccy) {
		List<CartItemList> listItems = Lists.newArrayList(cartItemListIndex
				.get(ci.getCid()));
		if (ci.getIitemtype() == CartItemType.Single.value()) {
			if (listItems.size() != 1) {
				throw new RuntimeException(
						"Data Inconsistent, SingleCarItem has "
								+ listItems.size() + " list records");
			}
			CartItemList cil = listItems.iterator().next();
			SingleCartItem si = new SingleCartItem();
			si.setIqty(ci.getIqty());
			si.setCid(ci.getCid());
			si.setClistingid(cil.getClistingid());
			si.setStorageID(cil.getIstorageid());
			IProductSpec spec = ProductSpecBuilder.build(si.getClistingid())
					.setQty(ci.getIqty()).get();
			si.setPrice(priceService.getPrice(spec, ccy));
			si.setDcreatedate(ci.getDcreatedate());
			boolean valStatus = productValidation.valStatus(si.getClistingid());
			if (si.getPrice() == null || !valStatus) {
				Logger.info(
						"Create NonExistenceCartItem listingId: {}, price: {}, valStatus: {}",
						si.getClistingid(), si.getPrice(), valStatus);
				return new NonExistenceCartItem(si);
			}
			return si;
		} else if (ci.getIitemtype() == CartItemType.Bundle.value()) {
			if (listItems.size() <= 1) {
				throw new RuntimeException(
						"Data Inconsistent, BundleCarItem has "
								+ listItems.size() + " list records");
			}
			Boolean hasWrong = false;
			BundleCartItem bi = new BundleCartItem();
			bi.setIqty(ci.getIqty());
			bi.setCid(ci.getCid());
			bi.setDcreatedate(ci.getDcreatedate());
			List<CartItemList> main = Lists.newArrayList(Collections2.filter(
					listItems, li -> li.getBismain()));
			if (main.size() != 1) {
				throw new RuntimeException(
						"Data Inconsistent, BundleCartItem has " + main.size()
								+ " main products");
			}
			CartItemList mainProduct = main.iterator().next();

			bi.setClistingid(mainProduct.getClistingid());
			Iterable<String> childIDs = Lists.transform(listItems,
					li -> li.getClistingid());
			IProductSpec spec = ProductSpecBuilder
					.build(mainProduct.getClistingid()).bundleWith(childIDs)
					.setQty(ci.getIqty()).get();
			bi.setPrice(priceService.getPrice(spec, ccy));
			bi.setStorageID(mainProduct.getIstorageid());
			BundlePrice p = (BundlePrice) bi.getPrice();

			Map<String, Price> mapprice1 = null;
			if (p == null) {
				hasWrong = true;
			} else {
				mapprice1 = Maps.uniqueIndex(p.getBreakdown(), objprice -> {
					return objprice.getListingId();
				});
			}
			List<CartItem> list = Lists.newArrayList();
			for (CartItemList li : listItems) {
				SingleCartItem si = new SingleCartItem();
				si.setCid(ci.getCid());
				si.setClistingid(li.getClistingid());
				si.setIqty(li.getIqty());
				si.setStorageID(li.getIstorageid());
				si.setBismain(li.getBismain());
				if (null != mapprice1)
					si.setPrice(mapprice1.get(li.getClistingid()));
				boolean valStatus = productValidation.valStatus(si
						.getClistingid());
				if (si.getPrice() == null || !valStatus) {
					Logger.info(
							"Create NonExistenceCartItem listingId: {}, price: {}, valStatus: {}",
							si.getClistingid(), si.getPrice(), valStatus);
					hasWrong = true;
					list.add(new NonExistenceCartItem(si));
				} else {
					list.add(si);
				}
			}
			if (!hasWrong) {
				bi.setChildList(Lists.transform(list, c -> (SingleCartItem) c));
				return bi;
			} else {
				NonExistenceBundleCartItem nebci = new NonExistenceBundleCartItem();
				nebci.setIqty(bi.getIqty());
				nebci.setCid(bi.getCid());
				nebci.setDcreatedate(bi.getDcreatedate());
				nebci.setChildList(list);
				nebci.setPrice(bi.getPrice());
				nebci.setClistingid(bi.getClistingid());
				boolean valStatus = productValidation.valStatus(nebci
						.getClistingid());
				if (nebci.getPrice() == null || !valStatus) {
					nebci.setExist(false);
				} else {
					nebci.setExist(true);
				}
				return nebci;
			}
		}
		throw new RuntimeException("Cart Item Type not valid: "
				+ ci.getIitemtype());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#convertFromEntityLite(dto.cart.CartItem,
	 * com.google.common.collect.Multimap)
	 */
	@Override
	public CartItem convertFromEntityLite(dto.cart.CartItem ci,
			Multimap<String, CartItemList> cartItemListIndex) {
		List<CartItemList> listItems = Lists.newArrayList(cartItemListIndex
				.get(ci.getCid()));
		if (ci.getIitemtype() == CartItemType.Single.value()) {
			if (listItems.size() != 1) {
				throw new RuntimeException(
						"Data Inconsistent, SingleCarItem has "
								+ listItems.size() + " list records");
			}
			SingleCartItem si = new SingleCartItem();
			si.setIqty(ci.getIqty());
			si.setCid(ci.getCid());
			si.setClistingid(listItems.iterator().next().getClistingid());
			si.setDcreatedate(ci.getDcreatedate());
			return si;
		} else if (ci.getIitemtype() == CartItemType.Bundle.value()) {
			if (listItems.size() <= 1) {
				throw new RuntimeException(
						"Data Inconsistent, BundleCarItem has "
								+ listItems.size() + " list records");
			}
			BundleCartItem bi = new BundleCartItem();
			bi.setIqty(ci.getIqty());
			bi.setCid(ci.getCid());
			bi.setDcreatedate(ci.getDcreatedate());
			List<CartItemList> main = Lists.newArrayList(Collections2.filter(
					listItems, li -> li.getBismain()));
			if (main.size() != 1) {
				throw new RuntimeException(
						"Data Inconsistent, BundleCartItem has " + main.size()
								+ " main products");
			}
			CartItemList mainProduct = main.iterator().next();

			bi.setClistingid(mainProduct.getClistingid());
			bi.setChildList(Lists.transform(listItems, li -> {
				SingleCartItem si = new SingleCartItem();
				si.setClistingid(li.getClistingid());
				si.setIqty(li.getIqty());
				si.setBismain(li.getBismain());
				return si;
			}));
			return bi;
		}
		throw new RuntimeException("Cart Item Type not valid: "
				+ ci.getIitemtype());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#validCart(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean validCart(String email, String cartId) {
		Context ctx = Context.current();
		ctx.response().setCookie("test", "test");
		int i = cartBaseMapper.validByEmailAndId(email, cartId);
		if (i > 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#readyForCheckout(java.lang.String)
	 */
	@Override
	public boolean readyForCheckout(String cartId) {
		CartBase cart = cartBaseMapper.selectByPrimaryKey(cartId);
		if (cart != null) {
			if (cart.getBgenerateorders() != null && !cart.getBgenerateorders()
					&& !StringUtils.isEmpty(cart.getCmemberemail())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#getCartOwner(java.lang.String)
	 */
	@Override
	public CartOwner getCartOwner(String cartId) {
		CartBase cart = cartBaseMapper.selectByPrimaryKey(cartId);
		if (cart != null) {
			return new CartOwner(cart.getCmemberemail(), cart.getCuuid());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#deleteCartItemList(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean deleteCartItemList(String cartItemID, String listingID) {
		int i = cartItemListMapper.deleteByItemIDAndListingID(cartItemID,
				listingID);
		if (1 == i) {
			int count = cartItemListMapper.getCountByCartItemID(cartItemID);
			if (1 == count) {
				cartItemMapper.updateTypeByID(cartItemID,
						CartItemType.Single.value());
			}
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.cart.ICartService#deleteMainItemList(java.lang.String,
	 * java.lang.String, facades.cart.Cart)
	 */
	@Override
	public boolean deleteMainItemList(String cartItemID, String listingID,
			Cart cart) {
		List<String> list = cart.getListingIDsByItemID(cartItemID);
		cart.deleteItem(cartItemID);
		for (String listing : list) {
			if (!listing.equals(listingID)) {
				SingleCartItem cartItem = new SingleCartItem();
				cartItem.setClistingid(listing);
				cartItem.setIqty(1);
				cart.addItem(cartItem);
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.cart.ICartService#checkItemQty(valueobjects.order_api.cart.CartItem
	 * , java.lang.Integer)
	 */
	@Override
	public boolean checkItemQty(CartItem ci, Integer qty) {
		if (null == ci) {
			return false;
		}
		qty = null != qty ? qty : ci.getIqty();
		boolean res = true;
		if (ci instanceof SingleCartItem) {
			res = inventoryService.checkInventory(ci.getClistingid(), qty);
		} else if (ci instanceof BundleCartItem) {
			BundleCartItem bi = (BundleCartItem) ci;
			List<SingleCartItem> list = bi.getChildList();
			for (SingleCartItem cItem : list) {
				if (!inventoryService.checkInventory(cItem.getClistingid(), qty
						* cItem.getIqty())) {
					res = false;
				}
			}
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.cart.ICartService#checkItemListQty(valueobjects.order_api.cart
	 * .CartItem, java.lang.Integer)
	 */
	@Override
	public boolean checkItemListQty(CartItem ci, Integer qty) {
		if (null == ci) {
			return false;
		}
		qty = null != qty ? qty : ci.getIqty();
		return inventoryService.checkInventory(ci.getClistingid(), qty);
	}

	@Override
	public List<ExtraLineView> convertExtras(facades.cart.Cart ct) {
		List<IOrderExtrasProvider> extrasSorted = Lists
				.newArrayList(extrasProviders);
		Collections.sort(extrasSorted,
				(a, b) -> a.getDisplayOrder() - b.getDisplayOrder());
		List<ExtraLineView> unfiltered = Lists.transform(extrasSorted,
				e -> e.extralineView(ct, ct.getExtraLines().get(e.getId())));
		return Lists.newArrayList(Iterables.filter(unfiltered, o -> o != null));
	}

	@Override
	public int updateCartIsCreateOrder(String cartId) {
		return cartBaseMapper.updateCartStatusByCartId(cartId);
	}
}
