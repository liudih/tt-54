package services.cart;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

/**
 *************************************************************** 
 * <p>
 * @DESCRIPTION : 购物车服务的service实现类 供第三方系统统一调用
 * @AUTHOR : wenlong
 * @DATE :2015-10-06 上午11:35:33
 *</p>
 **************************************************************** 
 */
public class CookieLaterService implements ICartLaterServices {

	@Inject
	ICartEnquiryService cartEnquiryService;

	/*
	 * 保存CartItem到cookies中
	 */
	public void addLaterItem(CartItem cartItem) {
		saveLaterItem(cartItem);
		cartEnquiryService
				.addCartHistory(cartItem, CartHistoryType.add.value());
	}

	protected void saveLaterItem(CartItem cartItem) {
		if (cartItem == null) {
			return;
		}
		JSONObject jo = null;

		Context ctx = Context.current();
		if (ctx.request().cookie("glist") != null) {
			String oglist = ctx.request().cookie("glist").value();
			jo = (JSONObject) JSONObject.parse(oglist);
		} else {
			jo = new JSONObject();
		}
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
//				Integer qty = detail.getInteger("qty");
//				qty = qty + cartItem.getIqty();
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

		ctx.response().setCookie("glist", jo.toJSONString(), 365 * 24 * 3600,
				"/");

		Logger.debug("saveCookies:{}", jo.toJSONString());
	}

	public void deleteLaterItem(List<CartItem> items) {
		if (items == null || items.size() == 0) {
			return;
		}
		Context ctx = Context.current();

		if (ctx.request().cookie("glist") != null) {
			String oglist = ctx.request().cookie("glist").value();
			JSONObject jo = (JSONObject) JSONObject.parse(oglist);

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
			ctx.response().setCookie("glist", jo.toJSONString(),
					365 * 24 * 3600, "/");
		}

	}

	public void updateLaterItemQty(CartItem item) {
		saveLaterItem(item);
		cartEnquiryService.addCartHistory(item, CartHistoryType.update.value());
	}

	public List<CartItem> getAllLaterItems(Integer siteid, Integer lang,
			String currency) {
		Context ctx = Context.current();
		List<CartItem> glist = Lists.newArrayList();
		if (ctx.request().cookie("glist") != null) {
			String oglist = ctx.request().cookie("glist").value();
			JSONObject jo = (JSONObject) JSONObject.parse(oglist);

			Iterator<String> iterator = jo.keySet().iterator();

			while (iterator.hasNext()) {
				String store = iterator.next();
				int storeId = Integer.parseInt(store);
				JSONObject storeNode = jo.getJSONObject(store);
				
				Iterator<String> piterator = storeNode.keySet().iterator();
				
				while(piterator.hasNext()){
					String key = piterator.next();
					JSONArray products = storeNode.getJSONArray(key);
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
						glist.add(item);
					} else {
						BundleCartItem item = new BundleCartItem();
						LinkedList<SingleCartItem> childs = Lists.newLinkedList();
						item.setStorageID(storeId);
						products.forEach(p -> {
							SingleCartItem child = new SingleCartItem();
							if (storeId != 0) {
								child.setStorageID(storeId);
							}
							JSONObject detail = (JSONObject) p;
							String listing = detail.getString("listing");
							Integer qty = detail.getInteger("qty");
							child.setClistingid(listing);
							child.setIqty(qty);
							childs.add(child);
						});
						if(childs.size()>0){
							item.setIqty(childs.get(0).getIqty());
						}
						item.setChildList(childs);
						glist.add(item);
					}
				}
				
			}
		}
		glist = cartEnquiryService.getCartItems(glist, siteid, lang, currency);
		return glist;
	}

	@Override
	public void saveForlater(CartItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getItem(CartItem item) {
		// TODO Auto-generated method stub

	}

}
