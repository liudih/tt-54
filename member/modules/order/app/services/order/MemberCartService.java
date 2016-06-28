package services.order;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import play.Logger;
import play.mvc.Http.Context;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

public class MemberCartService {
	
	public static final String DOMAIN = "tomtop.com";
	
	
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
		if (aborting) {
			Logger.error(oplist);
			this.setCookie("plist", plist.toJSONString());
		}

		return clist;
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
					if (storage.get(hashcode) == null && jo!=null) {
						jo.remove(StorageID + "");
					} else {
						storage.remove(hashcode);
					}
					if(jo.get(StorageID + "")==null || "{}".equals(jo.get(StorageID + "").toString()) ) {
						jo.remove(StorageID + "");
					}

				});
			this.setCookie("plist", jo.toJSONString());
		}

	}
	
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
	
	private String getCookie(Context ctx, String key) {
		String oplist = ctx.request().cookie(key).value();
		try {
			oplist = URLDecoder.decode(oplist, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.debug(e.toString());
		}
		return oplist;
	}
	
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
}
