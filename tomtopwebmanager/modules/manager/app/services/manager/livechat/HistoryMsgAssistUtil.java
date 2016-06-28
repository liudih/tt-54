package services.manager.livechat;

import java.util.Map;

import com.google.api.client.util.Maps;

public class HistoryMsgAssistUtil {
	private static Map<String, Object> map = Maps.newHashMap();

	public static void setValue(String key, Object value) {
		map.put(key, value);
	}

	public static Object getValue(String key) {
		return map.get(key);
	}

	public static Boolean isTrue(String key) {
		return Boolean.valueOf(map.get(key).toString());
	}

	public static Double getDouble(String key) {
		return Double.valueOf(map.get(key).toString());
	}
}
