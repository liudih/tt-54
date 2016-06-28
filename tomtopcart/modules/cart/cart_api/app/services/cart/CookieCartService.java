package services.cart;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.collect.Collections2;

import play.Logger;
import play.mvc.Http.Context;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartHistoryType;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class CookieCartService implements ICartServices {

	@Inject
	ICartEnquiryService cartEnquiryService;

	public static final String DOMAIN = "tomtop.com";

	/*
	 * 保存CartItem到cookies中
	 */
	public void addCartItem(CartItem cartItem) {
		saveCartItem(cartItem);
		cartEnquiryService
				.addCartHistory(cartItem, CartHistoryType.add.value());
	}

	protected void saveCartItem(CartItem cartItem) {
		if (cartItem == null) {
			return;
		}
		JSONObject jo = null;

		Context ctx = Context.current();
		if (ctx.request().cookie("plist") != null) {
			String oplist = this.getCookie(ctx, "plist");
			jo = (JSONObject) JSONObject.parse(oplist);
		} else {
			jo = new JSONObject();
		}

		Map<String, String> pjson = new LinkedHashMap<String, String>();
		if (cartItem instanceof SingleCartItem) {
			// 计算hashcode
			int hashCode = cartItem.getClistingid().hashCode();
			String hashCodeStr = hashCode + "";
			// 默认是0
			Integer storageId = 0;
			if (cartItem.getStorageID() != null) {
				storageId = cartItem.getStorageID();
			}

			JSONObject storage = jo.getJSONObject(storageId.toString());
			if (storage == null) {
				storage = new JSONObject();
				jo.put(storageId.toString(), storage);
			}

			JSONArray detailArray = storage.getJSONArray(hashCodeStr);
			if (detailArray == null) {
				detailArray = new JSONArray();

				JSONObject detail = new JSONObject();
				detail.put("listing", cartItem.getClistingid());
				detail.put("qty", cartItem.getIqty());
				detailArray.add(detail);

				storage.put(hashCodeStr, detailArray);
			} else {
				JSONObject detail = detailArray.getJSONObject(0);
				// Integer qty = detail.getInteger("qty");
				// qty = qty + cartItem.getIqty();
				detail.put("qty", cartItem.getIqty());
			}

		} else if (cartItem instanceof BundleCartItem) {
			List<SingleCartItem> slist = ((BundleCartItem) cartItem)
					.getChildList();
			StringBuilder sb = new StringBuilder();
			// 计算hashcode
			FluentIterable.from(slist).forEach(item -> {
				sb.append(item.getClistingid());
			});
			int hashCode = sb.toString().hashCode();
			String hashCodeStr = hashCode + "";

			// 默认是0
			Integer storageId = 0;
			if (cartItem.getStorageID() != null) {
				storageId = cartItem.getStorageID();
			}

			JSONObject storage = jo.getJSONObject(storageId.toString());
			if (storage == null) {
				storage = new JSONObject();
				jo.put(storageId.toString(), storage);
			}

			JSONArray detailArray = storage.getJSONArray(hashCodeStr);

			if (detailArray == null) {
				JSONArray tempArray = new JSONArray();

				FluentIterable.from(slist).forEach(item -> {
					JSONObject detail = new JSONObject();
					detail.put("listing", item.getClistingid());
					detail.put("qty", item.getIqty());
					tempArray.add(detail);
				});
				detailArray = tempArray;

				storage.put(hashCodeStr, detailArray);
			} else {
				Map<String, Integer> map = Maps.newHashMap();

				FluentIterable.from(slist).forEach(i -> {
					if (i.getIqty() != null) {
						map.put(i.getClistingid(), i.getIqty());
					}
				});
				Iterator iterator = detailArray.iterator();
				while (iterator.hasNext()) {
					JSONObject detail = (JSONObject) iterator.next();
					String listing = detail.getString("listing");
					Integer qty = map.get(listing);
					if (qty != null) {
						detail.put("qty", qty);
					}
				}
			}

		}
		this.setCookie("plist", jo.toJSONString());
		Logger.debug("saveCookies:{}", jo.toJSONString());
	}

	public void deleteItem(List<CartItem> items) {
		if (items == null || items.size() == 0) {
			return;
		}
		Context ctx = Context.current();

		if (ctx.request().cookie("plist") != null) {
			String oplist = this.getCookie(ctx, "plist");
			JSONObject jo = (JSONObject) JSONObject.parse(oplist);

			FluentIterable.from(items).forEach(item -> {
				// 0 代表没有仓库
					Integer StorageID = 0;
					if (item.getStorageID() != null) {
						StorageID = item.getStorageID();
					}

					JSONObject storage = jo.getJSONObject(StorageID + "");
					if (storage == null) {
						return;
					}
					String hashcode = null;
					if (item instanceof SingleCartItem) {
						// 计算hashcode
						hashcode = item.getClistingid().hashCode() + "";
					} else if (item instanceof BundleCartItem) {
						List<SingleCartItem> child = ((BundleCartItem) item)
								.getChildList();
						StringBuilder sb = new StringBuilder();

						FluentIterable.from(child).forEach(i -> {
							sb.append(i.getClistingid());
						});
						hashcode = sb.toString().hashCode() + "";
					}

					storage.remove(hashcode);

				});
			this.setCookie("plist", jo.toJSONString());
		}

	}

	public void updateItemQty(CartItem item) {
		saveCartItem(item);
		cartEnquiryService.addCartHistory(item, CartHistoryType.update.value());
	}

	public List<CartItem> getAllItems(Integer siteid, Integer lang,
			String currency) {
		Context ctx = Context.current();
		List<CartItem> clist = Lists.newArrayList();

		boolean aborting = false;
		JSONObject plist = null;
		String oplist = null;
		if (ctx.request().cookie("plist") != null) {
			oplist = this.getCookie(ctx, "plist");
			JSONObject jo = null;
			try {
				jo = (JSONObject) JSONObject.parse(oplist);
			} catch (Exception e) {
				this.deleteAllItem();
				Logger.error(oplist,e);
				return clist;
			}

			plist = jo;

			Iterator<String> iterator = jo.keySet().iterator();

			while (iterator.hasNext()) {
				String store = iterator.next();
				try {
					int storeId = Integer.parseInt(store);
					JSONObject storeNode = jo.getJSONObject(store);

					Iterator<String> piterator = storeNode.keySet().iterator();

					while (piterator.hasNext()) {
						String key = piterator.next();
						JSONArray products = storeNode.getJSONArray(key);
						try {

							if (products.size() == 1) {
								SingleCartItem item = new SingleCartItem();
								if (storeId != 0) {
									item.setStorageID(storeId);
								}
								JSONObject detail = products.getJSONObject(0);
								String listing = detail.getString("listing");
								Integer qty = detail.getInteger("qty");
								item.setClistingid(listing);
								item.setIqty(qty);
								clist.add(item);
							} else {
								BundleCartItem item = new BundleCartItem();
								LinkedList<SingleCartItem> childs = Lists
										.newLinkedList();
								item.setStorageID(storeId);
								products.forEach(p -> {
									SingleCartItem child = new SingleCartItem();
									if (storeId != 0) {
										child.setStorageID(storeId);
									}
									JSONObject detail = (JSONObject) p;
									String listing = detail
											.getString("listing");
									Integer qty = detail.getInteger("qty");
									child.setClistingid(listing);
									child.setIqty(qty);
									childs.add(child);
								});
								if (childs.size() > 0) {
									item.setIqty(childs.get(0).getIqty());
								}
								item.setChildList(childs);
								clist.add(item);
							}
						} catch (Exception e) {
							aborting = true;
							storeNode.remove(key);
						}
					}

				} catch (Exception e) {
					aborting = true;
					jo.remove(store);
				}
			}
		}
		clist = cartEnquiryService.getCartItems(clist, siteid, lang, currency);
		if (aborting) {
			Logger.error(oplist);
			this.setCookie("plist", plist.toJSONString());
		}

		return clist;
	}

	@Override
	public void saveForlater(CartItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public CartItem getItem(CartItem item) {
		if (item == null) {
			return null;
		}
		Context ctx = Context.current();

		if (ctx.request().cookie("plist") != null) {
			String oplist = this.getCookie(ctx, "plist");
			JSONObject jo = (JSONObject) JSONObject.parse(oplist);

			// 0 代表没有仓库
			Integer StorageID = 0;
			if (item.getStorageID() != null) {
				StorageID = item.getStorageID();
			}

			JSONObject storage = jo.getJSONObject(StorageID + "");
			if (storage == null) {
				return null;
			}
			String hashcode = null;
			if (item instanceof SingleCartItem) {
				// 计算hashcode
				hashcode = item.getClistingid().hashCode() + "";
			} else if (item instanceof BundleCartItem) {
				List<SingleCartItem> child = ((BundleCartItem) item)
						.getChildList();
				StringBuilder sb = new StringBuilder();

				FluentIterable.from(child).forEach(i -> {
					sb.append(i.getClistingid());
				});
				hashcode = sb.toString().hashCode() + "";
			}
			// 取一个产品
			JSONArray products = storage.getJSONArray(hashcode);
			if (products == null) {
				return null;
			}
			if (products.size() == 1) {
				SingleCartItem sitem = new SingleCartItem();
				if (StorageID != 0) {
					sitem.setStorageID(StorageID);
				}
				JSONObject detail = products.getJSONObject(0);
				String listing = detail.getString("listing");
				Integer qty = detail.getInteger("qty");
				sitem.setClistingid(listing);
				sitem.setIqty(qty);
				return sitem;
			} else {
				BundleCartItem bitem = new BundleCartItem();
				LinkedList<SingleCartItem> childs = Lists.newLinkedList();
				final Integer storageId = StorageID;
				products.forEach(p -> {
					SingleCartItem child = new SingleCartItem();
					if (storageId != 0) {
						child.setStorageID(storageId);
					}
					JSONObject detail = (JSONObject) p;
					String listing = detail.getString("listing");
					Integer qty = detail.getInteger("qty");
					child.setClistingid(listing);
					child.setIqty(qty);
					childs.add(child);
				});
				bitem.setChildList(childs);
				return bitem;
			}
		}
		return null;
	}

	@Override
	public boolean isEnoughQty(String itemID, Integer qty,
			List<String> listingids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteAllItem() {
		Context ctx = Context.current();

		String host = ctx.request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(DOMAIN) != -1) {
			Logger.debug("domain:{}", DOMAIN);
			ctx.response().discardCookie("plist", "/", "." + DOMAIN);
		} else {
			ctx.response().discardCookie("plist", "/");
		}
	}

	/**
	 * @author lijun
	 * @return
	 */
	private void setCookie(String key, String value) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.error(e.toString());
		}
		Context ctx = Context.current();
		String host = ctx.request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(DOMAIN) != -1) {
			Logger.debug("domain:{}", DOMAIN);
			ctx.response().setCookie(key, value, 365 * 24 * 3600, "/",
					"." + DOMAIN);
		} else {
			ctx.response().setCookie(key, value, 365 * 24 * 3600, "/");
		}
	}

	@Override
	public List<CartItem> getAllItemsCurrentStorageid(Integer siteid,
			Integer lang, String currency) {
		List<CartItem> cartItems = this.getAllItems(siteid, lang, currency);
		Context ctx = Context.current();
		if (ctx.request().cookie("storageid") != null) {
			final Integer storageid = Integer.parseInt(ctx.request()
					.cookie("storageid").value());
			cartItems = Lists.newArrayList(Collections2.filter(cartItems,
					c -> c.getStorageID() == storageid));
		}
		return cartItems;
	}

	private String getCookie(Context ctx, String key) {
		String oplist = ctx.request().cookie(key).value();
		try {
			oplist = URLDecoder.decode(oplist, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.debug(e.toString());
		}
		return oplist;
	}
}
