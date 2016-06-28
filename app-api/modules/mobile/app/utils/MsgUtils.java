package utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import play.api.i18n.Lang;
import play.i18n.Messages;
import services.mobile.MobileService;
import extensions.InjectorInstance;

public class MsgUtils {

	private static Map<Integer, String> langMap = new HashMap<Integer, String>();
	
	//缓存物流json信息
	private static Map<String, String> logisticsCache = new HashMap<String, String>();

	static {
		if (langMap.isEmpty()) {
			langMap.put(1, "en");
			langMap.put(2, "es");
			langMap.put(3, "ru");
			langMap.put(4, "de");
			langMap.put(5, "fr");
			langMap.put(6, "it");
			langMap.put(7, "jp");
		}
	}

	/**
	 * 
	  * @Description: 判断多个字符串类型是否有空，没有空返回true，有空返回false
	  * @param @param var
	  * @param @return  
	  * @return boolean  
	  * @author liuyufeng 
	  * @date 2016年5月4日 上午9:50:35
	 */
	public static boolean isNotBlank(String...var){
		for(int i=0;i<var.length;i++){
			if(StringUtils.isBlank(var[i])) return false;
		}
		return true;
	}
	
	public static void put(String _key,String _value){
		if(isNotBlank(_key,_value)){
			logisticsCache.put(_key, _value);
		}
	}
	
	public static String get(String _key){
		if(isNotBlank(_key)){
			return logisticsCache.get(_key);
		}
		return "";
	}
	
	public static String msg(String lang, String country, String key) {
		Lang l = new Lang(lang, country);
		return Messages.get(l, key);
	}

	public static String msg(String lang, String key) {
		Lang l = new Lang(lang, "");
		return Messages.get(l, key);
	}

	public static String msg(String key) {
		MobileService mobileService = InjectorInstance
				.getInstance(MobileService.class);
		int lid = mobileService.getLanguageID();
		String lstr = langMap.get(lid);
		if (StringUtils.isBlank(lstr)) {
			lstr = "en";
		}
		Lang l = new Lang(lstr, null);
		return Messages.get(l, key);
	}

	public static String msg(int lid, String key) {
		String lstr = langMap.get(lid);
		if (StringUtils.isBlank(lstr)) {
			lstr = "en";
		}
		Lang l = new Lang(lstr, null);
		return Messages.get(l, key);
	}
}
