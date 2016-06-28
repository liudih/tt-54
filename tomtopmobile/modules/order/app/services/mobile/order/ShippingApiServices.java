package services.mobile.order;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Logger;
import play.Play;
import valueobjects.cart.BundleCartItem;
import valueobjects.cart.CartItem;
import valueobjects.cart.SingleCartItem;
import valueobjects.order.ShippingMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.common.collect.Lists;

import dto.Country;
import dto.Storage;
import dto.shipping.ShippingStorage;

/**
 * api调用方式
 * 
 * @author lijun
 *
 */
public class ShippingApiServices {

	@Inject
	HttpRequestFactory requestFactory;


	public Storage getWebsiteLimitStorage(int siteId) {
		return null;
	}

	public List<ShippingStorage> getStorages(List<String> listingids) {
		return null;
	}

	public Storage getCountryDefaultStorage(Country country) {
		return null;
	}

	public Storage getShippingStorage(int siteId, List<String> listingids) {
		return null;
	}

	public Storage getShippingStorage(int siteId, Country country,
			List<String> listingids) {
		return null;
	}

	public boolean isSameStorage(List<String> listingids, String storageId) {
		return false;
	}

	public List<ShippingMethod> getShipMethod(String shipToCountryCode,
			int storageId, int language, List<CartItem> items,
			String currencyCode, double totalPrice) {
		String url = Play.application().configuration()
				.getString("shipMethod_url");
		String token = Play.application().configuration()
				.getString("shipMethodToken");
		if (items == null || items.size() == 0) {
			throw new NullPointerException("items is null");
		}

		try {
			JSONObject paras = new JSONObject();

			paras.put("country", shipToCountryCode);
			paras.put("currency", currencyCode);
			paras.put("language", language);
			paras.put("storageId", storageId);
			paras.put("totalPrice", totalPrice);

			JSONArray array = new JSONArray();
			items.forEach(i -> {
				JSONObject json = new JSONObject();
				json.put("listingId", i.getClistingid());
				json.put("qty", i.getIqty());
				if (i instanceof BundleCartItem) {
					JSONArray childrens = new JSONArray();
					List<String> listings = Lists.newArrayList();
					List<SingleCartItem> chlist = ((BundleCartItem) i).getChildList();
					for(int j=0;j<chlist.size();j++){
						if(j>0){
							listings.add(chlist.get(j).getClistingid());
						}
					}
					childrens.addAll(listings);
					json.put("chrd", childrens);
				}
				array.add(json);
			});

			paras.put("shippingCalculateLessParamBase", array);

			String urlStr = url;
			GenericUrl gurl = new GenericUrl(urlStr);

			String json = paras.toJSONString();

			Logger.debug("paras=={}",json);
			System.out.println("shipingmethod--json=="+json);

			HttpContent content = ByteArrayContent.fromString(
					"application/json", json);

			HttpRequest request = requestFactory.buildPostRequest(gurl, content);

			HttpHeaders headers = new HttpHeaders();
			headers.put("token", token);
			request.setHeaders(headers);
			String result = request.execute().parseAsString();

			Logger.debug("shipingmethod--result=={}",result);
			System.out.println("shipingmethod--result=="+result);
			JSONObject resultJson = JSONObject.parseObject(result);

			if ("Y".equals(resultJson.getString("status"))) {
				List<ShippingMethod> list = Lists.newLinkedList();
				JSONArray data = resultJson.getJSONArray("data");
				for(int i = 0 ; i < data.size() ; i++){
					list.add(data.getObject(i, ShippingMethod.class));
				}
				return list;
			}
		} catch (Exception e) {
			Logger.error("get ship method error", e);
		}

		return null;
	}
}
