package com.tomtop.es.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.Lists;
import com.tomtop.es.entity.Language;
import com.tomtop.es.entity.RemoteAttributeEntity;
import com.tomtop.es.entity.RemoteProductType;
import com.tomtop.product.configuration.EsConfigSettings;

/**
 * base 服务util
 * 
 * @author lijun
 *
 */
@Service
public class BaseServiceUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(BaseServiceUtil.class);

	@Autowired
	private EsConfigSettings esSetting;

	private Map<String, RemoteProductType> map = new HashMap<String, RemoteProductType>();

	@PostConstruct
	public void init() {
		String url = esSetting.getBaseProductType();

		String result = HttpClientUtil.doGet(url);
		try {
			if (map == null || map.size() < 1) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					JSONArray arr = (JSONArray) obj.get("data");
					if (arr != null && arr.size() > 0) {
						for (Object object : arr) {
							RemoteProductType model = JSON.parseObject(
									object.toString(), RemoteProductType.class);
							map.put(String.valueOf(model.getIcategoryid()),
									model);
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<Language> getLanguage() {
		String urlStr = this.esSetting.getBaseLanguage();
		if (StringUtils.isEmpty(urlStr)) {
			throw new NullPointerException("not find config->base.language");
		}

		List<Language> languages = Lists.newLinkedList();

		try {
			NetHttpTransport transport = new NetHttpTransport();

			GenericUrl url = new GenericUrl(urlStr);

			HttpRequest request = transport.createRequestFactory()
					.buildGetRequest(url);
			String result = request.execute().parseAsString();

			JSONObject json = JSONObject.parseObject(result);

			if (1 == json.getInteger("ret")) {

				JSONArray date = json.getJSONArray("data");
				date.forEach(jo -> {
					JSONObject j = (JSONObject) jo;
					Integer id = j.getInteger("id");
					String name = j.getString("name");
					String code = j.getString("code");
					Language language = new Language(id, name, code);
					languages.add(language);
				});
			}
		} catch (Exception e) {
			logger.error("get language error", e);
		}

		return languages;
	}

	public Map<String, Language> getLanguageMap() {
		String urlStr = this.esSetting.getBaseLanguage();
		if (StringUtils.isEmpty(urlStr)) {
			throw new NullPointerException("not find config->base.language");
		}

		Map<String, Language> map = new HashMap<String, Language>();

		try {
			NetHttpTransport transport = new NetHttpTransport();
			GenericUrl url = new GenericUrl(urlStr);
			HttpRequest request = transport.createRequestFactory()
					.buildGetRequest(url);
			String result = request.execute().parseAsString();
			JSONObject json = JSONObject.parseObject(result);
			if (1 == json.getInteger("ret")) {
				JSONArray date = json.getJSONArray("data");
				date.forEach(jo -> {
					JSONObject j = (JSONObject) jo;
					Integer id = j.getInteger("id");
					String name = j.getString("name");
					String code = j.getString("code");
					Language language = new Language(id, name, code);
					map.put(code, language);
				});
			}
		} catch (Exception e) {
			logger.error("get language error", e);
		}

		return map;
	}

	/**
	 * 远程接口获取类目信息
	 * 
	 * @param v
	 *            类目id
	 * @return
	 */
	public RemoteProductType getProductType(int v) {
		Assert.notNull(v, "类目id不能为空");
		String url = this.esSetting.getBaseProductType() + v;
		String result = HttpClientUtil.doGet(url);
		RemoteProductType model = null;
		try {
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				Object remoteType = obj.get("data");
				if (remoteType != null
						&& StringUtils.isNotBlank(remoteType.toString())) {
					model = JSON.parseObject(remoteType.toString(),
							RemoteProductType.class);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}

	/**
	 * @return
	 */

	public Map<String, RemoteProductType> getAllProductType() {
		return map;
	}

	/**
	 * 根据类目ID找到所有的key
	 * 
	 * @param productTypeId
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<RemoteAttributeEntity>> getAllShowKeyByProductTypeId(
			int productTypeId) {
		String url = this.esSetting.getBaseGetkeysbytypeid() + productTypeId;
		Map<String, List<RemoteAttributeEntity>> map = null;
		try {
			String result = HttpClientUtil.doGet(url);
			JSONObject obj = JSON.parseObject(result);
			if (obj != null) {
				if (obj.get("data") != null) {
					String mapStr = obj.get("data").toString();
					map = JSON.parseObject(mapStr, Map.class);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 根据key找到所有的类目
	 * 
	 * @param key
	 * @return Map<String,RemoteAttributeEntity>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<RemoteAttributeEntity>> getAllShowKeyByKey(
			String key) {
		Map<String, List<RemoteAttributeEntity>> map = new HashMap<String, List<RemoteAttributeEntity>>();
		try {
			String url = this.esSetting.getBaseGetkeysbytypeid() + key;
			String result = HttpClientUtil.doGet(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					if (obj.get("data") != null) {
						String mapStr = obj.get("data").toString();
						map = JSON.parseObject(mapStr, Map.class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 查询所有key对应的value
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getAllShowValues() {
		String url = this.esSetting.getBaseAllshowvalues();
		Map<String, List<String>> map = new TreeMap<String, List<String>>();
		try {
			String result = HttpClientUtil.doGet(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					if (obj.get("data") != null) {
						String mapStr = obj.get("data").toString();
						map = JSON.parseObject(mapStr, Map.class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取所有的需要展示的key
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getAllShowKey() {
		String url = this.esSetting.getBaseAllshowkey();
		Map<String, String> map = new HashMap<String, String>();
		try {
			String result = HttpClientUtil.doGet(url);
			if (StringUtils.isNotBlank(result)) {
				JSONObject obj = JSON.parseObject(result);
				if (obj != null) {
					if (obj.get("data") != null) {
						String mapStr = obj.get("data").toString();
						map = JSON.parseObject(mapStr, Map.class);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

}
